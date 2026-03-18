package com.vtt.retail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.vtt.retail.entities.RetailCart;
import com.vtt.retail.entities.RetailCoupon;
import com.vtt.retail.entities.RetailOrder;
import com.vtt.retail.entities.RetailPaymentRecord;
import com.vtt.retail.repository.RetailCartRepository;
import com.vtt.retail.repository.RetailCouponRepository;
import com.vtt.retail.repository.RetailOrderRepository;
import com.vtt.retail.repository.RetailPaymentRecordRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.codec.digest.HmacUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Razorpay Payment Controller
 *
 * Every payment attempt — request + response — is saved to `retail_payment_records`
 * collection and can be looked up by mobile number for order tracking.
 *
 * Flow:
 *  1. POST  /api/retail/payment/create-order          → create Razorpay order, save INITIATED record
 *  2. POST  /api/retail/payment/verify                → verify signature, place order, mark CAPTURED
 *  3. POST  /api/retail/payment/webhook               → update record from Razorpay server event
 *  4. GET   /api/retail/payment/status/{rzpOrderId}   → live status from Razorpay
 *  5. GET   /api/retail/payment/track/mobile/{mobile} → all payment records for a mobile number
 *  6. GET   /api/retail/payment/track/order/{orderId} → payment record for an internal order id
 *  7. GET   /api/retail/payment/track/user/{userId}   → all records for a user
 *  8. GET   /api/retail/payment/admin/all             → admin: paginated all records
 */
@RestController
@RequestMapping("/api/retail/payment")
@Tag(name = "Retail Razorpay Payment Controller",
     description = "Razorpay online payment + full payment record tracking by mobile number")
public class RetailRazorpayController {

    // ── Repositories ─────────────────────────────────────────────────────────
    private final RetailOrderRepository         retailOrderRepository;
    private final RetailCartRepository          retailCartRepository;
    private final RetailCouponRepository        retailCouponRepository;
    private final RetailPaymentRecordRepository paymentRecordRepository;

    // ── Razorpay config ───────────────────────────────────────────────────────
    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    @Value("${razorpay.webhook.secret:}")
    private String razorpayWebhookSecret;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public RetailRazorpayController(
            RetailOrderRepository retailOrderRepository,
            RetailCartRepository retailCartRepository,
            RetailCouponRepository retailCouponRepository,
            RetailPaymentRecordRepository paymentRecordRepository) {
        this.retailOrderRepository   = retailOrderRepository;
        this.retailCartRepository    = retailCartRepository;
        this.retailCouponRepository  = retailCouponRepository;
        this.paymentRecordRepository = paymentRecordRepository;
    }

    // =========================================================================
    // 1. CREATE RAZORPAY ORDER
    // =========================================================================

    @PostMapping("/create-order")
    public ResponseEntity<?> createRazorpayOrder(
            @RequestParam String userId,
            @RequestBody CreatePaymentOrderRequest request) {
        try {
            // ── Validate cart ─────────────────────────────────────────────────
            Optional<RetailCart> cartOpt = retailCartRepository.findByUserId(userId);
            if (cartOpt.isEmpty() || cartOpt.get().getItems() == null
                    || cartOpt.get().getItems().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Cart is empty", null, false));
            }
            RetailCart cart = cartOpt.get();

            if (request.getDeliveryAddressLine1() == null
                    || request.getDeliveryAddressLine1().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Delivery address is required", null, false));
            }

            // Mobile number is mandatory for tracking
            if (request.getMobileNumber() == null || request.getMobileNumber().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Mobile number is required for payment tracking", null, false));
            }

            // ── Coupon validation & discount ──────────────────────────────────
            double subtotal  = cart.getTotalPrice();
            double discount  = 0.0;
            String couponCode = null;

            if (request.getCouponCode() != null && !request.getCouponCode().isEmpty()) {
                Optional<RetailCoupon> couponOpt = retailCouponRepository
                        .findByCouponCode(request.getCouponCode());
                if (couponOpt.isEmpty()) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse<>("Invalid coupon code", null, false));
                }
                RetailCoupon coupon = couponOpt.get();
                if (!coupon.isActive()) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse<>("Coupon is not active", null, false));
                }
                if (coupon.getValidUntil().isBefore(LocalDateTime.now())) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse<>("Coupon has expired", null, false));
                }
                if (subtotal < coupon.getMinimumOrderValue()) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse<>(
                                    "Minimum order value ₹" + coupon.getMinimumOrderValue() + " required",
                                    null, false));
                }
                if (coupon.getMaximumUsageCount() != -1
                        && coupon.getCurrentUsageCount() >= coupon.getMaximumUsageCount()) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse<>("Coupon usage limit reached", null, false));
                }
                discount = "PERCENTAGE".equalsIgnoreCase(coupon.getDiscountType())
                        ? (subtotal * coupon.getDiscountPercentage()) / 100.0
                        : coupon.getFlatDiscount();
                couponCode = coupon.getCouponCode();
            }

            double totalAmount   = subtotal - discount;
            long   amountInPaise = Math.round(totalAmount * 100);

            if (totalAmount < 1) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Order amount must be at least ₹1", null, false));
            }

            // ── Call Razorpay ─────────────────────────────────────────────────
            RazorpayClient client = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

            JSONObject rzpRequest = new JSONObject();
            rzpRequest.put("amount", amountInPaise);
            rzpRequest.put("currency", "INR");
            rzpRequest.put("receipt", "rcpt_" + UUID.randomUUID().toString().substring(0, 10));
            rzpRequest.put("payment_capture", 1);

            JSONObject notes = new JSONObject();
            notes.put("userId", userId);
            notes.put("mobile", request.getMobileNumber());
            if (couponCode != null) notes.put("couponCode", couponCode);
            rzpRequest.put("notes", notes);

            Order  rzpOrder   = client.Orders.create(rzpRequest);
            String rzpOrderId = rzpOrder.get("id");

            // ── Save INITIATED payment record ─────────────────────────────────
            RetailPaymentRecord record = new RetailPaymentRecord();
            record.setUserId(userId);
            record.setMobileNumber(request.getMobileNumber());
            record.setRazorpayOrderId(rzpOrderId);
            record.setSubtotal(subtotal);
            record.setDiscount(discount);
            record.setTotalAmount(totalAmount);
            record.setAmountInPaise(amountInPaise);
            record.setCurrency("INR");
            record.setAppliedCouponCode(couponCode);
            record.setPaymentStatus("INITIATED");
            record.setRecipientName(request.getRecipientName());
            record.setRecipientPhone(request.getRecipientPhone());
            record.setDeliveryAddressLine1(request.getDeliveryAddressLine1());
            record.setDeliveryAddressLine2(request.getDeliveryAddressLine2());
            record.setDeliveryCity(request.getDeliveryCity());
            record.setDeliveryState(request.getDeliveryState());
            record.setDeliveryPostalCode(request.getDeliveryPostalCode());
            record.setDeliveryCountry(request.getDeliveryCountry());
            record.setCreateOrderRequest(toJson(request));
            record.setCreateOrderResponse(rzpOrder.toString());
            record.setInitiatedAt(LocalDateTime.now());
            record.setUpdatedAt(LocalDateTime.now());

            paymentRecordRepository.save(record);

            // ── Build API response ────────────────────────────────────────────
            PaymentOrderResponse response = new PaymentOrderResponse();
            response.setRazorpayOrderId(rzpOrderId);
            response.setAmount(amountInPaise);
            response.setCurrency("INR");
            response.setKeyId(razorpayKeyId);
            response.setSubtotal(subtotal);
            response.setDiscount(discount);
            response.setTotalAmount(totalAmount);
            response.setAppliedCouponCode(couponCode);
            response.setMobileNumber(request.getMobileNumber());
            response.setDeliveryAddressLine1(request.getDeliveryAddressLine1());
            response.setDeliveryAddressLine2(request.getDeliveryAddressLine2());
            response.setDeliveryCity(request.getDeliveryCity());
            response.setDeliveryState(request.getDeliveryState());
            response.setDeliveryPostalCode(request.getDeliveryPostalCode());
            response.setDeliveryCountry(request.getDeliveryCountry());
            response.setDeliveryLandmark(request.getDeliveryLandmark());
            response.setRecipientName(request.getRecipientName());
            response.setRecipientPhone(request.getRecipientPhone());

            return ResponseEntity.ok(new ApiResponse<>("Razorpay order created", response, true));

        } catch (RazorpayException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(new ApiResponse<>("Razorpay error: " + e.getMessage(), null, false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    // =========================================================================
    // 2. VERIFY PAYMENT & PLACE ORDER
    // =========================================================================

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPaymentAndPlaceOrder(
            @RequestParam String userId,
            @RequestBody VerifyPaymentRequest verifyRequest) {
        try {
            // ── Signature verification ────────────────────────────────────────
            String generated = new HmacUtils("HmacSHA256", razorpayKeySecret)
                    .hmacHex(verifyRequest.getRazorpayOrderId() + "|" + verifyRequest.getRazorpayPaymentId());

            if (!generated.equals(verifyRequest.getRazorpaySignature())) {
                updateRecordOnFailure(verifyRequest.getRazorpayOrderId(),
                        "FAILED", toJson(verifyRequest), "Invalid signature");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>("Payment verification failed: invalid signature", null, false));
            }

            // ── Validate cart ─────────────────────────────────────────────────
            Optional<RetailCart> cartOpt = retailCartRepository.findByUserId(userId);
            if (cartOpt.isEmpty() || cartOpt.get().getItems() == null
                    || cartOpt.get().getItems().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Cart is empty or already processed", null, false));
            }
            RetailCart cart = cartOpt.get();

            // ── Recalculate amounts & increment coupon usage ──────────────────
            double subtotal    = cart.getTotalPrice();
            double discount    = 0.0;
            Double couponValue = null;

            if (verifyRequest.getAppliedCouponCode() != null
                    && !verifyRequest.getAppliedCouponCode().isEmpty()) {
                Optional<RetailCoupon> couponOpt = retailCouponRepository
                        .findByCouponCode(verifyRequest.getAppliedCouponCode());
                if (couponOpt.isPresent()) {
                    RetailCoupon coupon = couponOpt.get();
                    if (coupon.isActive() && coupon.getValidUntil().isAfter(LocalDateTime.now())) {
                        discount = "PERCENTAGE".equalsIgnoreCase(coupon.getDiscountType())
                                ? (subtotal * coupon.getDiscountPercentage()) / 100.0
                                : coupon.getFlatDiscount();
                        couponValue = "PERCENTAGE".equalsIgnoreCase(coupon.getDiscountType())
                                ? coupon.getDiscountPercentage()
                                : coupon.getFlatDiscount();
                        coupon.setCurrentUsageCount(coupon.getCurrentUsageCount() + 1);
                        retailCouponRepository.save(coupon);
                    }
                }
            }

            double totalAmount = subtotal - discount;

            // ── Build & save RetailOrder ──────────────────────────────────────
            RetailOrder order = new RetailOrder();
            order.setOrderId("ORD-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase());
            order.setUserId(userId);
            order.setItems(new java.util.ArrayList<>());

            for (RetailCart.CartItem ci : cart.getItems()) {
                RetailOrder.OrderItem oi = new RetailOrder.OrderItem();
                oi.setProductId(ci.getProductId());
                oi.setProductName(ci.getProductName());
                oi.setSelectedSize(ci.getSelectedSize());
                oi.setQuantity(ci.getQuantity());
                oi.setPricePerUnit(ci.getPricePerUnit());
                oi.setTotalPrice(ci.getTotalPrice());
                oi.setProductImage(ci.getProductImage());
                order.getItems().add(oi);
            }

            order.setSubtotal(subtotal);
            order.setDiscount(discount);
            order.setCouponCode(couponValue);
            order.setTaxes(0.0);
            order.setTotalAmount(totalAmount);
            order.setDeliveryAddressLine1(verifyRequest.getDeliveryAddressLine1());
            order.setDeliveryAddressLine2(verifyRequest.getDeliveryAddressLine2());
            order.setDeliveryCity(verifyRequest.getDeliveryCity());
            order.setDeliveryState(verifyRequest.getDeliveryState());
            order.setDeliveryPostalCode(verifyRequest.getDeliveryPostalCode());
            order.setDeliveryCountry(verifyRequest.getDeliveryCountry());
            order.setDeliveryLandmark(verifyRequest.getDeliveryLandmark());
            order.setRecipientName(verifyRequest.getRecipientName());
            order.setRecipientPhone(verifyRequest.getRecipientPhone());
            order.setPaymentMethod("ONLINE");
            order.setPaymentStatus("COMPLETED");
            order.setOrderStatus("PLACED");
            order.setTrackingId("TRACK-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase());
            order.setPlacedAt(LocalDateTime.now());
            order.setCreatedAt(LocalDateTime.now());
            order.setUpdatedAt(LocalDateTime.now());

            RetailOrder savedOrder = retailOrderRepository.save(order);

            // ── Clear cart ────────────────────────────────────────────────────
            retailCartRepository.deleteByUserId(userId);

            // ── Build success response ────────────────────────────────────────
            PaymentSuccessResponse successResponse = new PaymentSuccessResponse();
            successResponse.setOrderId(savedOrder.getOrderId());
            successResponse.setRazorpayOrderId(verifyRequest.getRazorpayOrderId());
            successResponse.setRazorpayPaymentId(verifyRequest.getRazorpayPaymentId());
            successResponse.setTotalAmount(totalAmount);
            successResponse.setPaymentStatus("COMPLETED");
            successResponse.setOrderStatus("PLACED");

            // ── Update payment record → CAPTURED ─────────────────────────────
            paymentRecordRepository.findByRazorpayOrderId(verifyRequest.getRazorpayOrderId())
                    .ifPresent(rec -> {
                        rec.setRazorpayPaymentId(verifyRequest.getRazorpayPaymentId());
                        rec.setRazorpaySignature(verifyRequest.getRazorpaySignature());
                        rec.setOrderId(savedOrder.getOrderId());
                        rec.setPaymentStatus("CAPTURED");
                        rec.setVerifyRequest(toJson(verifyRequest));
                        rec.setVerifyResponse(toJson(successResponse));
                        rec.setCapturedAt(LocalDateTime.now());
                        rec.setUpdatedAt(LocalDateTime.now());
                        paymentRecordRepository.save(rec);
                    });

            return ResponseEntity.ok(new ApiResponse<>("Payment successful! Order placed.", successResponse, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    // =========================================================================
    // 3. WEBHOOK
    // =========================================================================

    @PostMapping("/webhook")
    public ResponseEntity<?> handleWebhook(
            @RequestHeader(value = "X-Razorpay-Signature", required = false) String webhookSig,
            @RequestBody String payload) {
        try {
            if (razorpayWebhookSecret != null && !razorpayWebhookSecret.isEmpty()) {
                String gen = new HmacUtils("HmacSHA256", razorpayWebhookSecret).hmacHex(payload);
                if (!gen.equals(webhookSig)) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(new ApiResponse<>("Invalid webhook signature", null, false));
                }
            }

            JSONObject body   = new JSONObject(payload);
            String     event  = body.optString("event");
            JSONObject entity = body.optJSONObject("payload")
                                    .optJSONObject("payment")
                                    .optJSONObject("entity");

            if (entity == null) {
                return ResponseEntity.ok(new ApiResponse<>("Webhook received – no entity", null, true));
            }

            String rzpOrderId = entity.optString("order_id");

            paymentRecordRepository.findByRazorpayOrderId(rzpOrderId).ifPresent(rec -> {
                rec.setLastWebhookEvent(event);
                rec.setLastWebhookPayload(payload);
                rec.setUpdatedAt(LocalDateTime.now());

                switch (event) {
                    case "payment.captured":
                        rec.setPaymentStatus("CAPTURED");
                        rec.setCapturedAt(LocalDateTime.now());
                        if (rec.getOrderId() != null) {
                            retailOrderRepository.findByOrderId(rec.getOrderId()).ifPresent(o -> {
                                o.setPaymentStatus("COMPLETED");
                                o.setUpdatedAt(LocalDateTime.now());
                                retailOrderRepository.save(o);
                            });
                        }
                        break;
                    case "payment.failed":
                        rec.setPaymentStatus("FAILED");
                        rec.setFailedAt(LocalDateTime.now());
                        if (rec.getOrderId() != null) {
                            retailOrderRepository.findByOrderId(rec.getOrderId()).ifPresent(o -> {
                                o.setPaymentStatus("FAILED");
                                o.setOrderStatus("PAYMENT_FAILED");
                                o.setUpdatedAt(LocalDateTime.now());
                                retailOrderRepository.save(o);
                            });
                        }
                        break;
                    default:
                        break;
                }
                paymentRecordRepository.save(rec);
            });

            return ResponseEntity.ok(new ApiResponse<>("Webhook processed", event, true));

        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>("Webhook error (logged)", null, true));
        }
    }

    // =========================================================================
    // 4. LIVE STATUS (Razorpay API)
    // =========================================================================

    @GetMapping("/status/{razorpayOrderId}")
    public ResponseEntity<?> getPaymentStatus(@PathVariable String razorpayOrderId) {
        try {
            RazorpayClient client = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
            Order rzpOrder = client.Orders.fetch(razorpayOrderId);

            PaymentStatusResponse resp = new PaymentStatusResponse();
            resp.setRazorpayOrderId(razorpayOrderId);
            resp.setStatus(rzpOrder.get("status"));
            resp.setAmountPaise((Integer) rzpOrder.get("amount"));
            resp.setAmountPaid((Integer) rzpOrder.get("amount_paid"));
            resp.setCurrency(rzpOrder.get("currency"));

            return ResponseEntity.ok(new ApiResponse<>("Success", resp, true));

        } catch (RazorpayException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(new ApiResponse<>("Razorpay error: " + e.getMessage(), null, false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    // =========================================================================
    // 5. TRACK BY MOBILE NUMBER
    // =========================================================================

    /** All payment attempts for a mobile number — most recent first. */
    @GetMapping("/track/mobile/{mobileNumber}")
    public ResponseEntity<?> trackByMobile(@PathVariable String mobileNumber) {
        try {
            List<RetailPaymentRecord> records =
                    paymentRecordRepository.findByMobileNumberOrderByInitiatedAtDesc(mobileNumber);

            if (records.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("No payment records found for this mobile number", null, false));
            }
            return ResponseEntity.ok(new ApiResponse<>("Success", records, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /** Payment records for a mobile filtered by status (INITIATED | CAPTURED | FAILED | REFUNDED). */
    @GetMapping("/track/mobile/{mobileNumber}/status/{status}")
    public ResponseEntity<?> trackByMobileAndStatus(
            @PathVariable String mobileNumber,
            @PathVariable String status) {
        try {
            List<RetailPaymentRecord> records =
                    paymentRecordRepository
                            .findByMobileNumberAndPaymentStatusOrderByInitiatedAtDesc(
                                    mobileNumber, status.toUpperCase());

            return ResponseEntity.ok(new ApiResponse<>("Success", records, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    // =========================================================================
    // 6. TRACK BY INTERNAL ORDER ID
    // =========================================================================

    @GetMapping("/track/order/{orderId}")
    public ResponseEntity<?> trackByOrderId(@PathVariable String orderId) {
        try {
            Optional<RetailPaymentRecord> record = paymentRecordRepository.findByOrderId(orderId);

            if (record.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("No payment record found for order: " + orderId, null, false));
            }
            return ResponseEntity.ok(new ApiResponse<>("Success", record.get(), true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    // =========================================================================
    // 7. TRACK BY USER ID
    // =========================================================================

    @GetMapping("/track/user/{userId}")
    public ResponseEntity<?> trackByUserId(@PathVariable String userId) {
        try {
            List<RetailPaymentRecord> records =
                    paymentRecordRepository.findByUserIdOrderByInitiatedAtDesc(userId);

            return ResponseEntity.ok(new ApiResponse<>("Success", records, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    // =========================================================================
    // 8. ADMIN – ALL RECORDS (paginated)
    // =========================================================================

    @GetMapping("/admin/all")
    public ResponseEntity<?> getAllPaymentRecords(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<RetailPaymentRecord> records =
                    paymentRecordRepository.findAllByOrderByInitiatedAtDesc(pageable);

            return ResponseEntity.ok(new ApiResponse<>("Success", records, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    // =========================================================================
    // PRIVATE HELPERS
    // =========================================================================

    private String toJson(Object obj) {
        try { return objectMapper.writeValueAsString(obj); }
        catch (Exception e) { return obj.toString(); }
    }

    private void updateRecordOnFailure(String rzpOrderId, String status,
                                       String verifyReqJson, String reason) {
        try {
            paymentRecordRepository.findByRazorpayOrderId(rzpOrderId).ifPresent(rec -> {
                rec.setPaymentStatus(status);
                rec.setVerifyRequest(verifyReqJson);
                rec.setVerifyResponse("{\"error\":\"" + reason + "\"}");
                rec.setFailedAt(LocalDateTime.now());
                rec.setUpdatedAt(LocalDateTime.now());
                paymentRecordRepository.save(rec);
            });
        } catch (Exception ignored) { }
    }

    // =========================================================================
    // DTOs
    // =========================================================================

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class CreatePaymentOrderRequest {
        private String couponCode;
        private String mobileNumber;          // required – used as primary tracking key
        private String deliveryAddressLine1;
        private String deliveryAddressLine2;
        private String deliveryCity;
        private String deliveryState;
        private String deliveryPostalCode;
        private String deliveryCountry;
        private String deliveryLandmark;
        private String recipientName;
        private String recipientPhone;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class PaymentOrderResponse {
        private String razorpayOrderId;
        private Long   amount;
        private String currency;
        private String keyId;
        private Double subtotal;
        private Double discount;
        private Double totalAmount;
        private String appliedCouponCode;
        private String mobileNumber;
        private String deliveryAddressLine1;
        private String deliveryAddressLine2;
        private String deliveryCity;
        private String deliveryState;
        private String deliveryPostalCode;
        private String deliveryCountry;
        private String deliveryLandmark;
        private String recipientName;
        private String recipientPhone;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class VerifyPaymentRequest {
        private String razorpayOrderId;
        private String razorpayPaymentId;
        private String razorpaySignature;
        private String appliedCouponCode;
        private String deliveryAddressLine1;
        private String deliveryAddressLine2;
        private String deliveryCity;
        private String deliveryState;
        private String deliveryPostalCode;
        private String deliveryCountry;
        private String deliveryLandmark;
        private String recipientName;
        private String recipientPhone;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class PaymentSuccessResponse {
        private String orderId;
        private String razorpayOrderId;
        private String razorpayPaymentId;
        private Double totalAmount;
        private String paymentStatus;
        private String orderStatus;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class PaymentStatusResponse {
        private String  razorpayOrderId;
        private String  status;
        private Integer amountPaise;
        private Integer amountPaid;
        private String  currency;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class ApiResponse<T> {
        private String  message;
        private T       data;
        private boolean success;
    }
}
