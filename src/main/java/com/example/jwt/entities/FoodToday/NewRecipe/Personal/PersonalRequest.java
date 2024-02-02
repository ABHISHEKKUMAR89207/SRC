package com.example.jwt.entities.FoodToday.NewRecipe.Personal;



import com.example.jwt.entities.FoodToday.NewRecipe.Ing.IngredientRequest;
import com.example.jwt.entities.FoodToday.NewRecipe.Ing.Ingredientn;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PersonalRequest {
    private Long recipeId;
//    private LocalDate date;
    private String recipeName;
    private double totalCookedQuantity;
    private double oneUnitSize;
//    private Double servingMeasure;
    private Double valueForOneUnit;
    private String servingUnit;
//    private Long userId;  // Assuming you need the user ID in the request
//    private Long recipenId;  // Assuming you need the recipe ID in the request
//    private List<Ingredientn> ingredientns;

    private List<IngredientRequest> ingredients;
}
