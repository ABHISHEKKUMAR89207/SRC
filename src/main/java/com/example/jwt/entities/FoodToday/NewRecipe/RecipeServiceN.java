package com.example.jwt.entities.FoodToday.NewRecipe;

import com.example.jwt.entities.FoodToday.Recipe.Recipe;
import com.example.jwt.entities.FoodToday.Recipe.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeServiceN {
    private final RecipeRepositoryN recipeRepository;

    @Autowired
    public RecipeServiceN(RecipeRepositoryN recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<RecipeN> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Optional<RecipeN> getRecipeById(Long uid) {
        return recipeRepository.findById(uid);
    }

    public RecipeN saveRecipe(RecipeN recipe) {
        return recipeRepository.save(recipe);
    }

    public void deleteRecipe(Long uid) {
        recipeRepository.deleteById(uid);
    }

    // You can add more business logic methods here if needed
}
