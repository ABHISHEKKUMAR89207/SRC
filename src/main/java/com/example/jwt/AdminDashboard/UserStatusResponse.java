package com.example.jwt.AdminDashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class UserStatusResponse {

    private String Name;
    private String UserName;
    private String emailId;
    private String mobileNo;
    private String state;

    private int age;
    private double averageStepsLastWeek;
    private double averageHoursOfSleepPerDay;
    private double averageWaterIntakePerDay;
    private long dishCount;
    private String mostFrequentlyConsumedMeal;
    private String mostSkippedMeal;
    private String mostConsumedDish;
    private String mostConsumedBreakfast;
    private String mostConsumedLunch;
    private String mostConsumedDinner;
    private String mostConsumedSnacks;
    private String mostConsumedDrink;
    private String mostConsumedNutrient;
    private String leastConsumedNutrient;
    private String mostProteinRichDiet;
    private String mostIronRichDiet;
    private String mostCalciumRichDiet;
    private String mostCalorieRichDiet;
    private String mostCHORichDiet;
//    private User userStatus;

    // Getters and Setters
}