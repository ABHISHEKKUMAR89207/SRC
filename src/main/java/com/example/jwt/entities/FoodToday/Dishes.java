package com.example.jwt.entities.FoodToday;

import com.example.jwt.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Dishes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dish_id")
    private Long dishId;

    private String dishName;
    private Double dishQuantity;
    private String mealName;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id")  // Use the actual field name defined in the User entity
    private User user;

//    @OneToMany
//    @Column(name = "ingrdient_Id")
//    private List<Ingredients> ingredient;

//    @OneToMany
//    @Column(name = "ingrdient_Id")
//    private Set<Ingredients> ingredient;

    @OneToMany(mappedBy = "dishes")
    private List<Ingredients> ingredientList;

}
