package com.example.jwt.FoodTodayResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientRequest {
    private List<com.example.jwt.entities.FoodToday.NewRecipe.Ing.IngredientRequest> ingredients;

    // getters and setters
}