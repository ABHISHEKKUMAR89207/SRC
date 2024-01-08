package com.example.jwt.service.FoodTodayService;

import com.example.jwt.entities.FoodToday.RecipeData;
import com.example.jwt.repository.RecipeDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeDataRepository recipeDataRepository;

    @Autowired
    public RecipeService(RecipeDataRepository recipeDataRepository) {
        this.recipeDataRepository = recipeDataRepository;
    }

    public Optional<RecipeData> getRecipeDataByRecipeId(Long recipeId) {
        return recipeDataRepository.findByRecipeId(recipeId);
    }


    public List<RecipeData> getRecipeByName(String recipeName) {
        return recipeDataRepository.findByRecipeName(recipeName);
    }


    public List<RecipeData> getRecipesByUserAndName(String username, String recipeName) {
        return recipeDataRepository.findByUserEmailAndRecipeName(username, recipeName);
    }
}

