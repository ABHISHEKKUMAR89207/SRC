package com.example.jwt.entities;

import com.example.jwt.entities.FoodToday.ear.Ear;
import com.example.jwt.entities.dashboardEntity.healthTrends.MenstrualCycle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

    private String variableType;

    private double height;
    private double weight;
    private Integer heightFt;
    private Integer heightIn;

    private String workLevel;



    private double bmi;
    private String googleAccountLink;
    private String facebookAccountLink;
    private String twitterAccountLink;
    private String linkedInAccountLink;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "userProfile")
    @JsonIgnore
    private MenstrualCycle menstrualCycle;

    @OneToMany(mappedBy = "userProfile")
    private List<Ear> ears;
}
