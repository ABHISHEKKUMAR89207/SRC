package com.example.jwt.dtos.FoodTodayDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DishDTO {

    private Long dishId;
    private String dishName;
    private Double dishQuantity;
    private String mealName;
}
