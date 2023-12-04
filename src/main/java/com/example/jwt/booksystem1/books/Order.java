package com.example.jwt.booksystem1.books;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private Long userId; // Change the data type to Long

    @ElementCollection
    private List<Integer> bookIdList;

    private double amount;
    private String book;
    private String createTimestamp;
    private String paymentId;
    private String deliveryAddress;
    private String deliveryDate;

//    public Order(Object o, double v, Object o1, double v1, Object o2, long l, String paymentId, Object o3, Object o4) {
//    }

    // Constructors, getters, and setters
    // You can generate these using your IDE or write them manually
}
