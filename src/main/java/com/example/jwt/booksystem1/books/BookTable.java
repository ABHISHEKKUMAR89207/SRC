package com.example.jwt.booksystem1.books;


import com.example.jwt.entities.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    private double ratings;

    private String imageFilename; // Add image filename property


    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference // Add this
    private Order order;
    @ManyToOne
    @JsonBackReference // Add this
    private User user;


}
