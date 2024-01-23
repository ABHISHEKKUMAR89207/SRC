
package com.example.jwt.entities.FoodToday.NewRecipe.Ing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepositoryN extends JpaRepository<Ingredientn, Long> {
    // You can add custom query methods here if needed
//    List<Ingredientn> findByUidRecipes(Long uidRecipes);
//    List<Ingredientn> findByUid_recipes(Long uidRecipes);
    @Query("SELECT i FROM Ingredientn i WHERE i.recipen.uidrecipesn = :uidRecipes")
    List<Ingredientn> findByUidRecipes(Long uidRecipes);
}

//package com.example.jwt.entities.FoodToday.NewRecipe.Ing;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface IngredientRepositoryN extends JpaRepository<Ingredientn, Long> {
//    // You can add custom query methods here if needed
//}

