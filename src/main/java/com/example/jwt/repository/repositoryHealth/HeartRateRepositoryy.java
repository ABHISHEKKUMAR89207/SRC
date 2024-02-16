package com.example.jwt.repository.repositoryHealth;

import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.HeartRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface HeartRateRepositoryy extends JpaRepository<HeartRate, Long> {


//    List<HeartRate> findByUserAndLocalDate(User user, LocalDate date);
List<HeartRate> findByUserAndLocalDate(User user, LocalDate date);
}
