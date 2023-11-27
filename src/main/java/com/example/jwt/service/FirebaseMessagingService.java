package com.example.jwt.service;


import com.example.jwt.entities.NotificationEntity;
import com.example.jwt.entities.User;
import com.example.jwt.repository.NotificationRepository;
import com.example.jwt.repository.UserRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FirebaseMessagingService {

    @Autowired
    private FirebaseMessaging firebaseMessaging;

    @Autowired
    private NotificationRepository notificationRepository;

    public String sendNotificationByToken(NotificationEntity notificationEntity)
    {
        Notification notification = Notification
                .builder()
                .setTitle(notificationEntity.getTitle())
                .setBody(notificationEntity.getBody())
//                .setImage(notificationEntity.getImage())
                .build();

        Message message = Message
                .builder()
                .setToken(notificationEntity.getRecipientToken())
                .setNotification(notification)
                .build();

        try {
            firebaseMessaging.send(message);
            return "Success Sending Notification";

        } catch (FirebaseMessagingException ex) {
            ex.printStackTrace();
            return "Error Sending Notification";
        }

    }

//
//    public String sendNotificationByToken(NotificationEntity notificationEntity) {
//        if (notificationEntity.shouldSendNotification() && notificationEntity.getRecipientToken() != null) {
//            Notification notification = Notification
//                    .builder()
//                    .setTitle(notificationEntity.getTitle())
//                    .setBody(notificationEntity.getBody())
//                    // .setImage(notificationEntity.getImage())
//                    .build();
//
//            Message message = Message
//                    .builder()
//                    .setToken(notificationEntity.getRecipientToken())
//                    .setNotification(notification)
//                    .build();
//
//            try {
//                firebaseMessaging.send(message);
//                return "Success Sending Notification";
//
//            } catch (FirebaseMessagingException ex) {
//                ex.printStackTrace();
//                return "Error Sending Notification";
//            }
//        } else {
//            return "Notification will be sent automatically at the specified startTime.";
//        }
//    }
//
//





//final
//    @Scheduled(fixedRate = 60000) // Run every minute, adjust the rate as needed
//    public void scheduleNotifications() {
//        // Get the current time
//        LocalTime currentTime = LocalTime.now();
//
//        // Find notifications within the specified time range with notificationOn set to true
//        List<NotificationEntity> notificationsToSend = notificationRepository
//                .findByStartTimeBeforeAndNotificationOnIsTrue(currentTime);
//
//        // Send notifications
//        for (NotificationEntity notification : notificationsToSend) {
//            sendNotificationToAllUsers(notification);
//        }
//    }








    //send for all user notification

    @Autowired
    private UserRepository userRepository; // Assuming you have a UserRepository

    public String sendNotificationToAllUsers(NotificationEntity notificationEntity) {
        List<User> users = userRepository.findAll(); // Retrieve all users from the database

        for (User user : users) {
            NotificationEntity userNotification = new NotificationEntity();
            userNotification.setRecipientToken(user.getNotificationToken()); // Assuming each user has a notification token field

            // Set other notification details (title, body, etc.)
            userNotification.setTitle(notificationEntity.getTitle());
            userNotification.setBody(notificationEntity.getBody());
            // Set other fields as needed

            sendNotificationByToken(userNotification); // Send notification to each user
        }

        return "Notifications sent to all users";
    }



}
