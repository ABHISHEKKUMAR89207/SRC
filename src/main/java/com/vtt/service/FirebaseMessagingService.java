package com.vtt.service;


import com.vtt.entities.NotificationEntity;
import com.vtt.entities.User;
import com.vtt.repository.NotificationRepository;
import com.vtt.repository.UserRepository;
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
    @Autowired
    private UserRepository userRepository;

    public String sendNotificationByToken(NotificationEntity notificationEntity) {
        Notification notification = Notification.builder().setTitle(notificationEntity.getTitle()).setBody(notificationEntity.getBody())
//                .setImage(notificationEntity.getImage())
                .build();

        Message message = Message.builder().setToken(notificationEntity.getRecipientToken()).setNotification(notification).build();

        try {
            firebaseMessaging.send(message);
            return "Success Sending Notification";

        } catch (FirebaseMessagingException ex) {
            ex.printStackTrace();
            return "Error Sending Notification";
        }

    }

    //send for all user notification
    public String sendNotificationToAllUsers(NotificationEntity notificationEntity) {
        List<User> users = userRepository.findAll(); // Retrieve all users from the database

        for (User user : users) {
            NotificationEntity userNotification = new NotificationEntity();
            userNotification.setRecipientToken(user.getNotificationToken());
            // Set other notification details (title, body, etc.)
            userNotification.setTitle(notificationEntity.getTitle());
            userNotification.setBody(notificationEntity.getBody());
            // Send notification to each user
            sendNotificationByToken(userNotification);
        }

        return "Notifications sent to all users";
    }
}
