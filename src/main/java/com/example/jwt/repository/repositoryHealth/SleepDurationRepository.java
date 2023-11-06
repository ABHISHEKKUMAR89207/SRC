package com.example.jwt.repository.repositoryHealth;


import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.HeartRate;
import com.example.jwt.entities.dashboardEntity.healthTrends.SleepDuration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface SleepDurationRepository extends JpaRepository<SleepDuration, Long> {
//    List<SleepDuration> findAllByTimeStampBetween(LocalDateTime localDateTime, LocalDateTime localDateTime1);

    // You can define custom query methods here if needed


//    List<SleepDuration> findByLocalTimeBetween(LocalTime startTime, LocalTime endTime);

//    List<SleepDuration> findByLocalTimeBetweenAndHealthTrends_Date(
//            LocalTime startTime, LocalTime endTime, LocalDate date);

//    List<SleepDuration> findByUserUsernameAndDate(String username, LocalDate date);
//    List<SleepDuration> findByDateAndStartTimeAfter(LocalDate date, LocalTime startTime);

    List<SleepDuration> findByDate(LocalDate date);

    List<SleepDuration> findBySleepDurationId(Long sleepDurationId);
    List<SleepDuration> findByDateAndStartTimeAfter(LocalDate date, LocalTime time);

//    SleepDuration save(SleepDuration sleepDuration); // Add this method

//    List<SleepDuration> findByUserAndLocalDate(User user, LocalDate date);

//    List<SleepDuration> findByUserAndLocalDateBetween(User user, LocalDate startDate, LocalDate endDate);


}
