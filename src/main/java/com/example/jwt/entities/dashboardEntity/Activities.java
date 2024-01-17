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
public class Activities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String activityType;
    private int steps;
    private Double walkingTarget;  //in km
    private Double calory;

    @Column(name = "jogging_duration")
    private Long joggingDurationInSeconds;

    @Column(name = "running_duration")
    private Long runningDurationInSeconds;

    private LocalDate activityDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JsonIgnore
    private UserProfile userProfile;
}


