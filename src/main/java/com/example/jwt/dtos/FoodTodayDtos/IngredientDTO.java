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
    private Double thiamine_B1;
    private Double riboflavin_B2;
    private Double niacin_B3;
    private Double folates_B9;
    private Double retinolVit_A;

    public IngredientDTO(String ingredientName,
                         Double ingredientQuantity,
                         String foodCode) {
        this.ingredientName = ingredientName;
        this.ingredientQuantity = ingredientQuantity;
        this.foodCode =foodCode;


    }


    public IngredientDTO(String ingredientName,
                         Double ingredientQuantity,
                         Double energy,
                         Double proteins,
                         Double fats,
                         Double carbs,
                         Double fibers,
                         Double magnesium,
                         Double zinc,
                         Double calcium,
                         Double iron,
                         Double thiamine_B1,
                         Double riboflavin_B2,
                         Double niacin_B3,
                         Double folates_B9,
                         Double retinolVit_A) {
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
        this.thiamine_B1 = thiamine_B1;
        this.riboflavin_B2 = riboflavin_B2;
        this.niacin_B3 = niacin_B3;
        this.folates_B9 = folates_B9;
        this.retinolVit_A =retinolVit_A;

    }

    public IngredientDTO(String ingredientName, Double ingredientQuantity) {
        this.ingredientName = ingredientName;
        this.ingredientQuantity = ingredientQuantity;
    }

    public IngredientDTO(String ingredientName, Double ingredientQuantity, double v) {
        this.ingredientName = ingredientName;
        this.ingredientQuantity = ingredientQuantity;
        this.energy = v;

    }
}
