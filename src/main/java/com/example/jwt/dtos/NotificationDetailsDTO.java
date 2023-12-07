package com.example.jwt.dtos;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;


@Getter
@Setter

@JsonInclude(JsonInclude.Include.NON_NULL)

public class NotificationDetailsDTO {
    private Long id;
    private String recipientToken;
    private String title;
    private String body;
    private LocalTime startTime;
    private LocalTime lastTime;
    private String notificationType;

    // Constructor, getters, and setters

    public NotificationDetailsDTO(Long id, String recipientToken, String title, String body,
                                  LocalTime startTime, LocalTime lastTime, String notificationType) {
        this.id = id;
        this.recipientToken = recipientToken;
        this.title = title;
        this.body = body;
        this.startTime = startTime;
        this.lastTime = lastTime;
        this.notificationType = notificationType;
    }

    // Add other fields as needed
}
