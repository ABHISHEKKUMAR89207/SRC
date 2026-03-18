package com.vtt.retail.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "retail_payment_records")
public class RetailPaymentRecord {

    @Id
    private String id;

    // ── User identity ────────────────────────────────────────────────────────
    private String userId;
    private String mobileNumber;          // for quick lookup by mobile

    // ── Our order reference ───────────────────────────────────────────────────
    private String orderId;               // ORD-XXXX  (set after verify succeeds)

    // ── Razorpay references ───────────────────────────────────────────────────
    private String razorpayOrderId;       // rzp order id (from create-order step)
    private String razorpayPaymentId;     // rzp payment id (from verify step)
    private String razorpaySignature;     // signature received from frontend

    // ── Amount details ────────────────────────────────────────────────────────
    private Double subtotal;
    private Double discount;
    private Double totalAmount;
    private Long   amountInPaise;
    private String currency = "INR";
    private String appliedCouponCode;

    // ── Payment status ────────────────────────────────────────────────────────
    // INITIATED | CAPTURED | FAILED | REFUNDED
    private String paymentStatus = "INITIATED";

    // ── Request / Response snapshots ─────────────────────────────────────────
    private String createOrderRequest;    // JSON snapshot of CreatePaymentOrderRequest
    private String createOrderResponse;   // JSON snapshot of Razorpay order creation response
    private String verifyRequest;         // JSON snapshot of VerifyPaymentRequest
    private String verifyResponse;        // JSON snapshot of our verify response

    // ── Webhook ───────────────────────────────────────────────────────────────
    private String lastWebhookEvent;      // e.g. payment.captured / payment.failed
    private String lastWebhookPayload;    // raw webhook JSON for audit

    // ── Delivery snapshot (for quick tracking without joining orders) ─────────
    private String recipientName;
    private String recipientPhone;
    private String deliveryAddressLine1;
    private String deliveryAddressLine2;
    private String deliveryCity;
    private String deliveryState;
    private String deliveryPostalCode;
    private String deliveryCountry;

    // ── Timestamps ────────────────────────────────────────────────────────────
    private LocalDateTime initiatedAt;    // when create-order was called
    private LocalDateTime capturedAt;     // when verify succeeded
    private LocalDateTime failedAt;       // when payment failed
    private LocalDateTime updatedAt;
}
