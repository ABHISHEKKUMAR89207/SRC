package com.example.jwt.repository.FoodTodayRepository;

import com.example.jwt.entities.FoodToday.Ingredients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientsRepository extends JpaRepository<Ingredients,Long> {

    Ingredients findByIngredientName(String ingredientName);
    List<Ingredients> findByIngredientNameAndIngredientQuantity(String ingredientName, Double ingredientQuantity);

}
