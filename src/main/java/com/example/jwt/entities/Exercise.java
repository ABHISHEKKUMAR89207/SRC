package com.example.jwt.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private Date date;
    @Column(name = "exercise_date")
    private LocalDate date;
    private String activityType;
    private double distance;
    private LocalTime startTime;
    private LocalTime endTime;
    private double duration; // in minutes
    private Double caloriesBurned;



//    public void setStartTime(Time startTime) {
//        this.startTime = startTime;
//        calculateDuration();
//    }
//
//    public void setEndTime(Time endTime) {
//        this.endTime = endTime;
//        calculateDuration();
//    }
//
//    private void calculateDuration() {
//        if (startTime != null && endTime != null) {
//            long milliseconds = endTime.getTime() - startTime.getTime();
//            this.duration = milliseconds / (60.0 * 1000.0); // Convert milliseconds to minutes
//        } else {
//            this.duration = 0;
//        }
//    }

//    // Method to format LocalTime to string "HH:mm:ss"
//    public String getFormattedStartTime() {
//        return startTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
//    }
//
//    public String getFormattedEndTime() {
//        return endTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
//    }

    // Method to set startTime from formatted string "HH:mm:ss"
    // Method to format LocalTime to string "HH:mm:ss"
    public String getFormattedStartTime() {
        if (startTime != null) {
            return startTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        }
        return null; // Handle null case if needed
    }

    public String getFormattedEndTime() {
        if (endTime != null) {
            return endTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        }
        return null; // Handle null case if needed
    }

    // Setters for formatted strings (used for serialization)
    public void setFormattedStartTime(String formattedStartTime) {
        this.startTime = LocalTime.parse(formattedStartTime, DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public void setFormattedEndTime(String formattedEndTime) {
        this.endTime = LocalTime.parse(formattedEndTime, DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
//    @JsonIgnoreProperties("exercises")
    @JsonBackReference // Add this annotation

    private User user;

}
