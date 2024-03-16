package com.example.jwt.AdminDashboard;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private Long orderId;

    private String Name;
    private String email;
    private String bookTitle;
    private int quantity;
    private double amount;
    private String paymentId;
    private String deliveryAddress;
    private String deliveryDate;
    private String contact;
    private String deliveryStatus;

    // Constructor, getters, and setters
}
