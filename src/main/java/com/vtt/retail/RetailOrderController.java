package com.vtt.retail;

import com.vtt.entities.ProductInventory;
import com.vtt.entities.ProductSets;
import com.vtt.repository.ProductSetsRepository;
import com.vtt.retail.entities.RetailCart;
import com.vtt.retail.entities.RetailCoupon;
import com.vtt.retail.entities.RetailOrder;
import com.vtt.retail.repository.RetailCartRepository;
import com.vtt.retail.repository.RetailCouponRepository;
import com.vtt.retail.repository.RetailOrderRepository;
import com.vtt.retail.repository.RetailProductRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final RetailProductRepository productInventoryRepository;
    private final RetailProductRepository retailProductRepository;
    @Autowired
    private ProductSetsRepository productSetsRepository;
    private ProductInventory applySetBreak(ProductInventory product, String requiredSize, int requiredQty) {

        List<ProductSets> sets = productSetsRepository
                .findByColorAndDisplayNamesCatAndFabric(
                        product.getColor(),
                        product.getDisplayNamesCat(),
                        product.getFabric()
                );

        if (sets == null || sets.isEmpty()) return product;

        if (product.getSizes() == null) {
            product.setSizes(new ArrayList<>());
        }

        List<ProductSets> updatedProductSets = new ArrayList<>();

        int remainingQty = requiredQty; // 👉 track remaining requirement

        for (ProductSets set : sets) {

            if (set.getSizes() == null || set.getTotalQuantity() <= 0) continue;

            int sizePerSet = 0;

            // 👉 find required size per set
            for (ProductSets.SizeQuantity setSize : set.getSizes()) {
                if (setSize.getLabel().equalsIgnoreCase(requiredSize)) {
                    sizePerSet = setSize.getQuantity();
                    break;
                }
            }

            if (sizePerSet == 0) continue;

            // 👉 how many sets needed from THIS set
            int setsNeeded = (int) Math.ceil((double) remainingQty / sizePerSet);
            setsNeeded = Math.min(setsNeeded, set.getTotalQuantity());

            if (setsNeeded <= 0) continue;

            // 👉 BREAK sets
            for (ProductSets.SizeQuantity setSize : set.getSizes()) {

                int addQty = setsNeeded * setSize.getQuantity();
                boolean found = false;

                for (ProductInventory.SizeQuantity prodSize : product.getSizes()) {

                    if (prodSize.getLabel().equalsIgnoreCase(setSize.getLabel())) {

                        prodSize.setQuantity(prodSize.getQuantity() + addQty);
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    product.getSizes().add(
                            new ProductInventory.SizeQuantity(
                                    setSize.getLabel(),
                                    addQty,
                                    set.getFabric().getRetailPrice(),
                                    set.getFabric().getWholesalePrice()
                            )
                    );
                }
            }

            // 👉 reduce set quantity
            set.setTotalQuantity(set.getTotalQuantity() - setsNeeded);
            updatedProductSets.add(set);

            // 👉 reduce remaining qty
            remainingQty -= (setsNeeded * sizePerSet);

            // ✅ if requirement fulfilled → STOP
            if (remainingQty <= 0) {
                break;
            }
        }

        // 👉 save only updated sets
        if (!updatedProductSets.isEmpty()) {
            productSetsRepository.saveAll(updatedProductSets);
            retailProductRepository.save(product);
        }

        return product;
    }
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

            List<ProductInventory> updatedProducts = new ArrayList<>();

            // Copy items from cart
            order.setItems(new java.util.ArrayList<>());
            for (RetailCart.CartItem cartItem : cart.getItems()) {

                ProductInventory product = productInventoryRepository
                        .findById(cartItem.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found"));

                // 👉 STEP 1: Try normal inventory first
                ProductInventory.SizeQuantity matchedSize = null;

                if (product.getSizes() != null) {
                    for (ProductInventory.SizeQuantity size : product.getSizes()) {
                        if (size.getLabel().equalsIgnoreCase(cartItem.getSelectedSize())) {
                            matchedSize = size;
                            break;
                        }
                    }
                }

                // 👉 STEP 2: If not found OR insufficient → break set
                if (matchedSize == null || matchedSize.getQuantity() < cartItem.getQuantity()) {

                    int availableQty = (matchedSize != null) ? matchedSize.getQuantity() : 0;
                    int requiredQty = cartItem.getQuantity() - availableQty;

                    // 🔥 APPLY SET BREAK
                    product = applySetBreak(product, cartItem.getSelectedSize(), requiredQty);

                    // 👉 re-fetch updated size
                    matchedSize = null;
                    for (ProductInventory.SizeQuantity size : product.getSizes()) {
                        if (size.getLabel().equalsIgnoreCase(cartItem.getSelectedSize())) {
                            matchedSize = size;
                            break;
                        }
                    }

                    // ❌ still not enough → fail
                    if (matchedSize == null || matchedSize.getQuantity() < cartItem.getQuantity()) {
                        return ResponseEntity.badRequest()
                                .body(new ApiResponse<>("Insufficient stock even after breaking sets for " +
                                        cartItem.getProductName(), null, false));
                    }
                }

                // 👉 STEP 3: Subtract inventory
                matchedSize.setQuantity(matchedSize.getQuantity() - cartItem.getQuantity());

                updatedProducts.add(product);

                // 👉 add to order
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
//            for (RetailCart.CartItem cartItem : cart.getItems()) {
//                // 🔹 Fetch ProductInventory
//                ProductInventory product = productInventoryRepository
//                        .findById(cartItem.getProductId())
//                        .orElseThrow(() -> new RuntimeException("Product not found"));
//
//                // 🔹 Find size and subtract quantity
//                boolean sizeFound = false;
//                for (ProductInventory.SizeQuantity size : product.getSizes()) {
//                    if (size.getLabel().equalsIgnoreCase(cartItem.getSelectedSize())) {
//
//                        if (size.getQuantity() < cartItem.getQuantity()) {
//                            return ResponseEntity.badRequest()
//                                    .body(new ApiResponse<>("Insufficient stock for " +
//                                            cartItem.getProductName() + " size " +
//                                            cartItem.getSelectedSize(), null, false));
//                        }
//
//                        // ✅ Subtract inventory
//                        size.setQuantity(size.getQuantity() - cartItem.getQuantity());
//                        sizeFound = true;
//                        break;
//                    }
//                }
//
//                if (!sizeFound) {
//                    return ResponseEntity.badRequest()
//                            .body(new ApiResponse<>("Size not available for product " +
//                                    cartItem.getProductName(), null, false));
//                }
//                updatedProducts.add(product);
//
//                RetailOrder.OrderItem orderItem = new RetailOrder.OrderItem();
//                orderItem.setProductId(cartItem.getProductId());
//                orderItem.setProductName(cartItem.getProductName());
//                orderItem.setSelectedSize(cartItem.getSelectedSize());
//                orderItem.setQuantity(cartItem.getQuantity());
//                orderItem.setPricePerUnit(cartItem.getPricePerUnit());
//                orderItem.setTotalPrice(cartItem.getTotalPrice());
//                orderItem.setProductImage(cartItem.getProductImage());
//                order.getItems().add(orderItem);
//            }

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
            // 3. UPDATE INVENTORY IN BULK
            productInventoryRepository.saveAll(updatedProducts);

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
     * Update order status + restore inventory if CANCELLED
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
            String oldStatus = order.getOrderStatus();

            // 👉 If changing to CANCELLED → restore inventory
            if ("CANCELLED".equalsIgnoreCase(newStatus) &&
                    !"CANCELLED".equalsIgnoreCase(oldStatus)) {

                List<ProductInventory> updatedProducts = new ArrayList<>();

                for (RetailOrder.OrderItem item : order.getItems()) {

                    ProductInventory product = productInventoryRepository
                            .findById(item.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found"));

                    boolean sizeFound = false;

                    for (ProductInventory.SizeQuantity size : product.getSizes()) {
                        if (size.getLabel().equalsIgnoreCase(item.getSelectedSize())) {

                            // ✅ restore quantity
                            size.setQuantity(size.getQuantity() + item.getQuantity());
                            sizeFound = true;
                            break;
                        }
                    }

                    if (!sizeFound) {
                        return ResponseEntity.badRequest()
                                .body(new ApiResponse<>("Size not found for product " +
                                        item.getProductName(), null, false));
                    }

                    updatedProducts.add(product);
                }

                // save inventory back
                productInventoryRepository.saveAll(updatedProducts);

                order.setCancelledAt(LocalDateTime.now());
            }

            // 👉 Update status timestamps
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
     * Cancel order + restore inventory
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

            // Only cancel allowed for PLACED orders
            if (!"PLACED".equalsIgnoreCase(order.getOrderStatus())) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Cannot cancel order in " + order.getOrderStatus() + " status", null, false));
            }

            // 👉 Restore inventory
            List<ProductInventory> updatedProducts = new ArrayList<>();

            for (RetailOrder.OrderItem item : order.getItems()) {

                ProductInventory product = productInventoryRepository
                        .findById(item.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found"));

                boolean sizeFound = false;

                for (ProductInventory.SizeQuantity size : product.getSizes()) {
                    if (size.getLabel().equalsIgnoreCase(item.getSelectedSize())) {

                        // ✅ Add quantity back
                        size.setQuantity(size.getQuantity() + item.getQuantity());
                        sizeFound = true;
                        break;
                    }
                }

                if (!sizeFound) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse<>("Size not found for product " + item.getProductName(), null, false));
                }

                updatedProducts.add(product);
            }

            // 👉 Update order status
            order.setOrderStatus("CANCELLED");
            order.setCancelledAt(LocalDateTime.now());
            order.setUpdatedAt(LocalDateTime.now());

            RetailOrder cancelledOrder = retailOrderRepository.save(order);

            // 👉 Save restored inventory in bulk
            productInventoryRepository.saveAll(updatedProducts);

            return ResponseEntity.ok(new ApiResponse<>("Order cancelled & inventory restored", cancelledOrder, true));

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
    @GetMapping("/date-range")
    public ResponseEntity<?> getOrdersByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(required = false, defaultValue = "ALL") String status) {
        try {
            LocalDateTime start = LocalDateTime.parse(startDate);
            LocalDateTime end = LocalDateTime.parse(endDate);

            List<RetailOrder> orders;

            // If status = ALL → ignore status filter
            if ("ALL".equalsIgnoreCase(status)) {
                orders = retailOrderRepository.findByPlacedAtBetween(start, end);
            } else {
                orders = retailOrderRepository
                        .findByPlacedAtBetweenAndOrderStatus(start, end, status);
            }

            return ResponseEntity.ok(
                    new ApiResponse<>("Orders fetched successfully", orders, true)
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }
    @PutMapping("/{orderId}/delivery-date")
    public ResponseEntity<?> setDeliveryDate(
            @PathVariable String orderId,
            @RequestBody String deliveryDate) {
        try {
            Optional<RetailOrder> orderOptional =
                    retailOrderRepository.findByOrderId(orderId);

            if (orderOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Order not found", null, false));
            }

            RetailOrder order = orderOptional.get();

            // Set delivery date
            order.setDeliveryDatesIfNotUsedAutoDelivery(deliveryDate);
            order.setUpdatedAt(LocalDateTime.now());

            RetailOrder updatedOrder = retailOrderRepository.save(order);

            return ResponseEntity.ok(
                    new ApiResponse<>("Delivery date updated successfully", updatedOrder, true)
            );

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
