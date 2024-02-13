package com.example.jwt.request;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@NonNull
public class TargetDataRequest {
    private Double calories;
    private Double carbs;
    private Double fats;
    private Double proteins;
    private Double fiber;
    // Add the missing nutrient properties with their getter and setter methods
    private Double magnesium;
    private Double zinc;
    private Double iron;
    private Double calcium;
    private Double thiamine_B1;
    private Double retinol_Vit_A;
    private Double riboflavin_B2;
    private Double niacin_B3;
    private Double folates_B9;

}
