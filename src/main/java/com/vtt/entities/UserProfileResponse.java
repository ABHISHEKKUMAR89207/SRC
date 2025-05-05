package com.vtt.entities;

import lombok.*;

import java.util.Date;

@Getter
@Setter
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


    private String workLevel;
    private String occupation;



    // Default constructor initializing other fields to null or default values
//    public UserProfileResponse() {
//        this.firstName = null;
//        this.lastName = null;
//        this.email = null;
//        this.mobile = null;
//        this.gender = null;
//        this.dateOfBirth = null;
//        this.height = 0.0;
//        this.weight = 0.0;
//        this.bmi = 0.0;
//        this.workLevel = null;
//        this.occupation = null;
//    }
    // Custom constructor with only the required fields
    // Custom constructor with only workLevel and occupation
    public UserProfileResponse(String workLevel, String occupation) {
        this.workLevel = workLevel;
        this.occupation = occupation;
    }
}
