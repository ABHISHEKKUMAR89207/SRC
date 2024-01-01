package com.example.jwt.FoodTodayResponse;

import com.example.jwt.dtos.FoodTodayDtos.IngredientDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class DishWithIngredientsResponse {

    private Long dishId;
    private String dishName;
    private boolean favourite;
    private List<IngredientDTO> ingredients;
    private Double totalEnergy;
    private Double totalProteins;
    private Double totalCarbs;
    private Double totalFats;
    private Double totalFibers;
//    private  Map<String, String> nutrientsNameWithSIUnit = new HashMap<>();
    public DishWithIngredientsResponse(Long dishId,
                                       String dishName,
                                       boolean favourite,
                                       List<IngredientDTO> ingredients,
                                       Double totalEnergy,
                                       Double totalProteins,
                                       Double totalCarbs,
                                       Double totalFats,
                                       Double totalFibers) {
        this.dishId=dishId;
        this.dishName = dishName;
        this.favourite = favourite;
        this.ingredients = ingredients;
        this.totalEnergy = totalEnergy;
        this.totalProteins = totalProteins;
        this.totalCarbs = totalCarbs;
        this.totalFats = totalFats;
        this.totalFibers = totalFibers;
    }


    public DishWithIngredientsResponse(Long dishId,
                                       String dishName,
                                      List<IngredientDTO> ingredientsList,
                                       Double totalEnergy) {
        this.dishId=dishId;
        this.dishName = dishName;
        this.ingredients = ingredientsList;
        this.totalEnergy = totalEnergy;
    }
}
