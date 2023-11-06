package com.example.jwt.dtos.FoodTodayDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientDTO {

    private String ingredientName;
    private Double ingredientQuantity;
//    private List<Double> nutri;
    private Double calories;
    private Double proteins;
    private Double fats;
    private Double carbs;
    private Double fibers;

}
