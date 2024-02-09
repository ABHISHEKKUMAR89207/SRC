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
    private Double mealEnergy;
    private Double mealProteins;
    private Double mealCarbs;
    private Double mealFats;
    private Double mealFibers;

    private Double mealCalcium;
    private Double mealIron;
    private Double mealZinc;
    private Double mealMagnesium;
   private  Map<String, String> nutrientsNameWithSIUnit = new HashMap<>();
    // to get the type of meal response we want
    public mealResponse(  List<DishWithIngredientsResponse> rList,
                          Double mealEnergy,
                          Double mealProteins,
                          Double mealCarbs,
                          Double mealFats,
                          Double mealFibers){
        this.rList = rList;
        this.mealEnergy = mealEnergy;
        this.mealProteins = mealProteins;
        this.mealCarbs = mealCarbs;
        this.mealFats = mealFats;
        this.mealFibers = mealFibers;

    }
//    public mealResponse(  Map<String, String> nutrientsNameWithSIUnit,
//                          List<DishWithIngredientsResponse> rList,
//                          Double mealEnergy,
//                          Double mealProteins,
//                          Double mealCarbs,
//                          Double mealFats,
//                          Double mealFibers){
//        this.nutrientsNameWithSIUnit = nutrientsNameWithSIUnit;
//        this.rList = rList;
//        this.mealEnergy = mealEnergy;
//        this.mealProteins = mealProteins;
//        this.mealCarbs = mealCarbs;
//        this.mealFats = mealFats;
//        this.mealFibers = mealFibers;
//
//    }

    public mealResponse(  Map<String, String> nutrientsNameWithSIUnit,
                          List<DishWithIngredientsResponse> rList,
                          Double mealEnergy,
                          Double mealProteins,
                          Double mealCarbs,
                          Double mealFats,
                          Double mealFibers,
                          Double mealCalcium,
                          Double mealIron,
                          Double mealZinc,
                          Double mealMagnesium){
        this.nutrientsNameWithSIUnit = nutrientsNameWithSIUnit;
        this.rList = rList;
        this.mealEnergy = mealEnergy;
        this.mealProteins = mealProteins;
        this.mealCarbs = mealCarbs;
        this.mealFats = mealFats;
        this.mealFibers = mealFibers;
        this.mealMagnesium = mealMagnesium;
        this.mealZinc = mealZinc;
        this.mealIron = mealIron;
        this.mealCalcium = mealCalcium;

    }


    public mealResponse(Map<String, String> nutrientsNameWithSIUnit, Double calories) {

        this.nutrientsNameWithSIUnit = nutrientsNameWithSIUnit;
        this.mealEnergy = calories;
    }



}
