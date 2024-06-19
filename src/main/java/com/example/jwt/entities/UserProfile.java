// User Profile Database Table 

package com.example.jwt.entities;

import com.example.jwt.entities.FoodToday.ear.Ear;
import com.example.jwt.entities.dashboardEntity.healthTrends.MenstrualCycle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
//    @JsonDeserialize(using = SqlTimeDeserializer.class)
//@JsonDeserialize(using = LocalTimeDeserializer.class)
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
private LocalTime wakeupTime;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
//private Time wakeupTime;



    private double bmi;
    private double bmr;
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
