package com.example.jwt.service;

import com.example.jwt.config.AppConstants;
import com.example.jwt.entities.AllToggle;
import com.example.jwt.entities.Role;
import com.example.jwt.entities.User;
import com.example.jwt.exception.RegistrationException;
import com.example.jwt.exception.UserAlreadyExistsException;
import com.example.jwt.registration.RegistrationRequest;
import com.example.jwt.registration.token.VerificationToken;
import com.example.jwt.registration.token.VerificationTokenRepository;
import com.example.jwt.repository.RoleRepo;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.AllToggleRepository;
import com.google.api.gax.paging.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
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
    @Autowired
    private AllToggleRepository allToggleRepository;

    public List<User> getUser() {
        return userRepository.findAll();
    }

//    public Page<User> getAllUsers(Pageable pageable) {
//        return userRepository.findAll(pageable);
//    }
    public User saveUser(User user) {
        // Use your UserRepository to save the user entity
        return userRepository.save(user);
    }

    // for registering the user in application
    public User registerUser(RegistrationRequest request) {
        // Check if the user with the provided email already exists
        Optional<User> existingUser = userRepository.findByEmail(request.email());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User with email " + request.email() + " already exists");
        }

        // Check registration term condition
        if (!request.registrationTermCondition()) {
            throw new RegistrationException("Registration terms and conditions must be accepted.");
        }

        // Proceed with user registration
        User newUser = new User();
        newUser.setMobileNo(request.mobileNo());
        newUser.setUserName(request.userName());
        newUser.setEmail(request.email());
        newUser.setDeviceType(request.deviceType());
        newUser.setLatitude(request.latitude());
        newUser.setLongitude(request.longitude());
        newUser.setPassword(passwordEncoder.encode(request.password()));

        // Assign a role to the user (adjust as needed)
        Role role = roleRepo.findById(AppConstants.NORMAL_USER).orElseThrow();
        newUser.getRoles().add(role);

        // Save the user
        User savedUser = userRepository.save(newUser);

        // Update AllToggle with registrationTermCondition
        Optional<AllToggle> allToggleOptional = Optional.ofNullable(allToggleRepository.findByUser(savedUser));
        AllToggle allToggle;
        if (allToggleOptional.isPresent()) {
            allToggle = allToggleOptional.get();
        } else {
            allToggle = new AllToggle(true, request.registrationTermCondition(), savedUser);
        }
        allToggle.setRegistration_term_condition(request.registrationTermCondition());
        allToggleRepository.save(allToggle);

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


    //    public User updateUserNotificationToken(Long userId, String newToken) {
//        Optional<User> optionalUser = userRepository.findById(userId);
//        if (optionalUser.isPresent()) {
//            User user = optionalUser.get();
//            if (newToken != null) {
//                user.updateNotificationToken(newToken);
//            }
//            return userRepository.save(user);
//        }
//        // Handle the case when the user with the given ID is not found
//        return null;
//    }
    public User updateUserNotificationToken(String username, String newToken) {
        Optional<User> optionalUser = userRepository.findByEmail(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (newToken != null) {
                user.updateNotificationToken(newToken);
            }

            return userRepository.save(user);
        }

        // Handle the case when the user with the given username is not found
        return null;
    }


    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }


}
