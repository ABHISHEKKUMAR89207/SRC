package com.example.jwt.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import lombok.Getter;
import lombok.Setter;

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

    // Add the missing nutrient properties with their getter methods
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double targetMagnesium;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double targetZinc;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double targetIron;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double targetCalcium;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double targetThiamine_B1;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double targetRetinol_Vit_A;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double targetRiboflavin_B2;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double targetNiacin_B3;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double targetFolates_B9;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
