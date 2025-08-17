package com.vtt.controllers;


import com.vtt.entities.User;
import com.vtt.entities.UserDetails;
import com.vtt.otherclass.MainRole;

import com.vtt.repository.UserDetailsRepository;
import com.vtt.repository.UserRepository;
import com.vtt.security.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserCompleteProfileController {

    private final UserRepository userRepository;
    private final JwtHelper jwtHelper;
    private final UserDetailsRepository userDetailsRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserCompleteProfileController.class);

    @PostMapping("/complete-profile")
    public ResponseEntity<String> updateUserProfile(
            @RequestHeader("Authorization") String tokenHeader,
            @RequestBody User updatedDetails
    ) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            logger.info("Extracted Username from Token: {}", username);

            Optional<User> userOpt = userRepository.findByEmail(username);
            if (userOpt.isPresent()) {
                User existingUser = userOpt.get();

                // Update necessary fields
                if (updatedDetails.getUserName() != null) existingUser.setUserName(updatedDetails.getUserName());
                if (updatedDetails.getAddress() != null) existingUser.setAddress(updatedDetails.getAddress());
                if (updatedDetails.getMainRole() != null) existingUser.setMainRole(updatedDetails.getMainRole());
                if (updatedDetails.getSubRole() != null) existingUser.setSubRole(updatedDetails.getSubRole());



                userRepository.save(existingUser);
                return ResponseEntity.ok("User profile updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

        } catch (Exception e) {
            logger.error("Error while updating user profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while updating user profile");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(@RequestHeader("Authorization") String tokenHeader) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);

            Optional<User> requestingUserOpt = userRepository.findByEmail(username);
            if (requestingUserOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token or user not found.");
            }

            User requestingUser = requestingUserOpt.get();
//            if (requestingUser.getMainRole() != MainRole.ADMIN) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only ADMIN can access this endpoint.");
//            }

            return ResponseEntity.ok(userRepository.findAll());

        } catch (Exception e) {
            logger.error("Error fetching all users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching users.");
        }
    }

    @PutMapping("/update-user/{userId}")
    public ResponseEntity<?> updateAnyUserByAdmin(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable String userId,
            @RequestBody User updatedDetails
    ) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);

            Optional<User> requestingUserOpt = userRepository.findByEmail(username);
            if (requestingUserOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token or user not found.");
            }

            User requestingUser = requestingUserOpt.get();
            if (requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only ADMIN can update user data.");
            }

          User userToUpdateOpt = userRepository.findByUserId(userId);
            if (userToUpdateOpt==null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }

            User userToUpdate = userToUpdateOpt;
            System.out.println("sdgdfgdfhfghjgjh===="+updatedDetails.getMainRole());
            // Update only allowed fields
            if (updatedDetails.getUserName() != null) userToUpdate.setUserName(updatedDetails.getUserName());
            if (updatedDetails.getAddress() != null) userToUpdate.setAddress(updatedDetails.getAddress());
            if (updatedDetails.getMobileNo() != null) userToUpdate.setMobileNo(updatedDetails.getMobileNo());
            if (updatedDetails.getMainRole() != null) userToUpdate.setMainRole(updatedDetails.getMainRole());
            if (updatedDetails.getSubRole() != null) userToUpdate.setSubRole(updatedDetails.getSubRole());
            if (updatedDetails.getEmail() != null) userToUpdate.setEmail(updatedDetails.getEmail());
            if (updatedDetails.getLatitude() != null) userToUpdate.setLatitude(updatedDetails.getLatitude());
            if (updatedDetails.getLongitude() != null) userToUpdate.setLongitude(updatedDetails.getLongitude());
            System.out.println("dfshdhfgjm======="  +updatedDetails.isActiveStatus());
            userToUpdate.setActiveStatus(updatedDetails.isActiveStatus());
            userRepository.save(userToUpdate);
            return ResponseEntity.ok("User updated successfully.");

        } catch (Exception e) {
            logger.error("Error updating user by admin", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while updating user.");
        }
    }

    @GetMapping("/user-details/{userId}")
    public ResponseEntity<?> getUserDetailsByAdmin(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable String userId
    ) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);

            Optional<User> requestingUserOpt = userRepository.findByEmail(username);
            if (requestingUserOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token or user not found.");
            }

            User requestingUser = requestingUserOpt.get();
            if (requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only ADMIN can access user details.");
            }

            User user = userRepository.findByUserId(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }

            Optional<UserDetails> userDetailsOpt = userDetailsRepository.findByUser(user);
            if (userDetailsOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User details not found.");
            }

            return ResponseEntity.ok(userDetailsOpt.get());

        } catch (Exception e) {
            logger.error("Error fetching user details", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user details.");
        }
    }

    @GetMapping("/role-info")
    public ResponseEntity<?> getUserRoleInfo(@RequestHeader("Authorization") String tokenHeader) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);

            Optional<User> userOpt = userRepository.findByEmail(username);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token or user not found.");
            }

            User user = userOpt.get();

            Map<String, Object> response = new HashMap<>();
            response.put("userId", user.getUserId());
            response.put("mainRole", user.getMainRole());
            response.put("subRole", user.getSubRole());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error retrieving user role info", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving user role info.");
        }
    }


}
