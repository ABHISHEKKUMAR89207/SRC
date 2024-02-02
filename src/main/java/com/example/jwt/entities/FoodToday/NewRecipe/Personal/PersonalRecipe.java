package com.example.jwt.entities.FoodToday.NewRecipe.Personal;

import com.example.jwt.entities.FoodToday.Dishes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonalRecipe extends JpaRepository<Personal, Long> {

//    List<Dishes> findByUserUserIdAndPersonalDish(Long userId, String date);
List<Personal> findAllByUserUserId(Long userId);

    Optional<Personal> findByPerRecIdAndUserUserId(Long perRecId, Long userId);

}
