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
private LocalDate date= LocalDate.now();;
    private String activityType;
    private double distance;
    private LocalTime startTime;
    private LocalTime endTime;
    private double duration; // in minutes
    private Double caloriesBurned;

//  getters and setters

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

    @ManyToOne
    @JoinColumn(name = "user_id")
//    @JsonIgnoreProperties("exercises")
    @JsonBackReference // Add this annotation

    private User user;

}
