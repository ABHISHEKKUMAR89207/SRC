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
@Table(name = "heartRate")
public class HeartRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Use identity strategy for auto-generation
    private Long heart_rate_Id;

    LocalDateTime timeStamp = LocalDateTime.now();
    //private LocalDateTime timeStamp;
    private double value;

    LocalDate localDate = LocalDate.now();

//    public HeartRate(Long healthTrendId) {
//        this.healthTrendId = healthTrendId;
//    }

    @ManyToOne
    @JoinColumn(name = "healthTrend_Id")
//    @JsonBackReference
//    @JsonManagedReference
    private HealthTrends healthTrends;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
//    @JsonBackReference
    @JsonIgnore
    private User user;





}
