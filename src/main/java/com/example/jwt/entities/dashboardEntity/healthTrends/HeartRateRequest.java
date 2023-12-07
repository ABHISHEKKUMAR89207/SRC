package com.example.jwt.entities.dashboardEntity.healthTrends;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeartRateRequest {
    private double value;
    private HealthTrends healthTrends;
}
