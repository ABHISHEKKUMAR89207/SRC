package com.example.jwt.entities.dashboardEntity.healthTrends;


import com.example.jwt.entities.User;
import com.example.jwt.entities.UserProfile;
import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakar.persistence.CascadeType;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MenstrualCycle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menstrualCycle_id;
//    LocalDateTime timeStamp = LocalDateTime.now();
    @Temporal(TemporalType.DATE)
    private LocalDate calculatedDate;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate lastPeriodStartDate;

    private boolean isMenstrualCycleEnabled; // true or false
    // Setter method for isMenstrualCycleEnabled
    public void setIsMenstrualCycleEnabled(boolean isMenstrualCycleEnabled) {
        this.isMenstrualCycleEnabled = isMenstrualCycleEnabled;
    }
//    @JsonProperty
//    private String gender;
    private int averageCycleLength; // New field for the user's average cycle length

//    @ManyToOne
//    @JoinColumn(name = "healthTrend_Id")
////    @JsonBackReference
////    @JsonManagedReference
//    private HealthTrends healthTrends;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
//    @JsonBackReference
    @JsonIgnore
    private User user;


//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "userProfile_id")
////    @JsonBackReference
//    @JsonIgnore
//    private UserProfile userProfile;

//    @OneToOne
//    @JoinColumn(name = "userProfile_id")
////    @Cascade({CascadeType.ALL})
//    @Cascade(org.hibernate.annotations.CascadeType.ALL)
//
//    private UserProfile userProfile;



//    @OneToOne
//    @JoinColumn(name = "userProfile_id")
//    private UserProfile userProfile;

    @OneToOne
    @JoinColumn(name = "userProfile_id")
    @JsonIgnore
    private UserProfile userProfile;



    // Getters and setters
}
