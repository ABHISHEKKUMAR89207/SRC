package com.vtt.retail.repository;

import com.vtt.retail.entities.RetailCart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RetailCartRepository extends MongoRepository<RetailCart, String> {

    // Find cart by userId
    Optional<RetailCart> findByUserId(String userId);

    // Check if cart exists for user
    boolean existsByUserId(String userId);

    // Delete cart by userId
    void deleteByUserId(String userId);
}
