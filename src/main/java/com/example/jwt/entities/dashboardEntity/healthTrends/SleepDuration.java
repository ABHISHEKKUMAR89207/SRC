package com.example.jwt.entities.dashboardEntity.healthTrends;

import com.example.jwt.entities.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class SleepDuration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfSleep;
    private long duration;
    private double manualDuration;
    private int efficiency;
    //@JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime endTime;



    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;
}

