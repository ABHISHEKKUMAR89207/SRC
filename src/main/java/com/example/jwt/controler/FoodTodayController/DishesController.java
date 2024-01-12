package com.example.jwt.controler.FoodTodayController;


import com.example.jwt.dtos.FoodTodayDtos.DishDTO;
import com.example.jwt.entities.FoodToday.Dishes;
import com.example.jwt.entities.FoodToday.Recipe.Recipe;
import com.example.jwt.entities.FoodToday.Recipe.RecipeDTO;
//import com.example.jwt.entities.FoodToday.Recipe.RecipeService;
import com.example.jwt.entities.FoodToday.Recipe.RecipeRepository;
import com.example.jwt.entities.FoodToday.Recipe.RecipeService;
import com.example.jwt.entities.User;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.FoodTodayService.DishesService;
import com.example.jwt.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

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
    private RecipeService recipeService;

    @PostMapping("/add-dish")
    public void saveDishForUser( @RequestHeader("Auth") String tokenHeader, @RequestBody DishDTO dishDTO) {

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


}
