package com.vtt.entities;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Entity
@Document
public class NotifySendSuccess {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate localDate = LocalDate.now();
    private LocalTime startTime;

//    @Column(name = "`desc`")
//    private String desc="";
    private String body;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
    @DBRef
    private User user;

//    @ManyToOne
//    @JoinColumn(name = "notification_id")
    @DBRef
    private NotificationEntity notificationEntity; // Reference to NotificationEntity











// Implement similar methods for other scenarios like breakfast, dinner, snacks, workout, drinking water, etc.

}
