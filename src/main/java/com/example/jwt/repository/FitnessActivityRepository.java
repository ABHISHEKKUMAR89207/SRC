package com.example.jwt.repository;

import com.example.jwt.entities.dashboardEntity.FitnessActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FitnessActivityRepository extends JpaRepository<FitnessActivity, Long> {
    List<FitnessActivity> findByActivityType(String activityType);

    List<FitnessActivity> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
