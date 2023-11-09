package com.example.jwt.service;



import com.example.jwt.config.AppConstants;
import com.example.jwt.entities.Role;
import com.example.jwt.entities.User;
import com.example.jwt.exception.UserAlreadyExistsException;
import com.example.jwt.registration.RegistrationRequest;
import com.example.jwt.registration.token.VerificationToken;
import com.example.jwt.registration.token.VerificationTokenRepository;
import com.example.jwt.repository.RoleRepo;
import com.example.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepo roleRepo;


    private final VerificationTokenRepository tokenRepository;



    public List<User> getUser(){

        return userRepository.findAll();
    }

//    public User createUser(User user)
//    {
////        user.setUserId(UUID.randomUUID().toString());
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        return userRepository.save(user);
//    }

    public User saveUser(User user) {
        // Use your UserRepository to save the user entity
        return userRepository.save(user);
    }

    @Override
    public User registerUser(RegistrationRequest request) {
        Optional<User> user = this.findByEmail(request.email());
        if (user.isPresent()){
            throw new UserAlreadyExistsException(
                    "User with email "+request.email() + " already exists");
        }
        var newUser = new User();
//        newUser.setFirstName(request.firstName());
//        newUser.setLastName(request.lastName());
        newUser.setMobileNo(request.mobileNo());
        newUser.setUserName(request.userName());
        newUser.setEmail(request.email());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        //roles
        Role role = this.roleRepo.findById(AppConstants.NORMAL_USER).get();

        newUser.getRoles().add(role);

        // Save the user and wrap it in an Optional
        User savedUser = userRepository.save(newUser);
//        newUser.setRole(request.role());
        return userRepository.save(savedUser);
    }



    public boolean isEmailInUse(String email) {
        // Perform a database query to check if the email is already in use.
        Optional<User> existingUserOptional = userRepository.findByEmail(email);

        // Check if the Optional contains a user and return true if it does.
        return existingUserOptional.isPresent();
    }

    public User updateUser(User user) {
        // This method should update the user and save the changes to the database
        return userRepository.save(user);
    }


    public User findByUsername(String username) {
        Optional<User> userOptional = userRepository.findByEmail(username);
        return userOptional.orElse(null); // Return null if not found, or handle differently if needed
    }


//    public User findByUsername(String username) {
//        return userRepository.findByEmail(username);
//    }

    @Override
    public Optional<User>  findByEmail(String email) {

        return userRepository.findByEmail(email);
    }

    @Override
    public void saveUserVerificationToken(User theUser, String token) {
        var verificationToken = new VerificationToken(token, theUser);
        tokenRepository.save(verificationToken);
    }

    @Override
    public String validateToken(String theToken) {
        VerificationToken token = tokenRepository.findByToken(theToken);
        if(token == null){
            return "Invalid verification token";
        }
        User user = (User) token.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
            tokenRepository.delete(token);
            return "Token already expired";
        }
        user.setEmailVerified(true);
        userRepository.save(user);
        return "valid";
    }















}
