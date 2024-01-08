package com.example.jwt.entities.FoodToday;


import com.example.jwt.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "recipe_data", indexes = {@Index(name = "recipe_id_index", columnList = "recipe_id")})
public class RecipeData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id")
//    @Index(name = "recipe_id_index")
    private Long recipeId;


    private Integer recipe_number;
    private String recipe_name;
    private String ingredients;
    private String code;
    private Double grams;
    private Double total_cooked_quantity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "recipeData", cascade = CascadeType.ALL)
    private List<UserRecipe> userRecipes = new ArrayList<>();
}