package com.example.jwt.entities.error;


import com.example.jwt.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Error {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime localDateTime;
    private String exceptionMessage;
    private String stackTrace;

    // Add a reference to the User entity
    @ManyToOne
    @JoinColumn(name = "user_id")  // Adjust the column name if needed
    private User user;

}
