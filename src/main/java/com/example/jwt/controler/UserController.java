package com.example.jwt.controler;



import com.example.jwt.entities.User;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {



    @Autowired
    private JwtHelper jwtHelper;

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PutMapping("/update-notification-token")
    public ResponseEntity<User> updateNotificationToken(
            @RequestHeader("Auth") String authorizationHeader,
            @RequestParam String newToken) {

        String token = authorizationHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Get the user by username and update the notification token
        User updatedUser = userService.updateUserNotificationToken(username, newToken);

        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            // Handle the case when the user with the given username is not found
            return ResponseEntity.notFound().build();
        }
    }


}

