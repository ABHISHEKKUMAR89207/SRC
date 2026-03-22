package com.vtt.retail.repository;

import com.vtt.retail.entities.DeliveryDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryDetailsRepository extends MongoRepository<DeliveryDetails, String> {
    
    // Find by orderId
    Optional<DeliveryDetails> findByOrderId(String orderId);
    
    // Find by userId
    List<DeliveryDetails> findByUserId(String userId);
    
    // Find by waybill number
    Optional<DeliveryDetails> findByWaybillNumber(String waybillNumber);
    
    // Find by pincode (delivery)
    List<DeliveryDetails> findByDeliveryPincode(String deliveryPincode);
    
    // Find by shipment status
    List<DeliveryDetails> findByShipmentStatus(String shipmentStatus);
    
    // Find by status
    List<DeliveryDetails> findByStatus(String status);
    
    // Find by userId and status
    List<DeliveryDetails> findByUserIdAndStatus(String userId, String status);
    
    // Find all active deliveries
    List<DeliveryDetails> findByIsActiveTrue();
    
    // Find by status with pagination
    Page<DeliveryDetails> findByStatus(String status, Pageable pageable);
    
    // Check if delivery details exist for an order
    Boolean existsByOrderId(String orderId);
    
    // Find by userId with pagination
    Page<DeliveryDetails> findByUserId(String userId, Pageable pageable);
    
    // Find by shipment status with pagination
    Page<DeliveryDetails> findByShipmentStatus(String shipmentStatus, Pageable pageable);
    
    // Find non-serviceable pincodes
    List<DeliveryDetails> findByIsServiceableFalse();
    
    // Find deliveries pending creation
    List<DeliveryDetails> findByStatusAndWaybillNumberIsNull(String status);
}
