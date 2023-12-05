//package com.example.jwt.entities;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.threeten.bp.LocalDate;
//
//import java.util.Date;
//@Entity
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class WatchData {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private LocalDate date;
//    private int watchSteps;
//    private int watchSleep;
//    private int watchHeartRate;
//    private double watchCaloriesConsumed;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//
//}
