package com.vtt.retail.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "retail_coupons")
public class RetailCoupon {

    @Id
    private String id;
    
    private String couponCode;
    private String description;
    
    // Discount details
    private Double discountPercentage;   // Either percentage or flat amount
    private Double flatDiscount;         // Flat discount amount
    private String discountType;        // PERCENTAGE or FLAT
    
    // Coupon constraints
    private Double minimumOrderValue;
    private Integer maximumUsageCount = -1;  // -1 means unlimited
    private Integer currentUsageCount = 0;
    private Integer maximumUsagePerUser = 1;
    
    // Validity
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
    private boolean isActive = true;
    
    // Metadata
    private String createdBy;  // Admin user id
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
