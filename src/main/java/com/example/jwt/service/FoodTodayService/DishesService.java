package com.example.jwt.service.FoodTodayService;

import com.example.jwt.dtos.FoodTodayDtos.DishDTO;
import com.example.jwt.entities.FoodToday.Dishes;
import com.example.jwt.entities.User;
import com.example.jwt.repository.FoodTodayRepository.DishesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DishesService {
    @Autowired
    private DishesRepository dishesRepository;

    public Long getDishIdByDishNameAndDate(String dishName, LocalDate date) {
        return dishesRepository.findDishIdByDishNameAndDate(dishName, date);
    }

    // Retrieve all dishes for a user with a specific meal name on the same date
    public void saveDishesForUser(DishDTO dishDTO, User user, LocalDate date) {
        Dishes dish = new Dishes();
        dish.setDishName(dishDTO.getDishName());
        dish.setDishQuantity(dishDTO.getDishQuantity());
        dish.setMealName(dishDTO.getMealName());
        dish.setDate(date);
        user.setUserId(user.getUserId());
        dish.setUser(user);
        dishesRepository.save(dish);
    }

    // Retrieve all dishes for a user with a specific meal name on the same date
    public List<DishDTO> getDishesWithMealName(User user, LocalDate date, String mealName) {
        List<Dishes> matchingDishes = dishesRepository.findByUserUserIdAndDateAndMealName(user.getUserId(), date, mealName);
        List<DishDTO> result = new ArrayList<>();
        for (Dishes dish : matchingDishes) {
            DishDTO dishDTO = new DishDTO();
            dishDTO.setDishId(dish.getDishId());
            dishDTO.setDishName(dish.getDishName());
            dishDTO.setDishQuantity(dish.getDishQuantity());
            dishDTO.setMealName(dish.getMealName());
            result.add(dishDTO);
        }
        return result;
    }

    // Get all the dishes without meal name on a specific date
    public List<DishDTO> getAllDishesForUser(User user, LocalDate date) {
        List<Dishes> matchingDishes = dishesRepository.findByUserUserIdAndDate(user.getUserId(), date);
        List<DishDTO> result = new ArrayList<>();
        for (Dishes dish : matchingDishes) {
            DishDTO dishDTO = new DishDTO();
            dishDTO.setDishId(dish.getDishId());
            dishDTO.setDishName(dish.getDishName());
            dishDTO.setDishQuantity(dish.getDishQuantity());
            dishDTO.setMealName(dish.getMealName());
            result.add(dishDTO);
        }
        return result;
    }

    // to add favourite dish name
    @Transactional
    public void updateFavouriteStatus(User user, Long dishId, boolean favourite) {
        Optional<Dishes> optionalDish = dishesRepository.findById(dishId);
        if (optionalDish.isPresent() && optionalDish.get().getUser().equals(user)) {
            Dishes dish = optionalDish.get();
            dish.setFavourite(favourite);
            dishesRepository.save(dish);
        } else {
            throw new RuntimeException("Dish not found or does not belong to the user");
        }
    }

    // to retrive all the favourite food item
    public List<DishDTO> getAllFavouriteDishes(User user) {
        List<Dishes> favouriteDishes = dishesRepository.findByUserAndFavourite(user, true);
        List<DishDTO> result = new ArrayList<>();
        for (Dishes dish : favouriteDishes) {
            DishDTO dishDTO = new DishDTO();
            dishDTO.setDishId(dish.getDishId());
            dishDTO.setDishName(dish.getDishName());
            dishDTO.setDishQuantity(dish.getDishQuantity());
            dishDTO.setMealName(dish.getMealName());
            result.add(dishDTO);
        }
        return result;
    }

    public Dishes saveDish(Dishes dish) {
        return dishesRepository.save(dish);
    }
}
