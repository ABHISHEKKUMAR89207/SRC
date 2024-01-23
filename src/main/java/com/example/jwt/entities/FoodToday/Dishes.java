package com.example.jwt.entities.FoodToday;

import com.example.jwt.entities.FoodToday.NewRecipe.Recipen;
import com.example.jwt.entities.FoodToday.Recipe.Recipe;
import com.example.jwt.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@Table(name = "dishes", indexes = {@Index(name = "idx_recipe_id", columnList = "recipe_id")})
public class Dishes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dish_id")
    private Long dishId;

    private String dishName;
    private Double dishQuantity;
    private String mealName;
    private LocalDate date;
    private boolean favourite;
    private Double servingSize;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

//    @OneToMany(mappedBy = "dishes")
//    private List<Ingredients> ingredientList;

    @OneToMany(mappedBy = "dishes", cascade = CascadeType.ALL)
    private List<Ingredients> ingredientList;

    // In Dishes entity

    // In Dishes entity

    @ManyToOne
    @JoinColumn(name = "recipes_id")
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "uidrecipesn")
    private Recipen recipen;











}
