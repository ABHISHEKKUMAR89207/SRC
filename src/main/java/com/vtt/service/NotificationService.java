package com.vtt.service;


import com.vtt.controller.NotificationController;
import com.vtt.dtos.NotificationDetailsDTO;
import com.vtt.dtos.NotifySendSuccessDTO;
import com.vtt.entities.NotificationEntity;
import com.vtt.entities.NotifySendSuccess;
import com.vtt.entities.User;
import com.vtt.repository.NotificationRepository;
import com.vtt.repository.NotifySendSuccessRepository;
import com.vtt.repository.UserRepository;
//import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    @Autowired
    private FirebaseMessagingService firebaseMessagingService;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public void toggleNotification(String username, String notificationType, boolean notificationOn) {
        List<NotificationEntity> userNotifications = notificationRepository.findByUserEmail(username);

        for (NotificationEntity notification : userNotifications) {
            // Check if the notification has the target type
            if (notification.hasNotificationType(notificationType)) {
                notification.setNotificationOn(notificationOn);
                notificationRepository.save(notification);
            }
        }
    }


    // Assuming you have a method to fetch a notification by type and user
//    public boolean isNotificationTypeOn(User user, String notificationType) {
//        NotificationEntity notification = notificationRepository.findByUserAndNotificationType(user, notificationType);
//
//        // Check if notification is not null and its notificationOn is true
//        return notification != null && notification.isNotificationOn();
//    }

    // for scheduling the notification time
//@Autowired
//private AllToggleRepository allToggleRepository;
//
//    public List<NotificationDetailsDTO> getUserNotifications(String email) {
//        // Retrieve the AllToggle entity for the user
//        Optional<AllToggle> allToggleOptional = allToggleRepository.findByUserEmail(email);
//
//        // Check if the AllToggle entity is present and has notificationOn set to true
//        if (allToggleOptional.isPresent() && allToggleOptional.get().isNotificationOn()) {
//            // Retrieve notifications for the user
//            List<NotificationEntity> notifications = notificationRepository.findByUserEmail(email);
//
//            // Convert notifications to DTOs
//            return notifications.stream()
//                    .map(this::convertToDTO)
//                    .collect(Collectors.toList());
//        }
//
//        // Return an empty list if notifications are not enabled for the user
//        return List.of();
//    }

    private NotificationDetailsDTO convertToDTO(NotificationEntity notificationEntity) {
        // Format LocalTime fields as strings
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        return new NotificationDetailsDTO(
                notificationEntity.getId(),
                notificationEntity.getRecipientToken(),
                notificationEntity.getTitle(),
                notificationEntity.getBody(),
                notificationEntity.getStartTime() != null ? notificationEntity.getStartTime().format(timeFormatter) : null,
                notificationEntity.getLastTime() != null ? notificationEntity.getLastTime().format(timeFormatter) : null,
                notificationEntity.getNotificationType(),
                notificationEntity.isNotificationOn()
                // Add other fields as needed
        );
    }

//    @Transactional
//@Transactional
//public void deleteNotification(User user, Long notificationId) {
//    try {
//        Long userId = user.getUserId();
//        notificationRepository.deleteByIdAndUser_UserId(notificationId, userId);
//        log.info("Notification deleted successfully: {}", notificationId);
//    } catch (Exception e) {
//        log.error("Failed to delete notification with ID {}", notificationId, e);
//        throw new RuntimeException("Failed to delete notification", e);
//    }
//}
//old
//    public void deleteNotification(User user, Long notificationId) {
//        try {
//            Optional<NotificationEntity> notificationOptional = notificationRepository.findById(notificationId);
//
//            if (notificationOptional.isPresent()) {
//                NotificationEntity notification = notificationOptional.get();
//
//                // Remove the notification from the user's collection
//                user.getNotifications().remove(notification);
//
//                notificationRepository.delete(notification);
//                log.info("Notification deleted successfully: {}", notificationId);
//            } else {
//                log.error("Notification with ID {} not found", notificationId);
//                throw new RuntimeException("Notification not found");
//            }
//        } catch (Exception e) {
//            log.error("Failed to delete notification with ID {}", notificationId, e);
//            throw new RuntimeException("Failed to delete notification", e);
//        }
//    }
public void deleteNotification(User user, Long notificationId) {
    try {
        Optional<NotificationEntity> notificationOptional = notificationRepository.findById(notificationId);

        if (notificationOptional.isPresent()) {
            NotificationEntity notification = notificationOptional.get();

            // Remove associated NotifySendSuccess records
            List<NotifySendSuccess> notifySendSuccessList = notifySendSuccessRepository.findByNotificationEntity(notification);
            notifySendSuccessList.forEach(notifySendSuccess -> notifySendSuccess.setUser(null)); // Or handle deletion/update as per your application logic
            notifySendSuccessRepository.deleteAll(notifySendSuccessList);

            // Remove the notification from the user's collection
            user.getNotifications().remove(notification);

            notificationRepository.delete(notification);
            log.info("Notification deleted successfully: {}", notificationId);
        } else {
            log.error("Notification with ID {} not found", notificationId);
            throw new RuntimeException("Notification not found");
        }
    } catch (Exception e) {
        log.error("Failed to delete notification with ID {}", notificationId, e);
        throw new RuntimeException("Failed to delete notification", e);
    }
}


//    @Transactional
//    public void scheduleNotification(String username, NotificationEntity request) {
//        // Retrieve the user by username
//        User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));
//
//        // Check if a notification with the same type for the same user exists
//        NotificationEntity existingNotification = notificationRepository.findByUserAndNotificationType(user, request.getNotificationType());
//
//        // Associate the user with the notification
//        request.setUser(user);
//
//        // Set the time zone to Indian Standard Time (IST)
//        ZoneId indianTimeZone = ZoneId.of("Asia/Kolkata");
//
//        // Set the start time and last time in the Indian time zone
//        request.setStartTime(LocalTime.from(ZonedDateTime.of(LocalDate.now(), request.getStartTime(), indianTimeZone).toLocalDateTime()));
//        request.setLastTime(LocalTime.from(ZonedDateTime.of(LocalDate.now(), request.getLastTime(), indianTimeZone).toLocalDateTime()));
//
//        // Convert to UTC for scheduling comparisons
//        ZonedDateTime utcStartTime = request.getStartTime().atDate(LocalDate.now()).atZone(indianTimeZone).withZoneSameInstant(ZoneId.of("UTC"));
//        ZonedDateTime utcLastTime = request.getLastTime().atDate(LocalDate.now()).atZone(indianTimeZone).withZoneSameInstant(ZoneId.of("UTC"));
//
//        if (existingNotification == null) {
//            // If a notification with the same type doesn't exist, save the new notification
//            notificationRepository.save(request);
//        } else {
//            // If a notification with the same type exists, update the existing notification
//            existingNotification.setStartTime(request.getStartTime());
//            existingNotification.setLastTime(request.getLastTime());
//
//            notificationRepository.save(existingNotification);
//        }
//
//        // Now you can use utcStartTime and utcLastTime for scheduling comparisons
//        ZonedDateTime currentUtcTime = ZonedDateTime.now(ZoneId.of("UTC"));
//        if (currentUtcTime.isAfter(utcStartTime) && currentUtcTime.isBefore(utcLastTime)) {
//            // Perform your scheduling logic here
//            if (user != null && user.getNotificationToken() != null) {
//                // Check if the user has notificationOn set to true
//                if (user.getAllToggle() != null && user.getAllToggle().isNotificationOn()) {
//                    // Create a new notification
//                    NotificationEntity newNotification = createNotificationForUser(user);
//
//                    // Set the recipient token for the notification
//                    newNotification.setRecipientToken(user.getNotificationToken());
//
//                    // Send the notification
//                    log.debug("Sending scheduled notification: {}", newNotification);
//                    firebaseMessagingService.sendNotificationByToken(newNotification);
//                } else {
//                    // Log a message or handle the case where the user has notificationOn set to false
//                    log.debug("User {} has notificationOn set to false. Skipping scheduled notification.", user.getUsername());
//                }
//            } else {
//                // Handle the case where the user or the notification token is null
//                // You might want to log a warning or handle it based on your requirements
//                log.warn("Invalid user or notification token for scheduled notification.");
//            }
//        }
//    }

//    @Transactional
//    public void scheduleNotification(String username, NotificationEntity request) {
//        // Retrieve the user by username
//        User user = userRepository.findByEmail(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // Check if a notification with the same type for the same user exists
//        NotificationEntity existingNotification = notificationRepository.findByUserAndNotificationType(user, request.getNotificationType());
//
//        // Associate the user with the notification
//        request.setUser(user);
//
//        // Set the time zone to Indian Standard Time (IST)
//        ZoneId indianTimeZone = ZoneId.of("Asia/Kolkata");
//
//        // Convert the start time and last time to Indian Standard Time (IST)
//        ZonedDateTime startTimeIST = ZonedDateTime.of(LocalDate.now(), request.getStartTime(), ZoneId.of("UTC")).withZoneSameInstant(indianTimeZone);
//        ZonedDateTime lastTimeIST = ZonedDateTime.of(LocalDate.now(), request.getLastTime(), ZoneId.of("UTC")).withZoneSameInstant(indianTimeZone);
//
//        // Set the start time and last time in the Indian time zone
//        request.setStartTime(startTimeIST.toLocalTime());
//        request.setLastTime(lastTimeIST.toLocalTime());
//
//        if (existingNotification == null) {
//            // If a notification with the same type doesn't exist, save the new notification
//            notificationRepository.save(request);
//        } else {
//            // If a notification with the same type exists, update the existing notification
//            existingNotification.setStartTime(request.getStartTime());
//            existingNotification.setLastTime(request.getLastTime());
//
//            notificationRepository.save(existingNotification);
//        }
//
//        // Now you can use startTimeIST and lastTimeIST for scheduling comparisons
//        ZonedDateTime currentUtcTime = ZonedDateTime.now(ZoneId.of("UTC"));
//        if (currentUtcTime.isAfter(startTimeIST) && currentUtcTime.isBefore(lastTimeIST)) {
//            // Perform your scheduling logic here
//            if (user != null && user.getNotificationToken() != null) {
//                // Check if the user has notificationOn set to true
//                if (user.getAllToggle() != null && user.getAllToggle().isNotificationOn()) {
//                    // Create a new notification
//                    NotificationEntity newNotification = createNotificationForUser(user);
//
//                    // Set the recipient token for the notification
//                    newNotification.setRecipientToken(user.getNotificationToken());
//
//                    // Send the notification
//                    log.debug("Sending scheduled notification: {}", newNotification);
//                    firebaseMessagingService.sendNotificationByToken(newNotification);
//                } else {
//                    // Log a message or handle the case where the user has notificationOn set to false
//                    log.debug("User {} has notificationOn set to false. Skipping scheduled notification.", user.getUsername());
//                }
//            } else {
//                // Handle the case where the user or the notification token is null
//                // You might want to log a warning or handle it based on your requirements
//                log.warn("Invalid user or notification token for scheduled notification.");
//            }
//        }
//    }
    // to save notification in backend with Indian time zone
    private void saveNotificationWithIndianTimeZone(String username, NotificationEntity request, ZoneId indianTimeZone) {
        // Retrieve the user by username
        User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));

        // Associate the user with the notification
        request.setUser(user);

        // Set the start time and last time in the Indian time zone
        request.setStartTime(LocalTime.from(ZonedDateTime.of(LocalDateTime.of(LocalDate.now(), request.getStartTime()), ZoneId.systemDefault()).withZoneSameInstant(indianTimeZone).toLocalDateTime()));
        request.setLastTime(LocalTime.from(ZonedDateTime.of(LocalDateTime.of(LocalDate.now(), request.getLastTime()), ZoneId.systemDefault()).withZoneSameInstant(indianTimeZone).toLocalDateTime()));

        // Save the notification
        notificationRepository.save(request);
    }

    // to get notification for current time
//    private List<NotificationEntity> getNotificationsForCurrentTime() {
//        ZoneId indianTimeZone = ZoneId.of("Asia/Kolkata");
//        LocalTime currentTime = LocalTime.now(indianTimeZone);
//
//        // Fetch notifications where startTime is exactly at the current time
//        return notificationRepository.findAll().stream().filter(notification -> {
//            LocalTime startTime = notification.getStartTime(); // Assuming getStartTime() already returns LocalTime
//            return currentTime.getHour() == startTime.getHour() && currentTime.getMinute() == startTime.getMinute();
//        }).collect(Collectors.toList());
//    }


    private List<NotificationEntity> getNotificationsForCurrentTime() {
        ZoneId indianTimeZone = ZoneId.of("Asia/Kolkata");
        LocalTime currentTime = LocalTime.now(indianTimeZone);
        System.out.println("Current Time ----------"+currentTime);
        // Fetch notifications where startTime is exactly at the current time
        return notificationRepository.findAll().stream()
                .filter(notification -> {
                    LocalTime startTime = notification.getStartTime();
                    System.out.println("Start Time --------- "+startTime);
                    // Check if startTime is not null before accessing its properties
                    return startTime != null &&
                            currentTime.getHour() == startTime.getHour() &&
                            currentTime.getMinute() == startTime.getMinute();

                })
                .collect(Collectors.toList());
    }


    // for seding notification at a fixed time interval
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
//                // Check if the user has notificationOn set to true
//                if (user.getAllToggle() != null && user.getAllToggle().isNotificationOn()) {
//                    // Create a new notification
//                    NotificationEntity newNotification = createNotificationForUser(user);
//
//                    // Set the recipient token for the notification
//                    newNotification.setRecipientToken(user.getNotificationToken());
//
//                    // Send the notification
//                    log.debug("Sending notification: {}", newNotification);
//                    firebaseMessagingService.sendNotificationByToken(newNotification);
//                } else {
//                    // Log a message or handle the case where the user has notificationOn set to false
//                    log.debug("User {} has notificationOn set to false. Skipping notification.", user.getUsername());
//                }
//            } else {
//                // Handle the case where the user or the notification token is null
//                // You might want to log a warning or handle it based on your requirements
//                log.warn("Invalid user or notification token for notification: {}", notification.getId());
//            }
//        }
//    }


//    @Scheduled(fixedDelay = 60000) // Run every minute, adjust as needed
//    public void sendScheduledNotifications() {
//        // Fetch all notifications that have a startTime matching the current time
//        List<NotificationEntity> notifications = getNotificationsForCurrentTime();
//
//        for (NotificationEntity notification : notifications) {
//            log.debug("Processing notification: {}", notification.getId());
//
//            // Fetch the associated user for the notification
//            User user = notification.getUser();
//
//            // Check if the user is not null and has a notification token
//            if (user != null && user.getNotificationToken() != null) {
//                // Check if the user has notificationOn set to true in AllToggle
//                if (user.getAllToggle() != null && user.getAllToggle().isNotificationOn()) {
//                    log.debug("AllToggle notificationOn is true for user: {}", user.getUsername());
//                    // Check if the notificationType's notificationOn is true
//                    if (user.getNotifications().stream().anyMatch(n ->
//                            n.getNotificationType().equals(notification.getNotificationType()) && n.isNotificationOn())) {
//                        log.debug("NotificationType {} notificationOn is true for user: {}", notification.getNotificationType(), user.getUsername());
//                        // Create a new notification
//                        NotificationEntity newNotification = createNotificationForUser(user);
//
//                        // Set the recipient token for the notification
//                        newNotification.setRecipientToken(user.getNotificationToken());
//
//                        // Send the notification
//                        log.debug("Sending notification: {}", newNotification);
//                        firebaseMessagingService.sendNotificationByToken(newNotification);
//
//                        // Save success details in NotificationSendSuccess
//                        saveNotificationSendSuccess(newNotification);
//                    } else {
//                        // Log a message or handle the case where the notificationType's notificationOn is false
//                        log.debug("User {} has notificationOn set to false for NotificationType {}. Skipping notification.",
//                                user.getUsername(), notification.getNotificationType());
//                    }
//                } else {
//                    // Log a message or handle the case where the user's AllToggle has notificationOn set to false
//                    log.debug("User {} has notificationOn set to false in AllToggle. Skipping notification.",
//                            user.getUsername());
//                }
//            } else {
//                // Handle the case where the user or the notification token is null
//                // You might want to log a warning or handle it based on your requirements
//                log.warn("Invalid user or notification token for notification: {}", notification.getId());
//            }
//        }
//    }


//    @Scheduled(fixedDelay = 60000)
//    public void sendScheduledNotifications() {
////        List<NotificationEntity> notifications = getNotificationsForCurrentTime();
//        ZoneId indianTimeZone = ZoneId.of("Asia/Kolkata");
//        LocalDateTime currentDateTime = LocalDateTime.now(indianTimeZone);
//        log.debug("Current time in Indian timezone: {}", currentDateTime);
//
//        List<NotificationEntity> notifications = getNotificationsForCurrentTime();
//        log.debug("Found {} notifications for the current time.", notifications.size());
//
//        for (NotificationEntity notification : notifications) {
//            log.debug("Processing notification: {}", notification.getId());
//
//            User user = notification.getUser();
//
//            if (user != null && user.getNotificationToken() != null) {
//                if (user.getAllToggle() != null && user.getAllToggle().isNotificationOn()) {
//                    if (user.getNotifications().stream().anyMatch(n ->
//                            n.getNotificationType().equals(notification.getNotificationType()) && n.isNotificationOn())) {
//                        NotificationEntity newNotification = createNotificationForUser(user);
//                        newNotification.setRecipientToken(user.getNotificationToken());
//
//                        log.debug("Sending notification: {}", newNotification);
//                        firebaseMessagingService.sendNotificationByToken(newNotification);
//
//                        // Save success details in NotificationSendSuccess
//                        saveNotificationSendSuccess(notification);
//                    } else {
//                        log.debug("User {} has notificationOn set to false for NotificationType {}. Skipping notification.",
//                                user.getUsername(), notification.getNotificationType());
//                    }
//                } else {
//                    log.debug("User {} has notificationOn set to false in AllToggle. Skipping notification.",
//                            user.getUsername());
//                }
//            } else {
//                log.warn("Invalid user or notification token for notification: {}", notification.getId());
//            }
//        }
//    }
//@Scheduled(fixedDelay = 60000) // Run every minute, adjust as needed
//
//private void sendScheduledNotifications() {
//        // Fetch all notifications that have a startTime matching the current time
//        List<NotificationEntity> notifications = getNotificationsForCurrentTime();
//
//        for (NotificationEntity notification : notifications) {
//            log.debug("Processing notification: {}", notification.getId());
//
//            // Fetch the associated user for the notification
//            User user = notification.getUser();
//
//            // Check if the user is not null and has a notification token
//            if (user != null && user.getNotificationToken() != null) {
//                // Check if the user has notificationOn set to true in AllToggle
//                if (user.getAllToggle() != null && user.getAllToggle().isNotificationOn()) {
//                    log.debug("AllToggle notificationOn is true for user: {}", user.getUsername());
//                    // Check if the notificationType's notificationOn is true
//                    if (user.getNotifications().stream().anyMatch(n ->
//                            n.getNotificationType().equals(notification.getNotificationType()) && n.isNotificationOn())) {
//                        log.debug("NotificationType {} notificationOn is true for user: {}", notification.getNotificationType(), user.getUsername());
//                        // Create a new notification
//                        NotificationEntity newNotification = createNotificationForUser(user);
//
//                        // Set the recipient token for the notification
//                        newNotification.setRecipientToken(user.getNotificationToken());
//
//                        // Send the notification
//                        log.debug("Sending notification: {}", newNotification);
//                        firebaseMessagingService.sendNotificationByToken(newNotification);
//
//                        // Save success details in NotificationSendSuccess
//                        saveNotificationSendSuccess(notification);
//                    } else {
//                        // Log a message or handle the case where the notificationType's notificationOn is false
//                        log.debug("User {} has notificationOn set to false for NotificationType {}. Skipping notification.",
//                                user.getUsername(), notification.getNotificationType());
//                    }
//                } else {
//                    // Log a message or handle the case where the user's AllToggle has notificationOn set to false
//                    log.debug("User {} has notificationOn set to false in AllToggle. Skipping notification.",
//                            user.getUsername());
//                }
//            } else {
//                // Handle the case where the user or the notification token is null
//                // You might want to log a warning or handle it based on your requirements
//                log.warn("Invalid user or notification token for notification: {}", notification.getId());
//            }
//        }
//    }

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
//                // Check if the user has notificationOn set to true and notificationType matches
//                if (user.getAllToggle() != null && user.getAllToggle().isNotificationOn()
//                        && notification.getNotificationType().equals(user.getNotifications().getNotificationType())) {
//                    // Create a new notification
//                    NotificationEntity newNotification = createNotificationForUser(user);
//
//                    // Set the recipient token for the notification
//                    newNotification.setRecipientToken(user.getNotificationToken());
//
//                    // Send the notification
//                    log.debug("Sending notification: {}", newNotification);
//                    firebaseMessagingService.sendNotificationByToken(newNotification);
//                } else {
//                    // Log a message or handle the case where the user has notificationOn set to false or notificationType mismatch
//                    log.debug("User {} has notificationOn set to false or notificationType mismatch. Skipping notification.",
//                            user.getUsername());
//                }
//            } else {
//                // Handle the case where the user or the notification token is null
//                // You might want to log a warning or handle it based on your requirements
//                log.warn("Invalid user or notification token for notification: {}", notification.getId());
//            }
//        }
//    }
//

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
//                // Check if the user has notificationOn set to true for the specific notificationType
//                if (user.getAllToggle() != null && user.getAllToggle().isNotificationOn()
//                        && isNotificationTypeEnabled(user.getAllToggle(), notification.getNotificationType())) {
//                    // Create a new notification
//                    NotificationEntity newNotification = createNotificationForUser(user);
//
//                    // Set the recipient token for the notification
//                    newNotification.setRecipientToken(user.getNotificationToken());
//
//                    // Send the notification
//                    log.debug("Sending notification: {}", newNotification);
//                    firebaseMessagingService.sendNotificationByToken(newNotification);
//                } else {
//                    // Log a message or handle the case where the user has notificationOn set to false
//                    log.debug("User {} has notificationOn set to false or specific notification type is disabled. Skipping notification.",
//                            user.getUsername());
//                }
//            } else {
//                // Handle the case where the user or the notification token is null
//                // You might want to log a warning or handle it based on your requirements
//                log.warn("Invalid user or notification token for notification: {}", notification.getId());
//            }
//        }
//    }
//
//    private boolean isNotificationTypeEnabled(UserToggleSettings toggleSettings, String notificationType) {
//        switch (notificationType) {
//            case "breakfast":
//                return toggleSettings.isBreakfastNotificationOn();
//            // Add more cases for other notification types
//            default:
//                return true; // Default to true if the notification type is not recognized
//        }
//    }




//previous final implimentation

//    private NotificationEntity createNotificationForUser(User user) {
//        ZoneId indianTimeZone = ZoneId.of("Asia/Kolkata");
//        LocalDateTime currentDateTime = LocalDateTime.now(indianTimeZone);
//
//        NotificationEntity newNotification = new NotificationEntity();
//
//        // Set other notification details based on your requirements
//        newNotification.setUser(user);
//        newNotification.setTitle("Hi " + user.getUserProfile().getFirstName());
//
//        // Filter notifications based on the current time
//        List<NotificationEntity> currentNotifications = user.getNotifications().stream().filter(notification -> {
//            LocalDateTime notificationDateTime = notification.getStartTime().atDate(LocalDate.now()).atZone(indianTimeZone).toLocalDateTime();
//            return notificationDateTime.truncatedTo(ChronoUnit.MINUTES).equals(currentDateTime.truncatedTo(ChronoUnit.MINUTES));
//        }).collect(Collectors.toList());
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



    private NotificationEntity createNotificationForUser(User user) {
        ZoneId indianTimeZone = ZoneId.of("Asia/Kolkata");
        LocalDateTime currentDateTime = LocalDateTime.now(indianTimeZone);

        NotificationEntity newNotification = new NotificationEntity();

        // Set other notification details based on your requirements
        newNotification.setUser(user);
        newNotification.setTitle("Hi " + user.getUserProfile().getFirstName());

        // Filter notifications based on the current time
        List<NotificationEntity> currentNotifications = user.getNotifications().stream()
                .filter(notification -> {
                    LocalTime startTime = notification.getStartTime();
                    // Check if startTime is not null before accessing its properties
                    return startTime != null &&
                            startTime.atDate(LocalDate.now()).atZone(indianTimeZone).toLocalDateTime()
                                    .truncatedTo(ChronoUnit.MINUTES)
                                    .equals(currentDateTime.truncatedTo(ChronoUnit.MINUTES));
                })
                .collect(Collectors.toList());

//        if (!currentNotifications.isEmpty()) {
//            // Use the first matching notification for the current time
//            NotificationEntity matchingNotification = currentNotifications.get(0);
//            newNotification.setBody("Time for " + matchingNotification.getNotificationType());
//        } else
        if (!currentNotifications.isEmpty()) {
            NotificationEntity matchingNotification = currentNotifications.get(0);
            String notificationType = matchingNotification.getNotificationType();

//            if (notificationType.equals("Sleep") || notificationType.equals("Drinking Water")) {
//                newNotification.setBody("Time to " + notificationType);
//
//            }
            if (notificationType.equals("Sleep") ) {
                newNotification.setBody("Time to " + notificationType);

            }
            else if ( notificationType.equals("Drinking Water")) {
                newNotification.setBody("Time to Drink Water" );}
            else {
                newNotification.setBody("Time for " + notificationType);
            }
        }
//            else {
//            log.warn("No matching notifications found for user: {}", user.getUserId());



        {
            // Handle the case where there are no notifications for the current time
            log.warn("No matching notifications found for user: {}", user.getUserId());
        }

        return newNotification;
    }

    @Autowired
    private NotifySendSuccessRepository notifySendSuccessRepository;

//    private void saveNotificationSendSuccess(NotificationEntity notification) {
//        // Assuming you have a repository for NotificationSendSuccess
//        NotifySendSuccess successDetails = new NotifySendSuccess();
//        successDetails.setStartTime(LocalTime.now());
//        successDetails.setBody(notification.getNotificationType() + " notification send successfully");
//
//        // Save success details in the database
//        notifySendSuccessRepository.save(successDetails);
//    }
//private void saveNotificationSendSuccess(NotificationEntity notification) {
//    NotifySendSuccess successDetails = new NotifySendSuccess();
//    successDetails.setStartTime(notification.getStartTime());
//    successDetails.setUser(notification.getUser());
//    successDetails.setDesc(notification.getNotificationType() + " notification send successfully");
//
//    // Save the associated NotificationEntity first
////    NotificationEntity savedNotification = notificationRepository.save(notification);
//
//    // Set the reference to the saved NotificationEntity
////    successDetails.setNotificationEntity(savedNotification);
//
//    // Save success details in the database
//    notifySendSuccessRepository.save(successDetails);
//}
//private void saveNotificationSendSuccess(NotificationEntity notification) {
//    NotifySendSuccess successDetails = new NotifySendSuccess();
//    successDetails.setUser(notification.getUser());
//    successDetails.setDesc(notification.getNotificationType() + " notification send successfully");
//
//    LocalTime startTime = notification.getStartTime();
//
//    // Check if startTime is not null before accessing its properties
//    if (startTime != null) {
//        successDetails.setStartTime(startTime);
//    } else {
//        // Handle the case where startTime is null
//        log.warn("NotificationEntity with null startTime encountered while saving NotifySendSuccess.");
//
//        // Set startTime to the current time
//        successDetails.setStartTime(LocalTime.now());
//    }
//
//    // Save the associated NotificationEntity first if it's not already saved
//    if (notification.getId() == null) {
//        notificationRepository.save(notification);
//    }
//
//    // Set the reference to the saved NotificationEntity
//    successDetails.setNotificationEntity(notification);
//
//    // Save success details in the database
//    notifySendSuccessRepository.save(successDetails);
//}

//    private void saveNotificationSendSuccess(NotificationEntity notification) {
//        NotifySendSuccess successDetails = new NotifySendSuccess();
//        successDetails.setUser(notification.getUser());
//        successDetails.setDesc(notification.getNotificationType() + " notification send successfully");
//
//        LocalTime startTime = notification.getStartTime();
////        successDetails.setId(notification.getId());
//
//        // Check if startTime is not null before accessing its properties
//        if (startTime != null) {
//            successDetails.setStartTime(startTime);
//        } else {
//            // Handle the case where startTime is null
//            log.warn("NotificationEntity with null startTime encountered while saving NotifySendSuccess.");
//
//            // Set startTime to the current time
//            successDetails.setStartTime(LocalTime.now());
//        }
//        // Set NotificationEntity id to NotifySendSuccess
////        Long notificationEntityId = notification.getId();
////        successDetails.setNotificationEntity(notification.getId());
////                successDetails.setNotificationEntity(notificationEntityId);
//
//
//
//        // Save success details in the database
//        notifySendSuccessRepository.save(successDetails);
//    }

//    private void saveNotificationSendSuccess(NotificationEntity notification) {
//        if (notification == null || notification.getUser() == null) {
//            // Log a warning or handle the case where notification or user is null
//            log.warn("Invalid NotificationEntity or User. Skipping NotifySendSuccess save.");
//            return;
//        }
//
//        NotifySendSuccess successDetails = new NotifySendSuccess();
//        successDetails.setUser(notification.getUser());
//
//
//        LocalTime startTime = notification.getStartTime();
//
//        // Check if startTime is not null before accessing its properties
//        if (startTime != null) {
//            successDetails.setStartTime(startTime);
//        } else {
//            // Handle the case where startTime is null
//            log.warn("NotificationEntity with null startTime encountered while saving NotifySendSuccess.");
//
//            // Set startTime to the current time
//            successDetails.setStartTime(LocalTime.now());
//        }
//        successDetails.setBody(notification.getNotificationType());
//        successDetails.setId(notification.getId());
//
//        // Save success details in the database
//        notifySendSuccessRepository.save(successDetails);
//    }


    private void saveNotificationSendSuccess(NotificationEntity notification) {
        if (notification == null || notification.getUser() == null) {
            log.warn("Invalid NotificationEntity or User. Skipping NotifySendSuccess save.");
            return;
        }

        NotifySendSuccess successDetails = new NotifySendSuccess();
        successDetails.setUser(notification.getUser());

        LocalTime startTime = notification.getStartTime();

        if (startTime != null) {
            successDetails.setStartTime(startTime);
        } else {
            log.warn("NotificationEntity with null startTime encountered while saving NotifySendSuccess.");
            successDetails.setStartTime(LocalTime.now());
        }

        // Set the id of the associated NotificationEntity
        successDetails.setNotificationEntity(notification);

        // Set the notification type in the body field
        successDetails.setBody(notification.getNotificationType());

        // Save success details in the database
        notifySendSuccessRepository.save(successDetails);
    }


//    private void saveNotificationSendSuccess(NotificationEntity notification) {
//        if (notification == null || notification.getUser() == null) {
//            log.warn("Invalid NotificationEntity or User. Skipping NotifySendSuccess save.");
//            return;
//        }
//
//        // Check if the associated NotificationEntity is already saved in NotifySendSuccess
////        if (notifySendSuccessRepository.existsByNotificationEntity(notification)) {
////            log.warn("NotificationEntity details already saved. Skipping NotifySendSuccess save.");
////            return;
////        }
//
//        NotifySendSuccess successDetails = new NotifySendSuccess();
//        successDetails.setUser(notification.getUser());
//
//        LocalTime startTime = notification.getStartTime();
//
//        if (startTime != null) {
//            successDetails.setStartTime(startTime);
//        } else {
//            log.warn("NotificationEntity with null startTime encountered while saving NotifySendSuccess.");
//            successDetails.setStartTime(LocalTime.now());
//        }
//
//        // Set only the id of the NotificationEntity
//        successDetails.setNotificationEntity(notification.getId());
//
//        // Set the notification type in the body field
//        successDetails.setBody(notification.getNotificationType());
//
//        // Save success details in the database
//        notifySendSuccessRepository.save(successDetails);
//    }
//
//
//
//    private Long getNotificationIdByStartTime(LocalTime startTime, User user) {
//        List<NotificationEntity> notifications = notificationRepository.findByStartTime(startTime);
//
//        // Filter notifications for the specific user
//        Optional<NotificationEntity> matchingNotification = notifications.stream()
//                .filter(notification -> notification.getUser().equals(user))
//                .findFirst();
//
//        return matchingNotification.map(NotificationEntity::getId).orElse(null);
//    }
//

//    private void saveNotificationSendSuccess(NotificationEntity notification) {
//        NotifySendSuccess successDetails = new NotifySendSuccess();
//        successDetails.setUser(notification.getUser());
//        successDetails.setDesc(notification.getNotificationType() + " notification send successfully");
//
//        LocalTime startTime = notification.getStartTime();
//
//        // Check if startTime is not null before accessing its properties
//        if (startTime != null) {
//            successDetails.setStartTime(startTime);
//        } else {
//            // Handle the case where startTime is null
//            log.warn("NotificationEntity with null startTime encountered while saving NotifySendSuccess.");
//
//            // Set startTime to the current time
//            successDetails.setStartTime(LocalTime.now());
//        }
//
//        // Save the associated NotificationEntity first if it's not already saved
////        if (notification.getId() == null) {
////            notificationRepository.save(notification);
////        }
//
//        // Set the reference to the saved NotificationEntity
//        successDetails.setNotificationEntity(notification);
//
//        // Save success details in the database
//        notifySendSuccessRepository.save(successDetails);
//    }

    public List<NotifySendSuccessDTO> getUserSendSuccessNotifications(String username) {
        List<NotifySendSuccess> notifications = notifySendSuccessRepository.findByUserEmail(username);

        // Convert entities to DTOs
        return notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }






    private NotifySendSuccessDTO convertToDTO(NotifySendSuccess notifySendSuccess) {
        NotifySendSuccessDTO dto = new NotifySendSuccessDTO();
        dto.setId(notifySendSuccess.getId());
        dto.setLocalDate(notifySendSuccess.getLocalDate());
        dto.setStartTime(notifySendSuccess.getStartTime());
        dto.setBody(notifySendSuccess.getBody());

        // Check if body is not null before processing
        if (notifySendSuccess.getBody() != null) {
            // Extract meal information from the body
            String bodyText = notifySendSuccess.getBody().toLowerCase(); // Convert to lowercase for case-insensitive matching

            // Set message based on different scenarios
//            if (isLunchTime(notifySendSuccess)) {
//                dto.setMessage("It's time for Lunch!");

            // Check if the body contains "breakfast" and set a custom message
            if (bodyText.contains("breakfast")) {
                dto.setMessage("Hey, It's time for Breakfast!");
            } else if (bodyText.contains("lunch")) {
                dto.setMessage("Hey, It's time for Lunch!");
            } else if (bodyText.contains("dinner")) {
                dto.setMessage("Hey, It's time for Dinner!");
            } else if (bodyText.contains("snacks")) {
                dto.setMessage("Hey, It's time for Snacks!");
            } else if (bodyText.contains("drinking water")) {
                dto.setMessage("Hey, It's time for Water!");
            } else if (bodyText.contains("sleep")) {
                dto.setMessage("Hey, It's time for Sleep!");
            } else if (bodyText.contains("calories")) {
                dto.setMessage("Hey, It's time for Calories!");
            }
//
        } else {
            // Handle the case where body is null
            dto.setMessage("No specific message. Body is null.");
        }

        return dto;
    }





    public boolean deleteByIdAndUser(Long id, User user) {
        Optional<NotifySendSuccess> record = notifySendSuccessRepository.findByIdAndUser(id, user);
        if (record.isPresent()) {
            notifySendSuccessRepository.delete(record.get());
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public void deleteAllByUser(User user) {
        notifySendSuccessRepository.deleteByUser(user);
    }

}