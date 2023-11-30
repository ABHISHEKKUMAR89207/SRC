package com.example.jwt.entities.dashboardEntity.healthTrends;

import com.example.jwt.entities.User;
import com.example.jwt.entities.UserProfile;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Entity
@Table(name = "healthTrends")
public class HealthTrends {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long healthTrendId;

    private LocalDate date = LocalDate.now();
    private double weight;
    private double bmi;


//    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "healthTrends")
//    @JsonManagedReference
//    private List<HeartRate> heartRates;
//
//    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "healthTrends")
//    @JsonManagedReference
//    private List<BloodPressure> bloodPressures;
//
//    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "healthTrends")
//    @JsonManagedReference
//    private List<BloodGlucose> bloodGlucose;
//
//    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "healthTrends")
//    @JsonManagedReference
//    private List<OxygenSaturatedLevel> oxygenSaturatedLevels;
//
//    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "healthTrends")
//    @JsonManagedReference
//    private List<SleepDuration> sleepDurations;
//
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    @JsonBackReference
//    private User user;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_profile_id")
//    @JsonBackReference
//    private UserProfile userProfile;





//    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "healthTrends")
//    @JsonManagedReference
//
//    private List<HeartRate> heartRates;
//
//    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "healthTrends")
//    @JsonManagedReference
//
//    private List<BloodPressure> bloodPressures;
//
//    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "healthTrends")
//    @JsonManagedReference
//
//    private List<BloodGlucose> bloodGlucose;
//
//    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "healthTrends")
//    @JsonManagedReference
//
//    private List<OxygenSaturatedLevel> oxygenSaturatedLevels;
//
//    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "healthTrends")
//    @JsonManagedReference
//
//    private List<SleepDuration> sleepDurations;
//
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    @JsonBackReference
//
//    private User user;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_profile_id")
//    @JsonBackReference
//
//    private UserProfile userProfile;
//







    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "healthTrends")
//    @JsonManagedReference
    @JsonIgnore
    private List<HeartRate> heartRates;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "healthTrends")
//    @JsonManagedReference
    @JsonIgnore
    private List<BloodPressure> bloodPressures;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "healthTrends")
//    @JsonManagedReference
    @JsonIgnore
    private List<BloodGlucose> bloodGlucose;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "healthTrends")
//    @JsonManagedReference
    @JsonIgnore
    private List<OxygenSaturatedLevel> oxygenSaturatedLevels;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "healthTrends")
//    @JsonManagedReference
//    @JsonIgnore
//    private List<SleepDuration> sleepDurations;
//
//    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "healthTrends")
//    @JsonManagedReference
    @JsonIgnore
    private List<HeartRate> menstrualCycle;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
//    @JsonBackReference
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id")
//    @JsonBackReference
    @JsonIgnore
    private UserProfile userProfile;

    // Other fields and methods...
}
//