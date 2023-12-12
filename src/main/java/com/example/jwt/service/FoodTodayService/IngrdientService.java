package com.example.jwt.service.FoodTodayService;

import com.example.jwt.FoodTodayResponse.DishWithIngredientsResponse;
import com.example.jwt.FoodTodayResponse.IngredientsResponse;
import com.example.jwt.FoodTodayResponse.mealResponse;
import com.example.jwt.dtos.FoodTodayDtos.IngredientDTO;
import com.example.jwt.entities.FoodToday.Dishes;
import com.example.jwt.entities.FoodToday.Ingredients;
import com.example.jwt.entities.FoodToday.NinData;
import com.example.jwt.entities.User;
import com.example.jwt.repository.FoodTodayRepository.DishesRepository;
import com.example.jwt.repository.FoodTodayRepository.IngredientsRepository;
import com.example.jwt.repository.FoodTodayRepository.NinDataRepository;
import com.example.jwt.service.TargetAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IngrdientService {
    @Autowired
    private DishesRepository dishesRepository;
    @Autowired
    private IngredientsRepository ingredientsRepository;
    @Autowired
    private NinDataRepository ninDataRepository;
    @Autowired
    private TargetAnalysisService analysisService;


    //final new method save ingredient
    public IngredientsResponse setIngredientsForDish(User user, String mealName, String dishName, List<IngredientDTO> ingredientDTOList) {
// Retrieve the dish by user ID, meal name, and dish name
        List<Dishes> dishesList = dishesRepository.findDishIdByUserUserIdAndMealNameAndDishName(user.getUserId(), mealName, dishName);
        List<IngredientDTO> updatedIngredients = new ArrayList<>();
        Double totalCalories = 0.0;
        Double totalProtiens = 0.0;
        Double totalCarbs = 0.0;
        Double totalFats = 0.0;
        Double totalFibers = 0.0;
        for (Dishes dish : dishesList) {
// Clear the existing list of ingredients associated with the dish
            dish.getIngredientList().clear();
            for (IngredientDTO ingredientDTO : ingredientDTOList) {
                // Search for the corresponding NinData based on ingredient name
                NinData ninData = ninDataRepository.findByName(ingredientDTO.getIngredientName());
                if (ninData != null) {
                    // Create a new Ingredients entity
                    Ingredients ingredient = new Ingredients();
                    ingredient.setIngredientName(ingredientDTO.getIngredientName());
                    ingredient.setIngredientQuantity(ingredientDTO.getIngredientQuantity());
                    ingredient.getNinDataList().add(ninData);
                    updatedIngredients.add(new IngredientDTO(ingredient.getIngredientName(),
                            ingredient.getIngredientQuantity(),
                            ingredient.getIngredientQuantity() * ninData.getCalories(),
                            ingredient.getIngredientQuantity() * ninData.getProtein(),
                            ingredient.getIngredientQuantity() * ninData.getFat(),
                            ingredient.getIngredientQuantity() * ninData.getCarbs(),
                            ingredient.getIngredientQuantity() * ninData.getFiber()));

                    totalCalories += ingredient.getIngredientQuantity() * ninData.getCalories();
                    totalProtiens += ingredient.getIngredientQuantity() * ninData.getProtein();
                    totalCarbs += ingredient.getIngredientQuantity() * ninData.getCarbs();
                    totalFats += ingredient.getIngredientQuantity() * ninData.getFat();
                    totalFibers += ingredient.getIngredientQuantity() * ninData.getFiber();
                    System.out.println("Total Calories Mesage......" + totalCalories);
                    ingredient.setDishes(dish);
                    // Explicitly persist the ingredient to the database
                    ingredient = ingredientsRepository.save(ingredient);
                    // Add the ingredient to the dish's list of ingredients
                    dish.getIngredientList().add(ingredient);
                } else {
                    System.out.println("Me hu gian");

                }
            }
        }

// Save the updated dishes with ingredients
        dishesRepository.saveAll(dishesList);
        return new IngredientsResponse(updatedIngredients,
                totalCalories,
                totalProtiens,
                totalCarbs,
                totalFats,
                totalFibers);
    }

    // Calculate total calories for an ingredient based on NinData
    private Double calculateCalories(Ingredients ingredient) {
        NinData ninData = ninDataRepository.findByName(ingredient.getIngredientName());
        if (ninData != null) {
            return ingredient.getIngredientQuantity() * ninData.getCalories();
        } else {
            return 0.0;
        }
    }

    private Double calculateProteins(Ingredients ingredient) {
        NinData ninData = ninDataRepository.findByName(ingredient.getIngredientName());
        if (ninData != null) {
            return ingredient.getIngredientQuantity() * ninData.getProtein();
        } else {
            return 0.0;
        }
    }

    private Double calculateFats(Ingredients ingredient) {
        NinData ninData = ninDataRepository.findByName(ingredient.getIngredientName());
        if (ninData != null) {
            return ingredient.getIngredientQuantity() * ninData.getFat();
        } else {
            return 0.0;
        }
    }

    private Double calculateCarbs(Ingredients ingredient) {
        NinData ninData = ninDataRepository.findByName(ingredient.getIngredientName());
        if (ninData != null) {
            return ingredient.getIngredientQuantity() * ninData.getCarbs();
        } else {
            return 0.0;
        }
    }

    private Double calculateFibers(Ingredients ingredient) {
        NinData ninData = ninDataRepository.findByName(ingredient.getIngredientName());
        if (ninData != null) {
            return ingredient.getIngredientQuantity() * ninData.getFiber();
        } else {
            return 0.0;
        }
    }


//get all ingredient with date

    public List<mealResponse> getDishesWithIngredientsByDate(User user, LocalDate date) {
        List<Dishes> dishesList = dishesRepository.findDishesByUserUserIdAndDate(user.getUserId(), date);
        List<DishWithIngredientsResponse> responseList = new ArrayList<>();
        List<mealResponse> finalResponseList = new ArrayList<>();


        Double calories = 0.0;
        Double proteins = 0.0;
        Double fats = 0.0;
        Double carbs = 0.0;
        Double fibers = 0.0;

        for (Dishes dish : dishesList) {
            List<Ingredients> ingredients = dish.getIngredientList();
            List<IngredientDTO> ingredientsList = new ArrayList<>();
            Map<String, Double> mapIngredient = new HashMap<>();

            Double totalCalories = 0.0;
            Double totalProtiens = 0.0;
            Double totalCarbs = 0.0;
            Double totalFats = 0.0;
            Double totalFibers = 0.0;


            for (Ingredients ingredient : ingredients) {
                NinData ninData = ninDataRepository.findByName(ingredient.getIngredientName());

                ingredientsList.add(new IngredientDTO(
                        ingredient.getIngredientName(),
                        ingredient.getIngredientQuantity(),
                        calculateCalories(ingredient),
                        calculateProteins(ingredient),
                        calculateCarbs(ingredient),
                        calculateFats(ingredient),
                        calculateFibers(ingredient)
                ));

                totalCalories += ingredient.getIngredientQuantity() * ninData.getCalories();
                totalProtiens += ingredient.getIngredientQuantity() * ninData.getProtein();
                totalCarbs += ingredient.getIngredientQuantity() * ninData.getCarbs();
                totalFats += ingredient.getIngredientQuantity() * ninData.getFat();
                totalFibers += ingredient.getIngredientQuantity() * ninData.getFiber();

            }
            calories = calories + totalCalories;
            proteins = proteins + totalProtiens;
            fats = fats + totalFats;
            carbs = carbs + totalCarbs;
            fibers = fibers + totalFibers;



    mapIngredient.put("Calories",calories);
    mapIngredient.put("Protiens",proteins);
    mapIngredient.put("carbs",carbs);
    mapIngredient.put("fat",fats);
    mapIngredient.put("fibers",fibers);
    analysisService.setmaps(mapIngredient);

    responseList.add(new DishWithIngredientsResponse(dish.getDishId(),dish.getDishName(),dish.isFavourite(), ingredientsList, totalCalories,totalProtiens,totalCarbs,totalFats,totalFibers));
}
    finalResponseList.add(new mealResponse(responseList,calories,proteins,carbs,fats,fibers));
    return finalResponseList;
}



        //get dish with ingreddient using mealName
        public List<mealResponse> getDishesWithIngredientsByDateAndMealType (
                User user, LocalDate date, String mealType){
            List<Dishes> dishesList = dishesRepository.findDishesByUserUserIdAndDate(user.getUserId(), date);
            List<DishWithIngredientsResponse> responseList = new ArrayList<>();
            List<mealResponse> finalResponseList = new ArrayList<>();


            Double calories = 0.0;
            Double proteins = 0.0;
            Double fats = 0.0;
            Double carbs = 0.0;
            Double fibers = 0.0;

            for (Dishes dish : dishesList) {
// Filter dishes based on meal type
                if (dish.getMealName().equalsIgnoreCase(mealType)) {
                    List<Ingredients> ingredients = dish.getIngredientList();
                    List<IngredientDTO> ingredientsList = new ArrayList<>();

                    Double totalCalories = 0.0;
                    Double totalProtiens = 0.0;
                    Double totalCarbs = 0.0;
                    Double totalFats = 0.0;
                    Double totalFibers = 0.0;

                    for (Ingredients ingredient : ingredients) {
                        NinData ninData = ninDataRepository.findByName(ingredient.getIngredientName());
                        ingredientsList.add(new IngredientDTO(
                                ingredient.getIngredientName(),
                                ingredient.getIngredientQuantity(),
                                calculateCalories(ingredient),
                                calculateProteins(ingredient),
                                calculateCarbs(ingredient),
                                calculateFats(ingredient),
                                calculateFibers(ingredient)

                        ));

                        totalCalories += ingredient.getIngredientQuantity() * ninData.getCalories();
                        totalProtiens += ingredient.getIngredientQuantity() * ninData.getProtein();
                        totalCarbs += ingredient.getIngredientQuantity() * ninData.getCarbs();
                        totalFats += ingredient.getIngredientQuantity() * ninData.getFat();
                        totalFibers += ingredient.getIngredientQuantity() * ninData.getFiber();
                    }
                    calories = calories + totalCalories;
                    proteins = proteins + totalProtiens;
                    fats = fats + totalFats;
                    carbs = carbs + totalCarbs;
                    fibers = fibers + totalFibers;

                    responseList.add(new DishWithIngredientsResponse(dish.getDishId(),dish.getDishName(),dish.isFavourite(), ingredientsList, totalCalories, totalProtiens, totalCarbs, totalFats, totalFibers));
                }
            }
            finalResponseList.add(new mealResponse(responseList, calories, proteins, carbs, fats, fibers));
            return finalResponseList;
        }
    }