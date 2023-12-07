package com.example.jwt.repository.repositoryHealth;

import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.SleepDuration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SleepDurationRepository extends JpaRepository<SleepDuration,Long> {

    Optional<SleepDuration> findByUserAndDateOfSleep(User user, LocalDate dateOfSleep);

    List<SleepDuration> findByUser(User user);
}
