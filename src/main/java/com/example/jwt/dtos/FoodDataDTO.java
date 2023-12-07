package com.example.jwt.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FoodDataDTO {
    private Long id;
    private String food;
    private Integer carbs;
    private Integer fat;

    public FoodDataDTO() {
    }

    public FoodDataDTO(Long id, String food, Integer carbs, Integer fat) {
        this.id = id;
        this.food = food;
        this.carbs = carbs;
        this.fat = fat;
    }

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