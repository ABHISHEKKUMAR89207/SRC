package com.example.jwt.AdminDashboard;

import lombok.Data;

@Data
public class AverageStepsByAgeDTO {
    private String ageGroup;
    private double averageSteps;
}
