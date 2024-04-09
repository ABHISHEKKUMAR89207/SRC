package com.example.jwt.entities.FoodToday.UserRowIngredient;

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
public class Source {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long RowSourceId;

    private String name;
    private int year;
    private String journal;
    private String page;
    private String productName;

    @OneToOne
    @JoinColumn(name = "user_row_ing_id")
    private UserRowIng userRowIng;

    // Constructors, getters, and setters
}
