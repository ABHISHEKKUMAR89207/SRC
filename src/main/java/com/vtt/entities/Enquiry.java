package com.vtt.entities;



import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "enquiries")
public class Enquiry {
    @Id
    private String id;

    private String productName;
    private String buyRequirements;
    private Double quantityValue;
    private String quantityType;
    private String frequency;
    private String customerName;
    private String email;
    private String mobileNo;
    private String country;
    private String city;
    private String companyName;
    private String status; // "Pending", "Approved", "Rejected", "Completed"

    private String userId; // Store user ID directly instead of @DBRef

    private LocalDateTime enquiryDate;
    private LocalDateTime lastUpdated;

    // Constructors
    public Enquiry() {
        this.enquiryDate = LocalDateTime.now();
        this.status = "Pending";
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getBuyRequirements() { return buyRequirements; }
    public void setBuyRequirements(String buyRequirements) { this.buyRequirements = buyRequirements; }

    public Double getQuantityValue() { return quantityValue; }
    public void setQuantityValue(Double quantityValue) { this.quantityValue = quantityValue; }

    public String getQuantityType() { return quantityType; }
    public void setQuantityType(String quantityType) { this.quantityType = quantityType; }

    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobileNo() { return mobileNo; }
    public void setMobileNo(String mobileNo) { this.mobileNo = mobileNo; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status;
        this.lastUpdated = LocalDateTime.now();
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public LocalDateTime getEnquiryDate() { return enquiryDate; }
    public void setEnquiryDate(LocalDateTime enquiryDate) { this.enquiryDate = enquiryDate; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}