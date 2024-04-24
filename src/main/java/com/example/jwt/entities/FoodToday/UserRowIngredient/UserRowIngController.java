package com.example.jwt.entities.FoodToday.UserRowIngredient;

import com.example.jwt.entities.User;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/api/food")
public class UserRowIngController {
    private final UserRowIngService userRowIngService;
    private final JwtHelper jwtHelper;
    private final UserService userService;

    @Autowired
    public UserRowIngController(UserRowIngService userRowIngService,JwtHelper jwtHelper,UserService userService) {
        this.userRowIngService = userRowIngService;
        this.jwtHelper = jwtHelper;
        this.userService = userService;
    }

//    @PostMapping("/add")
//    public ResponseEntity<String> addUserRowIng(@RequestBody UserRowIngRequest request, @RequestParam Long userId) {
//        try {
//            userRowIngService.addUserRowIng(request, userId);
//            return ResponseEntity.ok("UserRowIng added successfully");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
//        }
//    }
@PostMapping("/add-userRow-Ingredient")
public ResponseEntity<String> addUserRowIng(@RequestHeader("Auth") String tokenHeader,@RequestBody UserRowIngRequest request) {
    try {
        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Fetch the user's data from both User and UserProfile entities
        User user = userService.findByUsername(username);
        UserRowIng userRowIng = convertToUserRowIng(request); // Convert request to UserRowIng object
        userRowIng.setUser(user); // Set the user for UserRowIng


        userRowIngService.addUserRowIng(userRowIng); // Call service method with the converted object
        return ResponseEntity.ok("UserRowIng added successfully");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
    }
}

//    private UserRowIng convertToUserRowIng(UserRowIngRequest request) {
//        UserRowIng userRowIng = new UserRowIng();
//        userRowIng.setFoodName(request.getFoodName());
//        userRowIng.setFoodCode(request.getFoodCode());
//        userRowIng.setEnergy(request.getEnergy());
//        userRowIng.setFat(request.getFat());
//        userRowIng.setProtein(request.getProtein());
//        userRowIng.setCalcium(request.getCalcium());
//        userRowIng.setMagnesium(request.getMagnesium());
//        userRowIng.setIron(request.getIron());
//        userRowIng.setZinc(request.getZinc());
//        userRowIng.setThiamine(request.getThiamine());
//        userRowIng.setRiboflavin(request.getRiboflavin());
//        userRowIng.setNiacin(request.getNiacin());
//        userRowIng.setVitaminB6(request.getVitaminB6());
//        userRowIng.setTotalFolate(request.getTotalFolate());
//        userRowIng.setVitaminC(request.getVitaminC());
//        userRowIng.setVitaminA(request.getVitaminA());
//
//        Source source = new Source();
//        source.setName(request.getSourceName());
//        source.setYear(request.getSourceYear());
//        source.setJournal(request.getSourceJournal());
//        source.setPage(request.getSourcePage());
//        source.setProductName(request.getSourceProductName());
//
//        userRowIng.setSource(source);
//
//        return userRowIng;
//    }

    private UserRowIng convertToUserRowIng(UserRowIngRequest request) {
        UserRowIng userRowIng = new UserRowIng();

        // Generate a unique foodCode
        String foodCode = generateUniqueFoodCode();
        userRowIng.setFoodCode(foodCode);

//        // Set other properties
        userRowIng.setFoodName(request.getFoodName());
        userRowIng.setCategory(request.getCategory());

        userRowIng.setEnergy(request.getEnergy());
        userRowIng.setProtein(request.getProtein());
        userRowIng.setFat(request.getFat());
        userRowIng.setFiber(request.getFiber());
        userRowIng.setCarbohydrate(request.getCarbohydrate());
        userRowIng.setThiamine(request.getThiamine());
        userRowIng.setRiboflavin(request.getRiboflavin());
        userRowIng.setNiacin(request.getNiacin());
        userRowIng.setVitaminB6(request.getVitaminB6());
        userRowIng.setTotalFolate(request.getTotalFolate());
        userRowIng.setVitaminC(request.getVitaminC());
        userRowIng.setVitaminA(request.getVitaminA());
        userRowIng.setIron(request.getIron());
        userRowIng.setZinc(request.getZinc());
        userRowIng.setSodium(request.getSodium());
        userRowIng.setCalcium(request.getCalcium());
        userRowIng.setMagnesium(request.getMagnesium());





//        // Set other properties
//        userRowIng.setFoodName(request.getFoodName());
//        userRowIng.setEnergy(request.getEnergy() != null ? request.getEnergy() : 0.0);
//        userRowIng.setFat(request.getFat() != null ? request.getFat() : 0.0);
//        userRowIng.setProtein(request.getProtein() != null ? request.getProtein() : 0.0);
//        userRowIng.setCalcium(request.getCalcium() != null ? request.getCalcium() : 0.0);
//        userRowIng.setMagnesium(request.getMagnesium() != null ? request.getMagnesium() : 0.0);
//        userRowIng.setIron(request.getIron() != null ? request.getIron() : 0.0);
//        userRowIng.setZinc(request.getZinc() != null ? request.getZinc() : 0.0);
//        userRowIng.setThiamine(request.getThiamine() != null ? request.getThiamine() : 0.0);
//        userRowIng.setRiboflavin(request.getRiboflavin() != null ? request.getRiboflavin() : 0.0);
//        userRowIng.setNiacin(request.getNiacin() != null ? request.getNiacin() : 0.0);
//        userRowIng.setVitaminB6(request.getVitaminB6() != null ? request.getVitaminB6() : 0.0);
//        userRowIng.setTotalFolate(request.getTotalFolate() != null ? request.getTotalFolate() : 0.0);
//        userRowIng.setVitaminC(request.getVitaminC() != null ? request.getVitaminC() : 0.0);
//        userRowIng.setVitaminA(request.getVitaminA() != null ? request.getVitaminA() : 0.0);


        // Set source
        Source source = new Source();
        source.setName(request.getSourceName());
        source.setYear(request.getSourceYear());
        source.setJournal(request.getSourceJournal());
        source.setPage(request.getSourcePage());
        source.setProductName(request.getSourceProductName());
        source.setUserRowIng(userRowIng);

        userRowIng.setSource(source);

        return userRowIng;
    }

//    private String generateUniqueFoodCode() {
//        // Generate a UUID (Universally Unique Identifier) and return it as foodCode
//        return "USER-" + UUID.randomUUID().toString();
//    }
//private String generateUniqueFoodCode() {
//    // Generate two random alphabets
//    Random random = new Random();
//    char alphabet1 = (char) ('A' + random.nextInt(26));
//    char alphabet2 = (char) ('A' + random.nextInt(26));
//
//    // Generate four random digits
//    int randomDigits = random.nextInt(10000); // Generate a random number between 0 and 9999
//
//    // Format the code
//    return String.format("%c%c%04d", alphabet1, alphabet2, randomDigits);
//}

    private String generateUniqueFoodCode() {
        Random random = new Random();
        char firstAlphabet = (char) ('A' + random.nextInt(26)); // Randomly select first alphabet
        char secondAlphabet = (char) ('A' + random.nextInt(26)); // Randomly select second alphabet
        int randomNumber = random.nextInt(10000); // Randomly generate 4-digit number
        String formattedNumber = String.format("%04d", randomNumber); // Ensure 4-digit format with leading zeros if necessary
        return String.format("%c%c%s", firstAlphabet, secondAlphabet, formattedNumber);
    }
//    private Set<String> generatedCodes = new HashSet<>();
//
//    private String generateUniqueFoodCode() {
//        Random random = new Random();
//        String foodCode;
//        do {
//            char firstAlphabet = (char) ('A' + random.nextInt(26)); // Randomly select first alphabet
//            char secondAlphabet = (char) ('A' + random.nextInt(26)); // Randomly select second alphabet
//            int randomNumber = random.nextInt(10000); // Randomly generate 4-digit number
//            String formattedNumber = String.format("%04d", randomNumber); // Ensure 4-digit format with leading zeros if necessary
//            foodCode = String.format("%c%c%s", firstAlphabet, secondAlphabet, formattedNumber);
//        } while (generatedCodes.contains(foodCode)); // Check if the code is already generated
//        generatedCodes.add(foodCode); // Add the generated code to the set of generated codes
//        return foodCode;
//    }


}
