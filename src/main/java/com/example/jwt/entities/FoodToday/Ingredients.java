package com.example.jwt.entities.FoodToday;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
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
    private transient List<Double> nutrirnts;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dish_id")
    private Dishes dishes;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private Set<NinData> ninDataList = new HashSet<>();
}
