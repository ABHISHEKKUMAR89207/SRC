package com.vtt.retail;

import com.vtt.entities.ProductInventory;
import com.vtt.retail.entities.RetailReview;
import com.vtt.retail.repository.RetailProductRepository;
import com.vtt.retail.repository.RetailReviewRepository;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/retail/reviews")
@Tag(name = "Retail Review Controller", description = "API for product reviews and ratings")
public class RetailReviewController {

    private final RetailReviewRepository retailReviewRepository;
    private final RetailProductRepository retailProductRepository;

    /**
     * Create a review for a product
     */
    @PostMapping("/create")
    public ResponseEntity<?> createReview(@RequestBody CreateReviewRequest reviewRequest) {
        try {
            // Validate product exists
            Optional<ProductInventory>productOptional = retailProductRepository.findById(reviewRequest.getProductId());
            if (productOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Product not found", null, false));
            }

            // Validate rating
            if (reviewRequest.getRating() < 1 || reviewRequest.getRating() > 5) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Rating must be between 1 and 5", null, false));
            }

            // Check if user already reviewed this product
            Optional<RetailReview> existingReview = retailReviewRepository
                    .findByProductIdAndUserIdAndIsActiveTrue(reviewRequest.getProductId(), reviewRequest.getUserId());

            if (existingReview.isPresent()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("You have already reviewed this product", null, false));
            }

            // Create review
            RetailReview review = new RetailReview();
            review.setProductId(reviewRequest.getProductId());
            review.setUserId(reviewRequest.getUserId());
            review.setUserName(reviewRequest.getUserName());
            review.setRating(reviewRequest.getRating());
            review.setTitle(reviewRequest.getTitle());
            review.setDescription(reviewRequest.getDescription());
            review.setPurchaseOrderId(reviewRequest.getPurchaseOrderId());
            review.setVerifiedPurchase(reviewRequest.getPurchaseOrderId() != null && 
                    !reviewRequest.getPurchaseOrderId().isEmpty());
            review.setStatus("APPROVED");  // Auto-approve for now
            review.setCreatedAt(LocalDateTime.now());
            review.setUpdatedAt(LocalDateTime.now());

            RetailReview savedReview = retailReviewRepository.save(review);

            // Update product rating
            updateProductRating(reviewRequest.getProductId());

            return ResponseEntity.ok(new ApiResponse<>("Review submitted successfully", savedReview, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get reviews for a product
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getProductReviews(
            @PathVariable String productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            Optional<ProductInventory>productOptional = retailProductRepository.findById(productId);
            if (productOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Product not found", null, false));
            }

            Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            Page<RetailReview> reviews = retailReviewRepository
                    .findByProductIdAndIsActiveTrue(productId, pageable);

            return ResponseEntity.ok(new ApiResponse<>("Success", reviews, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get review statistics for a product
     */
    @GetMapping("/product/{productId}/stats")
    public ResponseEntity<?> getProductReviewStats(@PathVariable String productId) {
        try {
            Optional<ProductInventory>productOptional = retailProductRepository.findById(productId);
            if (productOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Product not found", null, false));
            }

            ProductInventory product = productOptional.get();

            ReviewStats stats = new ReviewStats();
            stats.setProductId(productId);
            stats.setProductName(product.getNameOfProduct());
            stats.setAverageRating(product.getRating());
            stats.setTotalRatings(product.getTotalRatings());

            // Get rating distribution
            Map<Integer, Long> distribution = new HashMap<>();
            for (int i = 5; i >= 1; i--) {
                long count = retailReviewRepository
                        .countByProductIdAndRatingAndIsActiveTrue(productId, i);
                distribution.put(i, count);
            }
            stats.setRatingDistribution(distribution);

            // Get verified purchases count
            List<RetailReview> verifiedReviews = retailReviewRepository
                    .findByProductIdAndVerifiedPurchaseAndIsActiveTrueAndStatus(productId, true, "APPROVED");
            stats.setVerifiedPurchaseCount((long) verifiedReviews.size());

            return ResponseEntity.ok(new ApiResponse<>("Success", stats, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get reviews by rating
     */
    @GetMapping("/product/{productId}/rating/{rating}")
    public ResponseEntity<?> getReviewsByRating(
            @PathVariable String productId,
            @PathVariable int rating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            if (rating < 1 || rating > 5) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Rating must be between 1 and 5", null, false));
            }

            List<RetailReview> reviews = retailReviewRepository
                    .findByProductIdAndRatingAndIsActiveTrue(productId, rating);

            return ResponseEntity.ok(new ApiResponse<>("Success", reviews, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get reviews by user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserReviews(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<RetailReview> reviews = retailReviewRepository.findByUserIdAndIsActiveTrue(userId, pageable);

            return ResponseEntity.ok(new ApiResponse<>("Success", reviews, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get single review
     */
    @GetMapping("/{reviewId}")
    public ResponseEntity<?> getReview(@PathVariable String reviewId) {
        try {
            Optional<RetailReview> reviewOptional = retailReviewRepository.findById(reviewId);

            if (reviewOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Review not found", null, false));
            }

            return ResponseEntity.ok(new ApiResponse<>("Success", reviewOptional.get(), true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Update review (user can edit their own review)
     */
    @PutMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(
            @PathVariable String reviewId,
            @RequestBody UpdateReviewRequest updateRequest) {
        try {
            Optional<RetailReview> reviewOptional = retailReviewRepository.findById(reviewId);

            if (reviewOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Review not found", null, false));
            }

            RetailReview review = reviewOptional.get();

            if (updateRequest.getRating() != null) {
                if (updateRequest.getRating() < 1 || updateRequest.getRating() > 5) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse<>("Rating must be between 1 and 5", null, false));
                }
                review.setRating(updateRequest.getRating());
            }

            if (updateRequest.getTitle() != null) {
                review.setTitle(updateRequest.getTitle());
            }

            if (updateRequest.getDescription() != null) {
                review.setDescription(updateRequest.getDescription());
            }

            review.setUpdatedAt(LocalDateTime.now());
            RetailReview updatedReview = retailReviewRepository.save(review);

            // Update product rating
            updateProductRating(review.getProductId());

            return ResponseEntity.ok(new ApiResponse<>("Review updated successfully", updatedReview, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Delete review (soft delete)
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable String reviewId) {
        try {
            Optional<RetailReview> reviewOptional = retailReviewRepository.findById(reviewId);

            if (reviewOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Review not found", null, false));
            }

            RetailReview review = reviewOptional.get();
            review.setActive(false);
            review.setUpdatedAt(LocalDateTime.now());

            retailReviewRepository.save(review);

            // Update product rating
            updateProductRating(review.getProductId());

            return ResponseEntity.ok(new ApiResponse<>("Review deleted successfully", null, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Mark review as helpful
     */
    @PutMapping("/{reviewId}/helpful")
    public ResponseEntity<?> markAsHelpful(@PathVariable String reviewId) {
        try {
            Optional<RetailReview> reviewOptional = retailReviewRepository.findById(reviewId);

            if (reviewOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Review not found", null, false));
            }

            RetailReview review = reviewOptional.get();
            review.setHelpfulCount(review.getHelpfulCount() + 1);
            review.setUpdatedAt(LocalDateTime.now());

            RetailReview updatedReview = retailReviewRepository.save(review);
            return ResponseEntity.ok(new ApiResponse<>("Marked as helpful", updatedReview, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Mark review as unhelpful
     */
    @PutMapping("/{reviewId}/unhelpful")
    public ResponseEntity<?> markAsUnhelpful(@PathVariable String reviewId) {
        try {
            Optional<RetailReview> reviewOptional = retailReviewRepository.findById(reviewId);

            if (reviewOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Review not found", null, false));
            }

            RetailReview review = reviewOptional.get();
            review.setUnhelpfulCount(review.getUnhelpfulCount() + 1);
            review.setUpdatedAt(LocalDateTime.now());

            RetailReview updatedReview = retailReviewRepository.save(review);
            return ResponseEntity.ok(new ApiResponse<>("Marked as unhelpful", updatedReview, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get recent reviews across all products
     */
    @GetMapping("/recent/all")
    public ResponseEntity<?> getRecentReviews() {
        try {
            List<RetailReview> recentReviews = retailReviewRepository.findTop10ByIsActiveTrueOrderByCreatedAtDesc();
            return ResponseEntity.ok(new ApiResponse<>("Success", recentReviews, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Helper method to update product rating
     */
    private void updateProductRating(String productId) {
        try {
            Optional<ProductInventory>productOptional = retailProductRepository.findById(productId);
            if (productOptional.isEmpty()) return;

            List<RetailReview> reviews = retailReviewRepository
                    .findByProductIdAndStatusAndIsActiveTrue(productId, "APPROVED");

            if (reviews.isEmpty()) {
                ProductInventory product = productOptional.get();
                product.setRating(0.0);
                product.setTotalRatings(0);
                retailProductRepository.save(product);
                return;
            }

            double averageRating = reviews.stream()
                    .mapToDouble(RetailReview::getRating)
                    .average()
                    .orElse(0.0);

            ProductInventory product = productOptional.get();
            product.setRating(Math.round(averageRating * 10.0) / 10.0);
            product.setTotalRatings(reviews.size());
            retailProductRepository.save(product);

        } catch (Exception e) {
            // Log error but don't break the flow
        }
    }

    // DTOs
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateReviewRequest {
        private String productId;
        private String userId;
        private String userName;
        private int rating;
        private String title;
        private String description;
        private String purchaseOrderId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateReviewRequest {
        private Integer rating;
        private String title;
        private String description;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewStats {
        private String productId;
        private String productName;
        private Double averageRating;
        private Integer totalRatings;
        private Map<Integer, Long> ratingDistribution;
        private Long verifiedPurchaseCount;
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
