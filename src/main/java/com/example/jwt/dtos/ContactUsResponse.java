package com.example.jwt.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactUsResponse {
    private Long id;
    private String localDateTime; // Change type to String
    private String name;
    private String number;
    private String email;
    private String queries;
    private String imageDataUrl;
    private boolean status;
    private String feedbackMessage;

    // Constructor
//    public ContactUsResponse(Long id, LocalDateTime localDateTime, String name, String number, String email,
//                             String queries, String imageDataUrl, boolean status, String feedbackMessage) {
//        this.id = id;
//        this.localDateTime = localDateTime;
//        this.name = name;
//        this.number = number;
//        this.email = email;
//        this.queries = queries;
//        this.imageDataUrl = imageDataUrl;
//        this.status = status;
//        this.feedbackMessage = feedbackMessage;
//    }

    // Getters (and setters if needed)
    // Implement getters for all fields

//    public String getFormattedLocalDateTime() {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        return localDateTime.format(formatter);
//    }
}

