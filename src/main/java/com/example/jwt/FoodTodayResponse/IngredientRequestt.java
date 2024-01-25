package com.example.jwt.FoodTodayResponse;

import com.example.jwt.dtos.FoodTodayDtos.IngredientRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientRequestt {
    private List<IngredientRequest> ingredients;

    // getters and setters
}