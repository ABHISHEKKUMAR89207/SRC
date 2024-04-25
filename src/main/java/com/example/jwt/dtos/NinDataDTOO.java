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
public class NinDataDTOO {
    private Long id;
    private String name;
    private  String foodCode;
    private String typesoffood;
    private String Category;
    private Double energy;
    private String carbohydrate;
    private String protein;
    private String Total_Fat;
//    private Double cholestrol;
    private String  sodium;
    private String Total_Dietary_Fibre;
    private String calcium;
    private String iron;
//    private Double potassium;
//    private Double phosphorus;
    private String magnesium;
    private String zinc;
//    private Double selenium;
//    private Double copper;
//    private Double manganese;



}