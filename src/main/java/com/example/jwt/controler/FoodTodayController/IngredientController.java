package com.example.jwt.controler.FoodTodayController;

import com.example.jwt.FoodTodayResponse.*;
import com.example.jwt.dtos.FoodTodayDtos.IngredientDTO;
import com.example.jwt.dtos.FoodTodayDtos.IngredientRequest;
import com.example.jwt.entities.FoodToday.Dishes;
import com.example.jwt.entities.FoodToday.Ingredients;
import com.example.jwt.entities.FoodToday.NewRecipe.Personal.Personal;
import com.example.jwt.entities.FoodToday.NinData;
import com.example.jwt.entities.User;
import com.example.jwt.repository.FoodTodayRepository.IngredientsRepository;
import com.example.jwt.request.NutrientRequest;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.FoodTodayService.IngrdientService;
import com.example.jwt.service.FoodTodayService.NinDataService;
import com.example.jwt.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


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

    @Autowired
    private NinDataService ninDataService;



    @Value("${upload.path}") // This will get the base path
    private String basePath;

    //    @PostMapping("/uploadImages")
    //    public ResponseEntity<String> uploadImages(@RequestParam("images") MultipartFile[] images) {
    //        try {
    //            // Create "rowIngImage" directory inside the "images" folder if it doesn't exist
    //            File rowIngImageDir = new File(basePath, "rowIngImage");
    //            if (!rowIngImageDir.exists()) {
    //                rowIngImageDir.mkdirs();
    //            }
    //
    //            for (MultipartFile image : images) {
    //                String fileName = image.getOriginalFilename();
    //                // Save the image to the "rowIngImage" folder within the "images" folder
    //                File dest = new File(rowIngImageDir.getAbsolutePath(), fileName);
    //                image.transferTo(dest);
    //            }
    //            return new ResponseEntity<>("Images uploaded successfully", HttpStatus.OK);
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //            return new ResponseEntity<>("Failed to upload images", HttpStatus.INTERNAL_SERVER_ERROR);
    //        }
    //    }

//        @PostMapping("/uploadImages")
//        public ResponseEntity<String> uploadImages(@RequestParam("images") MultipartFile[] images) {
//            try {
//                // Create "images" directory if it doesn't exist
//                File imagesDir = new File(basePath);
//                if (!imagesDir.exists()) {
//                    imagesDir.mkdirs();
//                }
//
//                for (MultipartFile image : images) {
//                    String fileName = image.getOriginalFilename();
//                    // Save the image to the "images" folder
//                    File dest = new File(imagesDir.getAbsolutePath(), fileName);
//                    image.transferTo(dest);
//                }
//                return new ResponseEntity<>("Images uploaded successfully", HttpStatus.OK);
//            } catch (IOException e) {
//                e.printStackTrace();
//                return new ResponseEntity<>("Failed to upload images", HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//        }
@PostMapping("/uploadImages")
public ResponseEntity<String> uploadImages(@RequestParam("images") MultipartFile[] images) {
    try {
        // Create "images" directory if it doesn't exist
        File imagesDir = new File(basePath);
        if (!imagesDir.exists()) {
            imagesDir.mkdirs();
        }

        StringBuilder responseMessage = new StringBuilder();
        for (MultipartFile image : images) {
            String fileName = image.getOriginalFilename();
            File dest = new File(imagesDir.getAbsolutePath(), fileName);

            // Check if the file already exists
            if (dest.exists()) {
                responseMessage.append("Image already exists: ").append(fileName).append("\n");
            } else {
                // Save the image to the "images" folder
                image.transferTo(dest);
                responseMessage.append("Image uploaded: ").append(fileName).append("\n");
            }
        }
        return new ResponseEntity<>(responseMessage.toString(), HttpStatus.OK);
    } catch (IOException e) {
        e.printStackTrace();
        return new ResponseEntity<>("Failed to upload images", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}



    @PostMapping("/save")
        public ResponseEntity<String> saveNutrient(@RequestBody NutrientRequest nutrientRequest) {
            NinData nutrient = ingrdientService.saveNutrient(nutrientRequest);
            if (nutrient != null) {
                return ResponseEntity.ok("Nutrient saved successfully with ID: " + nutrient.getNinDataId());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save nutrient");
            }
        }


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


    @GetMapping("/getTotalEnergyIntake-by-date")
    public List<mealResponse> getTotalEnergyIntakeByDate(@RequestHeader("Auth") String tokenHeader, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the username to fetch the userId from your user service
        User user = userService.findByUsername(username);

        return ingrdientService.getTotalEnergyIntakeByDate(user, date);
    }



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


//
//
//        @GetMapping("/get-salt-and-sugar")
//        public ResponseEntity<Map<String, Double>> getSaltAndSugarQuantity(
//                @RequestHeader("Auth") String authorizationHeader) {
//            try {
//                String token = authorizationHeader.replace("Bearer ", "");
//
//                // Use the jwtHelper to validate and extract information from the token
//                String username = jwtHelper.getUsernameFromToken(token);
//
//                // Use the username to fetch the user from your user service
//                User user = userService.findByUsername(username);
//
//                // Check if the user exists
//                if (user == null) {
//                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//                }
//
//                // Retrieve the user's dishes for the current date
//                List<Dishes> dishes = user.getDishesList();
//                List<Personal> personals = user.getPersonals();
//                double totalSaltQuantity = 0.0;
//                double totalSugarQuantity = 0.0;
//
//                // Get the current date
//                LocalDate currentDate = LocalDate.now();
//
//                // Iterate through the user's dishes for the current date to find the ingredients
//                for (Dishes dish : dishes) {
//                    // Check if the dish was taken on the current date
//                    if (dish.getDate().isEqual(currentDate)) {
//                        List<Ingredients> ingredients = dish.getIngredientList();
//                        for (Ingredients ingredient : ingredients) {
//                            // Check if the ingredient category is "Salt" or "Sugars"
//                            if (ingredient.getCategory().equalsIgnoreCase("Salt")) {
//                                // Calculate the total quantity of salt from dish ingredients
//                                totalSaltQuantity += (dish.getServingSize() / dish.getDishQuantity()) * ingredient.getIngredientQuantity();
//                            } else if (ingredient.getCategory().equalsIgnoreCase("Sugars")) {
//                                // Calculate the total quantity of sugar from dish ingredients
//                                totalSugarQuantity += (dish.getServingSize() / dish.getDishQuantity()) * ingredient.getIngredientQuantity();
//                            }
//                        }
//                    }
//                }
//
//                // Iterate through the user's personals for the current date to find the ingredients
//                for (Personal personal : personals) {
//                    // Check if the personal was recorded on the current date
//                    if (personal.getDate().isEqual(currentDate)) {
//                        List<Ingredients> ingredients = personal.getIngredientList();
//                        for (Ingredients ingredient : ingredients) {
//
//                            System.out.println("Personal ingredient name: " + ingredient.getIngredientName());
//
//                            // Check if the ingredient category is "Salt" or "Sugars"
//                            if (ingredient.getCategory().equalsIgnoreCase("Salt")) {
//                                // Calculate the total quantity of salt from personal ingredients
//                                totalSaltQuantity += (personal.getOneServingWtG() / personal.getOneUnitSize()) * ingredient.getIngredientQuantity();
//                            } else if (ingredient.getCategory().equalsIgnoreCase("Sugars")) {
//                                // Calculate the total quantity of sugar from personal ingredients
//                                totalSugarQuantity += (personal.getOneServingWtG() / personal.getOneUnitSize()) * ingredient.getIngredientQuantity();
//                            }
//                        }
//                    }
//                }
//
//                // Create a map to hold the response
//                Map<String, Double> response = new HashMap<>();
//                response.put("Salt", totalSaltQuantity);
//                response.put("Sugar", totalSugarQuantity);
//
//                // Return the response
//                return new ResponseEntity<>(response, HttpStatus.OK);
//            } catch (Exception e) {
//                e.printStackTrace();
//                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//        }


    @GetMapping("/get-salt-and-sugar")
    public ResponseEntity<Map<String, Double>> getSaltAndSugarQuantity(
            @RequestHeader("Auth") String authorizationHeader,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");

            // Use the jwtHelper to validate and extract information from the token
            String username = jwtHelper.getUsernameFromToken(token);

            // Use the username to fetch the user from your user service
            User user = userService.findByUsername(username);

            // Check if the user exists
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            // Retrieve the user's dishes for the specified date
            List<Dishes> dishes = user.getDishesList();
            List<Personal> personals = user.getPersonals();
            double totalSaltQuantity = 0.0;
            double totalSugarQuantity = 0.0;

            // Iterate through the user's dishes for the specified date to find the ingredients
            for (Dishes dish : dishes) {
                // Check if the dish was taken on the specified date
                if (dish.getDate().isEqual(date)) {
                    List<Ingredients> ingredients = dish.getIngredientList();
                    for (Ingredients ingredient : ingredients) {
                        // Check if the ingredient category is "Salt" or "Sugars"
                        if (ingredient.getCategory().equalsIgnoreCase("Salt")) {
                            // Calculate the total quantity of salt from dish ingredients
                            totalSaltQuantity += (dish.getServingSize() / dish.getDishQuantity()) * ingredient.getIngredientQuantity();
                        } else if (ingredient.getCategory().equalsIgnoreCase("Sugars")) {
                            // Calculate the total quantity of sugar from dish ingredients
                            totalSugarQuantity += (dish.getServingSize() / dish.getDishQuantity()) * ingredient.getIngredientQuantity();
                        }
                    }
                }
            }

            // Iterate through the user's personals for the specified date to find the ingredients
            for (Personal personal : personals) {
                // Check if the personal was recorded on the specified date
                if (personal.getDate().isEqual(date)) {
                    List<Ingredients> ingredients = personal.getIngredientList();
                    for (Ingredients ingredient : ingredients) {

                        System.out.println("Personal ingredient name: " + ingredient.getIngredientName());

                        // Check if the ingredient category is "Salt" or "Sugars"
                        if (ingredient.getCategory().equalsIgnoreCase("Salt")) {
                            // Calculate the total quantity of salt from personal ingredients
                            totalSaltQuantity += (personal.getOneServingWtG() / personal.getOneUnitSize()) * ingredient.getIngredientQuantity();
                        } else if (ingredient.getCategory().equalsIgnoreCase("Sugars")) {
                            // Calculate the total quantity of sugar from personal ingredients
                            totalSugarQuantity += (personal.getOneServingWtG() / personal.getOneUnitSize()) * ingredient.getIngredientQuantity();
                        }
                    }
                }
            }

            // Create a map to hold the response
            Map<String, Double> response = new HashMap<>();
            response.put("Salt", totalSaltQuantity);
            response.put("Sugar", totalSugarQuantity);

            // Return the response
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



        @GetMapping("/category-wise-ingredient-quantity")
        public ResponseEntity<Map<String, Double>> getCategoryWiseIngredientQuantity(@RequestHeader("Auth") String authorizationHeader) {
            String token = authorizationHeader.replace("Bearer ", "");

            // Use the jwtHelper to validate and extract information from the token
            String username = jwtHelper.getUsernameFromToken(token);

            // Use the username to fetch the user from your user service
            User user = userService.findByUsername(username);

            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            // Map to hold category-wise ingredient quantity totals
            Map<String, Double> categoryWiseIngredientQuantity = new HashMap<>();

            // Get the current date
            LocalDate currentDate = LocalDate.now();

            // Retrieve all categories from ninData and initialize their quantities to 0.0
            List<NinData> allNinData = ninDataService.getAllNinData();
            for (NinData ninData : allNinData) {
                categoryWiseIngredientQuantity.put(ninData.getDDS_Category(), 0.0);
            }

            // Iterate through each dish of the user
            for (Dishes dish : user.getDishesList()) {
                // Check if the date of the dish matches the current date
                if (dish.getDate().equals(currentDate)) {
                    // Iterate through each ingredient of the dish
                    for (Ingredients ingredient : dish.getIngredientList()) {
                        // Get the foodCode of the ingredient
                        String foodCode = ingredient.getFoodCode();

                        // Find the NinData record corresponding to the foodCode
                        NinData ninData = ninDataService.findByFoodCode(foodCode);

                        if (ninData != null) {
                            // Get the DDS_Category
                            String ddsCategory = ninData.getDDS_Category();

                            // Get the ingredient quantity
                            double ingredientQuantity = ingredient.getIngredientQuantity();

                            // Update the total quantity for the DDS_Category
                            categoryWiseIngredientQuantity.put(ddsCategory,
                                    categoryWiseIngredientQuantity.get(ddsCategory) + ingredientQuantity);
                        }
                    }
                }
            }

            // Now you have category-wise ingredient quantity totals in the map
            return ResponseEntity.ok(categoryWiseIngredientQuantity);
        }
}
