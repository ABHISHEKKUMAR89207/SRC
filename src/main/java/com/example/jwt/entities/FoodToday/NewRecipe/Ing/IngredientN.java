//package com.example.jwt.entities.FoodToday.NewRecipe.Ing;
//
//
//import com.example.jwt.entities.FoodToday.NewRecipe.RecipeN;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Entity
////@Table(name = "recipe", indexes = {@Index(name = "recipes_id_index", columnList = "recipes_id")})
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//public class IngredientN {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(insertable = false, updatable = false) // Add this annotation to avoid column duplication
//    private String item_name;
//    private String source_recipe;
//    private Long uid_ingredients;
//    private String ingredients;
//    private Double weight;
//    private String ifctNvifCode;
//    private String sourceIngredients;
//
//    @ManyToOne
//    @JoinColumn(name = "uid_recipes") // Assuming uid_recipes is a foreign key in the IngredientN table
//    private RecipeN recipen;
//    // Constructors, getters, setters, and other methods as needed
//}
