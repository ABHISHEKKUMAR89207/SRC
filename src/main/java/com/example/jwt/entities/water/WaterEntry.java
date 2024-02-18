package com.example.jwt.entities.water;

import com.example.jwt.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class WaterEntry {
    @Id
    @GeneratedValue
    private Long entryId;

    private Double waterIntake;
    private String localTime;
    private LocalDate localDate;

    @ManyToOne
    @JoinColumn(name = "water_entity_id")
    private WaterEntity waterEntity;

//    @ManyToOne
//    @JsonIgnore
//    @JoinColumn(name = "user_id")
//    private User user;

    public void calculateWaterIntake() {
        this.waterIntake = waterIntake;
    }
    // You can have a constructor that takes a WaterEntity parameter to set the association
    public WaterEntry(WaterEntity waterEntity) {
        this.waterEntity = waterEntity;
    }
//    public void calculateWaterIntake() {
//        // Assuming waterIntake is initialized to 0.0
//        this.waterIntake = waterEntity.getWaterEntries()
//                .stream()
//                .filter(entry -> entry.getLocalDate().isEqual(localDate))
//                .mapToDouble(WaterEntry::getWaterIntake)
//                .sum();
//    }
}