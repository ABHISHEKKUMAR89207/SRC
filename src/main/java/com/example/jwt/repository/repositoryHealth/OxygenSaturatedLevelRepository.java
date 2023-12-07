package com.example.jwt.repository.repositoryHealth;

import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.OxygenSaturatedLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OxygenSaturatedLevelRepository extends JpaRepository<OxygenSaturatedLevel, Long> {

    List<OxygenSaturatedLevel> findByOxygenSaturatedLevelId(Long oxygenSaturatedLevelId);

    List<OxygenSaturatedLevel> findByUserAndLocalDate(User user, LocalDate date);

    List<OxygenSaturatedLevel> findByUserAndLocalDateBetween(User user, LocalDate startDate, LocalDate endDate);
}
