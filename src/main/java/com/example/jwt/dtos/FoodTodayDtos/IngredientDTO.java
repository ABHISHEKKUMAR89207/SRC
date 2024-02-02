package com.example.jwt.dtos.FoodTodayDtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL)

public class IngredientDTO {

    private String ingredientName;
    private Double ingredientQuantity;
    private Double energy;
    private Double proteins;
    private Double fats;
    private Double carbs;
    private Double fibers;

    public IngredientDTO(String ingredientName,
                         Double ingredientQuantity,
                         Double energy) {
        this.ingredientName = ingredientName;
        this.ingredientQuantity = ingredientQuantity;
        this.energy =energy;

    }

    public IngredientDTO(String ingredientName, Double ingredientQuantity) {
        this.ingredientName = ingredientName;
        this.ingredientQuantity = ingredientQuantity;
    }
}
