package com.example.jwt.booksystem1.books;


import com.example.jwt.entities.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
@Getter
@Setter
@Entity

@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "user_id") // This should match the user_id column in the User table
    @JsonBackReference // Add this to prevent serialization of `user` back reference
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private BookTable book;
    private int quantity;
    private double amount;
    private Date createTimestamp;
    private String paymentId;
    private String deliveryAddress;
    private String deliveryDate;
    private String contact;
    private String ninOrderId;
    private String deliveryStatus;

}
