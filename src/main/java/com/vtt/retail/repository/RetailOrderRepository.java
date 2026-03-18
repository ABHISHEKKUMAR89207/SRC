package com.vtt.retail.repository;

import com.vtt.retail.entities.RetailOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RetailOrderRepository extends MongoRepository<RetailOrder, String> {

    // Find order by orderId
    Optional<RetailOrder> findByOrderId(String orderId);

    // Find all orders by userId
    List<RetailOrder> findByUserId(String userId);

    // Find orders by userId with pagination
    Page<RetailOrder> findByUserId(String userId, Pageable pageable);

    // Find orders by order status
    List<RetailOrder> findByOrderStatus(String orderStatus);

    // Find orders by userId and status
    List<RetailOrder> findByUserIdAndOrderStatus(String userId, String orderStatus);

    // Find paginated orders by userId and status
    Page<RetailOrder> findByUserIdAndOrderStatus(String userId, String orderStatus, Pageable pageable);

    // Find orders by payment status
    List<RetailOrder> findByPaymentStatus(String paymentStatus);

    // Check if user has any orders
    boolean existsByUserId(String userId);

    // Get order count for user
    long countByUserId(String userId);

    // Find cancelled orders
    List<RetailOrder> findByOrderStatusAndUserId(String orderStatus, String userId);
}
