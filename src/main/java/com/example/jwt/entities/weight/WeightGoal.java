//package com.example.jwt.entities.weight;
//
//import com.example.jwt.entities.User;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//public class WeightGoal {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long weightGoalId;
//
//    private String weightGoal;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//}
