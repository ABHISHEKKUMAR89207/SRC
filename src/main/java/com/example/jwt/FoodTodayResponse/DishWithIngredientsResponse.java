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
    private Double dishQuantity;
    private Double servingSize;
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
                                       boolean favourite,
                                       List<IngredientDTO> ingredients,
                                       Double totalEnergy,
                                       Double totalProteins,
                                       Double totalCarbs,
                                       Double totalFats,
                                       Double totalFibers,
                                       Double dishQuantity,
                                       Double servingSize) {
        this.dishId = dishId;
        this.dishName = dishName;
        this.dishQuantity=dishQuantity;
        this.servingSize=servingSize;
        this.favourite = favourite;

        this.ingredients = ingredients;

        this.totalEnergy = calculateTotalEnergy(totalEnergy, dishQuantity, servingSize);
        this.totalProteins = calculateTotalProteins(totalProteins, dishQuantity, servingSize);
        this.totalCarbs = calculateTotalCarbs(totalCarbs, dishQuantity, servingSize);
        this.totalFats = calculateTotalFats(totalFats, dishQuantity, servingSize);
        this.totalFibers = calculateTotalFibers(totalFibers, dishQuantity, servingSize);
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
