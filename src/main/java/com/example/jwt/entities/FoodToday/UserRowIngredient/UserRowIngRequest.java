package com.example.jwt.entities.FoodToday.UserRowIngredient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRowIngRequest {
    private String foodName;
    private String foodCode;
    private String category;

//    private double energy;
//    private double fat;
//    private double protein;
//    private double calcium;
//    private double magnesium;
//    private double iron;
//    private double zinc;
//    private double thiamine; // Thiamine (B1)
//    private double riboflavin; // Riboflavin (B2)
//    private double niacin; // Niacin (B3)
//    private double vitaminB6; // Vitamin B6
//    private double totalFolate; // Total folate
//    private double vitaminC; // Vitamin C
//    private double vitaminA; // Vitamin A
    private double energy;
    private double protein;
    private double fat;
    private double fiber;
    private double carbohydrate;
    private double thiamine; // Thiamine (B1)
    private double riboflavin; // Riboflavin (B2)
    private double niacin; // Niacin (B3)
    private double vitaminB6; // Vitamin B6
    private double totalFolate; // Total folate
    private double vitaminC; // Vitamin C
    private double vitaminA; // Vitamin A
    private double iron;
    private double zinc;
    private double sodium;
    private double calcium;
    private double magnesium;

    // You can add more fields as needed
    private String sourceName;
    private int sourceYear;
    private String sourceJournal;
    private String sourcePage;
    private String sourceProductName;
    // Getters and setters
}
