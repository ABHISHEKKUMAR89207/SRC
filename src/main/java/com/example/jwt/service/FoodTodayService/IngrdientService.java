package com.example.jwt.service.FoodTodayService;

import com.example.jwt.FoodTodayResponse.DishWithIngredientsResponse;
import com.example.jwt.FoodTodayResponse.IngredientsResponse;
import com.example.jwt.FoodTodayResponse.mealResponse;
import com.example.jwt.dtos.FoodTodayDtos.IngredientDTO;
import com.example.jwt.entities.FoodToday.Dishes;
import com.example.jwt.entities.FoodToday.Ingredients;
import com.example.jwt.entities.FoodToday.NinData;
import com.example.jwt.entities.UnitsDatabase;
import com.example.jwt.entities.User;
import com.example.jwt.repository.FoodTodayRepository.DishesRepository;
import com.example.jwt.repository.FoodTodayRepository.IngredientsRepository;
import com.example.jwt.repository.FoodTodayRepository.NinDataRepository;
import com.example.jwt.repository.UnitsDatabaseRepository;
import com.example.jwt.service.TargetAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

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
    @Autowired
    private UnitsDatabaseRepository unitsDatabaseRepository;


    public String getSIUnitForNutrient(String nutrientName) {
        UnitsDatabase unitsDatabase = unitsDatabaseRepository.findByNutrientName(nutrientName);
        if (unitsDatabase != null) {
            return unitsDatabase.getSIUnit();
        } else {
            // Handle the case where the nutrientName is not found in the UnitsDatabase
            return null;
        }
    }


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

//    private Double calculateProteins(Ingredients ingredient) {
//        NinData ninData = ninDataRepository.findByName(ingredient.getIngredientName());
//        if (ninData != null) {
//            return ingredient.getIngredientQuantity() * ninData.getProtein();
//        } else {
//            return 0.0;
//        }
//    }


    private Double calculateProteins(Ingredients ingredient) {
        NinData ninData = ninDataRepository.findByName(ingredient.getIngredientName());
        if (ninData != null) {
            Double nutrientName = ninData.getProtein(); // Access the nutrient name directly
            String siUnit = getSIUnitForNutrient(String.valueOf(nutrientName));
            // Now you have the SI unit, you can use it as needed.
            System.out.println("SI Unit for " + nutrientName + ": " + siUnit);
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


            mapIngredient.put("Calories", calories);
            mapIngredient.put("Protiens", proteins);
            mapIngredient.put("carbs", carbs);
            mapIngredient.put("fat", fats);
            mapIngredient.put("fibers", fibers);
            analysisService.setmaps(mapIngredient);

            responseList.add(new DishWithIngredientsResponse(dish.getDishId(), dish.getDishName(), dish.isFavourite(), ingredientsList, totalCalories, totalProtiens, totalCarbs, totalFats, totalFibers));
        }
        finalResponseList.add(new mealResponse(responseList, calories, proteins, carbs, fats, fibers));
        return finalResponseList;
    }


    //get dish with ingreddient using mealName
    public List<mealResponse> getDishesWithIngredientsByDateAndMealType(
            User user, LocalDate date, String mealType) {
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

                responseList.add(new DishWithIngredientsResponse(dish.getDishId(), dish.getDishName(), dish.isFavourite(), ingredientsList, totalCalories, totalProtiens, totalCarbs, totalFats, totalFibers));
            }
        }

        Map<String, String> nutrientsNameWithSIUnit = new HashMap<>();

        List<UnitsDatabase> unitsDatabaseList = unitsDatabaseRepository.findAll();
        for (UnitsDatabase unitsDatabase : unitsDatabaseList) {
            String nutrientName = unitsDatabase.getNutrientName();
            String siUnit = unitsDatabase.getSIUnit();
            nutrientsNameWithSIUnit.put(nutrientName, siUnit);
//            System.out.println("Nutrient Name: " + nutrientName + ", SI Unit: " + siUnit);
        }
        finalResponseList.add(new mealResponse(nutrientsNameWithSIUnit,responseList, calories, proteins, carbs, fats, fibers));
        return finalResponseList;
    }


    public List<Map<String, Object>> calculateNutritionSummary(User user, LocalDate startDate, LocalDate endDate) {
        List<Dishes> dishesList = dishesRepository.findByUserUserIdAndDateBetween(user.getUserId(), startDate, endDate);

        List<Map<String, Object>> nutritionList = new ArrayList<>();

        for (Dishes dish : dishesList) {
            Map<String, Object> nutritionObject = new HashMap<>();
            LocalDate dishDate = dish.getDate();

            // Calculate total calories for each dish
            Double totalCalories = 0.0;

            for (Ingredients ingredient : dish.getIngredientList()) {
                NinData ninData = ninDataRepository.findByName(ingredient.getIngredientName());
                totalCalories += ingredient.getIngredientQuantity() * ninData.getCalories();
            }

            nutritionObject.put("activityDate", dishDate.toString());
            nutritionObject.put("calories", totalCalories.intValue()); // Convert to int if needed

            nutritionList.add(nutritionObject);
        }

        return nutritionList;
    }


    public Map<String, Map<String, Map<String, Map<String, Double>>>> getNutritionByMealTypeDishNameAndDate(
            User user, LocalDate startDate, LocalDate endDate) {

        List<Dishes> dishesList = dishesRepository.findByUserUserIdAndDateBetween(user.getUserId(), startDate, endDate);

        Map<String, Map<String, Map<String, Map<String, Double>>>> nutritionByMealAndDate = new HashMap<>();

        for (Dishes dish : dishesList) {
            LocalDate dishDate = dish.getDate();
            String mealType = dish.getMealName();
            String dishName = dish.getDishName();

            if (!nutritionByMealAndDate.containsKey(dishDate.toString())) {
                nutritionByMealAndDate.put(dishDate.toString(), new HashMap<>());
            }

            Map<String, Map<String, Map<String, Double>>> nutritionForDate = nutritionByMealAndDate.get(dishDate.toString());

            if (!nutritionForDate.containsKey(mealType)) {
                nutritionForDate.put(mealType, new HashMap<>());
            }

            Map<String, Map<String, Double>> nutritionForMeal = nutritionForDate.get(mealType);

            if (!nutritionForMeal.containsKey(dishName)) {
                nutritionForMeal.put(dishName, new HashMap<>());
            }

            Map<String, Double> nutritionForDish = nutritionForMeal.get(dishName);

            for (Ingredients ingredient : dish.getIngredientList()) {
                NinData ninData = ninDataRepository.findByName(ingredient.getIngredientName());

                nutritionForDish.compute("Calories", (key, value) -> value != null ?
                        value + ingredient.getIngredientQuantity() * ninData.getCalories() :
                        ingredient.getIngredientQuantity() * ninData.getCalories());

                nutritionForDish.compute("Proteins", (key, value) -> value != null ?
                        value + ingredient.getIngredientQuantity() * ninData.getProtein() :
                        ingredient.getIngredientQuantity() * ninData.getProtein());

                nutritionForDish.compute("Carbs", (key, value) -> value != null ?
                        value + ingredient.getIngredientQuantity() * ninData.getCarbs() :
                        ingredient.getIngredientQuantity() * ninData.getCarbs());

                nutritionForDish.compute("Fats", (key, value) -> value != null ?
                        value + ingredient.getIngredientQuantity() * ninData.getFat() :
                        ingredient.getIngredientQuantity() * ninData.getFat());

                nutritionForDish.compute("Fibers", (key, value) -> value != null ?
                        value + ingredient.getIngredientQuantity() * ninData.getFiber() :
                        ingredient.getIngredientQuantity() * ninData.getFiber());
            }
        }
        return nutritionByMealAndDate;
    }
}