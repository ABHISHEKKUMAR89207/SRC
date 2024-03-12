package com.example.jwt.booksystem1.books;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private int year;
    private int quantity;
    private double price;
    private double ratings;
//    private byte[] image;
    private String imageUrl;  // Add this field
    private String getImageUrl(String filename) {
        return "/images/" + filename; // Adjust the path based on your configuration
    }


    // Getters and setters for all properties

    // Constructor

    // Other methods
}
