package com.vtt.retail.repository;

import com.vtt.retail.entities.RetailPaymentRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RetailPaymentRecordRepository extends MongoRepository<RetailPaymentRecord, String> {

    // By mobile number  ← primary tracking lookup
    List<RetailPaymentRecord> findByMobileNumberOrderByInitiatedAtDesc(String mobileNumber);

    // By userId
    List<RetailPaymentRecord> findByUserIdOrderByInitiatedAtDesc(String userId);

    // By razorpay order id
    Optional<RetailPaymentRecord> findByRazorpayOrderId(String razorpayOrderId);

    // By our internal orderId
    Optional<RetailPaymentRecord> findByOrderId(String orderId);

    // By payment status
    List<RetailPaymentRecord> findByPaymentStatusOrderByInitiatedAtDesc(String paymentStatus);

    // By mobile + status
    List<RetailPaymentRecord> findByMobileNumberAndPaymentStatusOrderByInitiatedAtDesc(
            String mobileNumber, String paymentStatus);

    // Paginated – all records
    Page<RetailPaymentRecord> findAllByOrderByInitiatedAtDesc(Pageable pageable);
}
