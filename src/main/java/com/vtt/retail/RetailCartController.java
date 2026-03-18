package com.vtt.retail;

import com.vtt.retail.entities.RetailCart;
import com.vtt.entities.ProductInventory;
import com.vtt.retail.repository.RetailCartRepository;
import com.vtt.retail.repository.RetailProductRepository;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/retail/cart")
@Tag(name = "Retail Cart Controller", description = "API for shopping cart management")
public class RetailCartController {

    private final RetailCartRepository retailCartRepository;
    private final RetailProductRepository retailProductRepository;

    /**
     * Add item to cart
     */
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestParam String userId,
            @RequestBody AddToCartRequest request) {
        try {
            // Validate product exists
            Optional<ProductInventory>productOptional = retailProductRepository.findById(request.getProductId());
            if (productOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Product not found", null, false));
            }

            ProductInventory product = productOptional.get();

            // Validate size exists and get price
            ProductInventory.SizeQuantity selectedSize = null;
            for (ProductInventory.SizeQuantity size : product.getSizes()) {
                if (size.getLabel().equals(request.getSelectedSize())) {
                    selectedSize = size;
                    break;
                }
            }

            if (selectedSize == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Selected size not available", null, false));
            }

            // Check quantity availability
            if (selectedSize.getQuantity() < request.getQuantity()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Insufficient quantity available", null, false));
            }

            // Get or create cart
            Optional<RetailCart> cartOptional = retailCartRepository.findByUserId(userId);
            RetailCart cart;

            if (cartOptional.isPresent()) {
                cart = cartOptional.get();
            } else {
                cart = new RetailCart();
                cart.setUserId(userId);
                cart.setCreatedAt(LocalDateTime.now());
            }

            // Check if item already exists in cart
            boolean itemExists = false;
            if (cart.getItems() != null) {
                for (RetailCart.CartItem item : cart.getItems()) {
                    if (item.getProductId().equals(request.getProductId()) &&
                        item.getSelectedSize().equals(request.getSelectedSize())) {
                        // Update quantity
                        item.setQuantity(item.getQuantity() + request.getQuantity());
                        item.setTotalPrice(item.getQuantity() * item.getPricePerUnit());
                        itemExists = true;
                        break;
                    }
                }
            }

            // Add new item if not exists
            if (!itemExists) {
                RetailCart.CartItem cartItem = new RetailCart.CartItem();
                cartItem.setProductId(request.getProductId());
                cartItem.setProductName(product.getNameOfProduct());
                cartItem.setSelectedSize(request.getSelectedSize());
                cartItem.setQuantity(request.getQuantity());
                cartItem.setPricePerUnit(selectedSize.getPrice());
                cartItem.setTotalPrice(request.getQuantity() * selectedSize.getPrice());
                cartItem.setProductImage(product.getProductImage());

                if (cart.getItems() == null) {
                    cart.setItems(new java.util.ArrayList<>());
                }
                cart.getItems().add(cartItem);
            }

            // Update cart totals
            updateCartTotals(cart);
            cart.setUpdatedAt(LocalDateTime.now());

            RetailCart savedCart = retailCartRepository.save(cart);
            return ResponseEntity.ok(new ApiResponse<>("Item added to cart successfully", savedCart, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Update cart item quantity
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateCartItem(
            @RequestParam String userId,
            @RequestBody UpdateCartRequest request) {
        try {
            Optional<RetailCart> cartOptional = retailCartRepository.findByUserId(userId);

            if (cartOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Cart not found", null, false));
            }

            RetailCart cart = cartOptional.get();

            // Find and update item
            boolean itemFound = false;
            if (cart.getItems() != null) {
                for (RetailCart.CartItem item : cart.getItems()) {
                    if (item.getProductId().equals(request.getProductId()) &&
                        item.getSelectedSize().equals(request.getSelectedSize())) {
                        item.setQuantity(request.getQuantity());
                        item.setTotalPrice(request.getQuantity() * item.getPricePerUnit());
                        itemFound = true;
                        break;
                    }
                }
            }

            if (!itemFound) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Item not found in cart", null, false));
            }

            // Update cart totals
            updateCartTotals(cart);
            cart.setUpdatedAt(LocalDateTime.now());

            RetailCart updatedCart = retailCartRepository.save(cart);
            return ResponseEntity.ok(new ApiResponse<>("Cart updated successfully", updatedCart, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Remove item from cart
     */
    @DeleteMapping("/remove")
    public ResponseEntity<?> removeFromCart(
            @RequestParam String userId,
            @RequestParam String productId,
            @RequestParam String selectedSize) {
        try {
            Optional<RetailCart> cartOptional = retailCartRepository.findByUserId(userId);

            if (cartOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Cart not found", null, false));
            }

            RetailCart cart = cartOptional.get();

            // Remove item
            boolean itemRemoved = false;
            if (cart.getItems() != null) {
                itemRemoved = cart.getItems().removeIf(item ->
                    item.getProductId().equals(productId) &&
                    item.getSelectedSize().equals(selectedSize)
                );
            }

            if (!itemRemoved) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Item not found in cart", null, false));
            }

            // Update cart totals
            updateCartTotals(cart);
            cart.setUpdatedAt(LocalDateTime.now());

            RetailCart updatedCart = retailCartRepository.save(cart);
            return ResponseEntity.ok(new ApiResponse<>("Item removed from cart", updatedCart, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get cart items
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getCart(@PathVariable String userId) {
        try {
            Optional<RetailCart> cartOptional = retailCartRepository.findByUserId(userId);

            if (cartOptional.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse<>("Cart is empty", new RetailCart(), true));
            }

            return ResponseEntity.ok(new ApiResponse<>("Success", cartOptional.get(), true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Clear cart
     */
    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<?> clearCart(@PathVariable String userId) {
        try {
            retailCartRepository.deleteByUserId(userId);
            return ResponseEntity.ok(new ApiResponse<>("Cart cleared successfully", null, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Helper method to update cart totals
     */
    private void updateCartTotals(RetailCart cart) {
        double totalPrice = 0.0;
        int totalItems = 0;
        int totalQuantity = 0;

        if (cart.getItems() != null) {
            totalItems = cart.getItems().size();
            for (RetailCart.CartItem item : cart.getItems()) {
                totalPrice += item.getTotalPrice();
                totalQuantity += item.getQuantity();
            }
        }

        cart.setTotalPrice(totalPrice);
        cart.setTotalItems(totalItems);
        cart.setTotalQuantity(totalQuantity);
    }

    // DTOs
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddToCartRequest {
        private String productId;
        private String selectedSize;
        private Integer quantity;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCartRequest {
        private String productId;
        private String selectedSize;
        private Integer quantity;
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
