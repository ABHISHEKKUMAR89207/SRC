package com.vtt.repository;



import com.vtt.entities.UserError;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserErrorRepository extends MongoRepository<UserError, Long> {
    // You can add custom query methods if needed
}
