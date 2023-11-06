package com.example.jwt.entities.dashboardEntity.healthTrends;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Entity
public class HealthTrend {

    @Id
    private Long healthTrendId;

    private double weight;



}
