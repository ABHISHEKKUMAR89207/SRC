// User Profile Database Table 

package com.vtt.entities;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Data

@NoArgsConstructor
@AllArgsConstructor
@Document
public class UserProfile {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

//    @OneToOne
    @JsonIgnore
//    @JoinColumn(name = "user_id")
    @DBRef
    private User user;




}
