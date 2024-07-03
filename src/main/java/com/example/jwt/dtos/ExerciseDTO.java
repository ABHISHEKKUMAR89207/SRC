package com.example.jwt.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExerciseDTO {
    private Long id;
    private String date;
    private String activityType;
    private Double duration;
    private String startTime;
    private String endTime;
    private Double caloriesBurned;


    // Constructor for specific fields
    public ExerciseDTO(Long id, String activityType, String startTime, String endTime) {
        this.id = id;
        this.activityType = activityType;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Constructor for all fields
    public ExerciseDTO(Long id, String date, String activityType, Double duration, String startTime, String endTime, Double caloriesBurned) {
        this.id = id;
        this.date = date;
        this.activityType = activityType;
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = endTime;
        this.caloriesBurned = caloriesBurned;
    }

    public ExerciseDTO(String startTime) {
        this.endTime = startTime;
    }

}
