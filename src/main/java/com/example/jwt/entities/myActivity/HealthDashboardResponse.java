package com.example.jwt.entities.myActivity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HealthDashboardResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    private int steps;
    private double calory;
    private long duration;
    private double manualDuration;
    private List<ExerciseData> exerciseData;

    public HealthDashboardResponse(LocalDate date, int steps, double calory, long duration, double manualDuration, List<ExerciseData> exerciseData) {
        this.date = date;
        this.steps = steps;
        this.calory = calory;
        this.duration = duration;
        this.manualDuration = manualDuration;
        this.exerciseData = exerciseData;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public double getCalory() {
        return calory;
    }

    public void setCalory(double calory) {
        this.calory = calory;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public double getManualDuration() {
        return manualDuration;
    }

    public void setManualDuration(double manualDuration) {
        this.manualDuration = manualDuration;
    }

    public List<ExerciseData> getExerciseData() {
        return exerciseData;
    }

    public void setExerciseData(List<ExerciseData> exerciseData) {
        this.exerciseData = exerciseData;
    }

    public static class ExerciseData {
        private String activityType;
        private double exerciseDuration;
        private double caloriesBurned; // Added caloriesBurned field

        public ExerciseData(String activityType, double exerciseDuration, double caloriesBurned) {
            this.activityType = activityType;
            this.exerciseDuration = exerciseDuration;
            this.caloriesBurned = caloriesBurned;
        }

        public String getActivityType() {
            return activityType;
        }

        public void setActivityType(String activityType) {
            this.activityType = activityType;
        }

        public double getExerciseDuration() {
            return exerciseDuration;
        }

        public void setExerciseDuration(double exerciseDuration) {
            this.exerciseDuration = exerciseDuration;
        }

        public double getCaloriesBurned() {
            return caloriesBurned;
        }

        public void setCaloriesBurned(double caloriesBurned) {
            this.caloriesBurned = caloriesBurned;
        }
    }
}
