//package com.vtt.entities;
//
//
//
//import lombok.Data;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.DBRef;
//import org.springframework.data.mongodb.core.mapping.Document;
//
//import java.time.Instant;
//
//@Document(collection = "salary_paid_details")
//@Data
//public class SalaryPaidDetails {
//    @Id
//    private String id;
//
//    @DBRef
//    private User user;  // Reference to User entity
//
//    private double totalAmountPaid;  // Total salary paid
//    private String dateOfPayment;   // Date the salary was paid
//    private String notes;            // Additional notes (optional)
//    private String paymentReferenceNumber;  // Reference number for the payment
//
//    private Instant createdAt;  // Date when this record was created
//    private Instant updatedAt;  // Date when this record was last updated
//}
