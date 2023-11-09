package com.example.jwt.registration.token;

import org.springframework.data.jpa.repository.JpaRepository;


public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);

    void deleteByUserUserId(Long userId);

}
