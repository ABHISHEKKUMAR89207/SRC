
package com.example.jwt.entities.FoodToday.NewRecipe;

import com.example.jwt.entities.FoodToday.Dishes;
import com.example.jwt.entities.User;
import com.example.jwt.repository.FoodTodayRepository.DishesRepository;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recipess")
public class RecipeControllerN {
    private final RecipeServiceN recipeService;

    @Autowired
    public RecipeControllerN(RecipeServiceN recipeService) {
        this.recipeService = recipeService;
    }


//    @GetMapping("/{itemName}")
//    public ResponseEntity<?> getRecipeAndIngredients(@PathVariable String itemName) {
//        Optional<Recipen> recipeOptional = recipeService.getRecipeByName(itemName);
//
//        if (recipeOptional.isPresent()) {
//            Recipen recipe = recipeOptional.get();
//            List<Ingredientn> ingredients = recipeService.getIngredientsByRecipe(recipe);
//
//            // You can now return the recipe and its ingredients in the response
//            return ResponseEntity.ok().body(new RecipeWithIngredientsResponse(recipe, ingredients));
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//@GetMapping("/{itemName}")
//public ResponseEntity<?> getRecipeAndIngredients(@PathVariable String itemName) {
//    Optional<Recipen> recipeOptional = recipeService.getRecipeByName(itemName);
//
//    if (recipeOptional.isPresent()) {
//        Recipen recipe = recipeOptional.get();
//        List<RecipeWithIngredientsResponse.IngredientResponse> ingredients =
//                recipeService.getIngredientsResponseByRecipe(recipe);
//
//        // You can now return the recipe and its ingredients in the response
//        return ResponseEntity.ok().body(new RecipeWithIngredientsResponse(recipe, ingredients));
//    } else {
//        return ResponseEntity.notFound().build();
//    }
//}


//    @GetMapping("/{itemName}")
//    public ResponseEntity<?> getRecipeAndIngredients(@PathVariable String itemName) {
//        // Assuming you have a method in RecipeServiceN to find the recipe by name
//        Optional<Recipen> recipeOptional = recipeService.getRecipeByName(itemName);
//
//        if (recipeOptional.isPresent()) {
//            Recipen recipe = recipeOptional.get();
//            List<RecipeWithIngredientsResponse.IngredientResponse> ingredients =
//                    recipeService.getIngredientsResponseByRecipe(recipe);
//
//            // You can now return the recipe and its ingredients in the response
//            return ResponseEntity.ok().body(new RecipeWithIngredientsResponse(recipe, ingredients));
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//@GetMapping("/{itemName}")
//public ResponseEntity<?> getRecipeAndIngredients(@PathVariable String itemName) {
//    // Assuming you have a method in RecipeService to find the recipe by name
//    Optional<Recipen> recipeOptional = recipeService.getRecipeByName(itemName);
//
//    if (recipeOptional.isPresent()) {
//        Recipen recipe = recipeOptional.get();
//        List<RecipeWithIngredientsResponse.Ingredientn> ingredients =
//                recipeService.getIngredientsResponseByRecipe(recipe);
//
//        // You can now return the recipe and its ingredients in the response
//        return ResponseEntity.ok().body(new RecipeWithIngredientsResponse(recipe, ingredients));
//    } else {
//        return ResponseEntity.notFound().build();
//    }
//}

    // In RecipeController
//    @GetMapping("/{itemName}")
//    public ResponseEntity<?> getRecipeAndIngredients(@PathVariable String itemName) {
//        Optional<RecipeWithIngredientsResponse> responseOptional =
//                recipeService.getRecipeAndIngredientsByName(itemName);
//
//        if (responseOptional.isPresent()) {
//            RecipeWithIngredientsResponse response = responseOptional.get();
//            return ResponseEntity.ok().body(response);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

//    @GetMapping("/{itemName}")
//    public ResponseEntity<?> getRecipeAndIngredients(@PathVariable String itemName) {
//        Optional<RecipeWithIngredientsResponse> responseOptional = recipeService.getRecipeByName(itemName);
//
//        if (responseOptional.isPresent()) {
//            RecipeWithIngredientsResponse response = responseOptional.get();
//
//            // Fetch ingredients using the corrected method name
//            List<RecipeWithIngredientsResponse.Ingredientn> ingredients =
//                    recipeService.getIngredientsResponseByRecipe(response.getRecipe());
//
//            // Set the ingredients in the response
//            response.setIngredients(ingredients);
//
//            return ResponseEntity.ok().body(response);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }


    @GetMapping("/{itemName}")
    public ResponseEntity<?> getRecipeAndIngredients(@PathVariable String itemName,@RequestHeader("Auth") String tokenHeader) {

        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the username to fetch the userId from your user service
        User user = userService.findByUsername(username);

        Optional<RecipeWithIngredientsResponse> responseOptional = recipeService.getRecipeByName(itemName,user);

        if (responseOptional.isPresent()) {
            RecipeWithIngredientsResponse response = responseOptional.get();
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Autowired
    private RecipeRepositoryN recipeRepositoryN;
//    @GetMapping("/{itemName}")
//    public ResponseEntity<?> getRecipeAndIngredients(@PathVariable String itemName) {
//        Optional<RecipeWithIngredientsResponse> responseOptional = recipeService.getRecipeByName(itemName);
//
//        if (responseOptional.isPresent()) {
//            RecipeWithIngredientsResponse response = responseOptional.get();
//
//            // Save the itemName as a new Dishes entry
//            Dishes newDish = new Dishes();
//            newDish.setDishName(itemName);
//            // Set other necessary fields in the newDish
//
//            // Retrieve Recipen entity by itemName and set uidrecipesn in newDish
//            Optional<Recipen> recipeOptional = recipeRepositoryN.findByItemname(itemName);
//            recipeOptional.ifPresent(recipen -> newDish.setRecipen(recipen));
//
//            saveDish(newDish);
//
//            return ResponseEntity.ok().body(response);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }


//    @GetMapping("/{itemName}")
//    public ResponseEntity<?> getRecipeAndIngredients(@PathVariable String itemName) {
//        Optional<RecipeWithIngredientsResponse> responseOptional = recipeService.getRecipeByName(itemName);
//
//        if (responseOptional.isPresent()) {
//            RecipeWithIngredientsResponse response = responseOptional.get();
//
//            // Save the itemName as a new Dishes entry
//            Dishes newDish = new Dishes();
//            newDish.setDishName(itemName);
//            // Set other necessary fields in the newDish
//
//            // Retrieve Recipen entity by itemName and set uidrecipesn in newDish
//            Optional<Recipen> recipeOptional = recipeRepositoryN.findByItemname(itemName);
//            recipeOptional.ifPresent(recipen -> {
//                newDish.setRecipen(recipen);
//
//                // Print ingredients to console
//                System.out.println("Ingredients for " + itemName + ":");
//                recipen.getIngredients().forEach(ingredientn -> {
//                    System.out.println(ingredientn.getIngredients());
//                });
//            });
//
//            saveDish(newDish);
//
//            return ResponseEntity.ok().body(response);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

//    @Autowired
//    private DishesRepository dishesRepository;
//
//    public void saveDish(Dishes dish) {
//        dishesRepository.save(dish);
//    }

    @Autowired
    private DishesRepository dishesRepository;
    public void saveDish(Dishes dish) {
        dishesRepository.save(dish);
    }
//    @PostMapping("/create")
//    public ResponseEntity<String> createRecipe(@RequestBody RecipeRequest recipeRequest) {
//        try {
//            recipeService.saveRecipeWithIngredients(recipeRequest);
//            return ResponseEntity.ok("Recipe created successfully");
//        } catch (Exception e) {
//            return (ResponseEntity<String>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//@PostMapping("/create")
//public ResponseEntity<?> createRecipe(@RequestBody RecipeRequest recipeRequest) {
//    try {
//        // Save the recipe and get the saved Recipen entity
//        Recipen savedRecipeN = recipeService.saveRecipeWithIngredients(recipeRequest);
//
//        // Extract uid_recipesn from the saved Recipen entity
//        Long uidRecipesn = savedRecipeN.getUidRecipesn();
//
//        System.out.println("uiddd  "+uidRecipesn);
//
//        // You can use uidRecipesn for any further processing
//
//        return ResponseEntity.ok("Recipe created successfully");
//    } catch (Exception e) {
//        return (ResponseEntity<?>) ResponseEntity.status(500);
//    }
//}

//    @PostMapping("/create")
//    public ResponseEntity<Object> createRecipeWithIngredients(@RequestBody RecipeRequest recipeRequest) {
//        try {
//            // Call the service method to save the recipe with ingredients
//            Dishes savedDish = recipeService.saveRecipeWithIngredients(recipeRequest);
//
//            // Return a success response with the saved dish or appropriate message
//            return ResponseEntity.status(HttpStatus.CREATED).body(savedDish);
//        } catch (Exception e) {
//            // Return an error response if an exception occurs during the process
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating recipe");
//        }
//    }

@Autowired
private JwtHelper jwtHelper;
    @Autowired
    private UserService userService;
    @PostMapping("/save-dish-ing")
    public ResponseEntity<String> saveRecipeAndIngredients(@RequestBody RecipeRequest request,@RequestHeader("Auth") String tokenHeader) {
        try {
            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);

            // Use the username to fetch the userId from your user service
            User user = userService.findByUsername(username);

            recipeService.saveRecipeAndIngredients(request, user);

            return ResponseEntity.ok("Recipe and ingredients saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving recipe and ingredients.");
        }
    }

//    @Autowired
//    private RecipeRepositoryN recipeRepositoryN;
//    @GetMapping("/names")
//    public List<String> getAllRecipeNames() {
//        List<Recipen> recipes = recipeRepositoryN.findAll();
//        return recipes.stream()
//                .map(Recipen::getItemname)
//                .collect(Collectors.toList());
//    }

    @GetMapping("/names")
    public ResponseEntity<?> getAllRecipeNames(@RequestHeader("Auth") String tokenHeader) {
        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
        String token = tokenHeader.replace("Bearer ", "");

        // Validate the token and extract the username (email)
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the username to fetch the user from your user service
        User user = userService.findByUsername(username);

        // Validate user existence and authentication
        if (user == null || !jwtHelper.validateToken(token, user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token or user not found.");
        }

        List<Recipen> recipes = recipeRepositoryN.findAll();
        List<String> recipeNames = recipes.stream()
                .map(Recipen::getItemname)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(recipeNames);
    }




}


//package com.example.jwt.entities.FoodToday.NewRecipe;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/recipes")
//public class RecipeControllerN {
//    private final RecipeServiceN recipeService;
//
//    @Autowired
//    public RecipeControllerN(RecipeServiceN recipeService) {
//        this.recipeService = recipeService;
//    }
//
//    @GetMapping
//    public List<Recipen> getAllRecipes() {
//        return recipeService.getAllRecipes();
//    }
//
//    @GetMapping("/{uid}")
//    public Recipen getRecipeById(@PathVariable Long uid) {
//        return recipeService.getRecipeById(uid).orElse(null);
//    }
//
//    @PostMapping
//    public Recipen saveRecipe(@RequestBody Recipen recipe) {
//        return recipeService.saveRecipe(recipe);
//    }
//
//    @DeleteMapping("/{uid}")
//    public void deleteRecipe(@PathVariable Long uid) {
//        recipeService.deleteRecipe(uid);
//    }
//
//    // You can add more endpoints and methods as needed
//}
