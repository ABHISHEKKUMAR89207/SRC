package com.example.jwt.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NinDataDTO {
    private Long id;
    private String name;
    private String typesoffood;
    private Double carbohydrate;
    private Double Total_Fat;
    private Double cholestrol;
    private Double  sodium;
    private Double Total_Dietary_Fibre;
    private Double calcium;
    private Double iron;
    private Double potassium;
    private Double phosphorus;
    private Double magnesium;
    private Double zinc;
    private Double selenium;
    private Double copper;
    private Double manganese;
}