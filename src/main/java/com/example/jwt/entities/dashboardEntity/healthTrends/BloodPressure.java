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
public class BloodPressure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bloodPressureId;

//    private LocalDateTime timeStamp;
    LocalDateTime timeStamp = LocalDateTime.now();
    private double value;
    LocalDate localDate = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "healthTrend_Id")
    @JsonBackReference
//    @JsonManagedReference
    private HealthTrends healthTrends;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
//    @JsonBackReference
    @JsonIgnore
    private User user;
}
