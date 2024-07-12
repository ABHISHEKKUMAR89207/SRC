package com.example.jwt.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientResponse {
    private String ingredientName;
    private double ingredientQuantity;
    private String foodCode;
    private String category;

    // Constructors, getters, and setters
}
