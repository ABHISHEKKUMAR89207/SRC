package com.example.jwt.dtos;


public class FoodDTO {
    private String foodName;
    private String foodCode;

    public FoodDTO(String foodName, String foodCode) {
        this.foodName = foodName;
        this.foodCode = foodCode;
    }

    // Getters and setters
    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodCode() {
        return foodCode;
    }

    public void setFoodCode(String foodCode) {
        this.foodCode = foodCode;
    }
}
