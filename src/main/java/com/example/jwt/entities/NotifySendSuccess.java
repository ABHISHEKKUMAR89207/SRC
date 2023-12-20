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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate localDate = LocalDate.now();
    private LocalTime startTime;

//    @Column(name = "`desc`")
//    private String desc="";
    private String body;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "notification_id")
    private NotificationEntity notificationEntity; // Reference to NotificationEntity











// Implement similar methods for other scenarios like breakfast, dinner, snacks, workout, drinking water, etc.

}
