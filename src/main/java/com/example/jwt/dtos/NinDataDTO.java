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

    private Double carbohydrate;
    private Double Total_Fat;

//    public NinDataDTO() {
//    }
//
//    public NinDataDTO(Long id, String name, Double carbs, Double fat) {
//        this.id = id;
//        this.name = name;
//        this.carbs = carbs;
//        this.Fat = fat;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public Double getCarbs() {
//        return carbs;
//    }
//
//    public Double getFat() {
//        return Fat;
//    }
//
//    public void setFat(Double fat) {
//        this.Fat = fat;
//    }
}