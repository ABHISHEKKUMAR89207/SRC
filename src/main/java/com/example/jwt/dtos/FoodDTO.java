package com.example.jwt.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class FoodDTO {
    private String foodName;
    private String foodCode;
    private String category;

    public FoodDTO(String foodName, String foodCode, String category) {
        this.foodName = foodName;
        this.foodCode = foodCode;
        this.category = category;
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
