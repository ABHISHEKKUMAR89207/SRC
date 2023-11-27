package com.example.jwt.service;


import com.example.jwt.controler.NotificationController;
import com.example.jwt.entities.NotificationEntity;
import com.example.jwt.entities.User;
import com.example.jwt.repository.NotificationRepository;
import com.example.jwt.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class NotificationService {

//    @Autowired
//    private NotificationRepository notificationRepository;

    @Autowired
    private FirebaseMessagingService firebaseMessagingService;

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }


    @Transactional
    public void scheduleNotification(String username, NotificationEntity request) {
        // Retrieve the user by username
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if a notification with the same type for the same user exists
        NotificationEntity existingNotification = notificationRepository.findByUserAndNotificationType(
                user, request.getNotificationType());

        // Associate the user with the notification
        request.setUser(user);

        // Set the time zone to Indian Standard Time (IST)
        ZoneId indianTimeZone = ZoneId.of("Asia/Kolkata");

        // Set the start time and last time in the Indian time zone
        request.setStartTime(
                LocalTime.from(ZonedDateTime.of(
                        LocalDate.now(),
                        request.getStartTime(),
                        ZoneId.systemDefault()
                ).withZoneSameInstant(indianTimeZone).toLocalDateTime())
        );
        request.setLastTime(
                LocalTime.from(ZonedDateTime.of(
                        LocalDate.now(),
                        request.getLastTime(),
                        ZoneId.systemDefault()
                ).withZoneSameInstant(indianTimeZone).toLocalDateTime())
        );

        if (existingNotification == null) {
            // If a notification with the same type doesn't exist, save the new notification
            notificationRepository.save(request);
        } else {
            // If a notification with the same type exists, update the existing notification
            existingNotification.setStartTime(request.getStartTime());
            existingNotification.setLastTime(request.getLastTime());

            notificationRepository.save(existingNotification);
        }
    }


    private void saveNotificationWithIndianTimeZone(String username, NotificationEntity request, ZoneId indianTimeZone) {
        // Retrieve the user by username
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Associate the user with the notification
        request.setUser(user);

        // Set the start time and last time in the Indian time zone
        request.setStartTime(
                LocalTime.from(ZonedDateTime.of(
                        LocalDateTime.of(LocalDate.now(), request.getStartTime()),
                        ZoneId.systemDefault()
                ).withZoneSameInstant(indianTimeZone).toLocalDateTime())
        );
        request.setLastTime(
                LocalTime.from(ZonedDateTime.of(
                        LocalDateTime.of(LocalDate.now(), request.getLastTime()),
                        ZoneId.systemDefault()
                ).withZoneSameInstant(indianTimeZone).toLocalDateTime())
        );

        // Save the notification
        notificationRepository.save(request);
    }






    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);



//previous final implimentation

//    @Scheduled(fixedDelay = 60000) // Run every minute, adjust as needed
//    public void sendScheduledNotifications() {
//        // Fetch all notifications that have a startTime matching the current time
//        List<NotificationEntity> notifications = getNotificationsForCurrentTime();
//
//        for (NotificationEntity notification : notifications) {
//            // Fetch the associated user for the notification
//            User user = notification.getUser();
//
//            // Check if the user is not null and has a notification token
//            if (user != null && user.getNotificationToken() != null) {
//                // Create a new notification
//                NotificationEntity newNotification = createNotificationForUser(user);
//
//                // Set the recipient token for the notification
//                newNotification.setRecipientToken(user.getNotificationToken());
//
//                // Send the notification
//                log.debug("Sending notification: {}", newNotification);
//                firebaseMessagingService.sendNotificationByToken(newNotification);
//            } else {
//                // Handle the case where the user or the notification token is null
//                // You might want to log a warning or handle it based on your requirements
//                log.warn("Invalid user or notification token for notification: {}", notification.getId());
//            }
//        }
//    }

    private List<NotificationEntity> getNotificationsForCurrentTime() {
        LocalTime currentTime = LocalTime.now();

        // Fetch notifications where startTime is within a certain range of the current time
        return notificationRepository.findByStartTimeBetween(currentTime.minusMinutes(1), currentTime.plusMinutes(1));
    }

//
    @Scheduled(fixedDelay = 60000) // Run every minute, adjust as needed
    public void sendScheduledNotifications() {
        // Fetch all notifications that have a startTime matching the current time
        List<NotificationEntity> notifications = getNotificationsForCurrentTime();

        for (NotificationEntity notification : notifications) {
            // Fetch the associated user for the notification
            User user = notification.getUser();

            // Check if the user is not null and has a notification token
            if (user != null && user.getNotificationToken() != null) {
                // Check if the user has notificationOn set to true
                if (user.getAllToggle() != null && user.getAllToggle().isNotificationOn()) {
                    // Create a new notification
                    NotificationEntity newNotification = createNotificationForUser(user);

                    // Set the recipient token for the notification
                    newNotification.setRecipientToken(user.getNotificationToken());

                    // Send the notification
                    log.debug("Sending notification: {}", newNotification);
                    firebaseMessagingService.sendNotificationByToken(newNotification);
                } else {
                    // Log a message or handle the case where the user has notificationOn set to false
                    log.debug("User {} has notificationOn set to false. Skipping notification.", user.getUsername());
                }
            } else {
                // Handle the case where the user or the notification token is null
                // You might want to log a warning or handle it based on your requirements
                log.warn("Invalid user or notification token for notification: {}", notification.getId());
            }
        }
    }



//previous final implimentation

    private NotificationEntity createNotificationForUser(User user) {
        NotificationEntity newNotification = new NotificationEntity();

        // Set other notification details based on your requirements
        newNotification.setUser(user);
        newNotification.setTitle("Hi " + user.getUserProfile().getFirstName());

        // Filter notifications based on the current time
        List<NotificationEntity> currentNotifications = user.getNotifications()
                .stream()
                .filter(notification -> {
                    LocalTime currentTime = LocalTime.now();
                    return notification.getStartTime().truncatedTo(ChronoUnit.MINUTES).equals(currentTime.truncatedTo(ChronoUnit.MINUTES));
                })
                .collect(Collectors.toList());

        if (!currentNotifications.isEmpty()) {
            // Use the first matching notification for the current time
            NotificationEntity matchingNotification = currentNotifications.get(0);
            newNotification.setBody("Time to " + matchingNotification.getNotificationType());
        } else {
            // Handle the case where there are no notifications for the current time
            log.warn("No matching notifications found for user: {}", user.getUserId());
        }

        return newNotification;
    }


//    private NotificationEntity createNotificationForUser(User user) {
//        NotificationEntity newNotification = new NotificationEntity();
//
//        // Set other notification details based on your requirements
//        newNotification.setUser(user);
//        newNotification.setTitle("Hi " + user.getUserProfile().getFirstName());
//
//        // Filter notifications based on the current time
//        List<NotificationEntity> currentNotifications = user.getNotifications()
//                .stream()
//                .filter(notification -> {
//                    LocalDateTime currentTime = LocalDateTime.now();
//                    LocalTime notificationTime = notification.getStartTime(); // Assuming getStartTime() returns a LocalDateTime
//                    return notificationTime.plusMinutes(1).truncatedTo(ChronoUnit.MINUTES).equals(currentTime.truncatedTo(ChronoUnit.MINUTES));
//                })
//                .collect(Collectors.toList());
//
//        if (!currentNotifications.isEmpty()) {
//            // Use the first matching notification for the current time
//            NotificationEntity matchingNotification = currentNotifications.get(0);
//            newNotification.setBody("Time to " + matchingNotification.getNotificationType());
//        } else {
//            // Handle the case where there are no notifications for the current time
//            log.warn("No matching notifications found for user: {}", user.getUserId());
//        }
//
//        return newNotification;
//    }


}