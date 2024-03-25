package com.example.jwt.entities.FoodToday;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MissingRowFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Change the generation type to IDENTITY
    private Long missingId;

    private Timestamp timestamp;
    private String messingMessage;
}
