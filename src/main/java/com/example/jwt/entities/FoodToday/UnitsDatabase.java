package com.example.jwt.entities.FoodToday;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "units database", indexes = {@Index(name = "idx_food_unit", columnList = "food_unit")})
public class UnitsDatabase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int si_unit_id;
//    @Column(name="food")
    private String food_unit;

    private String SI_Unit;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    @JsonIgnore
//    private User user;
}