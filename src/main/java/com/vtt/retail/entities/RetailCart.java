package com.vtt.retail.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "retail_carts")
public class RetailCart {

    @Id
    private String id;
    
    private String userId;  // Reference to User from auth system
    
    // List of cart items
    private List<CartItem> items;
    
    private Double totalPrice = 0.0;
    private Double shipmentCharge = 0.0;
    private Integer totalItems = 0;
    private Integer totalQuantity = 0;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Inner class for cart items
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartItem {
        private String productId;
        private String productName;
        private String selectedSize;
        private Integer quantity;
        private Double pricePerUnit;
        private Double totalPrice;  // quantity * pricePerUnit
        private String productImage;
    }
}
