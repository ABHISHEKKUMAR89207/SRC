package com.vtt.retail.repository;

import com.vtt.retail.entities.RetailReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RetailReviewRepository extends MongoRepository<RetailReview, String> {

    // Find reviews by product
    Page<RetailReview> findByProductIdAndIsActiveTrue(String productId, Pageable pageable);

    // Find reviews by product and status
    List<RetailReview> findByProductIdAndStatusAndIsActiveTrue(String productId, String status);

    // Find reviews by user
    Page<RetailReview> findByUserIdAndIsActiveTrue(String userId, Pageable pageable);

    // Find reviews by product and rating
    List<RetailReview> findByProductIdAndRatingAndIsActiveTrue(String productId, int rating);

    // Count reviews by product
    long countByProductIdAndStatusAndIsActiveTrue(String productId, String status);

    // Count reviews by product and rating
    long countByProductIdAndRatingAndIsActiveTrue(String productId, int rating);

    // Check if user already reviewed product
    Optional<RetailReview> findByProductIdAndUserIdAndIsActiveTrue(String productId, String userId);

    // Get reviews sorted by helpful count
    Page<RetailReview> findByProductIdAndStatusAndIsActiveTrue(String productId, String status, Pageable pageable);

    // Find verified purchase reviews
    List<RetailReview> findByProductIdAndVerifiedPurchaseAndIsActiveTrueAndStatus(String productId, boolean verifiedPurchase, String status);

    // Get pending reviews (admin)
    Page<RetailReview> findByStatus(String status, Pageable pageable);

    // Search reviews by title or description
    @Query("{ 'productId': ?0, $or: [ { 'title': { $regex: ?1, $options: 'i' } }, { 'description': { $regex: ?1, $options: 'i' } } ], 'isActive': true }")
    Page<RetailReview> searchByProductAndKeyword(String productId, String keyword, Pageable pageable);

    // Get recent reviews
    List<RetailReview> findTop10ByIsActiveTrueOrderByCreatedAtDesc();
}
