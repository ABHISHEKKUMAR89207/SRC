package com.example.jwt.entities.weight;

import com.example.jwt.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WeightManager {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long weightId;

    private Double weight;

//    private String localTime;
    private LocalDate localDate;


    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;


}
