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
}

