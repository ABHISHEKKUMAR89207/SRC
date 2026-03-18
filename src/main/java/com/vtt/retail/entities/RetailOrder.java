package com.vtt.retail.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "retail_orders")
public class RetailOrder {

    @Id
    private String id;
    
    private String orderId;  // Unique order number
    private String userId;   // Reference to User from auth system
    
    // Order items
    private List<OrderItem> items;
    
    // Pricing
    private Double subtotal = 0.0;
    private Double discount = 0.0;      // From coupon
    private Double couponCode;          // Coupon value
    private Double taxes = 0.0;
    private Double totalAmount = 0.0;
    
    // Delivery address
    private String deliveryAddressLine1;
    private String deliveryAddressLine2;
    private String deliveryCity;
    private String deliveryState;
    private String deliveryPostalCode;
    private String deliveryCountry;
    private String deliveryLandmark;
    private String recipientName;
    private String recipientPhone;
    
    // Payment
    private String paymentMethod = "COD";  // Currently only COD
    private String paymentStatus = "PENDING";  // PENDING, COMPLETED, FAILED
    
    // Order Status
    private String orderStatus = "PLACED";  // PLACED, SHIPPED, DELIVERED, CANCELLED, RETURNED
    
    // Tracking
    private String trackingId;
    private LocalDateTime placedAt;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime cancelledAt;
    
    // Return
    private boolean isReturned = false;
    private LocalDateTime returnedAt;
    private String returnReason;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Inner class for order items
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {
        private String productId;
        private String productName;
        private String selectedSize;
        private Integer quantity;
        private Double pricePerUnit;
        private Double totalPrice;
        private String productImage;
    }
}
