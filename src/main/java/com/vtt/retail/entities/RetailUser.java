package com.vtt.retail.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "retail_users")
public class RetailUser {

    @Id
    private String id;
    
    private String userId;           // Reference to User from auth system
    private String name;
    private String email;
    private String mobileNo;
    private String phone;
    
    // Address details
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String landmark;
    
    // Profile information
    private String profileImage;
    private String gender;
    private String dateOfBirth;
    
    // Account status
    private boolean isActive = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
