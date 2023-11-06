package com.example.jwt.dtos.FoodTodayDtos;

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

}
