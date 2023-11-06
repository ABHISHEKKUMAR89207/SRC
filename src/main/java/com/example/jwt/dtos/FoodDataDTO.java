package com.example.jwt.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

//import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.Data;
//
//@Data
//public class FoodDataDTO {
//    private Long id;
//    private String food;
////    @JsonProperty
//    private Integer value; // Either carbs or fat
//
//    public FoodDataDTO(Long id, String food, Integer value) {
//        this.id = id;
//        this.food = food;
//        this.value = value;
//    }
//
//    // Getters and setters
//}
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FoodDataDTO {
    private Long id;
    private String food;
    private Integer carbs; // Add carbs field
    private Integer fat;   // Add fat field

    // Empty constructor
    public FoodDataDTO() {
    }

    // Constructor for both "carbs" and "fat"
    public FoodDataDTO(Long id, String food, Integer carbs, Integer fat) {
        this.id = id;
        this.food = food;
        this.carbs = carbs;
        this.fat = fat;
    }

    // Getters and setters for all fields
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public Integer getCarbs() {
        return carbs;
    }

    public void setCarbs(Integer carbs) {
        this.carbs = carbs;
    }

    public Integer getFat() {
        return fat;
    }

    public void setFat(Integer fat) {
        this.fat = fat;
    }
}