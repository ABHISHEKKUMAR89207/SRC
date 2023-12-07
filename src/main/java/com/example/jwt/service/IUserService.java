package com.example.jwt.service;

import com.example.jwt.entities.User;
import com.example.jwt.registration.RegistrationRequest;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<User> getUser();

    User registerUser(RegistrationRequest request);

    Optional<User> findByEmail(String email);

    void saveUserVerificationToken(User theUser, String verificationToken);

    String validateToken(String theToken);


//    User findByUsername(String username);
}
