package com.example.jwt.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String gender;
    private Date dateOfBirth;
    private double height;
    private double weight;
    private double bmi;
}
