package com.example.jwt.entities.FoodToday.ear;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EarResponse {
    private String gender;
    private String age;
    private String group;
    private String workLevel;



    private double energy;
    private double protein;
    private boolean fatsOilsVisible;
    private double fiber;
    private double calcium;
    private double magnesium;
    private double iron;
    private double zinc;
    private double thiamine;
    private double riboflavin;
    private double niacin;


}

