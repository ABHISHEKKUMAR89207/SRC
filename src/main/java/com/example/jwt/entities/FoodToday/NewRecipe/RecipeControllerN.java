//package com.example.jwt.entities.FoodToday.NewRecipe;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/recipes")
//public class RecipeControllerN {
//    private final RecipeServiceN recipeService;
//
//    @Autowired
//    public RecipeControllerN(RecipeServiceN recipeService) {
//        this.recipeService = recipeService;
//    }
//
//    @GetMapping
//    public List<RecipeN> getAllRecipes() {
//        return recipeService.getAllRecipes();
//    }
//
//    @GetMapping("/{uid}")
//    public RecipeN getRecipeById(@PathVariable Long uid) {
//        return recipeService.getRecipeById(uid).orElse(null);
//    }
//
//    @PostMapping
//    public RecipeN saveRecipe(@RequestBody RecipeN recipe) {
//        return recipeService.saveRecipe(recipe);
//    }
//
//    @DeleteMapping("/{uid}")
//    public void deleteRecipe(@PathVariable Long uid) {
//        recipeService.deleteRecipe(uid);
//    }
//
//    // You can add more endpoints and methods as needed
//}
