package com.example.jwt.entities.water;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WaterEntityResponse {
    private Long waterId;
    private Double cupCapacity;
    private int noOfCups;
//    private Double waterIntake;
//    private String localTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")

    private LocalDate localDate;
    private List<WaterEntryResponse> waterEntries;
}
