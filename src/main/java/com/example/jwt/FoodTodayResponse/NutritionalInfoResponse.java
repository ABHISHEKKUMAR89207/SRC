package com.example.jwt.FoodTodayResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NutritionalInfoResponse {
    private Double energy;
    private Double protein;
    private Double fat;
    private Double carbohydrates;
    private Double fiber;

    // Constructors, getters, and setters
}
