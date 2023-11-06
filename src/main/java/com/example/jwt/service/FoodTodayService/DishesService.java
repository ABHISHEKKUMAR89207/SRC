package com.example.jwt.service.FoodTodayService;


import com.example.jwt.dtos.FoodTodayDtos.DishDTO;
import com.example.jwt.entities.FoodToday.Dishes;

import com.example.jwt.entities.User;
import com.example.jwt.repository.FoodTodayRepository.DishesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DishesService {
    @Autowired
    private DishesRepository dishesRepository;

    public void saveDishesForUser(DishDTO dishDTO, User user, LocalDate date) {
        Dishes dish = new Dishes();
        dish.setDishName(dishDTO.getDishName());
        dish.setDishQuantity(dishDTO.getDishQuantity());
        dish.setMealName(dishDTO.getMealName());
        dish.setDate(date);

//        User user = new User();
        user.setUserId(user.getUserId());
        dish.setUser(user);

        dishesRepository.save(dish);
    }







    // Retrieve all dishes for a user with a specific meal name on the same date
    public List<DishDTO> getDishesForUser(User user, LocalDate date, String mealName) {
        List<Dishes> matchingDishes = dishesRepository.findByUserUserIdAndDateAndMealName(user.getUserId(), date, mealName);

        List<DishDTO> result = new ArrayList<>();
        for (Dishes dish : matchingDishes) {
            DishDTO dishDTO = new DishDTO();
            dishDTO.setDishName(dish.getDishName());
            dishDTO.setDishQuantity(dish.getDishQuantity());
            dishDTO.setMealName(dish.getMealName());
            result.add(dishDTO);
        }

        return result;
    }


}
