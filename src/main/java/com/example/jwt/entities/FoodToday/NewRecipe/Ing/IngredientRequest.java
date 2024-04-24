package com.example.jwt.entities.FoodToday.NewRecipe.Ing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientRequest {
        private String ingredientName;
    private Double ingredientQuantity;
    private String foodCode;
    private String category;
    // getters and setters
    public Double getIngredientQuantity() {
        return ingredientQuantity;
    }

    public void setIngredientQuantity(Double ingredientQuantity) {
        this.ingredientQuantity = ingredientQuantity;
    }

    // getters and setters
}
