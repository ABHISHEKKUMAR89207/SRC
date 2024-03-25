package com.example.jwt.repository.FoodTodayRepository;



import com.example.jwt.entities.FoodToday.MissingRowFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissingRowFoodRepository extends JpaRepository<MissingRowFood, Long> {
}
