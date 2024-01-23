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
    private Double weight;
    // getters and setters
    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    // getters and setters
}
