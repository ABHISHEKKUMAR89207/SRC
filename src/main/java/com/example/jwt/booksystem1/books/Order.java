package com.example.jwt.booksystem1.books;


import com.example.jwt.entities.User;
import jakarta.persistence.*;
import lombok.*;

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
    private User user;
//    @ManyToOne
//    @JoinColumn(name = "book-id") // This should match the user_id column in the User table
//    private BookTable book;
@ManyToOne
@JoinColumn(name = "book_id")
private BookTable book;

//    @ElementCollection
    private int quantity;

    private double amount;
//    private String book;
    private String createTimestamp;
    private String paymentId;
    private String deliveryAddress;
    private String deliveryDate;
    private String contact;
    private String ninOrderId;
    private String deliveryStatus;




//    public Order(Object o, double v, Object o1, double v1, Object o2, long l, String paymentId, Object o3, Object o4) {
//    }

    // Constructors, getters, and setters
    // You can generate these using your IDE or write them manually
}
