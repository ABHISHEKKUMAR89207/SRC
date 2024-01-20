package com.example.jwt.repository.repositoryHealth;

import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.HeartRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface HeartRateRepository extends JpaRepository<HeartRate, Long> {
    List<HeartRate> findAllByTimeStampBetween(LocalDateTime localDateTime, LocalDateTime localDateTime1);

    List<HeartRate> findByTimeStampBetween(LocalDateTime startTime, LocalDateTime endTime);

    HeartRate findAllByLocalDate(LocalDate localDate);

//    List<HeartRate> findByUserAndLocalDate(User user, LocalDate date);
HeartRate findByUserAndLocalDate(User user, LocalDate date);
    List<HeartRate> findByUserAndLocalDateBetween(User user, LocalDate startDate, LocalDate endDate);
}
