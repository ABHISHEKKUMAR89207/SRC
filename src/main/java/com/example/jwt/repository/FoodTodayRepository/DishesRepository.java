package com.example.jwt.repository.FoodTodayRepository;


import com.example.jwt.entities.FoodToday.Dishes;
import com.example.jwt.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DishesRepository extends JpaRepository<Dishes, Long> {

    List<Dishes> findByUserUserIdAndDateAndMealName(Long userId, LocalDate date, String mealName);

    List<Dishes> findByUserUserIdAndDate(Long userId, LocalDate date);

    List<Dishes> findByUserUserId(Long userId);

    List<Dishes> findDishNameByUserUserIdAndMealName(Long userId, String mealName);

    List<Dishes> findDishIdByUserUserIdAndMealNameAndDishName(Long userId, String mealName, String dishName);

    List<Dishes> findDishesByUser_UserIdAndMealNameAndDishNameAndDate(Long userId, String mealName, String dishName, LocalDate date);

    public List<Dishes> findDishesByUserUserIdAndDate(Long userId, LocalDate date);

    List<Dishes> findByUserAndFavourite(User user, boolean favourite);

    Dishes findByDishName(String dishName);

}
