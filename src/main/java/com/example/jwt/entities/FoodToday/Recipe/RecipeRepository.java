package com.example.jwt.entities.FoodToday.Recipe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    // You can add custom query methods here if needed
    @Query("SELECT r FROM Recipe r WHERE lower(r.recipe_name) = lower(:recipeName)")
    List<Recipe> findByRecipeName(String recipeName);

    @Query("SELECT r.recipe_name FROM Recipe r")
    List<String> findAllRecipeNames();
}
