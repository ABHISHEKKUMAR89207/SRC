package com.vtt.retail.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "retail_wishlists")
public class RetailWishlist {

    @Id
    private String id;
    
    private String userId;  // Reference to User from auth system
    
    // List of wishlist items
    private List<WishlistItem> items;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Inner class for wishlist items
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WishlistItem {
        private String productId;
        private String productName;
        private Double price;
        private String productImage;
        private LocalDateTime addedAt;
    }
}
