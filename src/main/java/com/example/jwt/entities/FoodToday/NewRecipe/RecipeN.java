package com.example.jwt.entities.FoodToday.NewRecipe;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
//@Table(name = "recipe", indexes = {@Index(name = "recipes_id_index", columnList = "recipes_id")})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeN {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)


    private Long uidRecipes;

    private String itemcode;
    private String itemname;
    private String typeOfPreparation;
    private double totalCookedWtG;
    private double oneServingWtG;
    private String servingMeasure;
    private String servingUnit;



}
