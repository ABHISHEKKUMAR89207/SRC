package com.example.jwt.repository.repositoryHealth;


import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.SleepTarget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SleepTargetRepository extends JpaRepository<SleepTarget, Long> {
    SleepTarget findByUser(User user);
}