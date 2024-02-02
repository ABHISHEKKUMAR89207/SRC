package com.example.jwt.entities.FoodToday.NewRecipe.Personal;


import com.example.jwt.entities.FoodToday.Dishes;
import com.example.jwt.entities.FoodToday.Ingredients;
import com.example.jwt.entities.FoodToday.NewRecipe.Ing.Ingredientn;
import com.example.jwt.entities.FoodToday.NewRecipe.Recipen;
import com.example.jwt.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "personal", indexes = {@Index(name = "personal_id_index", columnList = "personal_id")})
//public class Recipen {
//    @Id
public class Personal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "personal_id")
    private Long perRecId;

    private LocalDate date;

private Double oneUnitSize;
    //    private String itemcode;
    private String itemname;
    //    private String typeOfPreparation;
    private double totalCookedWtG;
    private double oneServingWtG;
    private Double servingMeasure;
    private String servingUnit;

    //    @OneToMany(mappedBy = "recipen", cascade = CascadeType.ALL)
//    private List<Ingredientn> ingredients;
//
//    @OneToMany(mappedBy = "recipen", cascade = CascadeType.ALL)
//    private List<Dishes> recipes = new ArrayList<>();
//    @OneToMany(mappedBy = "personal", cascade = CascadeType.ALL)
//    private List<Ingredientn> ingredients = new ArrayList<>();
//

//    @OneToMany(mappedBy = "personal", cascade = CascadeType.ALL)
//    private List<Dishes> recipes = new ArrayList<>();

    // Constructor with a parameter
//    public Personal(Long uidrecipesn) {
//        this.uidrecipesn = uidrecipesn;
//    }
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "personal_idd")
    private Recipen recipen;

    @OneToMany(mappedBy = "personal", cascade = CascadeType.ALL)
    private List<Ingredients> ingredientList;
}