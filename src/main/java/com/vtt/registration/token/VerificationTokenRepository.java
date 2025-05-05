package com.vtt.registration.token;

import com.vtt.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VerificationTokenRepository extends MongoRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
    VerificationToken findByUser(User user);
    void deleteByUserUserId(Long userId);

}
