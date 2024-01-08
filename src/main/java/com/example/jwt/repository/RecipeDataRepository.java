package com.example.jwt.repository;

import com.example.jwt.entities.FoodToday.RecipeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeDataRepository extends JpaRepository<RecipeData, Long> {
//    List<RecipeData> findByRecipeNameIgnoreCase(String recipeName);
@Query("SELECT r FROM RecipeData r WHERE r.recipeId = :recipeId")
Optional<RecipeData> findByRecipeId(Long recipeId);

    @Query("SELECT r FROM RecipeData r WHERE lower(r.recipe_name) = lower(:recipeName)")
    List<RecipeData> findByRecipeNameIgnoreCase(String recipeName);
    @Query("SELECT r FROM RecipeData r WHERE lower(r.recipe_name) = lower(:recipeName)")
    List<RecipeData> findByRecipeName(String recipeName);


    @Query("SELECT DISTINCT r.recipe_name FROM RecipeData r")
    List<String> findDistinctRecipeNames();


    @Query("SELECT r FROM RecipeData r JOIN r.user u WHERE u.email = :username AND r.recipe_name = :recipeName")
    List<RecipeData> findByUserEmailAndRecipeName(@Param("username") String username, @Param("recipeName") String recipeName);
}
