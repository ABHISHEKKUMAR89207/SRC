package com.vtt.retail.repository;

import com.vtt.retail.entities.RetailCoupon;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RetailCouponRepository extends MongoRepository<RetailCoupon, String> {

    // Find coupon by code
    Optional<RetailCoupon> findByCouponCode(String couponCode);

    // Find all active coupons
    List<RetailCoupon> findByIsActiveTrue();

    // Find coupons valid for a specific date
    List<RetailCoupon> findByValidFromBeforeAndValidUntilAfterAndIsActiveTrue(LocalDateTime validFrom, LocalDateTime validUntil);

    // Find coupons with available usage count
    List<RetailCoupon> findByMaximumUsageCountGreaterThanAndIsActiveTrue(Integer count);

    // Check if coupon code exists
    boolean existsByCouponCode(String couponCode);

    // Find all coupons created by admin
    List<RetailCoupon> findByCreatedBy(String createdBy);
}
