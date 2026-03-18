package com.vtt.retail;

import com.vtt.entities.ProductInventory;
import com.vtt.retail.entities.RetailWishlist;
import com.vtt.retail.repository.RetailProductRepository;
import com.vtt.retail.repository.RetailWishlistRepository;
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
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/retail/wishlist")
@Tag(name = "Retail Wishlist Controller", description = "API for wishlist management")
public class RetailWishlistController {

    private final RetailWishlistRepository retailWishlistRepository;
    private final RetailProductRepository retailProductRepository;

    /**
     * Add product to wishlist
     */
    @PostMapping("/add")
    public ResponseEntity<?> addToWishlist(
            @RequestParam String userId,
            @RequestParam String productId) {
        try {
            // Validate product exists
            Optional<ProductInventory>productOptional = retailProductRepository.findById(productId);
            if (productOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Product not found", null, false));
            }

            ProductInventory product = productOptional.get();

            // Get or create wishlist
            Optional<RetailWishlist> wishlistOptional = retailWishlistRepository.findByUserId(userId);
            RetailWishlist wishlist;

            if (wishlistOptional.isPresent()) {
                wishlist = wishlistOptional.get();
            } else {
                wishlist = new RetailWishlist();
                wishlist.setUserId(userId);
                wishlist.setCreatedAt(LocalDateTime.now());
            }

            // Check if item already exists in wishlist
            boolean itemExists = false;
            if (wishlist.getItems() != null) {
                for (RetailWishlist.WishlistItem item : wishlist.getItems()) {
                    if (item.getProductId().equals(productId)) {
                        itemExists = true;
                        break;
                    }
                }
            }

            if (itemExists) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Product already in wishlist", null, false));
            }

            // Add new item
            RetailWishlist.WishlistItem wishlistItem = new RetailWishlist.WishlistItem();
            wishlistItem.setProductId(productId);
            wishlistItem.setProductName(product.getNameOfProduct());
            wishlistItem.setPrice(product.getSizes() != null && !product.getSizes().isEmpty() 
                    ? product.getSizes().get(0).getPrice() : 0.0);
            wishlistItem.setProductImage(product.getProductImage());
            wishlistItem.setAddedAt(LocalDateTime.now());

            if (wishlist.getItems() == null) {
                wishlist.setItems(new java.util.ArrayList<>());
            }
            wishlist.getItems().add(wishlistItem);
            wishlist.setUpdatedAt(LocalDateTime.now());

            RetailWishlist savedWishlist = retailWishlistRepository.save(wishlist);
            return ResponseEntity.ok(new ApiResponse<>("Product added to wishlist", savedWishlist, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Remove product from wishlist
     */
    @DeleteMapping("/remove")
    public ResponseEntity<?> removeFromWishlist(
            @RequestParam String userId,
            @RequestParam String productId) {
        try {
            Optional<RetailWishlist> wishlistOptional = retailWishlistRepository.findByUserId(userId);

            if (wishlistOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Wishlist not found", null, false));
            }

            RetailWishlist wishlist = wishlistOptional.get();

            // Remove item
            boolean itemRemoved = false;
            if (wishlist.getItems() != null) {
                itemRemoved = wishlist.getItems().removeIf(item -> item.getProductId().equals(productId));
            }

            if (!itemRemoved) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Product not found in wishlist", null, false));
            }

            wishlist.setUpdatedAt(LocalDateTime.now());
            RetailWishlist updatedWishlist = retailWishlistRepository.save(wishlist);
            return ResponseEntity.ok(new ApiResponse<>("Product removed from wishlist", updatedWishlist, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get wishlist items
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getWishlist(@PathVariable String userId) {
        try {
            Optional<RetailWishlist> wishlistOptional = retailWishlistRepository.findByUserId(userId);

            if (wishlistOptional.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse<>("Wishlist is empty", new RetailWishlist(), true));
            }

            return ResponseEntity.ok(new ApiResponse<>("Success", wishlistOptional.get(), true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Check if product is in wishlist
     */
    @GetMapping("/{userId}/contains/{productId}")
    public ResponseEntity<?> isInWishlist(
            @PathVariable String userId,
            @PathVariable String productId) {
        try {
            Optional<RetailWishlist> wishlistOptional = retailWishlistRepository.findByUserId(userId);

            boolean inWishlist = false;
            if (wishlistOptional.isPresent() && wishlistOptional.get().getItems() != null) {
                inWishlist = wishlistOptional.get().getItems()
                        .stream()
                        .anyMatch(item -> item.getProductId().equals(productId));
            }

            return ResponseEntity.ok(new ApiResponse<>("Success", inWishlist, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Clear wishlist
     */
    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<?> clearWishlist(@PathVariable String userId) {
        try {
            retailWishlistRepository.deleteByUserId(userId);
            return ResponseEntity.ok(new ApiResponse<>("Wishlist cleared", null, true));
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
    public static class ApiResponse<T> {
        private String message;
        private T data;
        private boolean success;
    }
}
