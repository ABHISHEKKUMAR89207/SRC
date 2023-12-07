package com.example.jwt.repository;

import com.example.jwt.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    UserProfile findByUserEmail(String email);
}
