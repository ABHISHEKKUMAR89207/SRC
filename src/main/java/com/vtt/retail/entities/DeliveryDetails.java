package com.vtt.retail.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "delivery_details")
public class DeliveryDetails {

    @Id
    private String id;
    
    private String orderId;  // Reference to RetailOrder.orderId
    private String userId;   // Reference to user
    
    // Delhivery Shipment Details
    private String waybillNumber;           // Delhivery waybill/AWB number
    private String shipmentStatus;          // Status from Delhivery (PLACED, PICKED, IN_TRANSIT, DELIVERED, FAILED, etc.)
    private String pickupPincode;           // Pickup location pincode
    private String deliveryPincode;         // Delivery location pincode
    
    // Serviceability Info
    private Boolean isServiceable;          // Whether pincode is serviceable
    private Boolean codAvailable;           // COD available for this pincode
    private Boolean prepaidAvailable;       // Prepaid available for this pincode
    private String serviceability;          // Full serviceability response from API
    
    // Delhivery API Responses
    private String delhiveryOrderId;        // Order ID from Delhivery system
    private String delhiveryResponse;       // Full JSON response from Delhivery
    private Map<String, Object> shipmentDetails;  // Additional shipment details
    
    // Delivery Address Details
    private String recipientName;
    private String recipientPhone;
    private String deliveryAddress;
    private String deliveryCity;
    private String deliveryState;
    private String deliveryCountry;
    
    // Shipment Details
    private Double weight;                  // Weight in kg
    private Double totalAmount;             // Invoice value
    private String paymentMode;             // PREPAID or COD
    private String deliveryType;            // FORWARD or RETURN
    private String shipmentType;            // PARCEL, DOCUMENT, etc.
    
    // Tracking Info
    private String trackingUrl;             // Delhivery tracking URL
    private LocalDateTime estimatedDeliveryDate;
    private LocalDateTime actualDeliveryDate;
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime processedAt;     // When admin marked order as PROCESSED and created Delhivery shipment
    
    // Status tracking
    private String status;                  // PENDING, CREATED, IN_TRANSIT, DELIVERED, FAILED, CANCELLED
    private String errorMessage;            // Error message if shipment creation failed
    private Boolean isActive;               // Soft delete flag
}
