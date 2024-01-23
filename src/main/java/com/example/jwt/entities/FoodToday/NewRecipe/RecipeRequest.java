package com.example.jwt.entities.FoodToday.NewRecipe;


import com.example.jwt.entities.FoodToday.NewRecipe.Ing.IngredientRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeRequest {
    private Long recipeId;
    private String recipeName;
    private String mealType;
    private Double dishQuantity;
    private Double servingSize;
    private List<IngredientRequest> ingredients;
}
