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

    private Integer totalBookSale;


    // Constructor
    public OrderDTO(Long orderId, String name, String email, String bookTitle, int quantity, double amount,
                    String paymentId, String deliveryAddress, String deliveryDate, String contact, String deliveryStatus) {
        this.orderId = orderId;
        this.Name = name;
        this.email = email;
        this.bookTitle = bookTitle;
        this.quantity = quantity;
        this.amount = amount;
        this.paymentId = paymentId;
        this.deliveryAddress = deliveryAddress;
        this.deliveryDate = deliveryDate;
        this.contact = contact;
        this.deliveryStatus = deliveryStatus;
    }

    // Constructor, getters, and setters
}
