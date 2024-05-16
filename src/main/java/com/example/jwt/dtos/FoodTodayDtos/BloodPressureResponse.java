// Data transfer Object 

package com.example.jwt.dtos.FoodTodayDtos;

import com.example.jwt.entities.dashboardEntity.healthTrends.DiastolicBloodPressure;
import com.example.jwt.entities.dashboardEntity.healthTrends.SystolicBloodPressure;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class BloodPressureResponse {
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
//    private LocalDateTime timeStamp;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate localDate;
    private double systolicValue;
    private double diastolicValue;

    public BloodPressureResponse() {
        // Default constructor
    }
}

