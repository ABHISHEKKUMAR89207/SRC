package com.vtt.entities;



import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Document(collection = "user_details")
@Data
public class UserDetails {

    @Id
    private String id;

    @DBRef
    private User user; // Reference to the user

    // Profile Information
    private String profilePictureUrl;


    private String mobileNumber;
    private String panNumber;
    private String aadharNumber;
    private String address;

    // Employment Type
    private String employmentType; // SALARIED or WAGES

    // Bank Details
    private String bankName;
    private String accountNumber;
    private String ifscCode;
    private String upiId;

    // Wallet Details
    private String paytmNumber;
    private String phonePeNumber;

    private Instant createdAt;
    private Instant updatedAt;
}