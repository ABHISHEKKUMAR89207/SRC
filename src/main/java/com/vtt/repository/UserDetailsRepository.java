package com.vtt.repository;



import com.vtt.entities.User;
import com.vtt.entities.UserDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserDetailsRepository extends MongoRepository<UserDetails, String> {
    Optional<UserDetails> findByUser(User user);
}