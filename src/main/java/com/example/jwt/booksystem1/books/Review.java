package com.example.jwt.booksystem1.books;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.example.jwt.entities.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private BookTable book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String username; // New field

    private String comment;
    private double rating;
    private String date;

    // Constructors, getters, setters, and other methods

    public Review(BookTable book, User user, String username, String comment, double rating, String date) {
        this.book = book;
        this.user = user;
        this.username = username;
        this.comment = comment;
        this.rating = rating;
        this.date = date;
    }

    // Update other methods as needed
}
