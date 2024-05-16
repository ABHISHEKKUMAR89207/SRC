package com.example.jwt.entities.dashboardEntity.healthTrends;

import com.example.jwt.entities.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class SystolicBloodPressure {
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
//    LocalDateTime timeStamp = LocalDateTime.now();
    @JsonFormat(pattern = "yyyy-MM-dd")

//    LocalDate localDate;
    LocalDate localDate;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long systolicId;
    private double value;

    private double systolicValue;
    private double diastolicValue; // Added field for diastolic blood pressure

    @ManyToOne
    @JoinColumn(name = "healthTrend_Id")
    @JsonBackReference
    private HealthTrends healthTrends;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
