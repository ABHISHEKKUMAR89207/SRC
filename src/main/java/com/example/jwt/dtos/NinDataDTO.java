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
    private  String foodCode;
    private String typesoffood;
    private String Category;

    private Double energy;
    private Double protein;
    private Double fats;
    private Double carbs;
    private Double Total_Dietary_Fibre;
    private Double calcium;
    private Double iron;
    private Double zinc;
    private Double magnesium;
    private Double thiamine_B1;
    private Double riboflavin_B2;
    private Double niacin_B3;
    private Double folates_B9;
    private Double retinolVit_A;
    private Double carbohydrate;
    private Double Total_Fat;
    private Double  sodium;



//    private Double potassium;
//    private Double phosphorus;


//    private Double selenium;
//    private Double copper;
//    private Double manganese;


    // Custom getter for carbohydrate with unit
//    public String getCarbohydrateWithUnit() {
//        return String.format("%.2f g", this.carbohydrate);
//    }

}