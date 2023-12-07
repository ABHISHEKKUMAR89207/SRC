package com.example.jwt.registration.token;

import com.example.jwt.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
    VerificationToken findByUser(User user);
    void deleteByUserUserId(Long userId);

}
