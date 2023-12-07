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
public class OxygenSaturatedLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long oxygenSaturatedLevelId;
    LocalDateTime timeStamp = LocalDateTime.now();
    private double value;
    LocalDate localDate = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "healthTrend_Id")
    private HealthTrends healthTrends;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
