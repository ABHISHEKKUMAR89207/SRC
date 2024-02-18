package com.example.jwt.entities.water;

import com.example.jwt.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private String localTime;
    private LocalDate localDate = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "waterEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WaterEntry> waterEntries = new ArrayList<>();
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
    public void addWaterEntry(WaterEntry entry) {
        waterEntries.add(entry);
        entry.setWaterEntity(this);
    }
    public Double calculateTotalWaterIntake() {
        return waterEntries.stream()
                .mapToDouble(WaterEntry::getWaterIntake)
                .sum();
    }
}

