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
//    private Long id;
//    private String name;
//    private  String foodCode;
//    private String typesoffood;
//    private String Category;
//    private Double energy;
//    private String carbohydrate;
//    private String protein;
//    private String Total_Fat;
////    private Double cholestrol;
//    private String  sodium;
//    private String Total_Dietary_Fibre;
//    private String calcium;
//    private String iron;
////    private Double potassium;
////    private Double phosphorus;
//    private String magnesium;
//    private String zinc;
////    private Double selenium;
////    private Double copper;
////    private Double manganese;


    private Long id;
    private String name;
    private String foodCode;
    private String typesoffood;
    private String category;
    private Double energy;
//    private String energyUnit;
    private Double carbohydrate;
//    private String carbohydrateUnit;
    private Double protein;
//    private String proteinUnit;
    private Double total_fat;
//    private String totalFatUnit;
    private Double sodium;
//    private String sodiumUnit;
    private Double total_dietary_fibre;
//    private String totalDietaryFibreUnit;
    private Double calcium;
//    private String calciumUnit;
    private Double iron;
//    private String ironUnit;
    private Double magnesium;
//    private String magnesiumUnit;
    private Double zinc;
    private Double niacin;
    private Double thiamine;
    private Double vita;
    private Double vitb;
    private Double vitc;
    private Double totalfloate;
    private Double riboflavin;
//    private String zincUnit;

    private String unit;

    // Custom getters that append unit to the value

//    public String getEnergyWithUnit() {
//        return this.energy + " " + this.energyUnit;
//    }
//
//    public String getCarbohydrateWithUnit() {
//        return this.carbohydrate + " " + this.carbohydrateUnit;
//    }
//
//    public String getProteinWithUnit() {
//        return this.protein + " " + this.proteinUnit;
//    }
//
//    public String getTotalFatWithUnit() {
//        return this.totalFat + " " + this.totalFatUnit;
//    }
//
//    public String getSodiumWithUnit() {
//        return this.sodium + " " + this.sodiumUnit;
//    }
//
//    public String getTotalDietaryFibreWithUnit() {
//        return this.totalDietaryFibre + " " + this.totalDietaryFibreUnit;
//    }
//
//    public String getCalciumWithUnit() {
//        return this.calcium + " " + this.calciumUnit;
//    }
//
//    public String getIronWithUnit() {
//        return this.iron + " " + this.ironUnit;
//    }
//
//    public String getMagnesiumWithUnit() {
//        return this.magnesium + " " + this.magnesiumUnit;
//    }
//
//    public String getZincWithUnit() {
//        return this.zinc + " " + this.zincUnit;
//    }


}