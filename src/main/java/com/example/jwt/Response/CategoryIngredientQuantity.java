package com.example.jwt.Response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// Define a class to hold category-wise ingredient quantity
class CategoryIngredientQuantity {
    private String category;
    private double totalQuantity;

}