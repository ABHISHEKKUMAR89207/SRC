package com.example.jwt.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WaterGoalAndIntakeResponse {

    private Double waterGoal;
    private Double waterIntakeForCurrentDate;


    public WaterGoalAndIntakeResponse() {
        // Default constructor
    }

    public WaterGoalAndIntakeResponse(Double waterGoal, Double waterIntakeForCurrentDate) {
        this.waterGoal = waterGoal;
        this.waterIntakeForCurrentDate = waterIntakeForCurrentDate;
    }
}
