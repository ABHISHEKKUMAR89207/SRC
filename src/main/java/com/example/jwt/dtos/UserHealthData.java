package com.example.jwt.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserHealthData {
    private LocalDate localDate;
    private Double heartRateValue;
    private Integer steps;
    private Double calorie;
    private long sleepDuration;

    // Constructor, getters, and setters
}