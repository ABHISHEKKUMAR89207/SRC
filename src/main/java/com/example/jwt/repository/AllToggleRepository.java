package com.example.jwt.repository;

import com.example.jwt.entities.User;
import com.example.jwt.entities.AllToggle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AllToggleRepository extends JpaRepository<AllToggle, Long> {
    AllToggle findByUser(User user);
}
