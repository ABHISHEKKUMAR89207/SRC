
package com.example.jwt.entities.FoodToday.NewRecipe;

import com.example.jwt.entities.FoodToday.Dishes;
import com.example.jwt.entities.FoodToday.Ingredients;
import com.example.jwt.entities.FoodToday.NewRecipe.Ing.IngredientRepositoryN;
import com.example.jwt.entities.FoodToday.NewRecipe.Ing.Ingredientn;
import com.example.jwt.entities.User;
import com.example.jwt.repository.FoodTodayRepository.DishesRepository;
import com.example.jwt.repository.FoodTodayRepository.IngredientsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeServiceN {
    private final RecipeRepositoryN recipeRepositoryN;
    private final IngredientRepositoryN ingredientRepositoryN;

    @Autowired
    public RecipeServiceN(RecipeRepositoryN recipeRepositoryN, IngredientRepositoryN ingredientRepositoryN) {
        this.recipeRepositoryN = recipeRepositoryN;
        this.ingredientRepositoryN = ingredientRepositoryN;
    }

//    public Optional<RecipeWithIngredientsResponse> getRecipeByName(String itemname) {
//        Optional<Recipen> recipeOptional = recipeRepositoryN.findByItemname(itemname);
//
//        return recipeOptional.map(recipeN -> {
//            RecipeWithIngredientsResponse response = new RecipeWithIngredientsResponse();
//            response.setRecipeName(recipeN.getItemname());
//            response.setIngredients(recipeN.getIngredients());
//            // You can customize this mapping based on your requirements
//
//            return response;
//        });
//    }

//    @Autowired
//    private RecipeRepositoryN recipeRepositoryN;
//    public List<Ingredientn> getIngredientsByRecipeName(String recipeName) {
//        Recipen recipe = recipeRepositoryN.findByItemname(recipeName);
//        if (recipe != null) {
//            return recipe.getIngredients();
//        }
//        return null;
//    }


//        public List<RecipeWithIngredientsResponse.IngredientResponse> getIngredientsResponseByRecipe(Recipen recipeN) {
//            List<Ingredientn> ingredients = recipeN.getIngredients();
//            return ingredients.stream()
//                    .map(ingredient -> new RecipeWithIngredientsResponse.IngredientResponse(
//                            ingredient.getItem_name(),
//                            ingredient.getUid_ingredients()))
//                    .collect(Collectors.toList());
//        }


//    @Autowired
//    private IngredientRepositoryN ingredientRepositoryN;

    // In RecipeService
//    public List<RecipeWithIngredientsResponse.Ingredientn> getIngredientsResponseByRecipe(Recipen recipeN) {
//        Long uidRecipesn = recipeN.getUidRecipesn();
//
//        // Fetch the ingredients directly from the database using uidRecipesn
//        List<Ingredientn> ingredients = ingredientRepositoryN.findByUidRecipes(uidRecipesn);
//
//        // Map ingredients to RecipeWithIngredientsResponse.Ingredientn
//        return ingredients.stream()
//                .map(ingredient -> new RecipeWithIngredientsResponse.Ingredientn(
//                        ingredient.getItem_name(),
//                        ingredient.getUid_ingredients(),
//                        ingredient.getIngredients())) // Include the 'ingredients' property
//                .collect(Collectors.toList());
//    }
//    public List<RecipeWithIngredientsResponse.Ingredientn> getIngredientsResponseByRecipe(Recipen recipeN) {
//        Long uidRecipesn = recipeN.getUidRecipesn();
//
//        // Fetch the ingredients directly from the database using uidRecipesn
//        List<Ingredientn> ingredients = ingredientRepositoryN.findByUidRecipes(uidRecipesn);
//
//        // Map ingredients to RecipeWithIngredientsResponse.Ingredientn
//        return ingredients.stream()
//                .map(ingredient -> new RecipeWithIngredientsResponse.Ingredientn(
//                        ingredient.getItem_name(),
//                        ingredient.getUid_ingredients(),
//                        ingredient.getIngredients()))
//                .collect(Collectors.toList());
//    }
//
//
////    public Optional<RecipeWithIngredientsResponse> getRecipeByName(String itemName) {
////        Optional<Recipen> recipeOptional = recipeRepositoryN.findByItemname(itemName);
////
////        return recipeOptional.map(recipe -> {
////            List<RecipeWithIngredientsResponse.Ingredientn> ingredients =
////                    getIngredientsResponseByRecipee(recipe);
////            return new RecipeWithIngredientsResponse(recipe, ingredients);
////        });
////    }
//
//    public List<Ingredientn> getIngredientsByRecipe(Recipen recipeN) {
//        return recipeN.getIngredients();
//    }



//    public Optional<Recipen> getRecipeByName(String itemName) {
//        return recipeRepository.findByItemname(itemName);
//    }

    // In RecipeService

//    public Optional<RecipeWithIngredientsResponse> getRecipeByName(String itemName) {
//        Optional<Recipen> recipeOptional = recipeRepositoryN.findByItemname(itemName);
//
//        return recipeOptional.map(recipe -> {
//            Long uidRecipesn = recipe.getUidRecipesn();
//            List<Ingredientn> ingredients = ingredientRepositoryN.findByUidRecipes(uidRecipesn);
//
//            List<RecipeWithIngredientsResponse.Ingredientn> ingredientResponses = ingredients.stream()
//                    .map(ingredient -> new RecipeWithIngredientsResponse.Ingredientn(
//                            ingredient.getItem_name(),
//                            ingredient.getUid_ingredients(),
//                            ingredient.getIngredients()))
//                    .collect(Collectors.toList());
//
//            return new RecipeWithIngredientsResponse(recipe, ingredientResponses);
//        });
//    }

//    public Optional<RecipeWithIngredientsResponse> getRecipeByName(String itemName) {
//        Optional<Recipen> recipeOptional = recipeRepositoryN.findByItemname(itemName);
//
//        return recipeOptional.map(recipe -> {
//            Long uidRecipesn = recipe.getUidRecipesn();
//            List<Ingredientn> ingredients = ingredientRepositoryN.findByUidRecipes(uidRecipesn);
//
//            List<RecipeWithIngredientsResponse.Ingredientn> ingredientResponses = ingredients.stream()
//                    .map(ingredient -> new RecipeWithIngredientsResponse.Ingredientn(
//                            ingredient.getItem_name(),
//                            ingredient.getUid_ingredients(),
//                            ingredient.getIngredients(),
//                            ingredient.getWeight(), // Add other fields if needed
//                            ingredient.getIfctNvifCode(),
//                            ingredient.getSourceIngredients()))
//                    .collect(Collectors.toList());
//
//            return new RecipeWithIngredientsResponse(recipe, ingredientResponses);
//        });
//    }
//public List<Ingredientn> getIngredientsByItemname(String itemname) {
//    Optional<Recipen> optionalRecipe = recipeRepositoryN.findByItemname(itemname);
//
//    if (optionalRecipe.isPresent()) {
//        Recipen recipe = optionalRecipe.get();
//        return recipe.getIngredients();
//    } else {
//        // Handle case when recipe with the given itemname is not found
//        return null; // or throw an exception
//    }
//}
//public Optional<RecipeWithIngredientsResponse> getRecipeByName(String itemname) {
//    Optional<Recipen> recipeOptional = recipeRepositoryN.findByItemname(itemname);
//
//    return recipeOptional.map(recipeN -> {
//        RecipeWithIngredientsResponse response = new RecipeWithIngredientsResponse();
//        response.setRecipeName(recipeN.getItemname());
//
//        // Extract source_recipe from each ingredient and add to the response
//        List<String> sourceRecipes = recipeN.getIngredients().stream()
//                .map(Ingredientn::getSource_recipe)
//                .collect(Collectors.toList());
//        response.setSourceRecipes(sourceRecipes);
//
//        return response;
//    });
//}
//public Optional<RecipeWithIngredientsResponse> getRecipeByName(String itemname) {
//    Optional<Recipen> recipeOptional = recipeRepositoryN.findByItemname(itemname);
//
//    return recipeOptional.map(recipeN -> {
//        RecipeWithIngredientsResponse response = new RecipeWithIngredientsResponse();
//        response.setRecipeName(recipeN.getItemname());
//
//        // Extract source_recipe from each ingredient and add to the response
//        List<String> sourceRecipes = recipeN.getIngredients().stream()
//                .map(Ingredientn::getIngredients)
//                .collect(Collectors.toList());
//        response.setSourceRecipes(sourceRecipes);
//
//        return response;
//    });
//}


//    public Optional<RecipeWithIngredientsResponse> getRecipeByName(String itemname,User user) {
//        Optional<Recipen> recipeOptional = recipeRepositoryN.findByItemname(itemname);
//
//        return recipeOptional.map(recipeN -> {
//            RecipeWithIngredientsResponse response = new RecipeWithIngredientsResponse();
//            response.setRecipeId(recipeN.getUidrecipesn());
//            response.setRecipeName(recipeN.getItemname());
//
//            double servingMeasure = Double.parseDouble(recipeN.getServingMeasure());
//            double result = recipeN.getOneServingWtG() / servingMeasure;
//
//
////            recipeN.getOneServingWtG()/recipeN.getServingMeasure();
//
//            // Map source_recipe to IngredientResponse
//            List<IngredientResponse> ingredients = recipeN.getIngredients().stream()
//                    .map(this::mapToIngredientResponse)
//                    .collect(Collectors.toList());
//
//            response.setIngredients(ingredients);
//
//            // Extract source_recipe from each ingredient and add to the response
//            List<String> sourceRecipes = ingredients.stream()
//                    .map(IngredientResponse::getIngredientName)
//                    .collect(Collectors.toList());
////            response.setSourceRecipes(sourceRecipes);
//
//            return response;
//        });
//    }


    public Optional<RecipeWithIngredientsResponse> getRecipeByName(String itemname, User user) {
        Optional<Recipen> recipeOptional = recipeRepositoryN.findByItemname(itemname);

        return recipeOptional.map(recipeN -> {
            RecipeWithIngredientsResponse response = new RecipeWithIngredientsResponse();
            response.setRecipeId(recipeN.getUidrecipesn());
            response.setRecipeName(recipeN.getItemname());

            // Calculate the result
            double servingMeasure = Double.parseDouble(recipeN.getServingMeasure());
            double result = recipeN.getOneServingWtG() / servingMeasure;

            // Set the result in the response
            response.setValueForOneUnit(result);
            response.setUnit(recipeN.getServingUnit());
            response.setTotalCookedQuantity(recipeN.getTotalCookedWtG());

            // Map source_recipe to IngredientResponse
            List<IngredientResponse> ingredients = recipeN.getIngredients().stream()
                    .map(this::mapToIngredientResponse)
                    .collect(Collectors.toList());

            response.setIngredients(ingredients);

            // Extract source_recipe from each ingredient and add to the response
            List<String> sourceRecipes = ingredients.stream()
                    .map(IngredientResponse::getIngredientName)
                    .collect(Collectors.toList());

            // Set the source recipes in the response if needed
            // response.setSourceRecipes(sourceRecipes);

            return response;
        });
    }


    private IngredientResponse mapToIngredientResponse(Ingredientn ingredient) {
        IngredientResponse response = new IngredientResponse();
        response.setIngredientName(ingredient.getIngredients());
        response.setWeight(ingredient.getWeight());
        // Set other necessary fields in IngredientResponse
        return response;
    }


//    public Optional<RecipeWithIngredientsResponse> getRecipeByName(String itemname) {
//        Optional<Recipen> recipeOptional = recipeRepositoryN.findByItemname(itemname);
//
//        return recipeOptional.map(recipeN -> {
//            RecipeWithIngredientsResponse response = new RecipeWithIngredientsResponse();
//            response.setRecipeName(recipeN.getItemname());
//
//            List<IngredientResponse> ingredients = recipeN.getIngredients().stream()
//                    .map(this::mapToIngredientResponse)
//                    .collect(Collectors.toList());
//
//            response.setIngredients(ingredients);
//
//            return response;
//        });
//    }
//
//    private IngredientResponse mapToIngredientResponse(Ingredients ingredient) {
//        IngredientResponse response = new IngredientResponse();
//        response.setIngredientName(ingredient.getIngredientName());
//        response.setIngredientQuantity(ingredient.getIngredientQuantity());
//        response.setNutrients(ingredient.getNutrients());
//        return response;
//    }
//



    @Autowired
    private DishesRepository dishRepository;

    @Autowired
    private IngredientsRepository ingredientsRepository;



//    public void saveRecipeWithIngredients(RecipeRequest recipeRequest) {
//        // Create a new Recipen entity
//        Recipen recipeN = new Recipen();
////        recipeN.setItemname(recipeRequest.getRecipeName());
//        // Set other properties as needed
//
//        // Save the recipeN entity
////        Recipen savedRecipeN = recipeRepositoryN.save(recipeN);
//
//        // Create a new Dish entity
//        Dishes dish = new Dishes();
//        dish.setDishName(recipeRequest.getRecipeName());
//        dish.setDishQuantity(recipeRequest.getRecipeQuantity());
//        dish.setMealName(recipeRequest.getMealtype());
//        dish.setDate(LocalDate.now());  // Set the current date or as needed
//        dish.setRecipen(recipeN);  // Set the recipe association
//
//        // Save the dish entity
//        Dishes savedDish = dishRepository.save(dish);
//
//        // Retrieve ingredients from the saved Recipen entity
//        List<Ingredientn> ingredients = recipeN.getIngredients();
//
//        // Save each ingredient in the Ingredients table
//        for (Ingredientn ingredient : ingredients) {
//            Ingredients savedIngredient = new Ingredients();
//            savedIngredient.setIngredientName(ingredient.getItem_name());
//            savedIngredient.setIngredientQuantity(ingredient.getWeight());
//            // Set other properties as needed
//            savedIngredient.setDishes(savedDish);  // Set the dish association
//            ingredientsRepository.save(savedIngredient);
//        }
//    }

//    public Recipen saveRecipeWithIngredients(RecipeRequest recipeRequest) {
//        // Create a new Recipen entity
//        Recipen recipeN = new Recipen();
//
//        // Create a new Dish entity
//        Dishes dish = new Dishes();
//        dish.setDishName(recipeRequest.getRecipeName());
//        dish.setDishQuantity(recipeRequest.getRecipeQuantity());
//        dish.setMealName(recipeRequest.getMealtype());
//        dish.setDate(LocalDate.now());
//        dish.setRecipen(recipeN);
//
//        // Save the dish entity
//        Dishes savedDish = dishRepository.save(dish);
//
//        // Retrieve ingredients from the saved Recipen entity
//        List<Ingredientn> ingredients = recipeN.getIngredients();
//
//        // Save each ingredient in the Ingredients table
//        for (Ingredientn ingredient : ingredients) {
//            Ingredients savedIngredient = new Ingredients();
//            savedIngredient.setIngredientName(ingredient.getItem_name());
//            savedIngredient.setIngredientQuantity(ingredient.getWeight());
//            // Set other properties as needed
//            savedIngredient.setDishes(savedDish);
//            ingredientsRepository.save(savedIngredient);
//        }
//
//        // Return the saved Recipen entity
//        return recipeN;
//    }


//    public Dishes saveRecipeWithIngredients(RecipeRequest recipeRequest) {
//        // Create a new Dishes entity
//        Dishes dish = new Dishes();
//        dish.setDishName(recipeRequest.getRecipeName());
////        dish.setDishQuantity(recipeRequest.getRecipeQuantity());
////        dish.setMealName(recipeRequest.getMealtype());
//        dish.setDate(LocalDate.now());
//
//        // Save the dish entity first to obtain dishId
//        Dishes savedDish = dishRepository.save(dish);
//
//        // Create a new Recipen entity
//        Recipen recipeN = new Recipen();
//        // Set other properties as needed
//
//        // Retrieve ingredients from the saved Recipen entity
//        List<Ingredientn> ingredients = recipeN.getIngredients();
//
//        // Save each ingredient in the Ingredients table
//        for (Ingredientn ingredient : ingredients) {
//            Ingredients savedIngredient = new Ingredients();
//            savedIngredient.setIngredientName(ingredient.getItem_name());
////            savedIngredient.setIngredientQuantity(ingredient.getWeight());
//            // Set other properties as needed
////            savedIngredient.setDishes(savedDish);  // Set the dish association
//            ingredientsRepository.save(savedIngredient);
//        }
//
//        // Set the recipeN association in the saved dish
//        savedDish.setRecipen(recipeN);
//        // Save the dish entity after setting the association
//        dishRepository.save(savedDish);
//
//        // Return the saved Dishes entity
//        return savedDish;
//    }

    @Autowired
    private DishesRepository dishesRepository;
//    @Autowired
//    private IngredientsRepository ingredientsRepository;
//    @Transactional
//    public void saveRecipeAndIngredients(RecipeRequest request) {
//        Dishes newDish = new Dishes();
//        newDish.setDishName(request.getRecipeName());
//        // Set other necessary fields in the newDish
//        newDish.setRecipen(new Recipen(request.getRecipeId())); // assuming Recipen has a constructor that takes recipeId
//
//        List<Ingredients> ingredients = request.getIngredients().stream()
//                .map(ingredientRequest -> {
//                    Ingredients ingredient = new Ingredients();
//                    ingredient.setIngredientName(ingredientRequest.getIngredientName());
//                    ingredient.setIngredientQuantity(ingredientRequest.getIngredientQuantity());
//                    // Set other necessary fields in the ingredient
//                    ingredient.setDishes(newDish);
//                    return ingredient;
//                })
//                .collect(Collectors.toList());
//
//        newDish.setIngredientList(ingredients);
//
//        dishesRepository.save(newDish);
//    }
@Transactional
public void saveRecipeAndIngredients(RecipeRequest request, User user) {
    Dishes newDish = new Dishes();
    newDish.setDishName(request.getRecipeName());
    newDish.setDishQuantity(request.getDishQuantity());
    newDish.setMealName(request.getMealType());
    newDish.setServingSize(request.getServingSize());
    // Set other necessary fields in the newDish

    newDish.setUser(user);


    // Set creation date to the current date
    newDish.setDate(LocalDate.now());

    Recipen recipen = new Recipen();
    recipen.setUidrecipesn(request.getRecipeId());
    newDish.setRecipen(recipen);

    List<Ingredients> ingredients = request.getIngredients().stream()
            .map(ingredientRequest -> {
                Ingredients ingredient = new Ingredients();
                ingredient.setIngredientName(ingredientRequest.getIngredientName());
                ingredient.setIngredientQuantity(ingredientRequest.getWeight());
                // Set other necessary fields in the ingredient
                ingredient.setDishes(newDish); // Set the reference to Dishes
                return ingredient;
            })
            .collect(Collectors.toList());


    newDish.setIngredientList(ingredients);
//    ingredientsRepository.save(newDish)

    dishesRepository.save(newDish);
}


}

//package com.example.jwt.entities.FoodToday.NewRecipe;
//
//import com.example.jwt.entities.FoodToday.Recipe.Recipe;
//import com.example.jwt.entities.FoodToday.Recipe.RecipeRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class RecipeServiceN {
//    private final RecipeRepositoryN recipeRepository;
//
//    @Autowired
//    public RecipeServiceN(RecipeRepositoryN recipeRepository) {
//        this.recipeRepository = recipeRepository;
//    }
//
//    public List<Recipen> getAllRecipes() {
//        return recipeRepository.findAll();
//    }
//
//    public Optional<Recipen> getRecipeById(Long uid) {
//        return recipeRepository.findById(uid);
//    }
//
//    public Recipen saveRecipe(Recipen recipe) {
//        return recipeRepository.save(recipe);
//    }
//
//    public void deleteRecipe(Long uid) {
//        recipeRepository.deleteById(uid);
//    }
//
//    // You can add more business logic methods here if needed
//}

