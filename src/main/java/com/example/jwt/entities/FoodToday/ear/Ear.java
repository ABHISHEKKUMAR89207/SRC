
package com.example.jwt.entities.FoodToday.ear;

import com.example.jwt.entities.UserProfile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ear")
public class Ear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long earId;

    private String hgroup;
    private String workLevel;
    private String gender;
    private String age;
    private double bodyWt;

    private double energy;
    private double protein;
    private boolean fats;
    private double fiber;
    private double calcium;
    private double magnesium;
    private double iron;
    private double zinc;
    private double thiamine;
    private double riboflavin;
    private double niacin;
//    private double vitB6;
    private double folate;
    private double vitA;
//    private double vitD;


    private double iodine;
    private double vitB12;
    private double vitC;
    private double carbohyderate;

    @ManyToOne
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;

    public String getAge() {
        return this.age; // Replace with the actual field name if different
    }


}


//package com.example.jwt.entities.FoodToday.ear;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//public class Ear {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long earId;
//
//    private String group;
//    private String workLevel;
//    private int age;
//    private double bodyWt;
//
//    private double energy;
//    private double protein;
//    private boolean fatsOilsVisible;
//    private double fiber;
//    private double calcium;
//    private double magnesium;
//    private double iron;
//    private double zinc;
//    private double thiamine;
//    private double riboflavin;
//    private double niacin;
////    private double vitB6;
//    private double folate;
//    private double vitA;
////    private double vitD;
//
//
//    private double iodine;
//    private double vitB12;
//    private double vitC;
//    private double carbohyderate;
//
//}


