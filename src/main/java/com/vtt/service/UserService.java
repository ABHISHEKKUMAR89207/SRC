package com.vtt.service;


import com.vtt.config.AppConstants;
import com.vtt.entities.Role;
import com.vtt.entities.User;
import com.vtt.exception.RegistrationException;
import com.vtt.exception.UserAlreadyExistsException;
import com.vtt.registration.RegistrationRequest;
import com.vtt.registration.token.VerificationToken;
import com.vtt.registration.token.VerificationTokenRepository;
import com.vtt.repository.RoleRepo;
import com.vtt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final VerificationTokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepo roleRepo;
//    @Autowired
//    private AllToggleRepository allToggleRepository;
public User findUserByMobileNo(String mobileNo) {
    return userRepository.findByMobileNo(mobileNo).orElse(null);
}



    public List<User> getUser() {
        return userRepository.findAll();
    }
    public void saveUserVerificationOtp(User user, String otp) {
        user.setOtp(otp);
        user.setOtpExpirationTime(LocalDateTime.now().plusMinutes(5)); // OTP valid for 5 minutes
        userRepository.save(user);
    }



    //    public Page<User> getAllUsers(Pageable pageable) {
//        return userRepository.findAll(pageable);
//    }
    public User saveUser(User user) {
        // Use your UserRepository to save the user entity
        return userRepository.save(user);
    }

    public User registerUser(RegistrationRequest request) {
        // Check if the user with the provided email or mobile number already exists
        Optional<User> existingUserByEmail = userRepository.findByEmail(request.email());
        Optional<User> existingUserByMobile = userRepository.findByMobileNo(request.mobileNo());

        // If the user exists and is already verified, throw an exception
        if (existingUserByEmail.isPresent() && existingUserByEmail.get().isEmailVerified()) {
            throw new UserAlreadyExistsException("User with email " + request.email() + " already exists and is verified.");
        }
        if (existingUserByMobile.isPresent() && existingUserByMobile.get().isEmailVerified()) {
            throw new UserAlreadyExistsException("User with phone number " + request.mobileNo() + " already exists and is verified.");
        }

        // If the user exists but is not verified, update their data and resend OTP
        User user;
        if (existingUserByEmail.isPresent() || existingUserByMobile.isPresent()) {
            user = existingUserByEmail.orElse(existingUserByMobile.get());
            user.setMobileNo(request.mobileNo());
            user.setUserName(request.userName());
            user.setEmail(request.email());
            user.setPasswordTemp(request.password());
            user.setDeviceType(request.deviceType());
            user.setLatitude(request.latitude());
            user.setLongitude(request.longitude());
            user.setPassword(passwordEncoder.encode(request.password()));
            user.setEmailVerified(false); // Ensure emailVerified is false
        } else {
            // If the user does not exist, create a new user
            user = new User();
            user.setMobileNo(request.mobileNo());
            user.setUserName(request.userName());
            user.setEmail(request.email());
            user.setDeviceType(request.deviceType());
            user.setPasswordTemp(request.password());
            user.setLatitude(request.latitude());
            user.setLongitude(request.longitude());
            user.setPassword(passwordEncoder.encode(request.password()));
            user.setEmailVerified(false); // New users are not verified by default
        }

        // Assign a role to the user (adjust as needed)
        Role role = roleRepo.findById(AppConstants.NORMAL_USER).orElseThrow();
        user.getRoles().add(role);

        // Save the user
        User savedUser = userRepository.save(user);

        return savedUser;
    }
    // to check if e mail in use
    public boolean isEmailInUse(String email) {
        // Perform a database query to check if the email is already in use.
        Optional<User> existingUserOptional = userRepository.findByEmail(email);

        // Check if the Optional contains a user and return true if it does.
        return existingUserOptional.isPresent();
    }

    // to update the user
    public User updateUser(User user) {
        // This method should update the user and save the changes to the database
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        Optional<User> userOptional = userRepository.findByEmail(username);
        return userOptional.orElse(null); // Return null if not found, or handle differently if needed
    }

    @Override
    public Optional<User> findByEmail(String email) {

        return userRepository.findByEmail(email);
    }

    @Override
    public void saveUserVerificationToken(User theUser, String token) {
        // Check if the user already has a verification token
        VerificationToken existingToken = tokenRepository.findByUser(theUser);

        if (existingToken != null) {
            // If an existing token is found, update it with the new token and reset the expiration time
            existingToken.setToken(token);
            existingToken.setExpirationTime(existingToken.getTokenExpirationTime());
            tokenRepository.save(existingToken);
        } else {
            // If no existing token is found, create a new one
            var verificationToken = new VerificationToken(token, theUser);
            tokenRepository.save(verificationToken);
        }
    }


    @Override
    public String validateToken(String theToken) {
        VerificationToken token = tokenRepository.findByToken(theToken);
        if (token == null) {
            return "Invalid verification token";
        }
        User user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            tokenRepository.delete(token);
            return "Token already expired";
        }
        user.setEmailVerified(true);
        userRepository.save(user);
        return "valid";
    }



}
