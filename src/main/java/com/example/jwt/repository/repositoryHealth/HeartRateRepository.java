package com.example.jwt.repository.repositoryHealth;

import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.HeartRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface HeartRateRepository extends JpaRepository<HeartRate, Long> {
//    List<HeartRate> findAllByTimeStampBetween(LocalDateTime localDateTime, LocalDateTime localDateTime1);

//    List<HeartRate> findByTimeStampBetween(LocalDateTime startTime, LocalDateTime endTime);
@Query("SELECT hr FROM HeartRate hr WHERE hr.user = :user AND hr.localDate = :localDate ORDER BY hr.timeStamp DESC")
List<HeartRate> findLatestHeartRateForUserAndDate(@Param("user") User user, @Param("localDate") LocalDate localDate);
    HeartRate findAllByLocalDate(LocalDate localDate);

//    List<HeartRate> findByUserAndLocalDate(User user, LocalDate date);
HeartRate findByUserAndLocalDate(User user, LocalDate date);

    HeartRate findTopByUserAndLocalDateOrderByTimeStampDesc(User user, LocalDate date);

HeartRate findByUserAndTimeStamp(User user, LocalTime localTime);
    List<HeartRate> findByUserAndLocalDateBetween(User user, LocalDate startDate, LocalDate endDate);
}
