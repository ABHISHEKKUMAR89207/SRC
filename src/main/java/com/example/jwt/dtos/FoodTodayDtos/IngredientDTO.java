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
    private String foodCode;
    private Double energy;
    private Double proteins;
    private Double fats;
    private Double carbs;
    private Double fibers;
    private Double calcium;
    private Double iron;
    private Double zinc;
    private Double magnesium;

    public IngredientDTO(String ingredientName,
                         Double ingredientQuantity,
                         Double energy) {
        this.ingredientName = ingredientName;
        this.ingredientQuantity = ingredientQuantity;
        this.energy =energy;

    }
    public IngredientDTO(String ingredientName, Double ingredientQuantity, Double energy,
                         Double proteins, Double fats, Double carbs, Double fibers, Double magnesium, Double zinc,Double calcium,
    Double iron) {
        this.ingredientName = ingredientName;
        this.ingredientQuantity = ingredientQuantity;
        this.energy = energy;
        this.proteins = proteins;
        this.fats = fats;
        this.carbs = carbs;
        this.fibers = fibers;
        this.calcium = calcium;
        this.iron = iron;
        this.zinc = zinc;
        this.magnesium = magnesium;
    }

    public IngredientDTO(String ingredientName, Double ingredientQuantity) {
        this.ingredientName = ingredientName;
        this.ingredientQuantity = ingredientQuantity;
    }
}
