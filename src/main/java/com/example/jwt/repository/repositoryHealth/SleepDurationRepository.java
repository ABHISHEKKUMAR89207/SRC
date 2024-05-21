package com.example.jwt.repository.repositoryHealth;

import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.SleepDuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SleepDurationRepository extends JpaRepository<SleepDuration,Long> {

    @Query("SELECT sd FROM SleepDuration sd WHERE sd.user = :user AND sd.dateOfSleep = :date")
    Optional<SleepDuration> findByUserAndDate(@Param("user") User user, @Param("date") LocalDate date);

    Optional<SleepDuration> findByUserAndDateOfSleep(User user, LocalDate dateOfSleep);

    List<SleepDuration> findByUser(User user);

    List<SleepDuration> findByUserAndDateOfSleepBetween(User user, LocalDate startDate, LocalDate endDate);

    List<SleepDuration> findByDateOfSleep(LocalDate today);

    List<SleepDuration> findByDateOfSleepAndUser(LocalDate date, User user);
}
