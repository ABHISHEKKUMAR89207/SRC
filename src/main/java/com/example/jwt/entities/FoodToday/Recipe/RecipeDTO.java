package com.example.jwt.entities.FoodToday.Recipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDTO {
    private Long recipesId;
    private String recipeName;
    private double energyJoules;
    private double carbohydrate;
    private double totalDietaryFibre;
    private double totalFat;
    private double protein;

    // Constructor to convert from entity to DTO
    public RecipeDTO(Recipe recipe) {
        this.recipesId = recipe.getRecipesId();
        this.recipeName = recipe.getRecipe_name();
        this.energyJoules = recipe.getEnergy_joules();
        this.carbohydrate = recipe.getCarbohydrate();
        this.totalDietaryFibre = recipe.getTotal_dietary_fibre();
        this.totalFat = recipe.getTotal_fat();
        this.protein = recipe.getProtein();
    }
}
