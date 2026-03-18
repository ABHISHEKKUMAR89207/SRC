package com.vtt.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "productinventory")
public class ProductInventory {

    @Id
    private String id;
    private String nameOfProduct;
    private String active;
    private String color;
    private String productImage;
    private String productImag2;
    private String productImag3;
    private String productLocation;
    private String articleName;


    // List of sizes and their quantities
    private List<SizeQuantity> sizes;

    @DBRef
    private DisplayNamesCat displayNamesCat;

    // Add a reference to the Fabric entity
    @DBRef
    private Fabric fabric;
//    private String displayName;

    // Retail-specific fields
    private Double rating = 0.0;
    private Integer totalRatings = 0;
    private Double totalSales = 0.0;  // Track sales count for trending

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastedArrivedAt;

    private String subcategory;  // From DisplayNamesCat.subCategoryName
    private String category;     // From DisplayNamesCat.categoryName
    private String fabricName;   // From Fabric.displayName

    // Inner class to hold size and quantity
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SizeQuantity {
        private String label; // Size label (e.g., "M", "L", "XL")
        private int quantity; // Available quantity for that size
        private double price;
    }

}
