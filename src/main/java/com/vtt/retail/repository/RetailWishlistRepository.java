package com.vtt.retail.repository;

import com.vtt.retail.entities.RetailWishlist;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RetailWishlistRepository extends MongoRepository<RetailWishlist, String> {

    // Find wishlist by userId
    Optional<RetailWishlist> findByUserId(String userId);

    // Check if wishlist exists for user
    boolean existsByUserId(String userId);

    // Delete wishlist by userId
    void deleteByUserId(String userId);
}
