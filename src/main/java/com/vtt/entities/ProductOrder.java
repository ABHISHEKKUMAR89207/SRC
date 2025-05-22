package com.vtt.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "productorder")
public class ProductOrder {

    @Id
    private String id;
    private double totalAmount;
    // List of product entries (each with inventory + sizes)
    private List<ProductEntry> productEntries;

    // Reference to the User who placed the order
    @DBRef
    private User user;

    private boolean payment;
    private LocalDateTime orderDate = LocalDateTime.now();

    // Inner class for each product in the order
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductEntry {
        @DBRef
        private ProductInventory productInventory;
        private List<OrderedSizeQuantity> orderedSizes;
    }



    // Inner class for size and quantity ordered
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderedSizeQuantity {
        private String label; // Size label (e.g., "M", "L", "XL")
        private int quantity; // Quantity ordered for that size
    }
}
