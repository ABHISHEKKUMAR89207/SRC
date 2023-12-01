package com.example.jwt.entities.water;


import com.example.jwt.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WaterGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long WaterGoalId;


    private Double waterGoal;

//    private Double waterGoalInLiters;


    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

//    @OneToMany(mappedBy = "waterGoal", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<WaterEntity> waterEntities = new ArrayList<>();

//    @OneToMany(mappedBy = "WaterGoal", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<WaterEntity> waterEntities = new ArrayList<>();

}
