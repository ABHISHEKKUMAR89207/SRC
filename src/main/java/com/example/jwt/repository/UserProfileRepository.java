package com.example.jwt.repository;

import com.example.jwt.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    UserProfile findByUserEmail(String email);
    List<UserProfile> findByGender(String gender);
    Integer countByGender(String gender);
}
