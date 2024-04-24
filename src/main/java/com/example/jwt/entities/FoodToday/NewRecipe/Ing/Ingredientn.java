package com.example.jwt.entities.FoodToday.NewRecipe.Ing;


import com.example.jwt.entities.FoodToday.NewRecipe.Recipen;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ingredientn {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "new_id")
    private Long id;

    //    @Column(insertable = false, updatable = false)
    private String item_name;

    private String source_recipe;
    private Long uid_ingredients;
    private String ingredients;
    private Double weight;
    private String ifctNvifCode;
    private String category;
    private String sourceIngredients;

    @ManyToOne
    @JoinColumn(name = "uidrecipesn")
    private Recipen recipen;
}