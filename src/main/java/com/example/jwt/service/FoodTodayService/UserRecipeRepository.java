package com.example.jwt.service.FoodTodayService;

import com.example.jwt.entities.FoodToday.UserRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRecipeRepository extends JpaRepository<UserRecipe, Long> {
    // Define any custom query methods if needed
    List<UserRecipe> findByUserEmailAndLocalDate(String username, LocalDate date);

}
