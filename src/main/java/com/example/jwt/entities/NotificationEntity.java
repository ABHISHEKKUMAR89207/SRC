package com.example.jwt.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipientToken;
    private String title;
    private String body;
    private LocalTime startTime;
    private LocalTime lastTime;
    private String notificationType;
    private boolean notificationOn;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;



    // Add a new method to check if the notification type matches
    public boolean hasNotificationType(String targetType) {
        return notificationType != null && notificationType.equals(targetType);
    }

//    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<NotifySendSuccess> sendSuccessList = new ArrayList<>();

    public boolean shouldSendNotification() {
        LocalTime currentTime = LocalTime.now();
        // Add a null check for startTime
        if (startTime == null || user == null || user.getAllToggle() == null) {
            return false;
        }
        // Check if the current time is after the startTime and the user's notification settings are enabled
        return user.getAllToggle().isNotificationOn() && currentTime.isAfter(startTime);
    }
}