package com.example.jwt.repository;


import com.example.jwt.entities.UserProfile;
import com.example.jwt.entities.dashboardEntity.healthTrends.HealthTrends;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {


//    UserProfile findByEmail(String username);

    UserProfile findByUserEmail(String email);
}
