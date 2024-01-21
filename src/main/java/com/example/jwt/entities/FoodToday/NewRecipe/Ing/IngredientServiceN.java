//package com.example.jwt.entities.FoodToday.NewRecipe.Ing;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class IngredientServiceN {
//    private final IngredientRepositoryN ingredientRepository;
//
//    @Autowired
//    public IngredientServiceN(IngredientRepositoryN ingredientRepository) {
//        this.ingredientRepository = ingredientRepository;
//    }
//
//    public List<IngredientN> getAllIngredients() {
//        return ingredientRepository.findAll();
//    }
//
//    public Optional<IngredientN> getIngredientById(Long uid) {
//        return ingredientRepository.findById(uid);
//    }
//
//    public IngredientN saveIngredient(IngredientN ingredient) {
//        return ingredientRepository.save(ingredient);
//    }
//
//    public void deleteIngredient(Long uid) {
//        ingredientRepository.deleteById(uid);
//    }
//
//    // You can add more business logic methods here if needed
//}
