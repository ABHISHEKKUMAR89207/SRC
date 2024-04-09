package com.example.jwt.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonalRecipeResponse {
    private Long recipeId;
    private String recipeName;
    private double totalCookedQuantity;
    private String unit;
    private double valueForOneUnit;
    private List<IngredientResponse> ingredients;

    // Constructors, getters, and setters
}
