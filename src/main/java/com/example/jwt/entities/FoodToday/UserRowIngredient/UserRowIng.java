package com.example.jwt.entities.FoodToday.UserRowIngredient;


import com.example.jwt.entities.User;
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

    private double energy;
    private double protein;
    private double fat;
    private double fiber;
    private double carbohydrate;
    private double thiamine; // Thiamine (B1)
    private double riboflavin; // Riboflavin (B2)
    private double niacin; // Niacin (B3)
    private double vitaminB6; // Vitamin B6
    private double totalFolate; // Total folate
    private double vitaminC; // Vitamin C
    private double vitaminA; // Vitamin A
    private double iron;
    private double zinc;
    private double sodium;
    private double calcium;
    private double magnesium;








    //    private String source;
    @OneToOne(mappedBy = "userRowIng", cascade = CascadeType.ALL)
    private Source source;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
