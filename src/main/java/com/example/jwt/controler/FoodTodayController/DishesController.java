package com.example.jwt.controler.FoodTodayController;


import com.example.jwt.dtos.FoodTodayDtos.DishDTO;
import com.example.jwt.entities.User;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.FoodTodayService.DishesService;
import com.example.jwt.service.FoodTodayService.IngrdientService;
import com.example.jwt.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
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

    @PutMapping("/add-favourite")
    public ResponseEntity<String> addDishToFavourites(
            @RequestHeader("Auth") String tokenHeader,
            @RequestParam("dish_id") Long dishId,
            @RequestParam("favourite") boolean favourite
    ) {
        try {
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);

            // Use the username to fetch the userId from your user service
            User user = userService.findByUsername(username);

            // Call the service to update the favourite status for the dish
            dishesService.updateFavouriteStatus(user, dishId, favourite);

            return ResponseEntity.ok("Favourite status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating favourite status: " + e.getMessage());
        }
    }


    @GetMapping("/favourites")
    public ResponseEntity<List<DishDTO>> getAllFavouriteDishes(
            @RequestHeader("Auth") String tokenHeader
    ) {
        try {
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);

            // Use the username to fetch the user from your user service
            User user = userService.findByUsername(username);

            // Call the service to get all favorite dishes for the user
            List<DishDTO> favouriteDishes = dishesService.getAllFavouriteDishes(user);

            return ResponseEntity.ok(favouriteDishes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }



}
