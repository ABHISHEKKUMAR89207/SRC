package com.example.jwt.entities.FoodToday.Recipe;

import com.example.jwt.entities.FoodToday.Dishes;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "recipe", indexes = {@Index(name = "recipes_id_index", columnList = "recipes_id")})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "recipes_id")
        private Long recipesId;


        private String recipe_name;

        private double protein;
        private double total_fat;
        private double total_dietary_fibre;
        private double carbohydrate;
        private double energy_joules; //energy in kcal
        private double Thiamine; //B1 (mg)

        private double riboflavinB2; //B2 (mg)
        private double Niacin; //B3 (mg)

        private double vitaminB6;
        private double FolateB9; //B9 (µg)
    //    private double total_folateb9;
        private double VitaminC; //C (mg)
        private double vitaminA;
        private double iron;
        private double zinc;
        private double sodium;
        private double calcium;
        private double Magnesium; //(mg)




       //older
    //    private double total_ascorbic_acid;
    //    private double total_carotenoids;
    //    private double ergocalciferolD2;
    //    private double cholecalciferolD3;
    //    private double vitamin25HydroxyD3;
    //    private double potassium;
    //    private double total_saturated_fatty_acids;


        @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
        private List<Dishes> recipes = new ArrayList<>();

}

