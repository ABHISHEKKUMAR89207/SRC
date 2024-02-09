package com.example.jwt.entities.FoodToday;

import com.example.jwt.entities.FoodToday.NewRecipe.Personal.Personal;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Ingredients {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ingredientId;
    private String ingredientName;
    private Double ingredientQuantity;
    private String foodCode;

//    private transient List<Double> nutrirnts;

    @ElementCollection
    @CollectionTable(name = "ingredient_nutrients", joinColumns = @JoinColumn(name = "ingredient_id"))
    @MapKeyColumn(name = "nutrient_name")
    @Column(name = "nutrient_value")
    private Map<String, Double> nutrients;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dish_id")
    private Dishes dishes;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private Set<NinData> ninDataList = new HashSet<>();


    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "dish_id")
    private Personal personal;
}
