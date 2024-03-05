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
public class AllTarget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long targetId;
    private int sleepTarget;
    private Double waterGoal;

    private String weightGoal;
    private Double weightChange;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;
}
