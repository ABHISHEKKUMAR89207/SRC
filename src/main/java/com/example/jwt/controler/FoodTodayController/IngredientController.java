package com.example.jwt.controler.FoodTodayController;

import com.example.jwt.FoodTodayResponse.*;
import com.example.jwt.dtos.FoodTodayDtos.IngredientDTO;
import com.example.jwt.dtos.FoodTodayDtos.IngredientRequest;
import com.example.jwt.entities.FoodToday.Ingredients;
import com.example.jwt.entities.FoodToday.NinData;
import com.example.jwt.entities.User;
import com.example.jwt.repository.FoodTodayRepository.IngredientsRepository;
import com.example.jwt.request.NutrientRequest;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.FoodTodayService.IngrdientService;
import com.example.jwt.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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


    @PostMapping("/save")
    public ResponseEntity<String> saveNutrient(@RequestBody NutrientRequest nutrientRequest) {
        NinData nutrient = ingrdientService.saveNutrient(nutrientRequest);
        if (nutrient != null) {
            return ResponseEntity.ok("Nutrient saved successfully with ID: " + nutrient.getNinDataId());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save nutrient");
        }
    }

    //    @PostMapping("/setIngredientsForDish")
//    @PostMapping("/setIngredientsForDish")
//    public IngredientsResponse setIngredientsForDish(@RequestHeader("Auth") String tokenHeader, @RequestParam String mealName, @RequestParam String dishName, @RequestBody List<IngredientDTO> ingredientDTOList) {
//
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        // Use the username to fetch the userId from your user service
//        User user = userService.findByUsername(username);
//        return ingrdientService.setIngredientsForDish(user, mealName, dishName, ingredientDTOList);
//    }

//    @PostMapping("/setIngredientsForDish")
//    public ResponseEntity<String> setIngredientsForDish(@RequestHeader("Auth") String tokenHeader,
//                                                        @RequestParam String mealName,
//                                                        @RequestParam String dishName,
//                                                        @RequestBody List<IngredientDTO> ingredientDTOList) {
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        // Use the username to fetch the userId from your user service
//        User user = userService.findByUsername(username);
//
//        ingrdientService.setIngredientsForDish(user, mealName, dishName, ingredientDTOList);
//
//        return ResponseEntity.ok().build();    }
@PostMapping("/setIngredientsForDish")
public ResponseEntity<String> setIngredientsForDish(@RequestHeader("Auth") String tokenHeader,
                                                    @RequestParam String mealName,
                                                    @RequestParam String dishName,
                                                    @RequestBody List<IngredientDTO> ingredientDTOList) {
    String token = tokenHeader.replace("Bearer ", "");

    // Extract the username (email) from the token
    String username = jwtHelper.getUsernameFromToken(token);

    // Use the username to fetch the userId from your user service
    User user = userService.findByUsername(username);

    try {
        ingrdientService.setIngredientsForDish(user, mealName, dishName, ingredientDTOList);
        return ResponseEntity.ok().build();
    } catch (Exception e) {
        return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }
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


//    @GetMapping("/getDishesWithIngredients-by-date-range")
//    public List<mealResponse> getDishesWithIngredientsByDateRange(@RequestHeader("Auth") String tokenHeader,
//                                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//                                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
//
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        // Use the username to fetch the userId from your user service
//        User user = userService.findByUsername(username);
//
//        List<mealResponse> finalResponseList = new ArrayList<>();
//
//        // Loop through the date range
//        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
//            List<mealResponse> dailyResponse = ingrdientService.getDishesWithIngredientsByDate(user, date);
//            finalResponseList.addAll(dailyResponse);
//        }
//
//        return finalResponseList;
//    }

    @GetMapping("/getDishesWithIngredients-by-date-range")
    public List<mealResponse> getDishesWithIngredientsByDateRange(@RequestHeader("Auth") String tokenHeader,
                                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the username to fetch the userId from your user service
        User user = userService.findByUsername(username);

        List<mealResponse> finalResponseList = new ArrayList<>();

        // Loop through the date range
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            List<mealResponse> dailyResponse = ingrdientService.getDishesWithIngredientsByDate(user, date);

            // Check if there are dishes for the current date
            if (!dailyResponse.isEmpty()) {
                finalResponseList.addAll(dailyResponse);
            }
        }

        return finalResponseList;
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



    //get dish with ingreddient using mealName
//    @GetMapping("/getDishesWithIngredientss")
//    public List<mealResponse> getDishesWithIngredientsByDateAndMealTypee(@RequestHeader("Auth") String tokenHeader, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam String mealType) {
//
//
//        String token = tokenHeader.replace("Bearer ", "");
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        // Use the username to fetch the userId from your user service
//        User user = userService.findByUsername(username);
//
//        return ingrdientService.getDishesWithIngredientsByDateAndMealTypee(user, date, mealType);
//    }


    @GetMapping("/get-calories-with-date")
    public Map<String, Double> getCaloriesByDate(@RequestHeader("Auth") String tokenHeader, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        String token = tokenHeader.replace("Bearer ", "");
        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the username to fetch the userId from your user service
        User user = userService.findByUsername(username);

//        return ingrdientService.getEnergyByDate(user, date);
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
                        ingredientInfo.getFoodCode(), ingredientInfo.getIngredientQuantity());
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
    @Autowired
    private IngredientsRepository ingredientsRepository;


    @GetMapping("/get-salt")
//    @GetMapping("/ingredients/{ingredientName}")
    public ResponseEntity<Double> getIngredientQuantityByName(
            @RequestParam String ingredientName,
            @RequestHeader("Auth") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        // Use the jwtHelper to validate and extract information from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the username to fetch the user from your user service
        User user = userService.findByUsername(username);

        Double ingredientQuantity = ingrdientService.findIngredientQuantityByName(ingredientName);
        if (ingredientQuantity == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(ingredientQuantity, HttpStatus.OK);
        }
    }

}
