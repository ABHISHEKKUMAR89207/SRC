package com.example.jwt.repository.repositoryHealth;

import com.example.jwt.entities.dashboardEntity.healthTrends.HealthTrends;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HealthTrendsRepository extends JpaRepository<HealthTrends, Long> {

    HealthTrends findByUserEmail(String email);
    List<HealthTrends> findByDate(LocalDate date);
    List<HealthTrends> findByHealthTrendId(Long healthTrend_Id);


}
