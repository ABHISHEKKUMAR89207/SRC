package com.example.jwt.controler.FoodTodayController;


import com.example.jwt.dtos.FoodTodayDtos.DishDTO;
import com.example.jwt.entities.FoodToday.Dishes;
import com.example.jwt.entities.FoodToday.Ingredients;
import com.example.jwt.entities.FoodToday.NinData;
import com.example.jwt.entities.FoodToday.Recipe.Recipe;
import com.example.jwt.entities.FoodToday.Recipe.RecipeDTO;
//import com.example.jwt.entities.FoodToday.Recipe.RecipeService;
import com.example.jwt.entities.FoodToday.Recipe.RecipeRepository;
import com.example.jwt.entities.FoodToday.Recipe.RecipeService;
import com.example.jwt.entities.User;
import com.example.jwt.repository.FoodTodayRepository.DishesRepository;
import com.example.jwt.repository.FoodTodayRepository.NinDataRepository;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.FoodTodayService.DishesService;
import com.example.jwt.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dishes")
@Tag(name = "Dishes Controller", description = "This is FoodTodayDishes Controller")
public class DishesController {
    @Autowired
    private DishesService dishesService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private DishesRepository dishesRepository;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private NinDataRepository ninDataRepository;


//    @GetMapping("/calculateDDScore")
//    public Map<String, Integer> calculateDDScore(@RequestHeader("Auth") String tokenHeader) {
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        // Use the username to fetch the userId from your user service
//        User user = userService.findByUsername(username);
//
//        Map<String, Integer> dDScoreMap = new HashMap<>();
//
//        // Get the current date
//        LocalDate currentDate = LocalDate.now();
//
//        // Retrieve the list of dishes chosen by the user for the current date
//        List<Dishes> dishesList = user.getDishesList().stream()
//                .filter(dish -> dish.getDate().isEqual(currentDate))
//                .collect(Collectors.toList());
//
//        // Maintain a set to store unique DDS_Food_Category_Codes
//        Set<String> uniqueDDSCodes = new HashSet<>();
//
//        // Iterate through each dish chosen by the user for the current date
//        for (Dishes dish : dishesList) {
//            List<Ingredients> ingredientsList = dish.getIngredientList();
//
//            for (Ingredients ingredient : ingredientsList) {
//                String foodCode = ingredient.getFoodCode();
//
//                // Retrieve all NinData entries with matching foodCode
//                List<NinData> ninDataList = ninDataRepository.findByFoodCodeCustomQuery(foodCode);
//
//                // Increment DDScore for each unique DDS_Food_Category_Code
//                for (NinData ninData : ninDataList) {
//                    String ddsFoodCategoryCode = ninData.getDDS_Food_Category_Code().toString();
//
//                    // If the DDS_Food_Category_Code is not already counted, increment DDScore
//                    if (!uniqueDDSCodes.contains(ddsFoodCategoryCode)) {
//                        uniqueDDSCodes.add(ddsFoodCategoryCode);
//                    }
//                }
//            }
//        }
//
//        // Set the size of uniqueDDSCodes as the total DDScore
//        int totalDDScore = uniqueDDSCodes.size();
//
//        // Add total DDScore to the map
//        dDScoreMap.put("DDScore", totalDDScore);
//
//        return dDScoreMap;
//    }


//    @GetMapping("/calculateDDScore")
//    public Map<String, Object> calculateDDScore(@RequestHeader("Auth") String tokenHeader) {
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        // Use the username to fetch the userId from your user service
//        User user = userService.findByUsername(username);
//
//        Map<String, Object> response = new HashMap<>();
//
//        // Get the current date
//        LocalDate currentDate = LocalDate.now();
//
//        // Retrieve the list of dishes chosen by the user for the current date
//        List<Dishes> dishesList = user.getDishesList().stream()
//                .filter(dish -> dish.getDate().isEqual(currentDate))
//                .collect(Collectors.toList());
//
//        // Maintain a set to store unique DDS_Food_Category_Codes
//        Set<String> uniqueDDSCodes = new HashSet<>();
//
//        // Maintain a list to store missing ingredients
//        List<String> missingIngredients = new ArrayList<>();
//
//        // Iterate through each dish chosen by the user for the current date
//        for (Dishes dish : dishesList) {
//            List<Ingredients> ingredientsList = dish.getIngredientList();
//
//            for (Ingredients ingredient : ingredientsList) {
//                String foodCode = ingredient.getFoodCode();
//
//                // Retrieve all NinData entries with matching foodCode
//                List<NinData> ninDataList = ninDataRepository.findByFoodCodeCustomQuery(foodCode);
//
//                if (ninDataList.isEmpty()) {
//                    // If ingredient data is not available, add it to the missing ingredients list
//                    missingIngredients.add(ingredient.getIngredientName());
//                } else {
//                    // Increment DDScore for each unique DDS_Food_Category_Code
//                    for (NinData ninData : ninDataList) {
//                        String ddsFoodCategoryCode = ninData.getDDS_Food_Category_Code().toString();
//
//                        // If the DDS_Food_Category_Code is not already counted, increment DDScore
//                        if (!uniqueDDSCodes.contains(ddsFoodCategoryCode)) {
//                            uniqueDDSCodes.add(ddsFoodCategoryCode);
//                        }
//                    }
//                }
//            }
//        }
//
//        // Set the size of uniqueDDSCodes as the total DDScore
//        int totalDDScore = uniqueDDSCodes.size();
//
//        // Add total DDScore to the response
//        response.put("DDScore", totalDDScore);
//
//        // If there are missing ingredients, include them in the response
//        if (!missingIngredients.isEmpty()) {
//            response.put("missingIngredients", missingIngredients);
//        }
//
//        return response;
//    }

//    @GetMapping("/calculateDDScore")
//    public Map<String, Object> calculateDDScore(@RequestHeader("Auth") String tokenHeader) {
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        // Use the username to fetch the userId from your user service
//        User user = userService.findByUsername(username);
//
//        Map<String, Object> response = new HashMap<>();
//
//        // Get the current date
//        LocalDate currentDate = LocalDate.now();
//
//        // Retrieve the list of dishes chosen by the user for the current date
//        List<Dishes> dishesList = user.getDishesList().stream()
//                .filter(dish -> dish.getDate().isEqual(currentDate))
//                .collect(Collectors.toList());
//
//        // Maintain a set to store unique DDS_Food_Category_Codes
//        Set<String> uniqueDDSCodes = new HashSet<>();
//
//        // Maintain a list to store missing ingredients
//        List<String> missingIngredients = new ArrayList<>();
//
//        // Maintain a map to store DDS_Food_Category_Codes and their names
//        Map<String, String> ddsCategoryNames = new HashMap<>();
//
//        // Iterate through each dish chosen by the user for the current date
//        for (Dishes dish : dishesList) {
//            List<Ingredients> ingredientsList = dish.getIngredientList();
//
//            for (Ingredients ingredient : ingredientsList) {
//                String foodCode = ingredient.getFoodCode();
//
//                // Retrieve all NinData entries with matching foodCode
//                List<NinData> ninDataList = ninDataRepository.findByFoodCodeCustomQuery(foodCode);
//
//                if (ninDataList.isEmpty()) {
//                    // If ingredient data is not available, add it to the missing ingredients list
//                    missingIngredients.add(ingredient.getIngredientName());
//                } else {
//                    // Increment DDScore for each unique DDS_Food_Category_Code
//                    for (NinData ninData : ninDataList) {
//                        String ddsFoodCategoryCode = ninData.getDDS_Food_Category_Code().toString();
//                        String ddsCategoryName = ninData.getDDS_Category(); // Assuming this method exists
//
//                        // If the DDS_Food_Category_Code is not already counted, increment DDScore
//                        if (!uniqueDDSCodes.contains(ddsFoodCategoryCode)) {
//                            uniqueDDSCodes.add(ddsFoodCategoryCode);
//                            ddsCategoryNames.put(ddsFoodCategoryCode, ddsCategoryName);
//                        }
//                    }
//                }
//            }
//        }
//
//        // Set the size of uniqueDDSCodes as the total DDScore
//        int totalDDScore = uniqueDDSCodes.size();
//
//        // Add total DDScore to the response
//        response.put("DDScore", totalDDScore);
//
//        // Add category names to the response
//        response.put("intakeCategoryNames", ddsCategoryNames);
//
//        // If there are missing ingredients, include them in the response
//        if (!missingIngredients.isEmpty()) {
//            response.put("missingIngredients", missingIngredients);
//        }
//
//        return response;
//    }

    @GetMapping("/calculateDDScore")
    public Map<String, Object> calculateDDScore(@RequestHeader("Auth") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the username to fetch the userId from your user service
        User user = userService.findByUsername(username);

        Map<String, Object> response = new HashMap<>();

        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Retrieve the list of dishes chosen by the user for the current date
        List<Dishes> dishesList = user.getDishesList().stream()
                .filter(dish -> dish.getDate().isEqual(currentDate))
                .collect(Collectors.toList());

        // Maintain a set to store unique DDS_Food_Category_Codes
        Set<String> uniqueDDSCodes = new HashSet<>();

        // Maintain a list to store missing ingredients
        List<String> missingIngredients = new ArrayList<>();

        // Maintain a map to store DDS_Food_Category_Codes and their names
        Map<String, String> ddsCategoryNames = new HashMap<>();

        // Iterate through each dish chosen by the user for the current date
        for (Dishes dish : dishesList) {
            List<Ingredients> ingredientsList = dish.getIngredientList();

            for (Ingredients ingredient : ingredientsList) {
                String foodCode = ingredient.getFoodCode();

                // Retrieve all NinData entries with matching foodCode
                List<NinData> ninDataList = ninDataRepository.findByFoodCodeCustomQuery(foodCode);

                if (ninDataList.isEmpty()) {
                    // If ingredient data is not available, add it to the missing ingredients list
                    missingIngredients.add(ingredient.getIngredientName());
                } else {
                    // Increment DDScore for each unique DDS_Food_Category_Code
                    for (NinData ninData : ninDataList) {
                        String ddsFoodCategoryCode = ninData.getDDS_Food_Category_Code().toString();
                        String ddsCategoryName = ninData.getDDS_Category(); // Assuming this method exists

                        // If the DDS_Food_Category_Code is not already counted, increment DDScore
                        if (!uniqueDDSCodes.contains(ddsFoodCategoryCode)) {
                            uniqueDDSCodes.add(ddsFoodCategoryCode);
                            ddsCategoryNames.put(ddsFoodCategoryCode, ddsCategoryName);
                        }
                    }
                }
            }
        }

        // Set the size of uniqueDDSCodes as the total DDScore
        int totalDDScore = uniqueDDSCodes.size();

        // Add total DDScore to the response
        response.put("DDScore", totalDDScore);

        // Add category names to the response
        response.put("intakeCategoryNames", ddsCategoryNames);

        // Define all possible categories
        Map<String, String> allCategories = new HashMap<>();
        allCategories.put("1", "Cereals & grains");
        allCategories.put("2", "Pulses & legumes");
        allCategories.put("3", "Green leafy vegetables");
        allCategories.put("4", "Other vegetables");
        allCategories.put("5", "Fruits");
        allCategories.put("6", "Roots & tubers");
        allCategories.put("7", "Nuts and oil seeds");
        allCategories.put("8", "Milk & milk products");
        allCategories.put("9", "Egg");
        allCategories.put("10", "Meat, poultry and fishes");

        // Maintain a map for left categories
        Map<String, String> leftCategoryNames = new HashMap<>(allCategories);

        // Remove the intake categories from left categories
        for (String code : uniqueDDSCodes) {
            leftCategoryNames.remove(code);
        }

        // Add left category names to the response
        response.put("leftCategoryNames", leftCategoryNames);

        // If there are missing ingredients, include them in the response
        if (!missingIngredients.isEmpty()) {
            response.put("missingIngredients", missingIngredients);
        }

        return response;
    }


//    @PostMapping("/add-dish-id")
//    public void saveDishForUser(@RequestHeader("Auth") String tokenHeader, @RequestBody DishDTO dishDTO) {
//
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        // Use the username to fetch the userId from your user service
//        User user = userService.findByUsername(username);
//
//        // Get the current date
//        LocalDate currentDate = LocalDate.now();
//
//        // Retrieve the dish ID based on the dish name and current date
//        Long dishId = dishesService.getDishIdByDishNameAndDate(dishDTO.getDishName(), currentDate);
//
//        // If the dish ID is not found, you can create a new dish
//        if (dishId == null) {
//            Dishes newDish = new Dishes();
//            newDish.setDishName(dishDTO.getDishName());
//            newDish.setDishQuantity(dishDTO.getDishQuantity());
//            newDish.setMealName(dishDTO.getMealName());
//            newDish.setDate(currentDate);
//            newDish.setUser(user);
//            dishesRepository.save(newDish);
//        } else {
//            // If the dish ID is found, update the existing dish
//            Dishes existingDish = dishesRepository.findById(dishId).orElse(null);
//            if (existingDish != null) {
//                existingDish.setDishQuantity(dishDTO.getDishQuantity());
//                existingDish.setMealName(dishDTO.getMealName());
//                dishesRepository.save(existingDish);
//            }
//        }
//    }


    @PostMapping("/add-dish")
    public void saveDishForUserId( @RequestHeader("Auth") String tokenHeader, @RequestBody DishDTO dishDTO) {

        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the username to fetch the userId from your user service
        User user = userService.findByUsername(username);

        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Call the service to save the dish for the user with the current date
        dishesService.saveDishesForUser(dishDTO, user, currentDate);
    }


    // Endpoint to retrieve all dinner dishes for a user on the same date
    @GetMapping("/get-dish")
    public List<DishDTO> getDishesWithMealNameForUser( @RequestHeader("Auth") String tokenHeader, @RequestParam("date") String date, @RequestParam("mealName") String mealName) {
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the username to fetch the userId from your user service
        User user = userService.findByUsername(username);

        LocalDate parsedDate = LocalDate.parse(date);
        return dishesService.getDishesWithMealName(user, parsedDate, mealName);
    }



//get all dish
    @GetMapping("/get-all-dish")
    public List<DishDTO> getAllDishesForUser( @RequestHeader("Auth") String tokenHeader, @RequestParam("date") String date) {
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the username to fetch the userId from your user service
        User user = userService.findByUsername(username);

        LocalDate parsedDate = LocalDate.parse(date);
        return dishesService.getAllDishesForUser(user, parsedDate);
    }




    @PutMapping("/add-favourite")
    public ResponseEntity<String> addDishToFavourites(
            @RequestHeader("Auth") String tokenHeader,
            @RequestParam("dish_id") Long dishId,
            @RequestParam("favourite") boolean favourite
    ) {
        try {
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);

            // Use the username to fetch the userId from your user service
            User user = userService.findByUsername(username);

            // Call the service to update the favourite status for the dish
            dishesService.updateFavouriteStatus(user, dishId, favourite);

            return ResponseEntity.ok("Favourite status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating favourite status: " + e.getMessage());
        }
    }


    @GetMapping("/favourites")
    public ResponseEntity<List<DishDTO>> getAllFavouriteDishes(
            @RequestHeader("Auth") String tokenHeader
    ) {
        try {
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);

            // Use the username to fetch the user from your user service
            User user = userService.findByUsername(username);

            // Call the service to get all favorite dishes for the user
            List<DishDTO> favouriteDishes = dishesService.getAllFavouriteDishes(user);

            return ResponseEntity.ok(favouriteDishes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }


    @PostMapping("/add-recipe")
    public ResponseEntity<RecipeDTO> saveCalculation(
            @RequestHeader("Auth") String tokenHeader,
            @RequestParam("recipeName") String recipeName,
            @RequestParam("recipeType") String recipeType,
            @RequestParam("recipeQuantity") Double recipeQuantity
    ) {
        // Your existing code to extract user information from the token
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(username);

        // Fetch the recipe data based on the provided recipeName
        List<Recipe> recipeDataList = recipeService.getRecipeByName(recipeName);

        if (user != null && !recipeDataList.isEmpty()) {
            Recipe recipeData = recipeDataList.get(0);

            // Your existing calculation logic
            Double totalProteinContent = recipeData.getProtein() / 100 * recipeQuantity;
            Double totalEnergyContent = recipeData.getEnergy_joules() / 100 * recipeQuantity;
            Double totalFatContent = recipeData.getTotal_fat() / 100 * recipeQuantity;
            Double totalCarbsContent = recipeData.getCarbohydrate() / 100 * recipeQuantity;
            Double totalFiberContent = recipeData.getTotal_dietary_fibre() / 100 * recipeQuantity;

            // Construct the CalculatedRecipe object using the modified DTO
            RecipeDTO calculatedValues = new RecipeDTO();
            calculatedValues.setRecipesId(recipeData.getRecipesId());
            calculatedValues.setRecipeName(recipeData.getRecipe_name());
            calculatedValues.setEnergyJoules(totalEnergyContent);
            calculatedValues.setCarbohydrate(totalCarbsContent);
            calculatedValues.setTotalDietaryFibre(totalFiberContent);
            calculatedValues.setTotalFat(totalFatContent);
            calculatedValues.setProtein(totalProteinContent);

            // Save the calculated values to the Dishes table
            Dishes dishes = new Dishes();
            dishes.setDishName(recipeData.getRecipe_name());
            dishes.setDishQuantity(recipeQuantity);
            dishes.setMealName(recipeType);
            dishes.setDate(LocalDate.now());
            dishes.setFavourite(false);
            dishes.setMealName(recipeType);
            dishes.setDishQuantity(recipeQuantity);
            dishes.setUser(user);
            dishes.setRecipe(recipeData); // Associate with Recipe

            dishesService.saveDish(dishes);

            return ResponseEntity.ok(calculatedValues);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/updateQuantity")
    public ResponseEntity<String> updateDishQuantity(
            @RequestParam Long dishId,
            @RequestParam Double newQuantity,
            @RequestHeader("Auth") String tokenHeader

            ) {
        try {
            // Your existing code to extract user information from the token
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            User user = userService.findByUsername(username);

            dishesService.updateDishQuantity(dishId, newQuantity,user);
            return ResponseEntity.ok("Dish quantity updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update dish quantity: " + e.getMessage());
        }
    }
}