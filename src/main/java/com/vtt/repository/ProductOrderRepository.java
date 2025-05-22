package com.vtt.repository;

// ProductOrderRepository.java

import com.vtt.entities.ProductOrder;
import com.vtt.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductOrderRepository extends MongoRepository<ProductOrder, String> {
    List<ProductOrder> findByUser(User user);
}
