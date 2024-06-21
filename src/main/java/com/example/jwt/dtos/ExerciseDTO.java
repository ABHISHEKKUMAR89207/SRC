package com.example.jwt.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseDTO {
    private Long id;
    private String date;
    private String activityType;
    private Double duration;
    private String startTime;
    private String endTime;
    private Double caloriesBurned;
}
