package com.vtt.retail.repository;

import com.vtt.retail.entities.RetailUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RetailUserRepository extends MongoRepository<RetailUser, String> {

    // Find user by userId (from auth system)
    Optional<RetailUser> findByUserId(String userId);

    // Find user by email
    Optional<RetailUser> findByEmail(String email);

    // Find user by mobile number
    Optional<RetailUser> findByMobileNo(String mobileNo);

    // Check if user exists by userId
    boolean existsByUserId(String userId);
}
