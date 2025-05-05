package com.vtt.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

public class NotificationEntity {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipientToken;
    private String title;
    private String body;
    private LocalTime startTime;
    private LocalTime lastTime;
    private String notificationType;
    private boolean notificationOn = true;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
    @DBRef
    private User user;



    // Add a new method to check if the notification type matches
    public boolean hasNotificationType(String targetType) {
        return notificationType != null && notificationType.equals(targetType);
    }

//    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<NotifySendSuccess> sendSuccessList = new ArrayList<>();


}