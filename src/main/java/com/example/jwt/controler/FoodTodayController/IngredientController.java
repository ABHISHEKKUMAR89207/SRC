package com.example.jwt.controler.FoodTodayController;

import com.example.jwt.FoodTodayResponse.*;
import com.example.jwt.dtos.FoodTodayDtos.IngredientDTO;
import com.example.jwt.dtos.FoodTodayDtos.IngredientRequest;
import com.example.jwt.entities.User;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.FoodTodayService.IngrdientService;
import com.example.jwt.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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





    @GetMapping("/get-calories-with-date")
    public Map<String, Double> getCaloriesByDate(@RequestHeader("Auth") String tokenHeader, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        String token = tokenHeader.replace("Bearer ", "");
        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the username to fetch the userId from your user service
        User user = userService.findByUsername(username);

        return ingrdientService.getEnergyByDate(user, date);
    }



    @GetMapping("/nutrition-summary-detailed")
    public ResponseEntity<Map<String, Map<String, Map<String, Map<String, Double>>>>> getDetailedNutritionSummary(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestHeader("Auth") String tokenHeader) {

        String token = tokenHeader.replace("Bearer ", "");
        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the username to fetch the userId from your user service
        User user = userService.findByUsername(username);

        // Calculate nutrition summary
        Map<String, Map<String, Map<String, Map<String, Double>>>> summary = ingrdientService. getNutritionByMealTypeDishNameAndDate(user, startDate, endDate);
        return ResponseEntity.ok(summary);
    }



    @GetMapping("/nutrition-summary")
    public ResponseEntity<List<Map<String, Object>>> getNutritionSummary(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestHeader("Auth") String tokenHeader) {

        String token = tokenHeader.replace("Bearer ", "");
        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the username to fetch the userId from your user service
        User user = userService.findByUsername(username);


        List<Map<String, Object>> summary = ingrdientService.calculateNutritionSummary(user, startDate, endDate);
        return ResponseEntity.ok( summary);
    }


//    @GetMapping("/calculate")
//    public NutritionalInfoResponse calculateNutritionalInfo(
//            @RequestParam String ingredientName,
//            @RequestParam Double ingredientQuantity) {
//        return ingrdientService.getNutritionalInfo(ingredientName, ingredientQuantity);
//    }
//@GetMapping("/calculate")
//public ResponseEntity<NutritionalInfoResponse> calculateNutritionalInfo(
//        @RequestParam String ingredientName,
//        @RequestParam Double ingredientQuantity,
//        @RequestHeader("Authorization") String authorizationHeader) {
//
//    try {
//        // Extract the token from the authorization header
//        String token = authorizationHeader.replace("Bearer ", "");
//
//        // Use the jwtHelper to validate and extract information from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        // Use the username to fetch the user from your user service
//        User user = userService.findByUsername(username);
//
//        // Call the service method to get nutritional information
//        NutritionalInfoResponse nutritionalInfo = ingrdientService.getNutritionalInfo(ingredientName, ingredientQuantity);
//
//        return new ResponseEntity<>(nutritionalInfo, HttpStatus.OK);
//    } catch (Exception e) {
//        // Handle exceptions (e.g., invalid token, user not found) and return an appropriate response
//        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // You can customize the response code as needed
//    }
//}

//    @PostMapping("/calculate")
//    public ResponseEntity<List<NutritionalInfoResponse>> calculateNutritionalInfo(
//            @RequestBody IngredientRequestt ingredientRequest,
//            @RequestHeader("Auth") String authorizationHeader) {
//
//        try {
//            // Extract the token from the authorization header
//            String token = authorizationHeader.replace("Bearer ", "");
//
//            // Use the jwtHelper to validate and extract information from the token
//            String username = jwtHelper.getUsernameFromToken(token);
//
//            // Use the username to fetch the user from your user service
//            User user = userService.findByUsername(username);
//
//            List<NutritionalInfoResponse> nutritionalInfoList = new ArrayList<>();
//
//            for (com.example.jwt.entities.FoodToday.NewRecipe.Ing.IngredientRequestt ingredientInfo : ingredientRequest.getIngredients()) {
//                // Call the service method to get nutritional information for each ingredient
//                NutritionalInfoResponse nutritionalInfo = ingrdientService.getNutritionalInfo(
//                        ingredientInfo.getIngredientName(), ingredientInfo.getWeight());
//                nutritionalInfoList.add(nutritionalInfo);
//            }
//
//            return new ResponseEntity<>(nutritionalInfoList, HttpStatus.OK);
//        } catch (Exception e) {
//            // Handle exceptions (e.g., invalid token, user not found) and return an appropriate response
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // You can customize the response code as needed
//        }
//    }


    @PostMapping("/calculate-nutrition")
    public ResponseEntity<AggregatedNutritionalInfoResponse> calculateNutritionalInfo(
            @RequestBody IngredientRequestt ingredientRequest,
            @RequestHeader("Auth") String authorizationHeader) {

        try {
            // Extract the token from the authorization header
            String token = authorizationHeader.replace("Bearer ", "");

            // Use the jwtHelper to validate and extract information from the token
            String username = jwtHelper.getUsernameFromToken(token);

            // Use the username to fetch the user from your user service
            User user = userService.findByUsername(username);

            List<NutritionalInfoResponse> nutritionalInfoList = new ArrayList<>();

            for (IngredientRequest ingredientInfo : ingredientRequest.getIngredients()) {
                // Call the service method to get nutritional information for each ingredient
                NutritionalInfoResponse nutritionalInfo = ingrdientService.getNutritionalInfo(
                        ingredientInfo.getIngredientName(), ingredientInfo.getIngredientQuantity());
                nutritionalInfoList.add(nutritionalInfo);
            }

            // Create the aggregated response
            AggregatedNutritionalInfoResponse aggregatedResponse = new AggregatedNutritionalInfoResponse(nutritionalInfoList);

            return new ResponseEntity<>(aggregatedResponse, HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions (e.g., invalid token, user not found) and return an appropriate response
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // You can customize the response code as needed
        }
    }


}
