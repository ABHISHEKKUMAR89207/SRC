package com.example.jwt.repository.repositoryHealth;


import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.AllTarget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllTargetRepository extends JpaRepository<AllTarget, Long> {
    AllTarget findByUser(User user);
}