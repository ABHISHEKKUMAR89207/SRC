package com.example.jwt.dtos;

public class WaterGoalDto {
    private Long waterGoalId;
    private Double waterGoal;
    // Other fields, getters, and setters

    public Long getWaterGoalId() {
        return waterGoalId;
    }

    public void setWaterGoalId(Long waterGoalId) {
        this.waterGoalId = waterGoalId;
    }

    public Double getWaterGoal() {
        return waterGoal;
    }

    public void setWaterGoal(Double waterGoal) {
        this.waterGoal = waterGoal;
    }
}
