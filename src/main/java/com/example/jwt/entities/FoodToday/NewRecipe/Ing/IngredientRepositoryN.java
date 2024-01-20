package com.example.jwt.entities.FoodToday.NewRecipe.Ing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepositoryN extends JpaRepository<IngredientN, Long> {
    // You can add custom query methods here if needed
}
