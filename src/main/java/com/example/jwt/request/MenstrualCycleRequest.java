package com.example.jwt.request;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenstrualCycleRequest {
    private LocalDate lastPeriodStartDate;
    private int averageCycleLength;


}
