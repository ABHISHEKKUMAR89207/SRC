package com.example.jwt.entities.FoodToday.NewRecipe.Personal;

import com.example.jwt.dtos.FoodTodayDtos.DishDTO;
import com.example.jwt.dtos.FoodTodayDtos.IngredientDTO;
import com.example.jwt.entities.FoodToday.Dishes;
import com.example.jwt.entities.FoodToday.Ingredients;
import com.example.jwt.entities.FoodToday.NewRecipe.RecipeRequest;
import com.example.jwt.entities.User;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/personal")
public class PersonalController {

    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private UserService userService;
    @Autowired
    PersonalRecipe personalRecipe;
    @Autowired
    private PersonalService personalService;

//    @GetMapping("/ddd")
//    public List<PersonalDTO> getAllDishesForUser(@RequestHeader("Auth") String tokenHeader) {
//        String token = tokenHeader.replace("Bearer ", "");
//        String username = jwtHelper.getUsernameFromToken(token);
//        User user = userService.findByUsername(username);
//
//        List<Personal> personalList = personalRecipe.findAllByUserUserId(user.getUserId());
//        return personalList.stream()
//                .map(this::convertToPersonalDTO)
//                .collect(Collectors.toList());
//    }


    @GetMapping("/ddd/{perRecId}")
    public ResponseEntity<PersonalDTO> getPersonalById(@RequestHeader("Auth") String tokenHeader, @PathVariable Long perRecId) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(username);

        Optional<Personal> personalOptional = personalRecipe.findByPerRecIdAndUserUserId(perRecId, user.getUserId());

        if (personalOptional.isPresent()) {
            PersonalDTO personalDTO = convertToPersonalDTO(personalOptional.get());
            return ResponseEntity.ok(personalDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    private PersonalDTO convertToPersonalDTO(Personal personal) {
        return new PersonalDTO(
                personal.getPerRecId(),
                personal.getDate(),
                personal.getItemname(),
                personal.getTotalCookedWtG(),
                personal.getOneServingWtG(),
//                personal.getServingMeasure(),
                personal.getServingUnit(),
                convertToIngredientsDTOList(personal.getIngredientList())
        );
    }

    private List<IngredientDTO> convertToIngredientsDTOList(List<Ingredients> ingredientsList) {
        return ingredientsList.stream()
                .map(ingredient -> new IngredientDTO(ingredient.getIngredientName(), ingredient.getIngredientQuantity()))
                .collect(Collectors.toList());
    }



    @GetMapping("/get-personal-info")
    public List<PersonalRes> getPersonalInfo(@RequestHeader("Auth") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(username);

        List<Personal> personalList = personalRecipe.findAllByUserUserId(user.getUserId());

        // Convert Personal entities to PersonalDTOs
        List<PersonalRes> personalDTOList = personalList.stream()
                .map(personal -> new PersonalRes(personal.getPerRecId(), personal.getItemname()))
                .collect(Collectors.toList());

        return personalDTOList;
    }
//    // Get all the dishes without meal name on a specific date
//    public List<DishDTO> getAllDishesForUser(User user, String dishes) {
//        List<Dishes> matchingDishes = personalRecipe.findByUserUserIdAndPersonalDish(user.getUserId(), dishes);
//        List<DishDTO> result = new ArrayList<>();
//        for (Dishes dish : matchingDishes) {
//            DishDTO dishDTO = new DishDTO();
//            dishDTO.setDishId(dish.getDishId());
//            dishDTO.setDishName(dish.getDishName());
//            dishDTO.setDishQuantity(dish.getDishQuantity());
//            dishDTO.setMealName(dish.getMealName());
//            dishDTO.setServingSize(dish.getServingSize());
//            result.add(dishDTO);
//        }
//        return result;
//    }

//
//    @GetMapping("/personal-dishes")
//    public List<PersonalDishDTO> getAllPersonalDishesForUser(@RequestHeader("Auth") String tokenHeader, @RequestParam String personalDish) {
//        String token = tokenHeader.replace("Bearer ", "");
//        String username = jwtHelper.getUsernameFromToken(token);
//        User user = userService.findByUsername(username);
//
//        return personalRecipeService.getAllPersonalDishesForUser(user, personalDish);
//    }
@PostMapping("/save-dish-personal")
public ResponseEntity<String> saveRecipeAndIngredientsp(@RequestBody PersonalRequest request, @RequestHeader("Auth") String tokenHeader) {
    try {
        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the username to fetch the userId from your user service
        User user = userService.findByUsername(username);

        personalService.saveRecipeAndIngredientsp(request, user);

        return ResponseEntity.ok("Recipe and ingredients saved successfully.");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving recipe and ingredients.");
    }
}
}
