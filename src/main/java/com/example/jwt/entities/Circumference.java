package com.example.jwt.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Circumference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "circumference_date")
    private LocalDate date= LocalDate.now();
    private String hipCircumference;
    private String waistCircumference;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
