package com.example.jwt.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BloodSisGlo {
    private double value;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate localDate;
}
