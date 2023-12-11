package com.example.jwt.FoodTodayResponse;

import com.example.jwt.dtos.FoodTodayDtos.IngredientDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DishWithIngredientsResponse {

    private Long dishId;
    private String dishName;
    private boolean favourite;
    private List<IngredientDTO> ingredients;
    private Double totalCalories;
    private Double totalProteins;
    private Double totalCarbs;
    private Double totalFats;
    private Double totalFibers;

    public DishWithIngredientsResponse(Long dishId,
                                       String dishName,
                                       boolean favourite,
                                       List<IngredientDTO> ingredients,
                                       Double totalCalories,
                                       Double totalProteins,
                                       Double totalCarbs,
                                       Double totalFats,
                                       Double totalFibers) {
        this.dishId=dishId;
        this.dishName = dishName;
        this.favourite = favourite;
        this.ingredients = ingredients;
        this.totalCalories = totalCalories;
        this.totalProteins = totalProteins;
        this.totalCarbs = totalCarbs;
        this.totalFats = totalFats;
        this.totalFibers = totalFibers;
    }


}
