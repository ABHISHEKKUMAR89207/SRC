package com.example.jwt.booksystem1.books;


import com.example.jwt.entities.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class BookTable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private int year;
    private int quantity;
    private double price;

    // Constructors, getters, setters, etc.
@ManyToOne
    private User user;


}
