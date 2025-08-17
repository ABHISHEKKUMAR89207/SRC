package com.vtt.controllers;



import com.vtt.entities.User;
import com.vtt.service.UserService;
import com.vtt.security.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@CrossOrigin(origins = "*")


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class ProfileController {

    private final UserService userService;
    private final JwtHelper jwtHelper;
    private final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    // Endpoint to get user details (username, email, phone number)
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String tokenHeader) {


        System.out.println("tokene is here"+tokenHeader);
        try {
            // Remove the "Bearer " prefix from the token
            String token = tokenHeader.replace("Bearer ", "");

            System.out.println("BEARE-----------"+token);

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);
            logger.info("Extracted Username: " + username);

            // Fetch the user data from UserService
            User user = userService.findByUsername(username);

            if (user != null) {
                // Return a response with the user details (username, email, phone number)
                return ResponseEntity.ok(new UserProfileResponse(user.getUserName(), user.getEmail(), user.getMobileNo()));
            } else {
                return ResponseEntity.status(404).body("User not found");
            }

        } catch (Exception e) {
            logger.error("Error while retrieving user profile: ", e);
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    // DTO class to return user details in response
    public static class UserProfileResponse {
        private String username;
        private String email;
        private String phoneNumber;

        public UserProfileResponse(String username, String email, String phoneNumber) {
            this.username = username;
            this.email = email;
            this.phoneNumber = phoneNumber;
        }

        // Getters and Setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }
}
