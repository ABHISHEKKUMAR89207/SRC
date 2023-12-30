package com.example.jwt.repository;

import com.example.jwt.entities.FoodToday.UnitsDatabase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitsDatabaseRepository extends JpaRepository<UnitsDatabase, Integer> {
//    UnitsDatabase findByFood_unit(String nutrientName);

    @Query("SELECT u FROM UnitsDatabase u WHERE u.food_unit = ?1")
    UnitsDatabase findByFoodUnit(String foodUnit);
}
