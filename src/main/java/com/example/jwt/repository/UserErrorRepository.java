package com.example.jwt.repository;



import com.example.jwt.entities.UserError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserErrorRepository extends JpaRepository<UserError, Long> {
    // You can add custom query methods if needed
}
