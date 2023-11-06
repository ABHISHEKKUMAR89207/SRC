package com.example.jwt.controler.FoodTodayController;


import com.example.jwt.dtos.FoodTodayDtos.DishDTO;
import com.example.jwt.entities.User;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.FoodTodayService.DishesService;
import com.example.jwt.service.FoodTodayService.IngrdientService;
import com.example.jwt.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/dishes")
@Tag(name = "Dishes Controller", description = "This is FoodTodayDishes Controller")
public class DishesController {
    @Autowired
    private DishesService dishesService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtHelper jwtHelper;

    @PostMapping("/add-dish")
    public void saveDishForUser( @RequestHeader("Auth") String tokenHeader, @RequestBody DishDTO dishDTO) {

        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the username to fetch the userId from your user service
        User user = userService.findByUsername(username);

        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Call the service to save the dish for the user with the current date
        dishesService.saveDishesForUser(dishDTO, user, currentDate);
    }


    // Endpoint to retrieve all dinner dishes for a user on the same date
    @GetMapping("/get-dish")
    public List<DishDTO> getDinnerDishesForUser( @RequestHeader("Auth") String tokenHeader, @RequestParam("date") String date, @RequestParam("mealName") String mealName) {
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the username to fetch the userId from your user service
        User user = userService.findByUsername(username);

        LocalDate parsedDate = LocalDate.parse(date);
        return dishesService.getDishesForUser(user, parsedDate, mealName);
    }







}
