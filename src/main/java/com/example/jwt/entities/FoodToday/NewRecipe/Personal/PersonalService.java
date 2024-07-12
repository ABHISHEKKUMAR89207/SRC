package com.example.jwt.entities.FoodToday.NewRecipe.Personal;


import com.example.jwt.Response.IngredientResponse;
import com.example.jwt.Response.PersonalRecipeResponse;
import com.example.jwt.entities.FoodToday.Dishes;
import com.example.jwt.entities.FoodToday.Ingredients;
import com.example.jwt.entities.FoodToday.NewRecipe.Recipen;
import com.example.jwt.entities.User;
import com.example.jwt.repository.FoodTodayRepository.DishesRepository;
import com.example.jwt.repository.FoodTodayRepository.IngredientsRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonalService {

    @Autowired
    private PersonalRecipeRepository personalRecipe;

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
                    ingredient.setFoodCode(ingredientRequest.getFoodCode());
                    ingredient.setCategory(ingredientRequest.getCategory());
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

@Autowired
private PersonalRecipeRepository personalRepository;
    @Autowired
    private DishesRepository dishesRepository;
    @Autowired
    private IngredientsRepository ingredientsRepository;
//    @Transactional
//    public void deletePersonalRecipe(Long personalRecipeId) {
//        // Fetch the personal recipe
//        Personal personalRecipe = personalRepository.findById(personalRecipeId)
//                .orElseThrow(() -> new EntityNotFoundException("Personal recipe not found with id: " + personalRecipeId));
//
//        // Check if the personal recipe has associated dishes
//        if (personalRecipe.getIngredientList() != null && !personalRecipe.getIngredientList().isEmpty()) {
//            // Delete associated dishes
//            List<Dishes> dishesList = personalRecipe.getIngredientList().stream()
//                    .map(Ingredients::getDishes)
//                    .collect(Collectors.toList());
//            dishesRepository.deleteAll(dishesList);
//        }
//
//        // Check if the personal recipe has associated ingredients
//        if (personalRecipe.getIngredientList() != null && !personalRecipe.getIngredientList().isEmpty()) {
//            // Delete associated ingredients
//            List<Ingredients> ingredientsList = personalRecipe.getIngredientList();
//            ingredientsRepository.deleteAll(ingredientsList);
//        }
//
//        // Delete personal recipe
//        personalRepository.delete(personalRecipe);
//    }

//    @Transactional
//    public void deletePersonalRecipe(Long personalRecipeId) {
//        // Fetch the personal recipe
//        Personal personalRecipe = personalRepository.findById(personalRecipeId)
//                .orElseThrow(() -> new EntityNotFoundException("Personal recipe not found with id: " + personalRecipeId));
//
//        // Check if the personal recipe has associated dishes
//        if (personalRecipe.getIngredientList() != null) {
//            List<Dishes> dishesList = personalRecipe.getIngredientList().stream()
//                    .map(Ingredients::getDishes)
//                    .collect(Collectors.toList());
//
//            // Delete associated dishes
//            if (!dishesList.isEmpty()) {
//                dishesList.forEach(dishesRepository::delete);
//            }
//        }
//
//        // Check if the personal recipe has associated ingredients
//        if (personalRecipe.getIngredientList() != null) {
//            List<Ingredients> ingredientsList = personalRecipe.getIngredientList();
//
//            // Delete associated ingredients
//            if (!ingredientsList.isEmpty()) {
//                ingredientsList.forEach(ingredientsRepository::delete);
//            }
//        }
//
//        // Delete personal recipe
//        personalRepository.delete(personalRecipe);
//    }

    private final Logger logger = LoggerFactory.getLogger(PersonalService.class);

    // Other autowired repositories...

    @Transactional
    public void deletePersonalRecipe(Long personalRecipeId) {
        try {
            // Fetch the personal recipe
            Personal personalRecipe = personalRepository.findById(personalRecipeId)
                    .orElseThrow(() -> new EntityNotFoundException("Personal recipe not found with id: " + personalRecipeId));

            // Logging before deletion
            logger.info("Deleting personal recipe with ID: {}", personalRecipeId);

            // Check if the personal recipe has associated dishes
            if (personalRecipe.getIngredientList() != null) {
                List<Dishes> dishesList = personalRecipe.getIngredientList().stream()
                        .map(Ingredients::getDishes)
                        .filter(dishes -> dishes != null)  // Filter out null dishes
                        .collect(Collectors.toList());

                // Delete associated dishes
                if (!dishesList.isEmpty()) {
                    dishesList.forEach(dishes -> {
                        if (dishes.getDishId() != null) {  // Check if dishes is not null before accessing its properties
                            // Logging before deleting each dish
                            logger.info("Deleting dish with ID: {}", dishes.getDishId());
                            dishesRepository.delete(dishes);
                        }
                    });
                    logger.info("Associated dishes deleted successfully.");
                }
            }

            // Check if the personal recipe has associated ingredients
            if (personalRecipe.getIngredientList() != null) {
                List<Ingredients> ingredientsList = personalRecipe.getIngredientList();

                // Delete associated ingredients
                if (!ingredientsList.isEmpty()) {
                    ingredientsList.forEach(ingredients -> {
                        // Logging before deleting each ingredient
                        logger.info("Deleting ingredient with ID: {}", ingredients.getIngredientId());
                        ingredientsRepository.delete(ingredients);
                    });
                    logger.info("Associated ingredients deleted successfully.");
                }
            }

            // Logging before deleting personal recipe
            logger.info("Deleting personal recipe entity itself.");

            // Delete personal recipe
            personalRepository.delete(personalRecipe);

            logger.info("Personal recipe deleted successfully.");

        } catch (Exception e) {
            logger.error("Error while deleting personal recipe with ID: {}", personalRecipeId, e);
            throw e;
        }
    }


    @Transactional
    public void deleteDish(Long dishId) {
        // Fetch the dish
        Dishes dish = dishesRepository.findById(dishId)
                .orElseThrow(() -> new EntityNotFoundException("Dish not found with id: " + dishId));

        // Delete associated ingredients
        List<Ingredients> ingredientsList = dish.getIngredientList();
        if (ingredientsList != null) {
            ingredientsList.forEach(ingredientsRepository::delete);
        }

        // Delete the dish itself
        dishesRepository.delete(dish);
    }



    public PersonalRecipeResponse getPersonalRecipeByName(User username, String foodName) {
        // Fetch personal recipe by user and food name from repository
        Personal personalRecipe = personalRepository.findByUserAndItemname(username, foodName);

        // Check if the personal recipe exists
        if (personalRecipe != null) {
            // Map personal recipe to response object
            return mapToPersonalRecipeResponse(personalRecipe);
        } else {
            return null;
        }
    }

    private PersonalRecipeResponse mapToPersonalRecipeResponse(Personal personal) {
        PersonalRecipeResponse response = new PersonalRecipeResponse();
        response.setRecipeId(personal.getRecipen().getUidrecipesn());
        response.setRecipeName(personal.getItemname());
        response.setTotalCookedQuantity(personal.getTotalCookedWtG());
        response.setUnit(personal.getServingUnit());
        response.setValueForOneUnit(personal.getOneUnitSize());

        // Map ingredients to response format
        List<IngredientResponse> ingredientResponses = new ArrayList<>();
        for (Ingredients ingredient : personal.getIngredientList()) {
            IngredientResponse ingredientResponse = new IngredientResponse();
            ingredientResponse.setIngredientName(ingredient.getIngredientName());
            ingredientResponse.setIngredientQuantity(ingredient.getIngredientQuantity());
            ingredientResponse.setFoodCode(ingredient.getFoodCode());
            ingredientResponse.setCategory(ingredient.getCategory());
            ingredientResponses.add(ingredientResponse);
        }
        response.setIngredients(ingredientResponses);

        return response;
    }

}
