package com.vtt.controller;

import com.vtt.security.JwtHelper;
import com.vtt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

//    // to update the verification token
//    @PutMapping("/update-notification-token")
//    public ResponseEntity<User> updateNotificationToken(@RequestHeader("Auth") String authorizationHeader, @RequestParam String newToken) {
//
//        String token = authorizationHeader.replace("Bearer ", "");
//
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        // Get the user by username and update the notification token
//        User updatedUser = userService.updateUserNotificationToken(username, newToken);
//
//        if (updatedUser != null) {
////            return ResponseEntity.ok(updatedUser);
//            return ResponseEntity.ok().build();
//        } else {
//            // Handle the case when the user with the given username is not found
//            return ResponseEntity.notFound().build();
//        }
//    }
}

