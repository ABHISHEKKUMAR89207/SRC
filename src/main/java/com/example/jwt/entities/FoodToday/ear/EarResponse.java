package com.example.jwt.entities.FoodToday.ear;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class EarResponse {

    private Long earId;


    private String gender;
    private String age;
    private String group;
    private String workLevel;

    private double energy;
    private double protein;
    private double fatsOilsVisible;
    private double fiber;
    private double calcium;
    private double magnesium;
    private double iron;
    private double zinc;
    private double thiamine;
    private double riboflavin;
    private double niacin;

    private double folate;
    private double vitA;
    private double carbohyderate;

    private String particulars;


    public void generateParticulars() {
        this.particulars = String.format("%s, %s , %s", gender, workLevel, age);
    }



    public EarResponse( String gender, String age, String group, String workLevel,
                       double energy, double protein, double fatsOilsVisible, double fiber,
                       double calcium, double magnesium, double iron, double zinc,
                       double thiamine, double riboflavin, double niacin,
                       double folate, double vitA, double carbohyderate) {
//        this.earId = 0L; // Set earId to 0L when it's null
        this.gender = gender;
        this.age = age;
        this.group = group;
        this.workLevel = workLevel;
        this.energy = energy;
        this.protein = protein;
        this.fatsOilsVisible = fatsOilsVisible;
        this.fiber = fiber;
        this.calcium = calcium;
        this.magnesium = magnesium;
        this.iron = iron;
        this.zinc = zinc;
        this.thiamine = thiamine;
        this.riboflavin = riboflavin;
        this.niacin = niacin;
        this.folate = folate;
        this.vitA = vitA;
        this.carbohyderate = carbohyderate;
    }



    public EarResponse(Ear ear) {
        this.earId = ear.getEarId();
        this.gender = ear.getGender();
        this.age = ear.getAge();
        this.group = ear.getHgroup();
        this.workLevel = ear.getWorkLevel();
        this.energy = ear.getEnergy();
        this.protein = ear.getProtein();
        this.fatsOilsVisible = ear.getFats(); // Adjust the field name as needed
        this.fiber = ear.getFiber();
        this.calcium = ear.getCalcium();
        this.magnesium = ear.getMagnesium();
        this.iron = ear.getIron();
        this.zinc = ear.getZinc();
        this.thiamine = ear.getThiamine();
        this.riboflavin = ear.getRiboflavin();
        this.niacin = ear.getNiacin();
        this.folate = ear.getFolate();
        this.vitA = ear.getVit_A();
        this.carbohyderate = ear.getCarbohyderate();
        generateParticulars(); // Call the method to set the "particulars" field

    }
}
