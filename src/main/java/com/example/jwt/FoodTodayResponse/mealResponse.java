package com.example.jwt.FoodTodayResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor

public class mealResponse {
    private List<DishWithIngredientsResponse> rList = new ArrayList<>();
    private Double mealCalories;
    private Double mealProteins;
    private Double mealCarbs;
    private Double mealFats;
    private Double mealFibers;
   private  Map<String, String> nutrientsNameWithSIUnit = new HashMap<>();
    // to get the type of meal response we want
    public mealResponse(  List<DishWithIngredientsResponse> rList,
                          Double mealCalories,
                          Double mealProteins,
                          Double mealCarbs,
                          Double mealFats,
                          Double mealFibers){
        this.rList = rList;
        this.mealCalories = mealCalories;
        this.mealProteins = mealProteins;
        this.mealCarbs = mealCarbs;
        this.mealFats = mealFats;
        this.mealFibers = mealFibers;

    }
    public mealResponse(  Map<String, String> nutrientsNameWithSIUnit,
                          List<DishWithIngredientsResponse> rList,
                          Double mealCalories,
                          Double mealProteins,
                          Double mealCarbs,
                          Double mealFats,
                          Double mealFibers){
        this.nutrientsNameWithSIUnit = nutrientsNameWithSIUnit;
        this.rList = rList;
        this.mealCalories = mealCalories;
        this.mealProteins = mealProteins;
        this.mealCarbs = mealCarbs;
        this.mealFats = mealFats;
        this.mealFibers = mealFibers;

    }
}
