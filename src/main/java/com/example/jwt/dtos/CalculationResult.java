package com.example.jwt.dtos;

public class CalculationResult {
    private Double caloriesBurned;
    private String errorMessage;

    public CalculationResult(Double caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public CalculationResult(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Double getCaloriesBurned() {
        return caloriesBurned;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
