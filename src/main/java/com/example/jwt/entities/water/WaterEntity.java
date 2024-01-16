package com.example.jwt.entities.water;

import com.example.jwt.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WaterEntity {
    @Id
    @GeneratedValue
    private Long waterId;

    private Double cupCapacity;
    private int noOfCups;
    private Double waterIntake;
    private LocalDate localDate = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getter method to calculate waterIntake based on cupCapacity and noOfCups
//    public Double getWaterIntake1() {
//        return cupCapacity * noOfCups;
//    }
    // Getter method to get the drink name (for example, "Water")
    public String getDrinkName() {
        return "Water1";
    }
    // Getter method to calculate waterIntake based on cupCapacity and noOfCups
    public void calculateWaterIntake() {
        this.waterIntake = cupCapacity * noOfCups;
    }

}

