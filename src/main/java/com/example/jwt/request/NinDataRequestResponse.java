package com.example.jwt.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NinDataRequestResponse {
    private Long foodId;
    private String food;
    private String foodCode;
    private String category;
    private String source;
    private String typesOfFood;

    private Double energy;
    private Double protein;
    private Double totalFat;
    private Double totalDietaryFibre;
    private Double carbohydrate;
    private Double thiamineB1;
    private Double riboflavinB2;
    private Double niacinB3;
    private Double vitB6;
    private Double totalFolatesB9;
    private Double vitC;
    private Double vitA;
    private Double iron;
    private Double zinc;
    private Double sodium;
    private Double calcium;
    private Double magnesium;

    // Getters and setters
}
