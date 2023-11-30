package com.example.jwt.entities.dashboardEntity.healthTrends;

import com.example.jwt.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Entity
public class SleepTarget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sleepDurationId;

    private int sleepTarget;

//    private LocalDateTime timeStamp;
//    private double value;


//    @ManyToOne
//    @JoinColumn(name = "healthTrend_Id")
////    @JsonBackReference
////    @JsonManagedReference
//    private HealthTrends healthTrends;


//    @OneToOne
//    @JsonIgnore
//    @JoinColumn(name = "user_id")
//    private User user;


    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

}
