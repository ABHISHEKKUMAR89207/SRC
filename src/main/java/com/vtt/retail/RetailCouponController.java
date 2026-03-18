package com.vtt.retail;

import com.vtt.retail.entities.RetailCoupon;
import com.vtt.retail.repository.RetailCouponRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/retail/coupons")
@Tag(name = "Retail Coupon Controller", description = "API for coupon and offer management")
public class RetailCouponController {

    private final RetailCouponRepository retailCouponRepository;

    /**
     * Create a new coupon (Admin only)
     */
    @PostMapping("/create")
    public ResponseEntity<?> createCoupon(
            @RequestHeader("Authorization") String token,
            @RequestBody CreateCouponRequest couponRequest) {
        try {
            // In real implementation, verify admin role from token
            
            // Validate coupon code
            if (retailCouponRepository.existsByCouponCode(couponRequest.getCouponCode())) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Coupon code already exists", null, false));
            }

            RetailCoupon coupon = new RetailCoupon();
            coupon.setCouponCode(couponRequest.getCouponCode());
            coupon.setDescription(couponRequest.getDescription());
            coupon.setDiscountType(couponRequest.getDiscountType());
            
            if ("PERCENTAGE".equalsIgnoreCase(couponRequest.getDiscountType())) {
                coupon.setDiscountPercentage(couponRequest.getDiscountPercentage());
            } else {
                coupon.setFlatDiscount(couponRequest.getFlatDiscount());
            }
            
            coupon.setMinimumOrderValue(couponRequest.getMinimumOrderValue());
            coupon.setMaximumUsageCount(couponRequest.getMaximumUsageCount() != null ? 
                    couponRequest.getMaximumUsageCount() : -1);
            coupon.setMaximumUsagePerUser(couponRequest.getMaximumUsagePerUser() != null ? 
                    couponRequest.getMaximumUsagePerUser() : 1);
            coupon.setValidFrom(couponRequest.getValidFrom());
            coupon.setValidUntil(couponRequest.getValidUntil());
            coupon.setActive(true);
            coupon.setCreatedAt(LocalDateTime.now());
            coupon.setUpdatedAt(LocalDateTime.now());

            RetailCoupon savedCoupon = retailCouponRepository.save(coupon);
            return ResponseEntity.ok(new ApiResponse<>("Coupon created successfully", savedCoupon, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get coupon by code
     */
    @GetMapping("/code/{couponCode}")
    public ResponseEntity<?> getCouponByCode(@PathVariable String couponCode) {
        try {
            Optional<RetailCoupon> couponOptional = retailCouponRepository.findByCouponCode(couponCode);

            if (couponOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Coupon not found", null, false));
            }

            RetailCoupon coupon = couponOptional.get();

            // Check if coupon is valid and active
            if (!coupon.isActive()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Coupon is not active", null, false));
            }

            if (coupon.getValidUntil().isBefore(LocalDateTime.now())) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Coupon has expired", null, false));
            }

            return ResponseEntity.ok(new ApiResponse<>("Success", coupon, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get coupon by ID
     */
    @GetMapping("/{couponId}")
    public ResponseEntity<?> getCoupon(@PathVariable String couponId) {
        try {
            Optional<RetailCoupon> couponOptional = retailCouponRepository.findById(couponId);

            if (couponOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Coupon not found", null, false));
            }

            return ResponseEntity.ok(new ApiResponse<>("Success", couponOptional.get(), true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get all active coupons
     */
    @GetMapping("/active/all")
    public ResponseEntity<?> getActiveCoupons() {
        try {
            List<RetailCoupon> coupons = retailCouponRepository.findByIsActiveTrue();
            
            // Filter expired coupons
            coupons.removeIf(coupon -> coupon.getValidUntil().isBefore(LocalDateTime.now()));

            return ResponseEntity.ok(new ApiResponse<>("Success", coupons, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get all coupons (Admin only)
     */
    @GetMapping("/admin/all")
    public ResponseEntity<?> getAllCoupons() {
        try {
            List<RetailCoupon> coupons = retailCouponRepository.findAll();
            return ResponseEntity.ok(new ApiResponse<>("Success", coupons, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Update coupon (Admin only)
     */
    @PutMapping("/{couponId}")
    public ResponseEntity<?> updateCoupon(
            @PathVariable String couponId,
            @RequestBody UpdateCouponRequest updateRequest) {
        try {
            Optional<RetailCoupon> couponOptional = retailCouponRepository.findById(couponId);

            if (couponOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Coupon not found", null, false));
            }

            RetailCoupon coupon = couponOptional.get();

            if (updateRequest.getDescription() != null) {
                coupon.setDescription(updateRequest.getDescription());
            }
            if (updateRequest.getDiscountPercentage() != null) {
                coupon.setDiscountPercentage(updateRequest.getDiscountPercentage());
            }
            if (updateRequest.getFlatDiscount() != null) {
                coupon.setFlatDiscount(updateRequest.getFlatDiscount());
            }
            if (updateRequest.getMinimumOrderValue() != null) {
                coupon.setMinimumOrderValue(updateRequest.getMinimumOrderValue());
            }
            if (updateRequest.getMaximumUsageCount() != null) {
                coupon.setMaximumUsageCount(updateRequest.getMaximumUsageCount());
            }
            if (updateRequest.getValidUntil() != null) {
                coupon.setValidUntil(updateRequest.getValidUntil());
            }
            if (updateRequest.getIsActive() != null) {
                coupon.setActive(updateRequest.getIsActive());
            }

            coupon.setUpdatedAt(LocalDateTime.now());
            RetailCoupon updatedCoupon = retailCouponRepository.save(coupon);

            return ResponseEntity.ok(new ApiResponse<>("Coupon updated successfully", updatedCoupon, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Delete coupon (Admin only)
     */
    @DeleteMapping("/{couponId}")
    public ResponseEntity<?> deleteCoupon(@PathVariable String couponId) {
        try {
            Optional<RetailCoupon> couponOptional = retailCouponRepository.findById(couponId);

            if (couponOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Coupon not found", null, false));
            }

            retailCouponRepository.deleteById(couponId);
            return ResponseEntity.ok(new ApiResponse<>("Coupon deleted successfully", null, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Validate coupon code and calculate discount
     */
    @PostMapping("/validate")
    public ResponseEntity<?> validateCoupon(@RequestBody ValidateCouponRequest validateRequest) {
        try {
            Optional<RetailCoupon> couponOptional = retailCouponRepository
                    .findByCouponCode(validateRequest.getCouponCode());

            if (couponOptional.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Invalid coupon code", null, false));
            }

            RetailCoupon coupon = couponOptional.get();

            // Validate coupon
            if (!coupon.isActive()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Coupon is not active", null, false));
            }

            if (coupon.getValidUntil().isBefore(LocalDateTime.now())) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Coupon has expired", null, false));
            }

            if (coupon.getValidFrom().isAfter(LocalDateTime.now())) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Coupon is not yet valid", null, false));
            }

            if (validateRequest.getOrderAmount() < coupon.getMinimumOrderValue()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Minimum order value of " + coupon.getMinimumOrderValue() + 
                               " required", null, false));
            }

            if (coupon.getMaximumUsageCount() != -1 && 
                coupon.getCurrentUsageCount() >= coupon.getMaximumUsageCount()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Coupon usage limit reached", null, false));
            }

            // Calculate discount
            Double discount = 0.0;
            if ("PERCENTAGE".equalsIgnoreCase(coupon.getDiscountType())) {
                discount = (validateRequest.getOrderAmount() * coupon.getDiscountPercentage()) / 100;
            } else {
                discount = coupon.getFlatDiscount();
            }

            ValidateCouponResponse response = new ValidateCouponResponse();
            response.setCouponCode(coupon.getCouponCode());
            response.setValid(true);
            response.setDiscount(discount);
            response.setFinalAmount(validateRequest.getOrderAmount() - discount);

            return ResponseEntity.ok(new ApiResponse<>("Coupon is valid", response, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    // DTOs
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateCouponRequest {
        private String couponCode;
        private String description;
        private String discountType;  // PERCENTAGE or FLAT
        private Double discountPercentage;
        private Double flatDiscount;
        private Double minimumOrderValue;
        private Integer maximumUsageCount;
        private Integer maximumUsagePerUser;
        private LocalDateTime validFrom;
        private LocalDateTime validUntil;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCouponRequest {
        private String description;
        private Double discountPercentage;
        private Double flatDiscount;
        private Double minimumOrderValue;
        private Integer maximumUsageCount;
        private LocalDateTime validUntil;
        private Boolean isActive;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidateCouponRequest {
        private String couponCode;
        private Double orderAmount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidateCouponResponse {
        private String couponCode;
        private boolean valid;
        private Double discount;
        private Double finalAmount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiResponse<T> {
        private String message;
        private T data;
        private boolean success;
    }
}
