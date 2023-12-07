package com.example.jwt.entities.dashboardEntity.healthTrends;

import com.example.jwt.entities.User;
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
@Table(name = "heartRate")
public class HeartRate {
    LocalDateTime timeStamp = LocalDateTime.now();
    LocalDate localDate = LocalDate.now();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Use identity strategy for auto-generation
    private Long heart_rate_Id;
    private double value;
    @ManyToOne
    @JoinColumn(name = "healthTrend_Id")
    private HealthTrends healthTrends;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
