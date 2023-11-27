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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String recipientToken;
    private String title;
    private String body;
//    private String image;



//    private boolean notificationSent; // Added field

    private LocalTime startTime;
    private LocalTime lastTime;
    private String notificationType;
//    private boolean notificationOn;

//    @JsonBackReference("userReference")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


//    public String getNotificationToken() {
//        return this.recipientToken;
//    }

//    public boolean shouldSendNotification() {
//        LocalTime currentTime = LocalTime.now();
//        return notificationOn && startTime.equals(currentTime);
//    }


//    public boolean shouldSendNotification() {
//        LocalTime currentTime = LocalTime.now();
//        LocalTime notificationTime = startTime.minusMinutes(5); // Adjust the range as needed
//
//        // Check if the current time is within a certain range before or after the startTime
//        return notificationOn && currentTime.isAfter(notificationTime) && currentTime.isBefore(startTime);
//    }


//    public boolean shouldSendNotification() {
//        LocalTime currentTime = LocalTime.now();
//
//        // Add a null check for startTime
//        if (startTime == null) {
//            return false;
//        }
//
//        LocalTime notificationTime = startTime.minusMinutes(1); // Adjust the range as needed
//
//        // Check if the current time is within a certain range before or after the startTime
//        return notificationOn && currentTime.isAfter(notificationTime) && currentTime.isBefore(startTime);
//    }





//    public boolean shouldSendNotification() {
//        LocalTime currentTime = LocalTime.now();
//
//        // Add a null check for startTime
//        if (startTime == null) {
//            return false;
//        }
//
//        // Check if the current time is after the startTime
//        return notificationOn && currentTime.isAfter(startTime);
//    }






    public boolean shouldSendNotification() {
        LocalTime currentTime = LocalTime.now();

        // Add a null check for startTime
        if (startTime == null || user == null || user.getAllToggle() == null) {
            return false;
        }

        // Check if the current time is after the startTime and the user's notification settings are enabled
        return user.getAllToggle().isNotificationOn() && currentTime.isAfter(startTime);
    }


//    public boolean shouldSendNotification() {
//        LocalTime currentTime = LocalTime.now();
//
//        // Add a null check for startTime
//        if (startTime == null) {
//            return false;
//        }
//
//        LocalTime notificationTime = startTime.minusMinutes(1); // Adjust the range as needed
//
//        // Check if the current time is within a certain range before or after the startTime
//        return notificationOn && currentTime.isAfter(notificationTime) && currentTime.isBefore(startTime);
//    }
}