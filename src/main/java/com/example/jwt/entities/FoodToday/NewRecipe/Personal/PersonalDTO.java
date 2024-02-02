package com.example.jwt.entities.FoodToday.NewRecipe.Personal;

import com.example.jwt.dtos.FoodTodayDtos.IngredientDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonalDTO {

    private Long perRecId;
    private LocalDate date;
    private String itemname;
    private double totalCookedQuantity;
    private double valueForOneUnit;
//    private Double servingMeasure;
    private String unit;
    private List<IngredientDTO> ingredients;

    public PersonalDTO(Long perRecId, String itemname) {
        this.perRecId = perRecId;
        this.itemname = itemname;
    }
}
