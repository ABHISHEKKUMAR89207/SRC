package com.example.jwt.controler.FoodTodayController;

import com.example.jwt.dtos.FoodDTO;
import com.example.jwt.dtos.NinDataDTO;
import com.example.jwt.entities.FoodToday.MissingRowFood;
import com.example.jwt.entities.FoodToday.MissingRowFoodDTO;
import com.example.jwt.entities.FoodToday.NinData;
import com.example.jwt.entities.FoodToday.Recipe.Recipe;
import com.example.jwt.entities.FoodToday.Recipe.RecipeRepository;
import com.example.jwt.entities.User;
import com.example.jwt.repository.FoodTodayRepository.MissingRowFoodRepository;
import com.example.jwt.repository.FoodTodayRepository.NinDataRepository;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.FoodTodayService.NinDataService;
import com.example.jwt.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/foods")
@Tag(name = "FoodTodayNinData Controller", description = "This is FoodTodayNinData Controller")

public class NinDataController {

    @Autowired
    private NinDataRepository ninDataRepository;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserService userService;

    // to get all the food items in order
//    @GetMapping("/all-food-list")
//    public List<String> getAllFoodNames(@RequestHeader("Auth") String tokenHeader) throws AccessDeniedException {
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        // Use the username to fetch the userId from your user service
//        User user = userService.findByUsername(username);
//
//        if (user != null && user.getUserId().equals(user.getUserId())) {
//            List<NinData> foodItems = ninDataRepository.findAll();
//            List<String> foodNames = foodItems.stream().map(NinData::getFood).collect(Collectors.toList());
//            return foodNames;
//        } else {
//            throw new AccessDeniedException("Access denied");
//        }
//    }

    @GetMapping("/all-food-list")
    public List<FoodDTO> getAllFoodNamesAndCodes(@RequestHeader("Auth") String tokenHeader) throws AccessDeniedException {
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the username to fetch the userId from your user service
        User user = userService.findByUsername(username);

        if (user != null && user.getUserId().equals(user.getUserId())) {
            List<NinData> foodItems = ninDataRepository.findAll();
            List<FoodDTO> foodList = foodItems.stream()
                    .map(foodItem -> new FoodDTO(foodItem.getFood(), foodItem.getFoodCode()))
                    .collect(Collectors.toList());
            return foodList;
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    @Autowired
    private RecipeRepository recipeRepository;
    @GetMapping("/all-recipes-list")
    public List<String> getAllRecipesNames(@RequestHeader("Auth") String tokenHeader) throws AccessDeniedException {
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the username to fetch the userId from your user service
        User user = userService.findByUsername(username);

        if (user != null && user.getUserId().equals(user.getUserId())) {
            List<Recipe> foodItems = recipeRepository.findAll();
            List<String> foodNames = foodItems.stream().map(Recipe::getRecipe_name).collect(Collectors.toList());
            return foodNames;
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }



    @Autowired
    private NinDataService ninDataService;

    @GetMapping("/ing-details")
    public List<NinDataDTO> getNutritionalData(@RequestParam Long foodId) {
        return ninDataService.getNutritionalDataByFood(foodId);
    }



    @Autowired
    private MissingRowFoodRepository missingRowFoodRepository;


//    @GetMapping("/missingRowFoods")
////    public ResponseEntity<List<MissingRowFood>> getAllMissingRowFoods(@RequestHeader("Auth") String tokenHeader) {
////        try {
////            String token = tokenHeader.replace("Bearer ", "");
//////
////            // Extract the username (email) from the token
////            String username = jwtHelper.getUsernameFromToken(token);
////
////            // Use the username to fetch the userId from your user service
////            User user = userService.findByUsername(username);
//////
////            List<MissingRowFood> missingRowFoods = missingRowFoodRepository.findAll();
////            return new ResponseEntity<>(missingRowFoods, HttpStatus.OK);
////        } catch (Exception e) {
////            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
////        }
////    }




    @PostMapping("/missing-messages")
    public ResponseEntity<String> saveMissingRowFood(@RequestHeader("Auth") String tokenHeader, @RequestParam String requestData) {
        try {

            String token = tokenHeader.replace("Bearer ", "");
//
        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the username to fetch the userId from your user service
        User user = userService.findByUsername(username);
//
            // Creating a new MissingRowFood object
            MissingRowFood missingRowFood = new MissingRowFood();

            // Setting timestamp
            missingRowFood.setTimestamp(new Timestamp(System.currentTimeMillis()));

            // Setting missing data
            missingRowFood.setMessingMessage(requestData);

            // Saving the MissingRowFood object
            missingRowFoodRepository.save(missingRowFood);

            return new ResponseEntity<>("Missing Row Food Saved Successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
