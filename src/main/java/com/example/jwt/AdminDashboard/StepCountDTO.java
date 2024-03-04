package com.example.jwt.AdminDashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StepCountDTO {
    private int totalSteps;
    private double totalKms;
}

