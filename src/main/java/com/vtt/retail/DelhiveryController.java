package com.vtt.retail;

import com.vtt.retail.entities.DeliveryDetails;
import com.vtt.retail.entities.RetailOrder;
import com.vtt.retail.repository.DeliveryDetailsRepository;
import com.vtt.retail.repository.RetailOrderRepository;
import com.vtt.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/retail/delivery")
@RequiredArgsConstructor
@Tag(name = "Retail Delivery Management", description = "Delhivery shipping integration and tracking")
public class DelhiveryController {

    private final DeliveryDetailsRepository deliveryDetailsRepository;
    private final RetailOrderRepository retailOrderRepository;
    private final RestTemplate restTemplate;

    @Value("${delhivery.api.token}")
    private String delhiveryToken;

    @Value("${delhivery.api.base-url}")
    private String delhiveryBaseUrl;

    @Value("${delhivery.api.pincode-url}")
    private String delhiveryPincodeUrl;

    @Value("${delhivery.api.shipment-url}")
    private String delhiveryShipmentUrl;

    // ==================== PINCODE SERVICEABILITY ENDPOINTS ====================

    /**
     * Check pincode serviceability with Delhivery
     * Returns availability for COD and Prepaid options
     */
    @PostMapping("/check-serviceability")
    @Operation(summary = "Check pincode serviceability", description = "Verify if Delhivery services the given pincode")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkPincodeServiceability(
            @RequestParam String pincode,
            @RequestParam(required = false, defaultValue = "110017") String pickupPincode) {

        try {
            Map<String, Object> response = new HashMap<>();
            
            // Delhivery API endpoint: https://staging-express.delhivery.com/c/api/pin-codes/json/?filter_codes=PINCODE
            String apiUrl = delhiveryPincodeUrl + "?filter_codes=" + pincode;
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Token " + delhiveryToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Accept", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            try {
                @SuppressWarnings("unchecked")
                ResponseEntity<Map> delhiveryResponse = restTemplate.postForEntity(apiUrl, entity, Map.class);
                
                if (delhiveryResponse.getStatusCode().is2xxSuccessful() && delhiveryResponse.getBody() != null) {
                    Map<String, Object> body = delhiveryResponse.getBody();
                    
                    // Extract serviceability details
                    boolean isServiceable = (boolean) body.getOrDefault("is_serviceable", false);
                    boolean codAvailable = (boolean) body.getOrDefault("cod", false);
                    boolean prepaidAvailable = (boolean) body.getOrDefault("prepaid", false);
                    
                    response.put("pincode", pincode);
                    response.put("isServiceable", isServiceable);
                    response.put("codAvailable", codAvailable);
                    response.put("prepaidAvailable", prepaidAvailable);
                    response.put("status", isServiceable ? "SERVICEABLE" : "NOT_SERVICEABLE");
                    response.put("fullResponse", body);
                    
                    return ResponseEntity.ok(
                        new ApiResponse<>("Pincode serviceability checked successfully", response, true, 200)
                    );
                } else {
                    return ResponseEntity.badRequest().body(
                        new ApiResponse<>("Failed to check serviceability", null, false, 400)
                    );
                }
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(
                    new ApiResponse<>("Delhivery API error: " + e.getMessage(), null, false, 400)
                );
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                new ApiResponse<>("Error: " + e.getMessage(), null, false, 500)
            );
        }
    }

    // ==================== DELIVERY DETAILS CREATION (ADMIN TRIGGERED) ====================

    /**
     * Create delivery shipment when admin marks order as PROCESSED
     * This endpoint should be called when admin changes order status to PROCESSED
     */
    @PostMapping("/create-shipment/{orderId}")
    @Operation(summary = "Create Delhivery shipment", description = "Create shipment record when order status changes to PROCESSED")
    public ResponseEntity<ApiResponse<DeliveryDetails>> createShipment(
            @PathVariable String orderId,
            @RequestBody DeliveryShipmentRequest shipmentRequest) {

        try {
            // Check if delivery details already exist
            if (deliveryDetailsRepository.existsByOrderId(orderId)) {
                return ResponseEntity.badRequest().body(
                    new ApiResponse<>("Delivery details already exist for this order", null, false, 400)
                );
            }

            // Fetch order details
            Optional<RetailOrder> orderOptional = retailOrderRepository.findByOrderId(orderId);
            if (orderOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            RetailOrder order = orderOptional.get();

            // Check pincode serviceability
            String deliveryPincode = shipmentRequest.getDeliveryPincode();
            String pickupPincode = shipmentRequest.getPickupPincode();

            Map<String, Object> serviceabilityCheck = checkServiceabilityInternal(deliveryPincode);
            Boolean isServiceable = (Boolean) serviceabilityCheck.get("isServiceable");

            if (!isServiceable) {
                return ResponseEntity.badRequest().body(
                    new ApiResponse<>("Delivery pincode is not serviceable by Delhivery", null, false, 400)
                );
            }

            // Create DeliveryDetails record
            DeliveryDetails deliveryDetails = new DeliveryDetails();
            deliveryDetails.setOrderId(orderId);
            deliveryDetails.setUserId(order.getUserId());
            deliveryDetails.setPickupPincode(pickupPincode);
            deliveryDetails.setDeliveryPincode(deliveryPincode);
            deliveryDetails.setIsServiceable(isServiceable);
            deliveryDetails.setCodAvailable((Boolean) serviceabilityCheck.get("codAvailable"));
            deliveryDetails.setPrepaidAvailable((Boolean) serviceabilityCheck.get("prepaidAvailable"));
            deliveryDetails.setServiceability(serviceabilityCheck.toString());

            // Set recipient details from order
            deliveryDetails.setRecipientName(shipmentRequest.getRecipientName());
            deliveryDetails.setRecipientPhone(shipmentRequest.getRecipientPhone());
            deliveryDetails.setDeliveryAddress(shipmentRequest.getDeliveryAddress());
            deliveryDetails.setDeliveryCity(shipmentRequest.getDeliveryCity());
            deliveryDetails.setDeliveryState(shipmentRequest.getDeliveryState());
            deliveryDetails.setDeliveryCountry(shipmentRequest.getDeliveryCountry());

            // Set shipment details
            deliveryDetails.setTotalAmount(order.getTotalAmount());
            deliveryDetails.setPaymentMode(order.getPaymentMethod());
            deliveryDetails.setDeliveryType("FORWARD");
            deliveryDetails.setWeight(shipmentRequest.getWeight() != null ? shipmentRequest.getWeight() : 1.0);
            deliveryDetails.setShipmentType(shipmentRequest.getShipmentType() != null ? shipmentRequest.getShipmentType() : "PARCEL");

            // Set status
            deliveryDetails.setStatus("PENDING");
            deliveryDetails.setShipmentStatus("PENDING");
            deliveryDetails.setIsActive(true);
            deliveryDetails.setProcessedAt(LocalDateTime.now());
            deliveryDetails.setCreatedAt(LocalDateTime.now());
            deliveryDetails.setUpdatedAt(LocalDateTime.now());

            // Save to database
            DeliveryDetails savedDelivery = deliveryDetailsRepository.save(deliveryDetails);

            // Update RetailOrder with delivery reference
            order.setDeliveryDetailsId(savedDelivery.getId());
            retailOrderRepository.save(order);

            return ResponseEntity.ok(
                new ApiResponse<>("Delivery shipment created successfully", savedDelivery, true, 200)
            );

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                new ApiResponse<>("Error creating shipment: " + e.getMessage(), null, false, 500)
            );
        }
    }

    // ==================== DELIVERY DETAILS RETRIEVAL ====================

    /**
     * Get delivery details by order ID
     */
    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get delivery details by order ID", description = "Retrieve delivery information for an order")
    public ResponseEntity<ApiResponse<DeliveryDetails>> getDeliveryByOrderId(
            @PathVariable String orderId) {

        try {
            Optional<DeliveryDetails> deliveryOptional = deliveryDetailsRepository.findByOrderId(orderId);
            
            if (deliveryOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(
                new ApiResponse<>("Delivery details retrieved", deliveryOptional.get(), true, 200)
            );

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                new ApiResponse<>("Error: " + e.getMessage(), null, false, 500)
            );
        }
    }

    /**
     * Get delivery details by waybill number
     */
    @GetMapping("/waybill/{waybillNumber}")
    @Operation(summary = "Get delivery by waybill", description = "Retrieve delivery details using Delhivery waybill number")
    public ResponseEntity<ApiResponse<DeliveryDetails>> getDeliveryByWaybill(
            @PathVariable String waybillNumber) {

        try {
            Optional<DeliveryDetails> deliveryOptional = deliveryDetailsRepository.findByWaybillNumber(waybillNumber);
            
            if (deliveryOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(
                new ApiResponse<>("Delivery details retrieved", deliveryOptional.get(), true, 200)
            );

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                new ApiResponse<>("Error: " + e.getMessage(), null, false, 500)
            );
        }
    }

    /**
     * Get all delivery details for a user
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get deliveries by user ID", description = "Retrieve all delivery records for a user")
    public ResponseEntity<ApiResponse<Page<DeliveryDetails>>> getUserDeliveries(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<DeliveryDetails> deliveries = deliveryDetailsRepository.findByUserId(userId, pageable);

            return ResponseEntity.ok(
                new ApiResponse<>("User deliveries retrieved", deliveries, true, 200)
            );

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                new ApiResponse<>("Error: " + e.getMessage(), null, false, 500)
            );
        }
    }

    /**
     * Get deliveries by shipment status
     */
    @GetMapping("/status/{shipmentStatus}")
    @Operation(summary = "Get deliveries by status", description = "Retrieve deliveries filtered by shipment status")
    public ResponseEntity<ApiResponse<Page<DeliveryDetails>>> getDeliveriesByStatus(
            @PathVariable String shipmentStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<DeliveryDetails> deliveries = deliveryDetailsRepository.findByShipmentStatus(shipmentStatus, pageable);

            return ResponseEntity.ok(
                new ApiResponse<>("Deliveries retrieved", deliveries, true, 200)
            );

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                new ApiResponse<>("Error: " + e.getMessage(), null, false, 500)
            );
        }
    }

    /**
     * Get all deliveries with pagination
     */
    @GetMapping("/all")
    @Operation(summary = "Get all deliveries", description = "Admin endpoint to view all delivery records")
    public ResponseEntity<ApiResponse<Page<DeliveryDetails>>> getAllDeliveries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<DeliveryDetails> deliveries = deliveryDetailsRepository.findAll(pageable);

            return ResponseEntity.ok(
                new ApiResponse<>("All deliveries retrieved", deliveries, true, 200)
            );

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                new ApiResponse<>("Error: " + e.getMessage(), null, false, 500)
            );
        }
    }

    /**
     * Get non-serviceable pincodes
     */
    @GetMapping("/non-serviceable")
    @Operation(summary = "Get non-serviceable deliveries", description = "View all deliveries with non-serviceable pincodes")
    public ResponseEntity<ApiResponse<List<DeliveryDetails>>> getNonServiceableDeliveries() {

        try {
            List<DeliveryDetails> deliveries = deliveryDetailsRepository.findByIsServiceableFalse();

            return ResponseEntity.ok(
                new ApiResponse<>("Non-serviceable deliveries retrieved", deliveries, true, 200)
            );

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                new ApiResponse<>("Error: " + e.getMessage(), null, false, 500)
            );
        }
    }

    // ==================== DELIVERY DETAILS UPDATE ====================

    /**
     * Update delivery status (when receiving webhook or manual update from Delhivery)
     */
    @PatchMapping("/{deliveryId}/status")
    @Operation(summary = "Update delivery status", description = "Update shipment status and tracking information")
    public ResponseEntity<ApiResponse<DeliveryDetails>> updateDeliveryStatus(
            @PathVariable String deliveryId,
            @RequestBody UpdateStatusRequest statusRequest) {

        try {
            Optional<DeliveryDetails> deliveryOptional = deliveryDetailsRepository.findById(deliveryId);
            
            if (deliveryOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            DeliveryDetails delivery = deliveryOptional.get();
            delivery.setShipmentStatus(statusRequest.getShipmentStatus());
            delivery.setStatus(statusRequest.getStatus());
            
            if (statusRequest.getTrackingUrl() != null) {
                delivery.setTrackingUrl(statusRequest.getTrackingUrl());
            }
            
            if (statusRequest.getEstimatedDeliveryDate() != null) {
                delivery.setEstimatedDeliveryDate(statusRequest.getEstimatedDeliveryDate());
            }
            
            if (statusRequest.getActualDeliveryDate() != null) {
                delivery.setActualDeliveryDate(statusRequest.getActualDeliveryDate());
            }
            
            delivery.setUpdatedAt(LocalDateTime.now());

            DeliveryDetails updatedDelivery = deliveryDetailsRepository.save(delivery);

            return ResponseEntity.ok(
                new ApiResponse<>("Delivery status updated", updatedDelivery, true, 200)
            );

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                new ApiResponse<>("Error: " + e.getMessage(), null, false, 500)
            );
        }
    }

    /**
     * Add waybill number to delivery record
     */
    @PatchMapping("/{deliveryId}/waybill/{waybillNumber}")
    @Operation(summary = "Update waybill number", description = "Set waybill number after successful Delhivery order creation")
    public ResponseEntity<ApiResponse<DeliveryDetails>> updateWaybillNumber(
            @PathVariable String deliveryId,
            @PathVariable String waybillNumber) {

        try {
            Optional<DeliveryDetails> deliveryOptional = deliveryDetailsRepository.findById(deliveryId);
            
            if (deliveryOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            DeliveryDetails delivery = deliveryOptional.get();
            delivery.setWaybillNumber(waybillNumber);
            delivery.setStatus("CREATED");
            delivery.setShipmentStatus("CREATED");
            delivery.setUpdatedAt(LocalDateTime.now());

            DeliveryDetails updatedDelivery = deliveryDetailsRepository.save(delivery);

            return ResponseEntity.ok(
                new ApiResponse<>("Waybill number updated", updatedDelivery, true, 200)
            );

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                new ApiResponse<>("Error: " + e.getMessage(), null, false, 500)
            );
        }
    }

    // ==================== HELPER METHODS ====================

    /**
     * Internal method to check serviceability
     */
    private Map<String, Object> checkServiceabilityInternal(String pincode) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String apiUrl = delhiveryPincodeUrl + "?filter_codes=" + pincode;
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Token " + delhiveryToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            @SuppressWarnings("unchecked")
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                result.put("isServiceable", body.getOrDefault("is_serviceable", false));
                result.put("codAvailable", body.getOrDefault("cod", false));
                result.put("prepaidAvailable", body.getOrDefault("prepaid", false));
            }
        } catch (Exception e) {
            result.put("isServiceable", false);
            result.put("codAvailable", false);
            result.put("prepaidAvailable", false);
        }
        
        return result;
    }

    // ==================== REQUEST/RESPONSE DTOs ====================

    @lombok.Data
    public static class DeliveryShipmentRequest {
        private String recipientName;
        private String recipientPhone;
        private String deliveryAddress;
        private String deliveryCity;
        private String deliveryState;
        private String deliveryCountry;
        private String deliveryPincode;
        private String pickupPincode;
        private Double weight;
        private String shipmentType;
    }

    @lombok.Data
    public static class UpdateStatusRequest {
        private String status;
        private String shipmentStatus;
        private String trackingUrl;
        private LocalDateTime estimatedDeliveryDate;
        private LocalDateTime actualDeliveryDate;
    }
}
