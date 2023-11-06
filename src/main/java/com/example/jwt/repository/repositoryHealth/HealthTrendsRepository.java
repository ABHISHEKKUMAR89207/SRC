package com.example.jwt.repository.repositoryHealth;



import com.example.jwt.entities.dashboardEntity.healthTrends.HealthTrends;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HealthTrendsRepository extends JpaRepository<HealthTrends, Long> {
    // You can define custom query methods here if needed

    List<HealthTrends> findByDate(LocalDate date);
    // Custom query method to find HealthTrends by healthTrend_Id
    List<HealthTrends> findByHealthTrendId(Long healthTrend_Id);

    HealthTrends findByUserEmail(String email);
}
