package com.example.jwt.repository.FoodTodayRepository;


import com.example.jwt.entities.FoodToday.Ingredients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientsRepository extends JpaRepository<Ingredients,Long> {
}
