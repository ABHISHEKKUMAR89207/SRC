package com.example.jwt.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class waterEntity {
    @Id
    @GeneratedValue
    private int id;
    private Double waterGoal;
    private int cupCapacity;
    private int noOfCups;
    private Double waterIntake;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

}

