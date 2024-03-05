package com.example.jwt.dtos;

import lombok.Data;

@Data
public class WeightGoalResponse {
    private String weightGoal;
    private Double weightChange;

    public WeightGoalResponse(String weightGoal, Double weightChange) {
        this.weightGoal = weightGoal;
        this.weightChange = weightChange;
    }
}
