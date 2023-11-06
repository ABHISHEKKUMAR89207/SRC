package com.example.jwt.entities;

import com.example.jwt.entities.dashboardEntity.Activities;
import com.example.jwt.entities.dashboardEntity.healthTrends.MenstrualCycle;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_profile")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    private String gender;
    private LocalDate dateOfBirth;

    private double height;
    private double weight;
    private double bmi;


    // Constructors, getters, and setters


    // Uncomment this line to establish a one-to-one relationship with User
//    @OneToOne(fetch = FetchType.LAZY)
    @OneToOne
//    @JoinColumn(name = "user_id")
//    @JsonBackReference
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

//    @OneToOne(mappedBy = "userProfile")
//    private FitnessActivity fitnessActivity;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_profile_id") // Isme user_profile_id ek foreign key hoga jo UserProfile se juda hoga
//    private UserProfile userProfile;


//    @OneToOne(mappedBy = "userProfile")
//    private MenstrualCycle menstrualCycle;

    @OneToOne(mappedBy = "userProfile")
    @JsonIgnore
    private MenstrualCycle menstrualCycle;

//

//    @OneToMany(mappedBy = "userProfile")
//    @JsonIgnoreProperties("userProfile") // Exclude userProfile from serialization
//    private List<Activities> activities;


//    @OneToMany(mappedBy = "userProfile")
//    @JsonIgnore
//    private List<Activities> activities;
////
//    @OneToMany(mappedBy = "userProfile")
////    @JsonManagedReference
//    private List<Activities> activities;

//    @OneToOne(mappedBy = "userProfile", cascade = CascadeType.ALL)
//    private MenstrualCycle menstrualCycle;
    // Other properties, getters, and setters

//    public MenstrualCycle getMenstrualCycle() {
//        return menstrualCycle;
//    }
//


}
