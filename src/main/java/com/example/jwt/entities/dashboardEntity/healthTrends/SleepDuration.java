package com.example.jwt.entities.dashboardEntity.healthTrends;

import com.example.jwt.entities.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Entity
public class SleepDuration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sleepDurationId;

//    private LocalDateTime timeStamp;
//    private double value;

    private LocalDate date;
    private LocalDateTime sleepStartTime;
    private LocalTime startTime;

    private LocalDateTime sleepEndTime;


    @ManyToOne
    @JoinColumn(name = "healthTrend_Id")
//    @JsonBackReference
//    @JsonManagedReference
    private HealthTrends healthTrends;


//    @OneToOne
//    @JsonIgnore
//    @JoinColumn(name = "user_id")
//    private User user;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
