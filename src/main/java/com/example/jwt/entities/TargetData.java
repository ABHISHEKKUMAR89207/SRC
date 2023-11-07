package com.example.jwt.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class TargetData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double targetCalories;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double targetProteins;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double targetCarbs;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double targetFat;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double targetFibers;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
//    @JsonBackReference
    @JsonIgnore
    private User user;

    // Constructors, getters, and setters
}
