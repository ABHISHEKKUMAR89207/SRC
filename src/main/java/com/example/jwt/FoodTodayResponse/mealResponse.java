package com.example.jwt.FoodTodayResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor

public class mealResponse {
    private List<DishWithIngredientsResponse> rList = new ArrayList<>();
    private String date;
    private Double mealEnergy;
    private Double mealProteins;
    private Double mealCarbs;
    private Double mealFats;
    private Double mealFibers;

    private Double mealCalcium;
    private Double mealIron;
    private Double mealZinc;
    private Double mealMagnesium;
    private Double mealThiamine_B1;
    private Double mealRiboflavin_B2;
    private Double mealNiacin_B3;
    private Double mealFolates_B9;
    private Double mealRetinolVit_A;
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
                          Double mealMagnesium,
                          Double mealThiamine_B1,
                          Double mealRiboflavin_B2,
                          Double mealNiacin_B3,
                          Double mealFolates_B9,
                          Double mealRetinolVit_A){
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
        this.mealThiamine_B1 = mealThiamine_B1;
        this.mealRiboflavin_B2 = mealRiboflavin_B2;
        this.mealNiacin_B3 = mealNiacin_B3;
        this.mealFolates_B9 = mealFolates_B9;
        this.mealRetinolVit_A = mealRetinolVit_A;

    }


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
                          Double mealMagnesium,
                          Double mealThiamine_B1,
                          Double mealRiboflavin_B2,
                          Double mealNiacin_B3,
                          Double mealFolates_B9,
                          Double mealRetinolVit_A,
                          LocalDate date
                          ){

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
        this.mealThiamine_B1 = mealThiamine_B1;
        this.mealRiboflavin_B2 = mealRiboflavin_B2;
        this.mealNiacin_B3 = mealNiacin_B3;
        this.mealFolates_B9 = mealFolates_B9;
        this.mealRetinolVit_A = mealRetinolVit_A;
        this.date = formatDate(date);


    }

    public mealResponse(Map<String, Double> energyByMealType) {
        this.mealEnergy = calculateTotalEnergy(energyByMealType);
    }

    private Double calculateTotalEnergy(Map<String, Double> energyByMealType) {
        Double totalEnergy = 0.0;
        for (Double energy : energyByMealType.values()) {
            totalEnergy += energy;
        }
        return totalEnergy;
    }


    private String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    public mealResponse(Map<String, String> nutrientsNameWithSIUnit, Double calories) {

        this.nutrientsNameWithSIUnit = nutrientsNameWithSIUnit;
        this.mealEnergy = calories;
    }



}
