package com.example.jwt.entities.FoodToday.NewRecipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeWithIngredientsResponse {
    private Long recipeId;
    private String recipeName;
//    private String mealType;
//    private Double dishQuantity;
    private Double totalCookedQuantity;
    private String unit;
private double valueForOneUnit;
    private List<IngredientResponse> ingredients;

//    public void setUnit() {
//    }
//    private List<Ingredientn> ingredients;
//private List<String> sourceRecipes; // Adjust the type based on what source_recipe represents


}
