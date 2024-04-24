package com.example.jwt.entities.FoodToday.UserRowIngredient;


import com.example.jwt.entities.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserRowIng {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userRowId;

    private String foodName;
    private String foodCode;
    private String category;

    private double energy;
    private double protein;
    private double fat;
    private double fiber;
    private double carbohydrate;
    private double thiamine;
    private double riboflavin;
    private double niacin;
    private double vitaminB6;
    private double totalFolate;
    private double vitaminC;
    private double vitaminA;
    private double iron;
    private double zinc;
    private double sodium;
    private double calcium;
    private double magnesium;







    @OneToOne(mappedBy = "userRowIng", cascade = CascadeType.ALL)
    private Source source;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private User user;
}
