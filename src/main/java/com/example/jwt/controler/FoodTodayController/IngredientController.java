package com.example.jwt.controler.FoodTodayController;

import com.example.jwt.FoodTodayResponse.IngredientsResponse;
import com.example.jwt.FoodTodayResponse.mealResponse;
import com.example.jwt.dtos.FoodTodayDtos.IngredientDTO;
import com.example.jwt.entities.User;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.FoodTodayService.IngrdientService;
import com.example.jwt.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/ingredient")
@Tag(name = "FoodTodayIngredient Controller", description = "This is FoodTodayIngredient Controller")

public class IngredientController {

    @Autowired
    private UserService userService;
    @Autowired
    private IngrdientService ingrdientService;
    @Autowired
    private JwtHelper jwtHelper;

    //    @PostMapping("/setIngredientsForDish")
    @PostMapping("/setIngredientsForDish")
    public IngredientsResponse setIngredientsForDish(@RequestHeader("Auth") String tokenHeader, @RequestParam String mealName, @RequestParam String dishName, @RequestBody List<IngredientDTO> ingredientDTOList) {

        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the username to fetch the userId from your user service
        User user = userService.findByUsername(username);
        return ingrdientService.setIngredientsForDish(user, mealName, dishName, ingredientDTOList);
    }

    //get all ingredient with date
    @GetMapping("/getDishesWithIngredients-by-date")
    public List<mealResponse> getDishesWithIngredientsByDate(@RequestHeader("Auth") String tokenHeader, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the username to fetch the userId from your user service
        User user = userService.findByUsername(username);

        return ingrdientService.getDishesWithIngredientsByDate(user, date);
    }

    //get dish with ingreddient using mealName
    @GetMapping("/getDishesWithIngredients")
    public List<mealResponse> getDishesWithIngredientsByDateAndMealType(@RequestHeader("Auth") String tokenHeader, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam String mealType) {


        String token = tokenHeader.replace("Bearer ", "");
        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the username to fetch the userId from your user service
        User user = userService.findByUsername(username);

        return ingrdientService.getDishesWithIngredientsByDateAndMealType(user, date, mealType);
    }
}
