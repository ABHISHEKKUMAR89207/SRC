package com.vtt.retail.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "retail_reviews")
public class RetailReview {

    @Id
    private String id;
    
    private String productId;
    private String userId;
    private String userName;
    
    private int rating;  // 1-5 stars
    private String title;
    private String description;
    
    // Review verification
    private String purchaseOrderId;  // Link to order for verification
    private boolean verifiedPurchase;
    
    // Review metrics
    private int helpfulCount = 0;
    private int unhelpfulCount = 0;
    
    // Status
    private String status = "PENDING";  // PENDING, APPROVED, REJECTED
    private boolean isActive = true;
    
    // Metadata
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
