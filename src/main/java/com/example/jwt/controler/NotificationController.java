package com.example.jwt.controler;

import com.example.jwt.dtos.NotificationDetailsDTO;
import com.example.jwt.entities.NotificationEntity;



import com.example.jwt.entities.User;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.FirebaseMessagingService;
import com.example.jwt.service.NotificationService;
import com.example.jwt.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private FirebaseMessagingService firebaseMessagingService;

    @Autowired
    private JwtHelper jwtHelper;
    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

    // for scheduling the notification
    @PostMapping("/scheduleNotification")
    public ResponseEntity<String> scheduleNotification(
            @RequestBody NotificationEntity request,
            @RequestHeader("Auth") String tokenHeader
    ) {
        try {
            // Logging to check the received data
            log.info("Received request: {}", request);

            // Extract the JWT token from the Authorization header
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username from the JWT token
            String username = jwtHelper.getUsernameFromToken(token);

            // Schedule or update the notification for the user
            notificationService.scheduleNotification(username, request);

            return ResponseEntity.ok("Notification scheduled or updated successfully!");
        } catch (Exception e) {
            // Handle exceptions appropriately (e.g., log, return an error response)
            log.error("Failed to schedule or update notification", e);
            return ResponseEntity.status(500).body("Failed to schedule or update notification");
        }
    }




    @GetMapping("/get-user-notifications")
    public ResponseEntity<List<NotificationDetailsDTO>> getUserNotifications(
            @RequestHeader("Auth") String tokenHeader
    ) {
        try {
            // Extract the JWT token from the Authorization header
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username from the JWT token
            String username = jwtHelper.getUsernameFromToken(token);

            // Retrieve notifications for the user where notificationOn is true
            List<NotificationDetailsDTO> userNotifications = notificationService.getUserNotifications(username);

            return ResponseEntity.ok(userNotifications);
        } catch (Exception e) {
            // Handle exceptions appropriately (e.g., log, return an error response)
            log.error("Failed to retrieve user notifications", e);
            return ResponseEntity.status(500).body(null);
        }
    }


@Autowired
private UserService userService;

//    del by waris
@DeleteMapping("/delete-notification/{notificationId}")
public ResponseEntity<String> deleteNotification(
        @PathVariable Long notificationId,
        @RequestHeader("Auth") String tokenHeader
) {
    try {
        // Extract the JWT token from the Authorization header
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username from the JWT token
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(username);

        // Delete the notification for the user
        notificationService.deleteNotification(user, notificationId);

        return ResponseEntity.ok("Notification deleted successfully");
    } catch (Exception e) {
        // Handle exceptions appropriately (e.g., log, return an error response)
        log.error("Failed to delete notification", e);
        return ResponseEntity.status(500).body("Failed to delete notification");
    }
}

// NotificationController.java

    @PutMapping("/update-toggle-notification-type")
    public ResponseEntity<Void> toggleNotification(
            @RequestHeader("Auth") String tokenHeader,
            @RequestParam String notificationType,
            @RequestParam boolean notificationOn) {
        try {
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username from the JWT token
            String username = jwtHelper.getUsernameFromToken(token);

            // Toggle notifications for the user and specific notification type
            notificationService.toggleNotification(username, notificationType, notificationOn);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


//    @GetMapping("/get-toggle-notification-type")
//    public ResponseEntity<Boolean> toggleNotificationType(
//            @RequestHeader("Auth") String tokenHeader,
//            @RequestParam String notificationType
//    ) {
//        try {
//            String token = tokenHeader.replace("Bearer ", "");
//
//            // Extract the username from the JWT token
//            String username = jwtHelper.getUsernameFromToken(token);
//            User user = userService.findByUsername(username);
//            // Assuming you have a method in your service to get the notification for the user and type
//            boolean isNotificationTypeOn = notificationService.isNotificationTypeOn(user, notificationType);
//
//            return ResponseEntity.ok(isNotificationTypeOn);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }



//    @PutMapping("/{notificationId}/toggle")
//    public ResponseEntity<Void> toggleNotification(
//            @PathVariable Long notificationId,
//            @RequestParam boolean notificationOn) {
//        try {
//            notificationService.toggleNotification(notificationId, notificationOn);
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

//
//@PostMapping("/remainder")
//public String sendReminderNotificationByType(@RequestBody NotificationEntity notificationEntity) {
//    if (notificationEntity.shouldSendNotification()) {
//        return firebaseMessagingService.sendNotificationByToken(notificationEntity);
//    } else {
//        return "Notification will be sent automatically at the specified startTime.";
//    }
//}



//for feature implimentation

    @PostMapping("/sendToAllUsers")
    public String sendNotificationToAll(@RequestBody NotificationEntity notificationEntity) {
        return firebaseMessagingService.sendNotificationToAllUsers(notificationEntity);
    }

//    @PostMapping("/sendToAllUsers")
//    public String sendNotificationToAll(
//            @RequestBody NotificationEntity notificationEntity,
//            @RequestHeader("Auth") String tokenHeader
//    ) {
//        // Extract the JWT token from the Authorization header
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username from the JWT token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        // Check if the user has admin privileges
//        if (isAdmin(username)) {
//            return firebaseMessagingService.sendNotificationToAllUsers(notificationEntity);
//        } else {
//            return "User does not have admin privileges";
//        }
//    }


//    private boolean isAdmin(String username) {
//        // Assuming you have a way to check if the user has admin privileges
//        // You can query the database or use any other mechanism based on your application design
//        // For example, if you have a 'roles' field in the User entity, check if it contains 'ROLE_ADMIN'
//        User user = userRepository.findByEmail(username).orElse(null);
//        return user != null && user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
//    }
//
//    public String sendNotificationToAllUsers(NotificationEntity notificationEntity) {
//        List<User> users = userRepository.findAll();
//
//        for (User user : users) {
//            NotificationEntity userNotification = new NotificationEntity();
//            userNotification.setRecipientToken(user.getNotificationToken());
//
//            // Set other notification details (title, body, etc.)
//            userNotification.setTitle(notificationEntity.getTitle());
//            userNotification.setBody(notificationEntity.getBody());
//            // Set other fields as needed
//
//            sendNotificationByToken(userNotification);
//        }
//
//        return "Notifications sent to all users";
//    }






    }


