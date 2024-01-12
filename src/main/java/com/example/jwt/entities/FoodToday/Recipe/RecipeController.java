//package com.example.jwt.entities.FoodToday.Recipe;
//
//import com.example.jwt.entities.FoodToday.Dishes;
//import com.example.jwt.entities.User;
//import com.example.jwt.security.JwtHelper;
//import com.example.jwt.service.FoodTodayService.DishesService;
//import com.example.jwt.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@RestController
//@RequestMapping("/recipes")
//public class RecipeController {
//    private final RecipeService recipeService;
//
//    @Autowired
//    public RecipeController(RecipeService recipeService) {
//        this.recipeService = recipeService;
//    }
//
//    @GetMapping("/all")
//    public List<Recipe> getAllRecipes() {
//        return recipeService.getAllRecipes();
//    }
//
////    @GetMapping("/{id}")
////    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
////        return recipeService.getRecipeById(id)
////                .map(ResponseEntity::ok)
////                .orElse(ResponseEntity.notFound().build());
////    }
//
//    @PostMapping
//    public Recipe createRecipe(@RequestBody Recipe recipe) {
//        return recipeService.saveRecipe(recipe);
//    }
//
////    @PutMapping("/{id}")
////    public ResponseEntity<Recipe> updateRecipe(@PathVariable Long id, @RequestBody Recipe updatedRecipe) {
////        return recipeService.getRecipeById(id)
////                .map(existingRecipe -> {
////                    existingRecipe.setRecipe_name(updatedRecipe.getRecipe_name());
////                    // Set other fields accordingly
////
////                    return ResponseEntity.ok(recipeService.saveRecipe(existingRecipe));
////                })
////                .orElse(ResponseEntity.notFound().build());
////    }
////
////    @DeleteMapping("/{id}")
////    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
////        recipeService.deleteRecipe(id);
////        return ResponseEntity.noContent().build();
////    }
//@Autowired
//private JwtHelper jwtHelper;
//@Autowired
//private UserService userService;
//
//
////    @PostMapping("/save-calculationn")
////    public ResponseEntity<RecipeDTO> saveCalculation(
////            @RequestHeader("Auth") String tokenHeader,
////            @RequestParam("recipeName") String recipeName,
////            @RequestParam("recipeQuantity") Double recipeQuantity
////    ) {
////        // Your existing code to extract user information from the token
////        String token = tokenHeader.replace("Bearer ", "");
////        String username = jwtHelper.getUsernameFromToken(token);
////        User user = userService.findByUsername(username);
////
////        // Fetch the recipe data based on the provided recipeName
////        List<Recipe> recipeDataList = recipeService.getRecipeByName(recipeName);
////
////        if (user != null && !recipeDataList.isEmpty()) {
////            Recipe recipeData = recipeDataList.get(0);
////
////            // Your existing calculation logic
////            Double totalProteinContent = recipeData.getProtein() / 100 * recipeQuantity;
////            Double totalEnergyContent = recipeData.getEnergyJoules() / 100 * recipeQuantity;
////            Double totalFatContent = recipeData.getTotalFat() / 100 * recipeQuantity;
////            Double totalCarbsContent = recipeData.getCarbohydrate() / 100 * recipeQuantity;
////            Double totalFiberContent = recipeData.getTotalDietaryFibre() / 100 * recipeQuantity;
////
////            // Construct the CalculatedRecipe object using the modified DTO
////            RecipeDTO calculatedValues = new RecipeDTO();
////            calculatedValues.setRecipesId(recipeData.getRecipes_id());
////            calculatedValues.setRecipeName(recipeData.getRecipe_name());
////            calculatedValues.setEnergyJoules(totalEnergyContent);
////            calculatedValues.setCarbohydrate(totalCarbsContent);
////            calculatedValues.setTotalDietaryFibre(totalFiberContent);
////            calculatedValues.setTotalFat(totalFatContent);
////            calculatedValues.setProtein(totalProteinContent);
////
////            return ResponseEntity.ok(calculatedValues);
////        } else {
////            return ResponseEntity.notFound().build();
////        }
////    }
//
//
//
////    @PostMapping("/save-calculationn")
////    public ResponseEntity<RecipeDTO> saveCalculation(
////            @RequestHeader("Auth") String tokenHeader,
////            @RequestParam("recipeName") String recipeName,
////            @RequestParam("recipeQuantity") Double recipeQuantity,
////            @RequestParam("recipeType") String recipeType // Add this parameter
////    ) {
////        // Your existing code to extract user information from the token
////        String token = tokenHeader.replace("Bearer ", "");
////        String username = jwtHelper.getUsernameFromToken(token);
////        User user = userService.findByUsername(username);
////
////        // Fetch the recipe data based on the provided recipeName
////        List<Recipe> recipeDataList = recipeService.getRecipeByName(recipeName);
////
////        if (user != null && !recipeDataList.isEmpty()) {
////            Recipe recipeData = recipeDataList.get(0);
////
////            // Your existing calculation logic
////            Double totalProteinContent = recipeData.getProtein() / 100 * recipeQuantity;
////            Double totalEnergyContent = recipeData.getEnergyJoules() / 100 * recipeQuantity;
////            Double totalFatContent = recipeData.getTotalFat() / 100 * recipeQuantity;
////            Double totalCarbsContent = recipeData.getCarbohydrate() / 100 * recipeQuantity;
////            Double totalFiberContent = recipeData.getTotalDietaryFibre() / 100 * recipeQuantity;
////
////            // Construct the CalculatedRecipe object using the modified DTO
////            RecipeDTO calculatedValues = new RecipeDTO();
////            calculatedValues.setRecipesId(recipeData.getRecipes_id());
////            calculatedValues.setRecipeName(recipeData.getRecipe_name());
////            calculatedValues.setEnergyJoules(totalEnergyContent);
////            calculatedValues.setCarbohydrate(totalCarbsContent);
////            calculatedValues.setTotalDietaryFibre(totalFiberContent);
////            calculatedValues.setTotalFat(totalFatContent);
////            calculatedValues.setProtein(totalProteinContent);
////
////            // Save to Dishes table
////            Dishes dish = new Dishes();
////            dish.setDishName(recipeData.getRecipe_name());
////            dish.setDishQuantity(recipeQuantity);
////            dish.setMealName(recipeType); // Assuming recipeType corresponds to mealName
////            dish.setDate(LocalDate.now()); // Assuming you want to store the current date
////            dish.setFavourite(false);
////            dish.setUser(user);
////
////            dishesService.saveDish(dish);
////
////            return ResponseEntity.ok(calculatedValues);
////        } else {
////            return ResponseEntity.notFound().build();
////        }
////    }
//
//
//}
//
