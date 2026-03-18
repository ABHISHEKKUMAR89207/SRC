//package com.vtt.retail.entities;
//
//import com.vtt.entities.DisplayNamesCat;
//import com.vtt.entities.Fabric;
//import lombok.*;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.DBRef;
//import org.springframework.data.mongodb.core.mapping.Document;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Getter @Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Document(collection = "retail_products")
//public class ProductInventory {
//
//    @Id
//    private String id;
//
//    private String nameOfProduct;
//    private String active;
//    private String color;
//    private String productImage;
//    private String productImag2;
//    private String productImag3;
//    private String productLocation;
//    private String articleName;
//    private String description;
//
//    // List of sizes and their quantities with individual prices
//    private List<SizeQuantity> sizes;
//
//    @DBRef
//    private DisplayNamesCat displayNamesCat;
//
//    @DBRef
//    private Fabric fabric;
//
//    // Retail-specific fields
//    private Double rating = 0.0;
//    private Integer totalRatings = 0;
//    private Double totalSales = 0.0;  // Track sales count for trending
//
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
//    private LocalDateTime lastedArrivedAt;
//
//    private String subcategory;  // From DisplayNamesCat.subCategoryName
//    private String category;     // From DisplayNamesCat.categoryName
//    private String fabricName;   // From Fabric.displayName
//
//    // Inner class to hold size and quantity with individual pricing
//    @Getter @Setter
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class SizeQuantity {
//        private String label;      // Size label (e.g., "M", "L", "XL")
//        private int quantity;      // Available quantity for that size
//        private double price;      // Price specific to this size
//    }
//}
