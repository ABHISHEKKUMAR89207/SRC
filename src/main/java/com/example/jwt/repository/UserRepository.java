package com.example.jwt.repository;

import com.example.jwt.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    public Optional<User> findByEmail(String email);

    List<User> findByEmailVerifiedFalseAndRegistrationTimestampBefore(LocalDateTime timestamp);
}
