package com.example.jwt.entities.FoodToday.NewRecipe;


import com.example.jwt.entities.FoodToday.Dishes;
import com.example.jwt.entities.FoodToday.NewRecipe.Ing.Ingredientn;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recipen", indexes = {@Index(name = "uidrecipesn_index", columnList = "uidrecipesn")})
//public class Recipen {
//    @Id
public class Recipen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "uidrecipesn")
    private Long uidrecipesn;


    private String itemcode;
    private String itemname;
    private String typeOfPreparation;
    private double totalCookedWtG;
    private double oneServingWtG;
    private String servingMeasure;
    private String servingUnit;

    //    @OneToMany(mappedBy = "recipen", cascade = CascadeType.ALL)
//    private List<Ingredientn> ingredients;
//
//    @OneToMany(mappedBy = "recipen", cascade = CascadeType.ALL)
//    private List<Dishes> recipes = new ArrayList<>();
    @OneToMany(mappedBy = "recipen", cascade = CascadeType.ALL)
    private List<Ingredientn> ingredients = new ArrayList<>();

    @OneToMany(mappedBy = "recipen", cascade = CascadeType.ALL)
    private List<Dishes> recipes = new ArrayList<>();

    // Constructor with a parameter
    public Recipen(Long uidrecipesn) {
        this.uidrecipesn = uidrecipesn;
    }
}