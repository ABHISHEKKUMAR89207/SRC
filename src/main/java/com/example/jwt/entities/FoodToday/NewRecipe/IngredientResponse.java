package com.example.jwt.entities.FoodToday.NewRecipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter@AllArgsConstructor
@NoArgsConstructor
public class IngredientResponse {
    private String ingredientName;
    private Double weight;
//    private Map<String, Double> nutrients;

    // ... getters and setters
}
