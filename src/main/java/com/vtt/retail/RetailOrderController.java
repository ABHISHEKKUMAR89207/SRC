package com.vtt.retail;

import com.vtt.retail.entities.RetailCart;
import com.vtt.retail.entities.RetailCoupon;
import com.vtt.retail.entities.RetailOrder;
import com.vtt.retail.repository.RetailCartRepository;
import com.vtt.retail.repository.RetailCouponRepository;
import com.vtt.retail.repository.RetailOrderRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/retail/orders")
@Tag(name = "Retail Order Controller", description = "API for order management")
public class RetailOrderController {

    private final RetailOrderRepository retailOrderRepository;
    private final RetailCartRepository retailCartRepository;
    private final RetailCouponRepository retailCouponRepository;

    /**
     * Place order from cart
     */
    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(
            @RequestParam String userId,
            @RequestBody PlaceOrderRequest orderRequest) {
        try {
            // Get cart
            Optional<RetailCart> cartOptional = retailCartRepository.findByUserId(userId);
            if (cartOptional.isEmpty() || cartOptional.get().getItems() == null || 
                cartOptional.get().getItems().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Cart is empty", null, false));
            }

            RetailCart cart = cartOptional.get();

            // Validate delivery address
            if (orderRequest.getDeliveryAddressLine1() == null || 
                orderRequest.getDeliveryAddressLine1().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Delivery address is required", null, false));
            }

            // Create order
            RetailOrder order = new RetailOrder();
            order.setOrderId("ORD-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase());
            order.setUserId(userId);

            // Copy items from cart
            order.setItems(new java.util.ArrayList<>());
            for (RetailCart.CartItem cartItem : cart.getItems()) {
                RetailOrder.OrderItem orderItem = new RetailOrder.OrderItem();
                orderItem.setProductId(cartItem.getProductId());
                orderItem.setProductName(cartItem.getProductName());
                orderItem.setSelectedSize(cartItem.getSelectedSize());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPricePerUnit(cartItem.getPricePerUnit());
                orderItem.setTotalPrice(cartItem.getTotalPrice());
                orderItem.setProductImage(cartItem.getProductImage());
                order.getItems().add(orderItem);
            }

            // Set pricing
            order.setSubtotal(cart.getTotalPrice());
            order.setTaxes(0.0);  // No taxes for now

            // Apply coupon if provided
            if (orderRequest.getCouponCode() != null && !orderRequest.getCouponCode().isEmpty()) {
                Optional<RetailCoupon> couponOptional = retailCouponRepository
                        .findByCouponCode(orderRequest.getCouponCode());

                if (couponOptional.isPresent()) {
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

                    if (order.getSubtotal() < coupon.getMinimumOrderValue()) {
                        return ResponseEntity.badRequest()
                                .body(new ApiResponse<>("Minimum order value of " + coupon.getMinimumOrderValue() + 
                                       " required for this coupon", null, false));
                    }

                    // Apply discount
                    if ("PERCENTAGE".equalsIgnoreCase(coupon.getDiscountType())) {
                        order.setDiscount((order.getSubtotal() * coupon.getDiscountPercentage()) / 100);
                    } else {
                        order.setDiscount(coupon.getFlatDiscount());
                    }
                    order.setCouponCode(coupon.getDiscountPercentage());
                } else {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse<>("Invalid coupon code", null, false));
                }
            }

            // Calculate total amount
            order.setTotalAmount(order.getSubtotal() - order.getDiscount() + order.getTaxes());

            // Set delivery address
            order.setDeliveryAddressLine1(orderRequest.getDeliveryAddressLine1());
            order.setDeliveryAddressLine2(orderRequest.getDeliveryAddressLine2());
            order.setDeliveryCity(orderRequest.getDeliveryCity());
            order.setDeliveryState(orderRequest.getDeliveryState());
            order.setDeliveryPostalCode(orderRequest.getDeliveryPostalCode());
            order.setDeliveryCountry(orderRequest.getDeliveryCountry());
            order.setDeliveryLandmark(orderRequest.getDeliveryLandmark());
            order.setRecipientName(orderRequest.getRecipientName());
            order.setRecipientPhone(orderRequest.getRecipientPhone());

            // Set payment and order details
            order.setPaymentMethod("COD");
            order.setPaymentStatus("PENDING");
            order.setOrderStatus("PLACED");
            order.setTrackingId("TRACK-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase());
            order.setPlacedAt(LocalDateTime.now());
            order.setCreatedAt(LocalDateTime.now());
            order.setUpdatedAt(LocalDateTime.now());

            // Save order
            RetailOrder savedOrder = retailOrderRepository.save(order);

            // Clear cart
            retailCartRepository.deleteByUserId(userId);

            return ResponseEntity.ok(new ApiResponse<>("Order placed successfully", savedOrder, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get order by orderId
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable String orderId) {
        try {
            Optional<RetailOrder> orderOptional = retailOrderRepository.findByOrderId(orderId);

            if (orderOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Order not found", null, false));
            }

            return ResponseEntity.ok(new ApiResponse<>("Success", orderOptional.get(), true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get all orders for a user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserOrders(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "placedAt"));
            Page<RetailOrder> orders = retailOrderRepository.findByUserId(userId, pageable);

            return ResponseEntity.ok(new ApiResponse<>("Success", orders, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get orders by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getOrdersByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<RetailOrder> orders = retailOrderRepository.findByOrderStatus(status);
            return ResponseEntity.ok(new ApiResponse<>("Success", orders, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get user orders by status
     */
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<?> getUserOrdersByStatus(
            @PathVariable String userId,
            @PathVariable String status) {
        try {
            List<RetailOrder> orders = retailOrderRepository.findByUserIdAndOrderStatus(userId, status);
            return ResponseEntity.ok(new ApiResponse<>("Success", orders, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Update order status
     */
    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable String orderId,
            @RequestBody UpdateStatusRequest statusRequest) {
        try {
            Optional<RetailOrder> orderOptional = retailOrderRepository.findByOrderId(orderId);

            if (orderOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Order not found", null, false));
            }

            RetailOrder order = orderOptional.get();
            String newStatus = statusRequest.getStatus();

            // Update status and timestamps
            order.setOrderStatus(newStatus);
            if ("SHIPPED".equalsIgnoreCase(newStatus)) {
                order.setShippedAt(LocalDateTime.now());
            } else if ("DELIVERED".equalsIgnoreCase(newStatus)) {
                order.setDeliveredAt(LocalDateTime.now());
            }

            order.setUpdatedAt(LocalDateTime.now());
            RetailOrder updatedOrder = retailOrderRepository.save(order);

            return ResponseEntity.ok(new ApiResponse<>("Order status updated", updatedOrder, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Cancel order
     */
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable String orderId) {
        try {
            Optional<RetailOrder> orderOptional = retailOrderRepository.findByOrderId(orderId);

            if (orderOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Order not found", null, false));
            }

            RetailOrder order = orderOptional.get();

            // Can only cancel PLACED orders
            if (!"PLACED".equalsIgnoreCase(order.getOrderStatus())) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Cannot cancel order in " + order.getOrderStatus() + " status", null, false));
            }

            order.setOrderStatus("CANCELLED");
            order.setCancelledAt(LocalDateTime.now());
            order.setUpdatedAt(LocalDateTime.now());

            RetailOrder cancelledOrder = retailOrderRepository.save(order);
            return ResponseEntity.ok(new ApiResponse<>("Order cancelled successfully", cancelledOrder, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Return order
     */
    @PutMapping("/{orderId}/return")
    public ResponseEntity<?> returnOrder(
            @PathVariable String orderId,
            @RequestBody ReturnOrderRequest returnRequest) {
        try {
            Optional<RetailOrder> orderOptional = retailOrderRepository.findByOrderId(orderId);

            if (orderOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Order not found", null, false));
            }

            RetailOrder order = orderOptional.get();

            // Can only return DELIVERED orders
            if (!"DELIVERED".equalsIgnoreCase(order.getOrderStatus())) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Can only return delivered orders", null, false));
            }

            order.setOrderStatus("RETURNED");
            order.setReturned(true);
            order.setReturnedAt(LocalDateTime.now());
            order.setReturnReason(returnRequest.getReturnReason());
            order.setUpdatedAt(LocalDateTime.now());

            RetailOrder returnedOrder = retailOrderRepository.save(order);
            return ResponseEntity.ok(new ApiResponse<>("Order returned successfully", returnedOrder, true));

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
    public static class PlaceOrderRequest {
        private String couponCode;
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

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateStatusRequest {
        private String status;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReturnOrderRequest {
        private String returnReason;
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
