package com.example.jwt.service;

import com.example.jwt.entities.User;
import com.example.jwt.registration.token.VerificationTokenRepository;
import com.example.jwt.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserAccountCleanupService {
    @Autowired
    private UserRepository userRepository;

//    @Scheduled(fixedRate = 60000) // Run every minute
//    public void cleanupUnverifiedUsers() {
//        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);
//        List<User> unverifiedUsers = userRepository.findByEmailVerifiedFalseAndRegistrationTimestampBefore(oneMinuteAgo);
//
//        if (!unverifiedUsers.isEmpty()) {
//            for (User user : unverifiedUsers) {
//                userRepository.delete(user);
//                // Delete associated data as needed
//            }
//        }
//    }

    @Scheduled(fixedRate = 60000) // Run every minute
    public void cleanupUnverifiedUsers() {
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(30);
        List<User> unverifiedUsers = userRepository.findByEmailVerifiedFalseAndRegistrationTimestampBefore(oneMinuteAgo);

        if (!unverifiedUsers.isEmpty()) {
            for (User user : unverifiedUsers) {
                user.getRoles().clear(); // Remove roles
                userRepository.delete(user);
                // Delete associated data as needed
            }
        }
    }





    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Transactional
    public void deleteUserWithCleanup(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Delete associated verification_token records
            verificationTokenRepository.deleteByUserUserId(userId);

            // Delete the User
            userRepository.delete(user);
        } else {
            // Handle the case when the user does not exist.
            // You can log an error or throw an exception if needed.
        }
    }


}
