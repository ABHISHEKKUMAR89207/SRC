package com.example.jwt.entities.dashboardEntity;

import com.example.jwt.entities.User;
import com.example.jwt.entities.UserProfile;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

public class Activities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


//        private Duration duration;
    private String activityType;
    private int steps;
    private Double walkingTarget;  //in km

//        private Double caloriesBurntByActivitiy;
//        private Double caloriesBurntBySteps;
//        private Double distanceCovered;
//        private LocalDateTime timestamp; // Timestamp when the steps were recorded


//        LocalTime startTime = LocalTime.now();
//        LocalTime endTime = LocalTime.now().plusSeconds(5);
//        private int durationInSeconds = (int) Duration.between(startTime, endTime).getSeconds();
    @Column(name = "jogging_duration")
    private Long joggingDurationInSeconds;

    @Column(name = "running_duration")
    private Long runningDurationInSeconds;

    private LocalDate activityDate; // Add this field to store the date



    @ManyToOne
    @JoinColumn(name = "user_id")  // Use the actual field name defined in the User entity
    @JsonIgnore
    private User user;


//    @ManyToOne
//    @JoinColumn(name = "userProfile_id")  // Use the actual field name defined in the User entity
//    @JsonIdentityReference(alwaysAsId = true)
////@JsonIgnore
//    private UserProfile userProfile;
    // Getters and setters

    @ManyToOne
    @JsonIgnore// Exclude activities from serialization
    private UserProfile userProfile;

}


