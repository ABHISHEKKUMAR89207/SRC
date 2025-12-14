package com.vtt.repository;

import com.vtt.entities.Discount;
import com.vtt.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends MongoRepository<Discount, String> {
    Discount findByUser(User linkedUser);
}

