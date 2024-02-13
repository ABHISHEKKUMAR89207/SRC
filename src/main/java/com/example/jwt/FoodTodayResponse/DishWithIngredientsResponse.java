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
    private String mealType;
    private Double dishQuantity;
    private Double servingSize;
    private boolean favourite;
    private List<IngredientDTO> ingredients;
    private Double totalEnergy;
    private Double totalProteins;
    private Double totalCarbs;
    private Double totalFats;
    private Double totalFibers;
    private Double totalCalcium;
    private Double totalIron;
    private Double totalZinc;
    private Double totalMagnesium;
    private Double totalThiamine_B1;
    private Double totalRiboflavin_B2;
    private Double totalNiacin_B3;
    private Double totalFolates_B9;
    private Double totalRetinolVit_A;
//    private  Map<String, String> nutrientsNameWithSIUnit = new HashMap<>();
    public DishWithIngredientsResponse(Long dishId,
                                       String dishName,
                                       String mealType,
                                       boolean favourite,
                                       List<IngredientDTO> ingredients,
                                       Double totalEnergy,
                                       Double totalProteins,
                                       Double totalCarbs,
                                       Double totalFats,
                                       Double totalFibers,
                                       Double totalMagnesium,
                                       Double totalZinc,
                                       Double totalIron,
                                       Double totalCalcium) {
        this.dishId=dishId;
        this.dishName = dishName;
        this.mealType = mealType;
        this.favourite = favourite;
        this.ingredients = ingredients;
        this.totalEnergy = totalEnergy;
        this.totalProteins = totalProteins;
        this.totalCarbs = totalCarbs;
        this.totalFats = totalFats;
        this.totalFibers = totalFibers;
        this.totalMagnesium = totalMagnesium;
        this.totalZinc = totalZinc;
        this.totalIron = totalIron;
        this.totalCalcium = totalCalcium;
    }
    public DishWithIngredientsResponse(Long dishId,
                                       String dishName,
                                       String mealType,
                                       boolean favourite,
                                       List<IngredientDTO> ingredients,
                                       Double totalEnergy,
                                       Double totalProteins,
                                       Double totalCarbs,
                                       Double totalFats,
                                       Double totalFibers,
                                       Double totalMagnesium,
                                       Double totalZinc,
                                       Double totalIron,
                                       Double totalCalcium,
                                       Double totalThiamine_B1,
                                       Double totalRiboflavin_B2,
                                       Double totalNiacin_B3,
                                       Double totalFolates_B9,
                                       Double totalRetinolVit_A,
                                       Double dishQuantity,
                                       Double servingSize
                               ) {
        this.dishId = dishId;
        this.dishName = dishName;
        this.mealType = mealType;
        this.dishQuantity=dishQuantity;
        this.servingSize=servingSize;
        this.favourite = favourite;


        this.ingredients = ingredients;

        this.totalEnergy = calculateTotalEnergy(totalEnergy, dishQuantity, servingSize);
        this.totalProteins = calculateTotalProteins(totalProteins, dishQuantity, servingSize);
        this.totalCarbs = calculateTotalCarbs(totalCarbs, dishQuantity, servingSize);
        this.totalFats = calculateTotalFats(totalFats, dishQuantity, servingSize);
        this.totalFibers = calculateTotalFibers(totalFibers, dishQuantity, servingSize);
        this.totalMagnesium = calculateTotalMagnesium(totalMagnesium, dishQuantity, servingSize);
        this.totalZinc = calculateTotalZinc(totalZinc, dishQuantity, servingSize);
        this.totalIron = calculateTotalIron(totalIron, dishQuantity, servingSize);
        this.totalCalcium = calculateTotalCalcium(totalCalcium, dishQuantity, servingSize);
        this.totalThiamine_B1 = calculateTotalThiamine_B1(totalThiamine_B1, dishQuantity, servingSize);
        this.totalRiboflavin_B2 = calculateTotalRiboflavin_B2(totalRiboflavin_B2, dishQuantity, servingSize);
        this.totalNiacin_B3 = calculateTotalNiacin_B3(totalNiacin_B3, dishQuantity, servingSize);
        this.totalFolates_B9 = calculateTotalFolates_B9(totalFolates_B9, dishQuantity, servingSize);
        this.totalRetinolVit_A = calculateTotalRetinolVit_A(totalRetinolVit_A, dishQuantity, servingSize);
    }

    // Add a new method to calculate total energy based on dish quantity and serving size
    private Double calculateTotalEnergy(Double energy, Double dishQuantity, Double servingSize) {
        return (energy / dishQuantity) * servingSize;
    }

    // Add new methods to calculate total nutrients based on dish quantity and serving size
    private Double calculateTotalProteins(Double proteins, Double dishQuantity, Double servingSize) {
        return (proteins / dishQuantity) * servingSize;
    }

    private Double calculateTotalCarbs(Double carbs, Double dishQuantity, Double servingSize) {
        return (carbs / dishQuantity) * servingSize;
    }

    private Double calculateTotalFats(Double fats, Double dishQuantity, Double servingSize) {
        return (fats / dishQuantity) * servingSize;
    }

    private Double calculateTotalFibers(Double fibers, Double dishQuantity, Double servingSize) {
        return (fibers / dishQuantity) * servingSize;
    }

    private Double calculateTotalMagnesium(Double magnesium, Double dishQuantity, Double servingSize) {
        return (magnesium / dishQuantity) * servingSize;
    }

    private Double calculateTotalZinc(Double zinc, Double dishQuantity, Double servingSize) {
        return (zinc / dishQuantity) * servingSize;
    }

    private Double calculateTotalIron(Double iron, Double dishQuantity, Double servingSize) {
        return (iron / dishQuantity) * servingSize;
    }

    private Double calculateTotalCalcium(Double calcium, Double dishQuantity, Double servingSize) {
        return (calcium / dishQuantity) * servingSize;
    }

    private Double calculateTotalThiamine_B1(Double thiamine_B1, Double dishQuantity, Double servingSize) {
        return (thiamine_B1 / dishQuantity) * servingSize;
    }

    private Double calculateTotalRiboflavin_B2(Double riboflavin_B2, Double dishQuantity, Double servingSize) {
        return (riboflavin_B2 / dishQuantity) * servingSize;
    }

    private Double calculateTotalNiacin_B3(Double niacin_B3, Double dishQuantity, Double servingSize) {
        return (niacin_B3 / dishQuantity) * servingSize;
    }

    private Double calculateTotalFolates_B9(Double folates_B9, Double dishQuantity, Double servingSize) {
        return (folates_B9 / dishQuantity) * servingSize;
    }

    private Double calculateTotalRetinolVit_A(Double retinolVit_A, Double dishQuantity, Double servingSize) {
        return (retinolVit_A / dishQuantity) * servingSize;
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
