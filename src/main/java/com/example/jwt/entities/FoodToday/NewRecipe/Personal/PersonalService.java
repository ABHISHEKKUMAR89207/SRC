package com.example.jwt.entities.FoodToday.NewRecipe.Personal;


import com.example.jwt.entities.FoodToday.Dishes;
import com.example.jwt.entities.FoodToday.Ingredients;
import com.example.jwt.entities.FoodToday.NewRecipe.RecipeRequest;
import com.example.jwt.entities.FoodToday.NewRecipe.Recipen;
import com.example.jwt.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonalService {

    @Autowired
    private PersonalRecipe personalRecipe;

    @Transactional
    public void saveRecipeAndIngredientsp(PersonalRequest request, User user) {
        Personal newDish = new Personal();
        newDish.setItemname(request.getRecipeName());
        newDish.setTotalCookedWtG(request.getTotalCookedQuantity());
//        newDish.setMealName(request.getMealType());
        newDish.setOneUnitSize(request.getValueForOneUnit());
        // Set other necessary fields in the newDish
//        newDish.setServingMeasure(request.getServingMeasure());
        newDish.setServingUnit(request.getServingUnit());
//        newDish.setNoOfServing(request.getNoOfServing());

        newDish.setUser(user);


        // Set creation date to the current date
        newDish.setDate(LocalDate.now());

        Recipen recipen = new Recipen();
        recipen.setUidrecipesn(request.getRecipeId());
        newDish.setRecipen(recipen);

        List<Ingredients> ingredients = request.getIngredients().stream()
                .map(ingredientRequest -> {
                    Ingredients ingredient = new Ingredients();
                    ingredient.setIngredientName(ingredientRequest.getIngredientName());
                    ingredient.setIngredientQuantity(ingredientRequest.getIngredientQuantity());
                    // Set other necessary fields in the ingredient
                    ingredient.setPersonal(newDish); // Set the reference to Dishes
                    return ingredient;
                })
                .collect(Collectors.toList());


        newDish.setIngredientList(ingredients);
//    ingredientsRepository.save(newDish)

        personalRecipe.save(newDish);
    }

//    public Dishes getDishByPersonalDish(String personalDish, User user) {
//        Optional<Dishes> optionalDishes = dishesRepository.findAllDishByPersonalDishAndUserUserId(personalDish);
//        return optionalDishes.orElse(null);
//
}
