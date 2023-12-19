package com.example.jwt.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class NotifySendSuccess {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private LocalDate localDate = LocalDate.now();
//    private LocalTime startTime;
//    private String desc = "notification send successfully";
//    private String body;
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id; // New ID for NotifySendSuccess

    private LocalDate localDate = LocalDate.now();
    private LocalTime startTime;
    private String desc = "notification send successfully";
    private String body;

    @ManyToOne
    @JoinColumn(name = "notification_id")
    private NotificationEntity notificationEntity; // Reference to NotificationEntity
}
