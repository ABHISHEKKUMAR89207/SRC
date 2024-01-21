package com.example.jwt.entities.FoodToday.Recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }


    public List<Recipe> getRecipeByName(String recipeName) {
        return recipeRepository.findByRecipeName(recipeName);
    }



    public List<String> getAllRecipeNames() {
        return recipeRepository.findAllRecipeNames();
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }



}
