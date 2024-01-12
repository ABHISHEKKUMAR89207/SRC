//package com.example.jwt.controler;
//
//import com.example.jwt.entities.FoodToday.CalculatedRecipe;
//import com.example.jwt.entities.FoodToday.NinData;
//import com.example.jwt.entities.FoodToday.RecipeData;
//import com.example.jwt.entities.FoodToday.UserRecipe;
//import com.example.jwt.entities.User;
//import com.example.jwt.repository.RecipeDataRepository;
//import com.example.jwt.security.JwtHelper;
//import com.example.jwt.service.FoodTodayService.NinDataService;
//import com.example.jwt.service.FoodTodayService.RecipeService;
//import com.example.jwt.service.FoodTodayService.UserRecipeRepository;
//import com.example.jwt.service.FoodTodayService.UserRecipeService;
//import com.example.jwt.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@RestController
//@RequestMapping("/recipes")
//public class RecipeDataController {
//
//    private final RecipeDataRepository recipeDataRepository;
//    private final NinDataService ninDataService;
//
//
//    @Autowired
//    public RecipeDataController(RecipeDataRepository recipeDataRepository,NinDataService ninDataService) {
//        this.recipeDataRepository = recipeDataRepository;
//        this.ninDataService = ninDataService;
//    }
//
////
//@GetMapping("/get-all-recipe")
//public ResponseEntity<List<String>> getAllRecipeNames() {
//    List<String> recipeNames = recipeDataRepository.findDistinctRecipeNames();
//
//    if (recipeNames.isEmpty()) {
//        return ResponseEntity.notFound().build();
//    }
//
//    return ResponseEntity.ok(recipeNames);
//}
//
//
//
//
//@Autowired
//    private RecipeService recipeService;
//
//
//
////    @GetMapping("/calculate-ingredient-quantity")
////    public ResponseEntity<Double> calculateIngredientQuantity(
////            @RequestParam("recipeName") String recipeName) {
////
////        // Retrieve recipe data based on recipeName
////        RecipeData recipeData = recipeService.getRecipeByName(recipeName);
////
////        if (recipeData != null) {
////            // Get the totalCookedQuantity from the database for the specified recipe
////            Double totalCookedQuantity = recipeData.getTotal_cooked_quantity();
////
////            // Assuming you want to calculate for 1 unit, you can directly use it
////                Double quantityForOneUnit = recipeData.getGrams()/totalCookedQuantity;
////
////            return ResponseEntity.ok(quantityForOneUnit);
////        } else {
////            return ResponseEntity.notFound().build();
////        }
////    }
//
////    @GetMapping("/calculate-ingredient-quantity")
////    public ResponseEntity<Double> calculateIngredientQuantity(@RequestParam("recipeName") String recipeName) {
////
////        // Retrieve recipe data based on recipeName
////        RecipeData recipeData = recipeService.getRecipeByName(recipeName);
////
////        if (recipeData != null) {
////            // Get the totalCookedQuantity from the database for the specified recipe
////            Double totalCookedQuantity = recipeData.getTotal_cooked_quantity();
////
////            // Assuming you want to calculate for 1 unit, you can directly use it
////            Double quantityForOneUnit = recipeData.getGrams() / totalCookedQuantity;
////
////            // Retrieve NinData based on the recipe code
////            // Assuming you have a method to find NinData by code from your service
////            List<NinData> ninData = ninDataService.getNinDataByFoodCode(recipeData.getCode());
////
////            if (ninData != null) {
////                // Calculate protein content based on the code
////                Double proteinContent = ninData.getProtein()/100;
////
////                // Calculate protein for the provided quantityForOneUnit
////                Double proteinForGivenQuantity = quantityForOneUnit * proteinContent;
////
////                return ResponseEntity.ok(proteinForGivenQuantity);
////            } else {
////                return ResponseEntity.notFound().build();
////            }
////        } else {
////            return ResponseEntity.notFound().build();
////        }
////    }
//
//    @Autowired
//    private JwtHelper jwtHelper;
//
//@GetMapping("/calculate-ingredient-quantity")
//public ResponseEntity<CalculatedRecipe> calculateIngredientQuantity(
//        @RequestHeader("Auth") String tokenHeader,
//        @RequestParam("recipeName") String recipeName,
//        @RequestParam("recipeQuantity") Double recipeQuantity
//) {
//    String token = tokenHeader.replace("Bearer ", "");
//
//    // Extract the username from the JWT token
//    String username = jwtHelper.getUsernameFromToken(token);
//
//    List<RecipeData> recipeDataList = recipeService.getRecipeByName(recipeName);
//    // Fetch recipes based on the username
////    List<RecipeData> recipeDataList = recipeService.getRecipesByUserAndName(username, recipeName);
//
//    if (!recipeDataList.isEmpty()) {
//        Double totalProteinContent = 0.0;
//        Double totalEnergyContent = 0.0;
//        Double totalFatContent = 0.0;
//        Double totalCarbsContent = 0.0;
//        Double totalFiberContent = 0.0;
//
//        for (RecipeData recipeData : recipeDataList) {
//            Double totalCookedQuantity = recipeData.getTotal_cooked_quantity();
//            Double quantityForOneUnit = recipeData.getGrams() / totalCookedQuantity;
//
//            List<NinData> ninDataList = ninDataService.getNinDataByFoodCode(recipeData.getCode());
//
//            if (ninDataList != null) {
//                for (NinData ninData : ninDataList) {
//                    String name = ninData.getFood();
//                    System.out.println("Ing name "+name);
//                    Double proteinContent = ninData.getProtein() / 100;
//                    Double energyContent = ninData.getEnergy() / 100;
//                    Double fatContent = ninData.getTotal_Fat() / 100;
//                    Double carbsContent = ninData.getCarbohydrate() / 100;
//                    Double fiberContent = ninData.getTotal_Dietary_Fibre() / 100;
//
//                    totalProteinContent += quantityForOneUnit * proteinContent * recipeQuantity;
//                    totalEnergyContent += quantityForOneUnit * energyContent * recipeQuantity;
//                    totalFatContent += quantityForOneUnit * fatContent * recipeQuantity;
//                    totalCarbsContent += quantityForOneUnit * carbsContent * recipeQuantity;
//                    totalFiberContent += quantityForOneUnit * fiberContent * recipeQuantity;
//                }
//            }
//        }
//
//        CalculatedRecipe calculatedValues = new CalculatedRecipe();
//        calculatedValues.setTotalProteinContent(totalProteinContent);
//        calculatedValues.setTotalEnergyContent(totalEnergyContent);
//        calculatedValues.setTotalFatContent(totalFatContent);
//        calculatedValues.setTotalCarbsContent(totalCarbsContent);
//        calculatedValues.setTotalFiberContent(totalFiberContent);
//
//        return ResponseEntity.ok(calculatedValues);
//    }
//
//    else {
//        return ResponseEntity.notFound().build();
//    }
//}
//
//@Autowired
//private UserService userService;
//
//@Autowired
//private UserRecipeRepository userRecipeRepository;
//
//@Autowired
//private UserRecipeService userRecipeService;
//
//    @PostMapping("/save-calculation")
//    public ResponseEntity<CalculatedRecipe> saveCalculation(
//            @RequestHeader("Auth") String tokenHeader,
//            @RequestParam("recipeName") String recipeName,
//            @RequestParam("recipeQuantity") Double recipeQuantity
//    ) {
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username from the JWT token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        // Fetch the user based on the username
//        User user = userService.findByUsername(username);
//
//
//   // Fetch the recipe data based on the provided recipeName
//        List<RecipeData> recipeDataList = recipeService.getRecipeByName(recipeName);
//
//        if (user != null && !recipeDataList.isEmpty()) {
//            // Assume you are using the first recipe found for calculation
//            RecipeData recipeData = recipeDataList.get(0);
//
//            Double totalProteinContent = 0.0;
//            Double totalEnergyContent = 0.0;
//            Double totalFatContent = 0.0;
//            Double totalCarbsContent = 0.0;
//            Double totalFiberContent = 0.0;
//
//            // Get the totalCookedQuantity from the database for the specified recipe
//            Double totalCookedQuantity = recipeData.getTotal_cooked_quantity();
//
//            // Calculate quantity for one unit
//            Double quantityForOneUnit = recipeData.getGrams() / totalCookedQuantity;
//
//            // Retrieve NinData based on the recipe code
//            List<NinData> ninDataList = ninDataService.getNinDataByFoodCode(recipeData.getCode());
//
//            if (ninDataList != null) {
//                for (NinData ninData : ninDataList) {
//                    String name = ninData.getFood();
//                    System.out.println("Ing name "+name);
//                    Double proteinContent = ninData.getProtein() / 100;
//                    Double energyContent = ninData.getEnergy() / 100;
//                    Double fatContent = ninData.getTotal_Fat() / 100;
//                    Double carbsContent = ninData.getCarbohydrate() / 100;
//                    Double fiberContent = ninData.getTotal_Dietary_Fibre() / 100;
//
//                    totalProteinContent += quantityForOneUnit * proteinContent * recipeQuantity;
//                    totalEnergyContent += quantityForOneUnit * energyContent * recipeQuantity;
//                    totalFatContent += quantityForOneUnit * fatContent * recipeQuantity;
//                    totalCarbsContent += quantityForOneUnit * carbsContent * recipeQuantity;
//                    totalFiberContent += quantityForOneUnit * fiberContent * recipeQuantity;
//                }
//            }
//
//            // Create a UserRecipe entity and save it
//            UserRecipe userRecipe = new UserRecipe();
//            userRecipe.setUser(user);
//            userRecipe.setRecipeData(recipeData);
//            userRecipe.setRecipeQuantity(recipeQuantity);
//            userRecipe.setLocalDate(LocalDate.now());
//
//             userRecipeRepository.save(userRecipe);
//
////            UserRecipe savedUserRecipe = userRecipeService.saveUserRecipe(userRecipe);
//            // ... (Save 'userRecipe' using your service)
//
//            // Construct the CalculatedRecipe object
//            CalculatedRecipe calculatedValues = new CalculatedRecipe();
//            calculatedValues.setTotalProteinContent(totalProteinContent);
//            calculatedValues.setTotalEnergyContent(totalEnergyContent);
//            calculatedValues.setTotalFatContent(totalFatContent);
//            calculatedValues.setTotalCarbsContent(totalCarbsContent);
//            calculatedValues.setTotalFiberContent(totalFiberContent);
//
//            return ResponseEntity.ok(calculatedValues);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//
//    }
//
//
//
//    @GetMapping("/calculate-ingredient-quantity-by-date")
//    public ResponseEntity<CalculatedRecipe> calculateIngredientQuantityByDate(
//            @RequestHeader("Auth") String tokenHeader,
//            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
//    ) {
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username from the JWT token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        // Fetch recipes based on the provided date
//        List<UserRecipe> userRecipes = userRecipeService.getUserRecipesByDate(username, date);
//
//        if (!userRecipes.isEmpty()) {
//            Double totalProteinContent = 0.0;
//            Double totalEnergyContent = 0.0;
//            Double totalFatContent = 0.0;
//            Double totalCarbsContent = 0.0;
//            Double totalFiberContent = 0.0;
//
//            for (UserRecipe userRecipe : userRecipes) {
//                RecipeData recipeData = userRecipe.getRecipeData();
//                Double totalCookedQuantity = recipeData.getTotal_cooked_quantity();
//                Double quantityForOneUnit = recipeData.getGrams() / totalCookedQuantity;
//
//                List<NinData> ninDataList = ninDataService.getNinDataByFoodCode(recipeData.getCode());
//
//                if (ninDataList != null) {
//                    for (NinData ninData : ninDataList) {
////                        String name = ninData.getFood();
////                        System.out.println("Ing name "+name);
//                        Double proteinContent = ninData.getProtein() / 100;
//                        Double energyContent = ninData.getEnergy() / 100;
//                        Double fatContent = ninData.getTotal_Fat() / 100;
//                        Double carbsContent = ninData.getCarbohydrate() / 100;
//                        Double fiberContent = ninData.getTotal_Dietary_Fibre() / 100;
//
//                        totalProteinContent += quantityForOneUnit * proteinContent * userRecipe.getRecipeQuantity();
//                        totalEnergyContent += quantityForOneUnit * energyContent * userRecipe.getRecipeQuantity();
//                        totalFatContent += quantityForOneUnit * fatContent * userRecipe.getRecipeQuantity();
//                        totalCarbsContent += quantityForOneUnit * carbsContent * userRecipe.getRecipeQuantity();
//                        totalFiberContent += quantityForOneUnit * fiberContent * userRecipe.getRecipeQuantity();
//                    }
//                }
//            }
//
//            CalculatedRecipe calculatedValues = new CalculatedRecipe();
//            calculatedValues.setTotalProteinContent(totalProteinContent);
//            calculatedValues.setTotalEnergyContent(totalEnergyContent);
//            calculatedValues.setTotalFatContent(totalFatContent);
//            calculatedValues.setTotalCarbsContent(totalCarbsContent);
//            calculatedValues.setTotalFiberContent(totalFiberContent);
//
//            return ResponseEntity.ok(calculatedValues);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//}