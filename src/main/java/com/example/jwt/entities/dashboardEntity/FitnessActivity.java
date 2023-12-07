package com.example.jwt.entities.dashboardEntity;

import com.example.jwt.entities.UserProfile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "fitness_activities")
public class FitnessActivity {
    @Id
    private Long id;
    private LocalDateTime timestamp;
    private String activityType; // Walking, Running, etc.
    private int steps;
    private double caloriesBurnt;
    private double distanceCovered;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;
}
