package com.example.jwt.entities.water;

import com.example.jwt.entities.User;
import com.example.jwt.entities.water.WaterGoal;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
//@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "localDate"})})

public class WaterEntity {
    @Id
    @GeneratedValue
    private Long waterId;

    private Double cupCapacity; // Update the type to Double
    private int noOfCups;
    private Double waterIntake;

    private LocalDate localDate = LocalDate.now();


//    @ManyToOne
//    @JoinColumn(name = "waterGoal_id")
//    private WaterGoal waterGoal;


//    @OneToOne
//    @JsonIgnore
//    @JoinColumn(name = "user_id")
//    private User user;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


//    @ManyToOne
//    @JoinColumn(name = "waterGoal_id")
//    private WaterGoal waterGoal;


}

