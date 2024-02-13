package com.example.jwt.service.FoodTodayService;

import com.example.jwt.FoodTodayResponse.DishWithIngredientsResponse;
import com.example.jwt.FoodTodayResponse.IngredientsResponse;
import com.example.jwt.FoodTodayResponse.NutritionalInfoResponse;
import com.example.jwt.FoodTodayResponse.mealResponse;
import com.example.jwt.dtos.FoodTodayDtos.IngredientDTO;
import com.example.jwt.entities.FoodToday.Dishes;
import com.example.jwt.entities.FoodToday.Ingredients;
import com.example.jwt.entities.FoodToday.NinData;
import com.example.jwt.entities.FoodToday.Recipe.Recipe;
import com.example.jwt.entities.FoodToday.UnitsDatabase;
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
        UnitsDatabase unitsDatabase = unitsDatabaseRepository.findByFoodUnit(nutrientName);
        if (unitsDatabase != null) {
            return unitsDatabase.getSI_Unit();
        } else {
            // Handle the case where the nutrientName is not found in the UnitsDatabase
            return null;
        }
    }


    //final new method save ingredient
    public IngredientsResponse setIngredientsForDish(User user, String mealName, String dishName, List<IngredientDTO> ingredientDTOList) {
// Retrieve the dish by user ID, meal name, and dish name
        List<Dishes> dishesList = dishesRepository.findDishIdByUserUserIdAndMealNameAndDishNameAndDate(
                user.getUserId(), mealName, dishName, LocalDate.now());        List<IngredientDTO> updatedIngredients = new ArrayList<>();
        Double totalEnergy = 0.0;
        Double totalProtiens = 0.0;
        Double totalCarbs = 0.0;
        Double totalFats = 0.0;
        Double totalFibers = 0.0;
        Double totalThiamine_B1 = 0.0;
        Double totalRiboflavin_B2 = 0.0;
        Double totalNiacin_B3 = 0.0;
        Double totalFolates_B9 = 0.0;
        Double totalRetinolVit_A = 0.0;
        for (Dishes dish : dishesList) {
// Clear the existing list of ingredients associated with the dish
            dish.getIngredientList().clear();
            for (IngredientDTO ingredientDTO : ingredientDTOList) {
                // Search for the corresponding NinData based on ingredient name
                NinData ninData = ninDataRepository.findByFood(ingredientDTO.getIngredientName());
                if (ninData != null) {
                    // Create a new Ingredients entity
                    Ingredients ingredient = new Ingredients();
                    ingredient.setIngredientName(ingredientDTO.getIngredientName());
                    ingredient.setIngredientQuantity(ingredientDTO.getIngredientQuantity());
                    ingredient.setFoodCode(ingredientDTO.getFoodCode());

                    ingredient.getNinDataList().add(ninData);
                    updatedIngredients.add(new IngredientDTO(ingredient.getIngredientName(),
                            ingredient.getIngredientQuantity(),
                            (ingredient.getIngredientQuantity()/100) * ninData.getEnergy(),
                            (ingredient.getIngredientQuantity()/100) * ninData.getProtein(),
                            (ingredient.getIngredientQuantity()/100) * ninData.getTotal_Fat(),
                            (ingredient.getIngredientQuantity()/100) * ninData.getCarbohydrate(),
                            (ingredient.getIngredientQuantity()/100) * ninData.getTotal_Dietary_Fibre(),
                            (ingredient.getIngredientQuantity()/100) * ninData.getMagnesium(),
                            (ingredient.getIngredientQuantity()/100) * ninData.getZinc(),
                            (ingredient.getIngredientQuantity()/100) * ninData.getIron(),
                            (ingredient.getIngredientQuantity()/100) * ninData.getCalcium(),
                            (ingredient.getIngredientQuantity()/100) * ninData.getTotalFolates_B9(),
                        (ingredient.getIngredientQuantity()/100) * ninData.getNiacin_B3(),
                    (ingredient.getIngredientQuantity()/100) * ninData.getThiamine_B1(),
                    (ingredient.getIngredientQuantity()/100) * ninData.getRetinolVit_A(),
                    (ingredient.getIngredientQuantity()/100) * ninData.getRiboflavin_B2()



                    ));

                    totalEnergy += (ingredient.getIngredientQuantity()/100) * ninData.getEnergy();
                    totalProtiens += (ingredient.getIngredientQuantity()/100) * ninData.getProtein();
                    totalCarbs += (ingredient.getIngredientQuantity()/100) * ninData.getCarbohydrate();
                    totalFats += (ingredient.getIngredientQuantity()/100) * ninData.getTotal_Fat();
                    totalFibers += (ingredient.getIngredientQuantity()/100) * ninData.getTotal_Dietary_Fibre();
                    System.out.println("Total Calories Mesage......" + totalEnergy);
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
                totalEnergy,
                totalProtiens,
                totalCarbs,
                totalFats,
                totalFibers);
    }

    // Calculate total calories for an ingredient based on NinData
    private Double calculateEnergy(Ingredients ingredient) {
        NinData ninData = ninDataRepository.findByFoodCode(ingredient.getFoodCode());
        if (ninData != null) {
            return (ingredient.getIngredientQuantity()/100) * ninData.getEnergy();
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
        NinData ninData = ninDataRepository.findByFoodCode(ingredient.getFoodCode());
        if (ninData != null) {
            Double nutrientName = ninData.getProtein(); // Access the nutrient name directly
            String siUnit = getSIUnitForNutrient(String.valueOf(nutrientName));
            // Now you have the SI unit, you can use it as needed.
            System.out.println("SI Unit for " + nutrientName + ": " + siUnit);
            return (ingredient.getIngredientQuantity()/100) * ninData.getProtein();
        } else {
            return 0.0;
        }
    }

    private Double calculateFats(Ingredients ingredient) {
        NinData ninData = ninDataRepository.findByFoodCode(ingredient.getFoodCode());
        if (ninData != null) {
            return (ingredient.getIngredientQuantity()/100) * ninData.getTotal_Fat();
        } else {
            return 0.0;
        }
    }

    private Double calculateCarbs(Ingredients ingredient) {
        NinData ninData = ninDataRepository.findByFoodCode(ingredient.getFoodCode());
        if (ninData != null) {
            return (ingredient.getIngredientQuantity()/100) * ninData.getCarbohydrate();
        } else {
            return 0.0;
        }
    }

    private Double calculateFibers(Ingredients ingredient) {
        NinData ninData = ninDataRepository.findByFoodCode(ingredient.getFoodCode());
        if (ninData != null) {
            return (ingredient.getIngredientQuantity()/100) * ninData.getTotal_Dietary_Fibre();
        } else {
            return 0.0;
        }
    }

    private Double calculateIron(Ingredients ingredient) {
        NinData ninData = ninDataRepository.findByFoodCode(ingredient.getFoodCode());
        if (ninData != null) {
            return (ingredient.getIngredientQuantity()/100) * ninData.getIron();
        } else {
            return 0.0;
        }
    }

    private Double calculateMagnesium(Ingredients ingredient) {
        NinData ninData = ninDataRepository.findByFoodCode(ingredient.getFoodCode());
        if (ninData != null) {
            return (ingredient.getIngredientQuantity()/100) * ninData.getMagnesium();
        } else {
            return 0.0;
        }
    }

    private Double calculateCalcium(Ingredients ingredient) {
        NinData ninData = ninDataRepository.findByFoodCode(ingredient.getFoodCode());
        if (ninData != null) {
            return (ingredient.getIngredientQuantity()/100) * ninData.getCalcium();
        } else {
            return 0.0;
        }
    }

    private Double calculateZinc(Ingredients ingredient) {
        NinData ninData = ninDataRepository.findByFoodCode(ingredient.getFoodCode());
        if (ninData != null) {
            return (ingredient.getIngredientQuantity()/100) * ninData.getZinc();
        } else {
            return 0.0;
        }
    }

    private Double calculateThiamin(Ingredients ingredient) {
        NinData ninData = ninDataRepository.findByFoodCode(ingredient.getFoodCode());
        if (ninData != null) {
            return (ingredient.getIngredientQuantity()/100) * ninData.getThiamine_B1();
        } else {
            return 0.0;
        }
    }

    private Double calculateRiboflavin(Ingredients ingredient) {
        NinData ninData = ninDataRepository.findByFoodCode(ingredient.getFoodCode());
        if (ninData != null) {
            return (ingredient.getIngredientQuantity()/100) * ninData.getRiboflavin_B2();
        } else {
            return 0.0;
        }
    }

    private Double calculateNiacin(Ingredients ingredient) {
        NinData ninData = ninDataRepository.findByFoodCode(ingredient.getFoodCode());
        if (ninData != null) {
            return (ingredient.getIngredientQuantity()/100) * ninData.getNiacin_B3();
        } else {
            return 0.0;
        }
    }
//
//    private Double calculateVitaminB6(Ingredients ingredient) {
//        NinData ninData = ninDataRepository.findByFoodCode(ingredient.getFoodCode());
//        if (ninData != null) {
//            return (ingredient.getIngredientQuantity()/100) * ninData.get;
//        } else {
//            return 0.0;
//        }
//    }
//
    private Double calculateTotalFolates(Ingredients ingredient) {
        NinData ninData = ninDataRepository.findByFoodCode(ingredient.getFoodCode());
        if (ninData != null) {
            return (ingredient.getIngredientQuantity()/100) * ninData.getTotalFolates_B9();
        } else {
            return 0.0;
        }
    }

    private Double calculateVitaminA(Ingredients ingredient) {
        NinData ninData = ninDataRepository.findByFoodCode(ingredient.getFoodCode());
        if (ninData != null) {
            return (ingredient.getIngredientQuantity()/100) * ninData.getRetinolVit_A();
        } else {
            return 0.0;
        }
    }
//
//    private Double calculateVitaminD(Ingredients ingredient) {
//        NinData ninData = ninDataRepository.findByFoodCode(ingredient.getFoodCode());
//        if (ninData != null) {
//            return (ingredient.getIngredientQuantity()/100) * ninData.getIron();
//        } else {
//            return 0.0;
//        }
//    }
//get all ingredient with date

//    public List<mealResponse> getDishesWithIngredientsByDate(User user, LocalDate date) {
//        List<Dishes> dishesList = dishesRepository.findDishesByUserUserIdAndDate(user.getUserId(), date);
//        List<DishWithIngredientsResponse> responseList = new ArrayList<>();
//        List<mealResponse> finalResponseList = new ArrayList<>();
//
//
//        Double energy = 0.0;
//        Double proteins = 0.0;
//        Double fats = 0.0;
//        Double carbs = 0.0;
//        Double fibers = 0.0;
//
//        for (Dishes dish : dishesList) {
//            List<Ingredients> ingredients = dish.getIngredientList();
//            List<IngredientDTO> ingredientsList = new ArrayList<>();
//            Map<String, Double> mapIngredient = new HashMap<>();
//
//            Double totalEnergy = 0.0;
//            Double totalProtiens = 0.0;
//            Double totalCarbs = 0.0;
//            Double totalFats = 0.0;
//            Double totalFibers = 0.0;
//
//
//            for (Ingredients ingredient : ingredients) {
//                NinData ninData = ninDataRepository.findByFood(ingredient.getIngredientName());
//
//                ingredientsList.add(new IngredientDTO(
//                        ingredient.getIngredientName(),
//                        ingredient.getIngredientQuantity(),
//                        calculateEnergy(ingredient),
//                        calculateProteins(ingredient),
//                        calculateCarbs(ingredient),
//                        calculateFats(ingredient),
//                        calculateFibers(ingredient)
//                ));
//
//                totalEnergy += (ingredient.getIngredientQuantity()/100) * ninData.getEnergy();
//                totalProtiens += (ingredient.getIngredientQuantity()/100) * ninData.getProtein();
//                totalCarbs += (ingredient.getIngredientQuantity()/100) * ninData.getCarbohydrate();
//                totalFats += (ingredient.getIngredientQuantity()/100) * ninData.getTotal_Fat();
//                totalFibers += (ingredient.getIngredientQuantity()/100) * ninData.getTotal_Dietary_Fibre();
//
//            }
//            energy = energy + totalEnergy;
//            proteins = proteins + totalProtiens;
//            fats = fats + totalFats;
//            carbs = carbs + totalCarbs;
//            fibers = fibers + totalFibers;
//
//
//            mapIngredient.put("Energy", energy);
//            mapIngredient.put("Protiens", proteins);
//            mapIngredient.put("carbs", carbs);
//            mapIngredient.put("fat", fats);
//            mapIngredient.put("fibers", fibers);
//            analysisService.setmaps(mapIngredient);
//
//
//            responseList.add(new DishWithIngredientsResponse(dish.getDishId(), dish.getDishName(), dish.isFavourite(), ingredientsList, totalEnergy, totalProtiens, totalCarbs, totalFats, totalFibers));
//        }
//
//        Map<String, String> nutrientsNameWithSIUnit = new HashMap<>();
//
//        List<UnitsDatabase> unitsDatabaseList = unitsDatabaseRepository.findAll();
//        for (UnitsDatabase unitsDatabase : unitsDatabaseList) {
//            String nutrientName = unitsDatabase.getFood_unit();
//            String siUnit = unitsDatabase.getSI_Unit();
//            nutrientsNameWithSIUnit.put(nutrientName, siUnit);
//        }
//        finalResponseList.add(new mealResponse(nutrientsNameWithSIUnit,responseList, energy, proteins, carbs, fats, fibers));
//        return finalResponseList;
//    }


    public List<mealResponse> getDishesWithIngredientsByDate(User user, LocalDate date) {
        List<Dishes> dishesList = dishesRepository.findDishesByUserUserIdAndDate(user.getUserId(), date);
        List<DishWithIngredientsResponse> responseList = new ArrayList<>();
        List<mealResponse> finalResponseList = new ArrayList<>();

        Double energy = 0.0;
        Double proteins = 0.0;
        Double fats = 0.0;
        Double carbs = 0.0;
        Double fibers = 0.0;
        Double magnesium = 0.0;
        Double iron = 0.0;
        Double zinc = 0.0;
        Double calcium = 0.0;
        Double thiamine_B1 = 0.0;
        Double riboflavin_B2 = 0.0;
        Double niacin_B3 = 0.0;
        Double folates_B9 = 0.0;
        Double retinolVit_A = 0.0;


        for (Dishes dish : dishesList) {
            List<Ingredients> ingredients = dish.getIngredientList();
            List<IngredientDTO> ingredientsList = new ArrayList<>();
            Map<String, Double> mapIngredient = new HashMap<>();

            Double totalEnergy = 0.0;
            Double totalProteins = 0.0;
            Double totalCarbs = 0.0;
            Double totalFats = 0.0;
            Double totalFibers = 0.0;
            Double totalMagnesium = 0.0;
            Double totalIron = 0.0;
            Double totalZinc = 0.0;
            Double totalCalcium = 0.0;
            Double totalThiamine_B1 = 0.0;
            Double totalRiboflavin_B2 = 0.0;
            Double totalNiacin_B3 = 0.0;
            Double totalFolates_B9 = 0.0;
            Double totalRetinolVit_A = 0.0;


            for (Ingredients ingredient : ingredients) {
                NinData ninData = ninDataRepository.findByFoodCode(ingredient.getFoodCode());

                ingredientsList.add(new IngredientDTO(
                        ingredient.getIngredientName(),
                        ingredient.getIngredientQuantity(),
                        calculateEnergy(ingredient),
                        calculateProteins(ingredient),
                        calculateCarbs(ingredient),
                        calculateFats(ingredient),
                        calculateFibers(ingredient),
                        calculateMagnesium(ingredient),
                        calculateZinc(ingredient),
                        calculateCalcium(ingredient),
                        calculateIron(ingredient),
                        calculateThiamin(ingredient),
                        calculateNiacin(ingredient),
                        calculateRiboflavin(ingredient),
                        calculateTotalFolates(ingredient),
                        calculateVitaminA(ingredient)
                ));

                totalEnergy += (ingredient.getIngredientQuantity()/100) * ninData.getEnergy();
                totalProteins += (ingredient.getIngredientQuantity()/100) * ninData.getProtein();
                totalCarbs += (ingredient.getIngredientQuantity()/100) * ninData.getCarbohydrate();
                totalFats += (ingredient.getIngredientQuantity()/100) * ninData.getTotal_Fat();
                totalFibers += (ingredient.getIngredientQuantity()/100) * ninData.getTotal_Dietary_Fibre();
                totalMagnesium += (ingredient.getIngredientQuantity()/100) * ninData.getMagnesium();
                totalZinc += (ingredient.getIngredientQuantity()/100) * ninData.getZinc();
                totalIron += (ingredient.getIngredientQuantity()/100) * ninData.getIron();
                totalCalcium += (ingredient.getIngredientQuantity()/100) * ninData.getCalcium();
                totalFolates_B9 += (ingredient.getIngredientQuantity()/100) * ninData.getTotalFolates_B9();
                totalNiacin_B3 += (ingredient.getIngredientQuantity()/100) * ninData.getNiacin_B3();
                totalThiamine_B1 += (ingredient.getIngredientQuantity()/100) * ninData.getThiamine_B1();
                totalRetinolVit_A += (ingredient.getIngredientQuantity()/100) * ninData.getRetinolVit_A();
                totalRiboflavin_B2 += (ingredient.getIngredientQuantity()/100) * ninData.getRiboflavin_B2();

            }

            energy = energy + totalEnergy;
            proteins = proteins + totalProteins;
            fats = fats + totalFats;
            carbs = carbs + totalCarbs;
            fibers = fibers + totalFibers;
            magnesium = magnesium + totalMagnesium;
            zinc = zinc + totalZinc;
            iron = iron + totalIron;
            calcium = calcium + totalCalcium;
            thiamine_B1 = thiamine_B1 + totalThiamine_B1;
            retinolVit_A = retinolVit_A + totalRetinolVit_A;
            riboflavin_B2 = riboflavin_B2 + totalRiboflavin_B2;
            niacin_B3 = niacin_B3 + totalNiacin_B3;
            folates_B9 = folates_B9 + totalFolates_B9;


            mapIngredient.put("Energy", energy);
            mapIngredient.put("Protiens", proteins);
            mapIngredient.put("carbs", carbs);
            mapIngredient.put("fat", fats);
            mapIngredient.put("fibers", fibers);
            mapIngredient.put("Magnesium", magnesium);
            mapIngredient.put("Zinc", zinc);
            mapIngredient.put("Iron", iron);
            mapIngredient.put("Calcium", calcium);
            mapIngredient.put("Thiamine-B1", thiamine_B1);
            mapIngredient.put("Retinol-Vit-A", retinolVit_A);
            mapIngredient.put("Riboflavin-B2", riboflavin_B2);
            mapIngredient.put("Niacin-B3", niacin_B3);
            mapIngredient.put("Folates-B9", folates_B9);

            analysisService.setmaps(mapIngredient);


            // Fetch recipe details based on dish's recipe ID
//            Recipe recipe = dish.getRecipe();
//            if (recipe != null) {
//                // Your existing calculation logic
//                Double recipeEnergy = recipe.getEnergy_joules();
//                Double recipeProteins = recipe.getProtein();
//                Double recipeCarbs = recipe.getCarbohydrate();
//                Double recipeFats = recipe.getTotal_fat();
//                Double recipeFibers = recipe.getTotal_dietary_fibre();
//
//                energy += recipeEnergy;
//                proteins += recipeProteins;
//                fats += recipeFats;
//                carbs += recipeCarbs;
//                fibers += recipeFibers;
//
//                // Update the response list with recipe details
//                responseList.add(new DishWithIngredientsResponse(
//                        dish.getDishId(),
//                        dish.getDishName(),
//                        dish.getMealName(),
//                        dish.isFavourite(),
//                        ingredientsList,
//                        totalEnergy + recipeEnergy,
//                        totalProteins + recipeProteins,
//                        totalCarbs + recipeCarbs,
//                        totalFats + recipeFats,
//                        totalFibers + recipeFibers
//                ));
//            } else {

                // Update the response list without recipe details
                responseList.add(new DishWithIngredientsResponse(
                        dish.getDishId(),
                        dish.getDishName(),
                        dish.getMealName(),
                        dish.isFavourite(),
                        ingredientsList,
                        totalEnergy,
                        totalProteins,
                        totalCarbs,
                        totalFats,
                        totalFibers,
                        totalMagnesium,
                        totalZinc,
                        totalIron,
                        totalCalcium,
                        totalThiamine_B1,
                        totalRiboflavin_B2,
                        totalNiacin_B3,
                        totalFolates_B9,
                        totalRetinolVit_A,
                        dish.getDishQuantity(),  // provide dish quantity here
                        dish.getServingSize()  // provide serving size here
                ));

//            }
        }

        Map<String, String> nutrientsNameWithSIUnit = new HashMap<>();

        List<UnitsDatabase> unitsDatabaseList = unitsDatabaseRepository.findAll();
        for (UnitsDatabase unitsDatabase : unitsDatabaseList) {
            String nutrientName = unitsDatabase.getFood_unit();
            String siUnit = unitsDatabase.getSI_Unit();
            nutrientsNameWithSIUnit.put(nutrientName, siUnit);
        }

        finalResponseList.add(new mealResponse(nutrientsNameWithSIUnit, responseList, energy, proteins, carbs, fats, fibers,magnesium,zinc,iron,calcium,thiamine_B1, riboflavin_B2,niacin_B3,folates_B9,retinolVit_A));
        return finalResponseList;
    }




    //get dish with ingreddient using mealName
//    public List<mealResponse> getDishesWithIngredientsByDateAndMealType(
//            User user, LocalDate date, String mealType) {
//        List<Dishes> dishesList = dishesRepository.findDishesByUserUserIdAndDate(user.getUserId(), date);
//        List<DishWithIngredientsResponse> responseList = new ArrayList<>();
//        List<mealResponse> finalResponseList = new ArrayList<>();
//
//
//        Double energy = 0.0;
//        Double proteins = 0.0;
//        Double fats = 0.0;
//        Double carbs = 0.0;
//        Double fibers = 0.0;
//
//        for (Dishes dish : dishesList) {
//// Filter dishes based on meal type
//            if (dish.getMealName().equalsIgnoreCase(mealType)) {
//                List<Ingredients> ingredients = dish.getIngredientList();
//                List<IngredientDTO> ingredientsList = new ArrayList<>();
//
//                Double totalEnergy = 0.0;
//                Double totalProtiens = 0.0;
//                Double totalCarbs = 0.0;
//                Double totalFats = 0.0;
//                Double totalFibers = 0.0;
//
//                for (Ingredients ingredient : ingredients) {
//                    NinData ninData = ninDataRepository.findByFood(ingredient.getIngredientName());
//                    ingredientsList.add(new IngredientDTO(
//                            ingredient.getIngredientName(),
//                            ingredient.getIngredientQuantity(),
//                            calculateEnergy(ingredient),
//                            calculateProteins(ingredient),
//                            calculateCarbs(ingredient),
//                            calculateFats(ingredient),
//                            calculateFibers(ingredient)
//
//                    ));
//
//                    totalEnergy += (ingredient.getIngredientQuantity()/100) * ninData.getEnergy();
//                    totalProtiens += (ingredient.getIngredientQuantity()/100) * ninData.getProtein();
//                    totalCarbs += (ingredient.getIngredientQuantity()/100) * ninData.getCarbohydrate();
//                    totalFats += (ingredient.getIngredientQuantity()/100) * ninData.getTotal_Fat();
//                    totalFibers += (ingredient.getIngredientQuantity()/100) * ninData.getTotal_Dietary_Fibre();
//                }
//                energy = energy + totalEnergy;
//                proteins = proteins + totalProtiens;
//                fats = fats + totalFats;
//                carbs = carbs + totalCarbs;
//                fibers = fibers + totalFibers;
//
//                responseList.add(new DishWithIngredientsResponse(dish.getDishId(), dish.getDishName(), dish.isFavourite(), ingredientsList, totalEnergy, totalProtiens, totalCarbs, totalFats, totalFibers));
//            }
//        }
//
//        Map<String, String> nutrientsNameWithSIUnit = new HashMap<>();
//
//        List<UnitsDatabase> unitsDatabaseList = unitsDatabaseRepository.findAll();
//        for (UnitsDatabase unitsDatabase : unitsDatabaseList) {
//            String nutrientName = unitsDatabase.getFood_unit();
//            String siUnit = unitsDatabase.getSI_Unit();
//            nutrientsNameWithSIUnit.put(nutrientName, siUnit);
////            System.out.println("Nutrient Name: " + nutrientName + ", SI Unit: " + siUnit);
//        }
//        finalResponseList.add(new mealResponse(nutrientsNameWithSIUnit,responseList, energy, proteins, carbs, fats, fibers));
//        return finalResponseList;
//    }

    //final
//    public List<mealResponse> getDishesWithIngredientsByDateAndMealType(
//            User user, LocalDate date, String mealType) {
//        List<Dishes> dishesList = dishesRepository.findDishesByUserUserIdAndDate(user.getUserId(), date);
//        List<DishWithIngredientsResponse> responseList = new ArrayList<>();
//        List<mealResponse> finalResponseList = new ArrayList<>();
//
//        Double energy = 0.0;
//        Double proteins = 0.0;
//        Double fats = 0.0;
//        Double carbs = 0.0;
//        Double fibers = 0.0;
//
//        for (Dishes dish : dishesList) {
//            // Filter dishes based on meal type
//            if (dish.getMealName().equalsIgnoreCase(mealType)) {
//                List<Ingredients> ingredients = dish.getIngredientList();
//                List<IngredientDTO> ingredientsList = new ArrayList<>();
//
//                Double totalEnergy = 0.0;
//                Double totalProteins = 0.0;
//                Double totalCarbs = 0.0;
//                Double totalFats = 0.0;
//                Double totalFibers = 0.0;
//
//                for (Ingredients ingredient : ingredients) {
//                    NinData ninData = ninDataRepository.findByFood(ingredient.getIngredientName());
//                    ingredientsList.add(new IngredientDTO(
//                            ingredient.getIngredientName(),
//                            ingredient.getIngredientQuantity(),
//                            calculateEnergy(ingredient),
//                            calculateProteins(ingredient),
//                            calculateCarbs(ingredient),
//                            calculateFats(ingredient),
//                            calculateFibers(ingredient)
//                    ));
//
//                    totalEnergy += (ingredient.getIngredientQuantity()/100) * ninData.getEnergy();
//                    totalProteins += (ingredient.getIngredientQuantity()/100) * ninData.getProtein();
//                    totalCarbs += (ingredient.getIngredientQuantity()/100) * ninData.getCarbohydrate();
//                    totalFats += (ingredient.getIngredientQuantity()/100) * ninData.getTotal_Fat();
//                    totalFibers += (ingredient.getIngredientQuantity()/100) * ninData.getTotal_Dietary_Fibre();
//                }
//
//                energy = energy + totalEnergy;
//                proteins = proteins + totalProteins;
//                fats = fats + totalFats;
//                carbs = carbs + totalCarbs;
//                fibers = fibers + totalFibers;
//
//                // Fetch recipe details based on dish's recipe ID
//                Recipe recipe = dish.getRecipe();
//                if (recipe != null) {
//                    // Your existing calculation logic
//                    Double recipeEnergy = recipe.getEnergy_joules();
//                    Double recipeProteins = recipe.getProtein();
//                    Double recipeCarbs = recipe.getCarbohydrate();
//                    Double recipeFats = recipe.getTotal_fat();
//                    Double recipeFibers = recipe.getTotal_dietary_fibre();
//
//                    energy += recipeEnergy;
//                    proteins += recipeProteins;
//                    fats += recipeFats;
//                    carbs += recipeCarbs;
//                    fibers += recipeFibers;
//
//                    // Update the response list with recipe details
//                    responseList.add(new DishWithIngredientsResponse(
//                            dish.getDishId(),
//                            dish.getDishName(),
//                            dish.isFavourite(),
//                            ingredientsList,
//                            totalEnergy + recipeEnergy,
//                            totalProteins + recipeProteins,
//                            totalCarbs + recipeCarbs,
//                            totalFats + recipeFats,
//                            totalFibers + recipeFibers
//                    ));
//                } else {
//                    // Update the response list without recipe details
//                    responseList.add(new DishWithIngredientsResponse(
//                            dish.getDishId(),
//                            dish.getDishName(),
//                            dish.isFavourite(),
//                            ingredientsList,
//                            totalEnergy,
//                            totalProteins,
//                            totalCarbs,
//                            totalFats,
//                            totalFibers
//                    ));
//                }
//            }
//        }
//
//        Map<String, String> nutrientsNameWithSIUnit = new HashMap<>();
//
//        List<UnitsDatabase> unitsDatabaseList = unitsDatabaseRepository.findAll();
//        for (UnitsDatabase unitsDatabase : unitsDatabaseList) {
//            String nutrientName = unitsDatabase.getFood_unit();
//            String siUnit = unitsDatabase.getSI_Unit();
//            nutrientsNameWithSIUnit.put(nutrientName, siUnit);
//        }
//
//        finalResponseList.add(new mealResponse(nutrientsNameWithSIUnit, responseList, energy, proteins, carbs, fats, fibers));
//        return finalResponseList;
//    }


//    public List<mealResponse> getDishesWithIngredientsByDateAndMealTypee(
//            User user, LocalDate date, String mealType) {
//        List<Dishes> dishesList = dishesRepository.findDishesByUserUserIdAndDate(user.getUserId(), date);
//        List<DishWithIngredientsResponse> responseList = new ArrayList<>();
//        List<mealResponse> finalResponseList = new ArrayList<>();
//
//        Double div = 0.0;
//        Double totalIng=0.0;
//        Double onegrmEng=0.0;
//        Double onegrmPro=0.0;
//        Double onegrmFat=0.0;
//        Double onegrmCarb=0.0;
//        Double onegrmFib=0.0;
//
//
//        Double energy = 0.0;
//        Double proteins = 0.0;
//        Double fats = 0.0;
//        Double carbs = 0.0;
//        Double fibers = 0.0;
//
//        for (Dishes dish : dishesList) {
//            // Filter dishes based on meal type
//            if (dish.getMealName().equalsIgnoreCase(mealType)) {
//                List<Ingredients> ingredients = dish.getIngredientList();
//                List<IngredientDTO> ingredientsList = new ArrayList<>();
//
//
//                Double totalEnergy = 0.0;
//                Double totalProteins = 0.0;
//                Double totalCarbs = 0.0;
//                Double totalFats = 0.0;
//                Double totalFibers = 0.0;
//
//                for (Ingredients ingredient : ingredients) {
//                    NinData ninData = ninDataRepository.findByFood(ingredient.getIngredientName());
//                    ingredientsList.add(new IngredientDTO(
//                            ingredient.getIngredientName(),
//                            ingredient.getIngredientQuantity(),
//                            calculateEnergy(ingredient),
//                            calculateProteins(ingredient),
//                            calculateCarbs(ingredient),
//                            calculateFats(ingredient),
//                            calculateFibers(ingredient),
//                            calculateMagnesium(ingredient),
//                            calculateZinc(ingredient),
//                            calculateCalcium(ingredient),
//                            calculateIron(ingredient)
//                    ));
//
//                    totalEnergy += (ingredient.getIngredientQuantity()/100) * ninData.getEnergy();
//                    totalProteins += (ingredient.getIngredientQuantity()/100) * ninData.getProtein();
//                    totalCarbs += (ingredient.getIngredientQuantity()/100) * ninData.getCarbohydrate();
//                    totalFats += (ingredient.getIngredientQuantity()/100) * ninData.getTotal_Fat();
//                    totalFibers += (ingredient.getIngredientQuantity()/100) * ninData.getTotal_Dietary_Fibre();
//                }
//
//                energy = energy + totalEnergy;
//                proteins = proteins + totalProteins;
//                fats = fats + totalFats;
//                carbs = carbs + totalCarbs;
//                fibers = fibers + totalFibers;
//
//                // Fetch recipe details based on dish's recipe ID
////                Recipe recipe = dish.getRecipe();
////                if (recipe != null) {
////                    // Your existing calculation logic
////                    Double recipeEnergy = recipe.getEnergy_joules();
////                    Double recipeProteins = recipe.getProtein();
////                    Double recipeCarbs = recipe.getCarbohydrate();
////                    Double recipeFats = recipe.getTotal_fat();
////                    Double recipeFibers = recipe.getTotal_dietary_fibre();
////
////                    energy += recipeEnergy;
////                    proteins += recipeProteins;
////                    fats += recipeFats;
////                    carbs += recipeCarbs;
////                    fibers += recipeFibers;
////
////                    // Update the response list with recipe details
////                    responseList.add(new DishWithIngredientsResponse(
////                            dish.getDishId(),
////                            dish.getDishName(),
////                            dish.isFavourite(),
////                            ingredientsList,
////                            totalEnergy + recipeEnergy,
////                            totalProteins + recipeProteins,
////                            totalCarbs + recipeCarbs,
////                            totalFats + recipeFats,
////                            totalFibers + recipeFibers
////                    ));
////                } else {
//                    // Update the response list without recipe details
//                    responseList.add(new DishWithIngredientsResponse(
//                            dish.getDishId(),
//                            dish.getDishName(),
//                            dish.getMealName(),
//                            dish.isFavourite(),
//                            ingredientsList,
//                            totalEnergy,
//                            totalProteins,
//                            totalCarbs,
//                            totalFats,
//                            totalFibers,
//
//                    ));
//                }
//
////            }
//            onegrmEng = (energy/dish.getDishQuantity())*100;
//            onegrmPro = (proteins/dish.getDishQuantity())*100;
//            onegrmCarb = (carbs/dish.getDishQuantity())*100;
//            onegrmFat = (fats/dish.getDishQuantity())*100;
//            onegrmFib = (fibers/dish.getDishQuantity())*100;
//
//            System.out.println("serving Energy  "+onegrmEng);
//            System.out.println("serving protein  "+onegrmPro);
//            System.out.println("serving carbs  "+onegrmCarb);
//            System.out.println("serving Fats  "+onegrmFat);
//            System.out.println("serving Fibers  "+onegrmFib);
////
//        }
//
//
////        Map<String, String> nutrientsNameWithSIUnit = new HashMap<>();
////
////        List<UnitsDatabase> unitsDatabaseList = unitsDatabaseRepository.findAll();
////        for (UnitsDatabase unitsDatabase : unitsDatabaseList) {
////            String nutrientName = unitsDatabase.getFood_unit();
////            String siUnit = unitsDatabase.getSI_Unit();
////            nutrientsNameWithSIUnit.put(nutrientName, siUnit);
////        }
//
//        finalResponseList.add(new mealResponse( responseList, onegrmEng, onegrmPro, onegrmCarb, onegrmFat, onegrmFib));
//        return finalResponseList;
//    }


    public List<mealResponse> getDishesWithIngredientsByDateAndMealType(
            User user, LocalDate date, String mealType) {
        List<Dishes> dishesList = dishesRepository.findDishesByUserUserIdAndDate(user.getUserId(), date);
        List<DishWithIngredientsResponse> responseList = new ArrayList<>();
        List<mealResponse> finalResponseList = new ArrayList<>();
        Double div = 0.0;
        Double totalIng=0.0;
        Double onegrmEng=0.0;
        Double onegrmPro=0.0;
        Double onegrmFat=0.0;
        Double onegrmCarb=0.0;
        Double onegrmFib=0.0;
        Double onegrmMagnesium = 0.0;
        Double onegrmIron = 0.0;
        Double onegrmZinc = 0.0;
        Double onegrmCalcium = 0.0;
        Double onegramThiamine_B1=0.0;
        Double onegrmRiboflavin_B2 = 0.0;
        Double onegrmNiacin_B3 = 0.0;
        Double onegrmFolates_B9 = 0.0;
        Double onegrmRetinolVit_A = 0.0;

        Double energy = 0.0;
        Double proteins = 0.0;
        Double fats = 0.0;
        Double carbs = 0.0;
        Double fibers = 0.0;
        Double magnesium = 0.0;
        Double iron = 0.0;
        Double zinc = 0.0;
        Double calcium = 0.0;
        Double thiamine_B1 = 0.0;
        Double riboflavin_B2 = 0.0;
        Double niacin_B3 = 0.0;
        Double folates_B9 = 0.0;
        Double retinolVit_A = 0.0;


        for (Dishes dish : dishesList) {
            // Filter dishes based on meal type
            if (dish.getMealName().equalsIgnoreCase(mealType)) {
                List<Ingredients> ingredients = dish.getIngredientList();
                List<IngredientDTO> ingredientsList = new ArrayList<>();


                Double totalEnergy = 0.0;
                Double totalProteins = 0.0;
                Double totalCarbs = 0.0;
                Double totalFats = 0.0;
                Double totalFibers = 0.0;
                Double totalMagnesium = 0.0;
                Double totalIron = 0.0;
                Double totalZinc = 0.0;
                Double totalCalcium = 0.0;
                Double totalThiamine_B1 = 0.0;
                Double totalRiboflavin_B2 = 0.0;
                Double totalNiacin_B3 = 0.0;
                Double totalFolates_B9 = 0.0;
                Double totalRetinolVit_A = 0.0;

                for (Ingredients ingredient : ingredients) {
                    NinData ninData = ninDataRepository.findByFoodCode(ingredient.getFoodCode());
                    ingredientsList.add(new IngredientDTO(
                            ingredient.getIngredientName(),
                            ingredient.getIngredientQuantity(),
                            calculateEnergy(ingredient),
                            calculateProteins(ingredient),
                            calculateCarbs(ingredient),
                            calculateFats(ingredient),
                            calculateFibers(ingredient),
                            calculateIron(ingredient),
                            calculateCalcium(ingredient),
                            calculateMagnesium(ingredient),
                            calculateZinc(ingredient),                        calculateThiamin(ingredient),
                            calculateNiacin(ingredient),
                            calculateRiboflavin(ingredient),
                            calculateTotalFolates(ingredient),
                            calculateVitaminA(ingredient)

                    ));

                    totalIng += ingredient.getIngredientQuantity();
                    System.out.println("Total Ing quantity   "+totalIng);
                    div = dish.getServingSize()/dish.getDishQuantity();

                    totalEnergy += ((ingredient.getIngredientQuantity()/100) * ninData.getEnergy());
                    totalProteins += (ingredient.getIngredientQuantity()/100) * ninData.getProtein();
                    totalCarbs += (ingredient.getIngredientQuantity()/100) * ninData.getCarbohydrate();
                    totalFats += (ingredient.getIngredientQuantity()/100) * ninData.getTotal_Fat();
                    totalFibers += (ingredient.getIngredientQuantity()/100) * ninData.getTotal_Dietary_Fibre();
                    totalMagnesium += (ingredient.getIngredientQuantity()/100) * ninData.getMagnesium();
                    totalZinc += (ingredient.getIngredientQuantity()/100) * ninData.getZinc();
                    totalIron += (ingredient.getIngredientQuantity()/100) * ninData.getIron();
                    totalCalcium += (ingredient.getIngredientQuantity()/100) * ninData.getCalcium();
                    totalFolates_B9 += (ingredient.getIngredientQuantity()/100) * ninData.getTotalFolates_B9();
                    totalNiacin_B3 += (ingredient.getIngredientQuantity()/100) * ninData.getNiacin_B3();
                    totalThiamine_B1 += (ingredient.getIngredientQuantity()/100) * ninData.getThiamine_B1();
                    totalRetinolVit_A += (ingredient.getIngredientQuantity()/100) * ninData.getRetinolVit_A();
                    totalRiboflavin_B2 += (ingredient.getIngredientQuantity()/100) * ninData.getRiboflavin_B2();

                }

                energy = energy + totalEnergy;
                proteins = proteins + totalProteins;
                fats = fats + totalFats;
                carbs = carbs + totalCarbs;
                fibers = fibers + totalFibers;
                magnesium = magnesium + totalMagnesium;
                zinc = zinc + totalZinc;
                iron = iron + totalIron;
                calcium = calcium + totalCalcium;
                thiamine_B1 = thiamine_B1 + totalThiamine_B1;
                retinolVit_A = retinolVit_A + totalRetinolVit_A;
                riboflavin_B2 = riboflavin_B2 + totalRiboflavin_B2;
                niacin_B3 = niacin_B3 + totalNiacin_B3;
                folates_B9 = folates_B9 + totalFolates_B9;



                // Fetch recipe details based on dish's recipe ID
                Recipe recipe = dish.getRecipe();
                if (recipe != null) {
                    // Your existing calculation logic
                    Double recipeEnergy = recipe.getEnergy_joules();
                    Double recipeProteins = recipe.getProtein();
                    Double recipeCarbs = recipe.getCarbohydrate();
                    Double recipeFats = recipe.getTotal_fat();
                    Double recipeFibers = recipe.getTotal_dietary_fibre();

                    energy += recipeEnergy;
                    proteins += recipeProteins;
                    fats += recipeFats;
                    carbs += recipeCarbs;
                    fibers += recipeFibers;

                    // Update the response list with recipe details
//                    responseList.add(new DishWithIngredientsResponse(
//                            dish.getDishId(),
//                            dish.getDishName(),
//                            dish.isFavourite(),
//                            ingredientsList,
////                            totalEnergy + recipeEnergy,
////                            totalProteins + recipeProteins,
////                            totalCarbs + recipeCarbs,
////                            totalFats + recipeFats,
////                            totalFibers + recipeFibers
//                            onegrmEng + recipeEnergy,
//                            onegrmPro + recipeProteins,
//                            onegrmCarb + recipeCarbs,
//                            onegrmFat + recipeFats,
//                            onegrmFib + recipeFibers
//
//                    ));
                    responseList.add(new DishWithIngredientsResponse(
                            dish.getDishId(),
                            dish.getDishName(),
                            dish.getMealName(),
                            dish.isFavourite(),
                            ingredientsList,
                            totalEnergy,  // provide the original total energy here
                            totalProteins,
                            totalCarbs,
                            totalFats,
                            totalFibers,
                            totalMagnesium,
                            totalZinc,
                            totalIron,
                            totalCalcium,
                            totalThiamine_B1,
                            totalRiboflavin_B2,
                            totalNiacin_B3,
                            totalFolates_B9,
                            totalRetinolVit_A,

                            dish.getDishQuantity(),  // provide dish quantity here
                            dish.getServingSize()  // provide serving size here
                    ));

                } else {
                    // Update the response list without recipe details
//                    responseList.add(new DishWithIngredientsResponse(
//                            dish.getDishId(),
//                            dish.getDishName(),
//                            dish.isFavourite(),
//                            ingredientsList,
////                            totalEnergy,
//                             onegrmEng,
//                            totalProteins,
//                            totalCarbs,
//                            totalFats,
//                            totalFibers
//
////                            onegrmPro,
////                            onegrmCarb,
////                            onegrmFat,
////                            onegrmFib
//                    ));
                    responseList.add(new DishWithIngredientsResponse(
                            dish.getDishId(),
                            dish.getDishName(),
                            dish.getMealName(),
                            dish.isFavourite(),
                            ingredientsList,
                            totalEnergy,  // provide the original total energy here
                            totalProteins,
                            totalCarbs,
                            totalFats,
                            totalFibers,
                            totalMagnesium,
                            totalZinc,
                            totalIron,
                            totalCalcium,
                            totalThiamine_B1,
                            totalRiboflavin_B2,
                            totalNiacin_B3,
                            totalFolates_B9,
                            totalRetinolVit_A,

                            dish.getDishQuantity(),  // provide dish quantity here
                            dish.getServingSize()  // provide serving size here
                    ));

                }
            }
            onegrmEng = (energy/dish.getDishQuantity())*dish.getServingSize();
            onegrmPro = (proteins/dish.getDishQuantity())*dish.getServingSize();
            onegrmCarb = (carbs/dish.getDishQuantity())*dish.getServingSize();
            onegrmFat = (fats/dish.getDishQuantity())*dish.getServingSize();
            onegrmFib = (fibers/dish.getDishQuantity())*dish.getServingSize();
            onegrmMagnesium = (magnesium/dish.getDishQuantity())*dish.getServingSize();
            onegrmZinc = (zinc/dish.getDishQuantity())*dish.getServingSize();
            onegrmIron = (iron/dish.getDishQuantity())*dish.getServingSize();
            onegrmCalcium = (calcium/dish.getDishQuantity())*dish.getServingSize();

            onegramThiamine_B1 = (thiamine_B1/dish.getDishQuantity())*dish.getServingSize();
            onegrmRetinolVit_A = (retinolVit_A/dish.getDishQuantity())*dish.getServingSize();
            onegrmRiboflavin_B2 = (riboflavin_B2/dish.getDishQuantity())*dish.getServingSize();
            onegrmNiacin_B3 = (niacin_B3/dish.getDishQuantity())*dish.getServingSize();
            onegrmFolates_B9 = (folates_B9/dish.getDishQuantity())*dish.getServingSize();



//            System.out.println("serving Energy  "+onegrmEng);
//            System.out.println("serving protein  "+onegrmPro);
//            System.out.println("serving carbs  "+onegrmCarb);
//            System.out.println("serving Fats  "+onegrmFat);
//            System.out.println("serving Fibers  "+onegrmFib);
        }

        Map<String, String> nutrientsNameWithSIUnit = new HashMap<>();

        List<UnitsDatabase> unitsDatabaseList = unitsDatabaseRepository.findAll();
        for (UnitsDatabase unitsDatabase : unitsDatabaseList) {
            String nutrientName = unitsDatabase.getFood_unit();
            String siUnit = unitsDatabase.getSI_Unit();
            nutrientsNameWithSIUnit.put(nutrientName, siUnit);
        }

        finalResponseList.add(new mealResponse(nutrientsNameWithSIUnit, responseList, onegrmEng, onegrmPro, onegrmCarb, onegrmFat, onegrmFib, onegrmMagnesium, onegrmZinc, onegrmIron, onegrmCalcium, onegramThiamine_B1, onegrmRetinolVit_A, onegrmRiboflavin_B2, onegrmNiacin_B3, onegrmFolates_B9));
        return finalResponseList;
    }



//    public Map<String, Double> getEnergyByDate(User user, LocalDate date) {
//        List<Dishes> dishesList = dishesRepository.findDishesByUserUserIdAndDate(user.getUserId(), date);
//
//        Map<String, Double> energyByMealType = new HashMap<>();
//
//        for (Dishes dish : dishesList) {
//            String mealType = dish.getMealName();
//            if (!energyByMealType.containsKey(mealType)) {
//                energyByMealType.put(mealType, 0.0);
//            }
//
//            if (dish.getMealName().equalsIgnoreCase(mealType)) {
//                Double totalEnergy = calculateTotalEnergyForDish(dish);
//                energyByMealType.put(mealType, energyByMealType.get(mealType) + totalEnergy);
//            }
//        }
//
//        return energyByMealType;
//    }
//
//    private Double calculateTotalEnergyForDish(Dishes dish) {
//        List<Ingredients> ingredients = dish.getIngredientList();
//        Double totalEnergy = 0.0;
//
//        for (Ingredients ingredient : ingredients) {
//            NinData ninData = ninDataRepository.findByFood(ingredient.getIngredientName());
//            Double energy = (ingredient.getIngredientQuantity()/100) * ninData.getEnergy();
//            totalEnergy += energy;
//        }
//
//        return totalEnergy;
//    }
//



    public Map<String, Double> getEnergyByDate(User user, LocalDate date) {
        List<Dishes> dishesList = dishesRepository.findDishesByUserUserIdAndDate(user.getUserId(), date);

        Map<String, Double> energyByMealType = new HashMap<>();

        for (Dishes dish : dishesList) {
            String mealType = dish.getMealName();
            if (!energyByMealType.containsKey(mealType)) {
                energyByMealType.put(mealType, 0.0);
            }

            if (dish.getMealName().equalsIgnoreCase(mealType)) {
                Double totalEnergy = calculateTotalEnergyForDish(dish);
                energyByMealType.put(mealType, energyByMealType.get(mealType) + totalEnergy);
            }
        }

        return energyByMealType;
    }

    private Double calculateTotalEnergyForDish(Dishes dish) {
        Double totalEnergy = 0.0;

        if (dish.getRecipe() != null) {
            // If the dish has a recipe, get energy directly from the recipe table
            Recipe recipe = dish.getRecipe();
            totalEnergy += (recipe.getEnergy_joules()/100)*dish.getDishQuantity();
        }

        // Calculate energy from ingredients
        List<Ingredients> ingredients = dish.getIngredientList();
        for (Ingredients ingredient : ingredients) {
            NinData ninData = ninDataRepository.findByFood(ingredient.getIngredientName());
            Double energy = (ingredient.getIngredientQuantity() / 100) * ninData.getEnergy();
            totalEnergy += energy;
        }

        return totalEnergy;
    }



//public Map<String, Double> getEnergyByDate(User user, LocalDate date) {
//    List<Dishes> dishesList = dishesRepository.findDishesByUserUserIdAndDate(user.getUserId(), date);
//
//    Map<String, Double> energyByMealType = new HashMap<>();
//
//    for (Dishes dish : dishesList) {
//        String mealType = dish.getMealName();
//        if (!energyByMealType.containsKey(mealType)) {
//            energyByMealType.put(mealType, 0.0);
//        }
//
//        Double totalEnergy = calculateTotalEnergyForDish(dish);
//        energyByMealType.put(mealType, energyByMealType.get(mealType) + totalEnergy);
//    }
//
//    return energyByMealType;
//}
//
//    private Double calculateTotalEnergyForDish(Dishes dish) {
//        // Check if the dish has a recipe
//        if (dish.getRecipe() != null) {
//            // If a recipe exists, use its energy directly
//            return dish.getRecipe().getEnergy_joules();
//        } else {
//            // If no recipe, calculate energy from ingredients
//            List<Ingredients> ingredients = dish.getIngredientList();
//            Double totalEnergy = 0.0;
//
//            for (Ingredients ingredient : ingredients) {
//                NinData ninData = ninDataRepository.findByFood(ingredient.getIngredientName());
//                Double energy = (ingredient.getIngredientQuantity() / 100) * ninData.getEnergy();
//                totalEnergy += energy;
//            }
//
//            return totalEnergy;
//        }
//    }
//








    public List<Map<String, Object>> calculateNutritionSummary(User user, LocalDate startDate, LocalDate endDate) {
        List<Dishes> dishesList = dishesRepository.findByUserUserIdAndDateBetween(user.getUserId(), startDate, endDate);

        List<Map<String, Object>> nutritionList = new ArrayList<>();

        for (Dishes dish : dishesList) {
            Map<String, Object> nutritionObject = new HashMap<>();
            LocalDate dishDate = dish.getDate();

            // Calculate total calories for each dish
            Double totalEnergy = 0.0;

            for (Ingredients ingredient : dish.getIngredientList()) {
                NinData ninData = ninDataRepository.findByFood(ingredient.getIngredientName());
                totalEnergy += (ingredient.getIngredientQuantity()/100) * ninData.getEnergy();
            }

            nutritionObject.put("activityDate", dishDate.toString());
            nutritionObject.put("energy", totalEnergy.intValue()); // Convert to int if needed

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
                NinData ninData = ninDataRepository.findByFood(ingredient.getIngredientName());

                nutritionForDish.compute("Energy", (key, value) -> value != null ?
                        value + (ingredient.getIngredientQuantity()/100) * ninData.getEnergy() :
                        (ingredient.getIngredientQuantity()/100) * ninData.getEnergy());

                nutritionForDish.compute("Proteins", (key, value) -> value != null ?
                        value + (ingredient.getIngredientQuantity()/100) * ninData.getProtein() :
                        (ingredient.getIngredientQuantity()/100) * ninData.getProtein());

                nutritionForDish.compute("Carbs", (key, value) -> value != null ?
                        value + (ingredient.getIngredientQuantity()/100) * ninData.getCarbohydrate() :
                        (ingredient.getIngredientQuantity()/100) * ninData.getCarbohydrate());

                nutritionForDish.compute("Fats", (key, value) -> value != null ?
                        value + (ingredient.getIngredientQuantity()/100) * ninData.getTotal_Fat() :
                        (ingredient.getIngredientQuantity()/100) * ninData.getTotal_Fat());

                nutritionForDish.compute("Fibers", (key, value) -> value != null ?
                        value + (ingredient.getIngredientQuantity()/100) * ninData.getTotal_Dietary_Fibre() :
                        (ingredient.getIngredientQuantity()/100) * ninData.getTotal_Dietary_Fibre());
            }
        }
        return nutritionByMealAndDate;
    }


    public NutritionalInfoResponse getNutritionalInfo(String ingredientName, Double ingredientQuantity) {
        NinData ninData = ninDataRepository.findByFood(ingredientName);

        if (ninData != null) {
            Double energy = (ingredientQuantity / 100) * ninData.getEnergy();
            Double protein = (ingredientQuantity / 100) * ninData.getProtein();
            Double fat = (ingredientQuantity / 100) * ninData.getTotal_Fat();
            Double carbohydrates = (ingredientQuantity / 100) * ninData.getCarbohydrate();
            Double fiber = (ingredientQuantity / 100) * ninData.getTotal_Dietary_Fibre();

            return new NutritionalInfoResponse(energy, protein, fat, carbohydrates, fiber);
        } else {
            return new NutritionalInfoResponse(0.0, 0.0, 0.0, 0.0, 0.0);
        }
    }
}