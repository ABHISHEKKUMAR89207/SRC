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

@AllArgsConstructor
@NoArgsConstructor
@Data

@Entity
public class BloodGlucose {

    LocalDateTime timeStamp = LocalDateTime.now();
    LocalDate localDate = LocalDate.now();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bloodGlucoseId;
    private double value;
    @ManyToOne
    @JoinColumn(name = "healthTrend_Id")
    private HealthTrends healthTrends;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
