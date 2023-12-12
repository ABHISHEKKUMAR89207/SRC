package com.example.jwt.repository;

import com.example.jwt.entities.User;
import com.google.api.gax.paging.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    public Optional<User> findByEmail(String email);

    List<User> findByEmailVerifiedFalseAndRegistrationTimestampBefore(LocalDateTime timestamp);

//    Page<User> findAll(Pageable pageable);


    List<User> findByRegistrationTimestampBetween(LocalDateTime startOfMonth, LocalDateTime endOfMonth);
}
