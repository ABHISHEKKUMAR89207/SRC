
package com.example.jwt.entities.FoodToday.NewRecipe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeRepositoryN extends JpaRepository<Recipen, Long> {
    // You can add custom query methods here if needed
    Optional<Recipen> findByItemname(String itemName);

}

//package com.example.jwt.entities.FoodToday.NewRecipe;
//
//import com.example.jwt.entities.FoodToday.Recipe.Recipe;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface RecipeRepositoryN extends JpaRepository<Recipen, Long> {
//    // You can add custom query methods here if needed
//}

