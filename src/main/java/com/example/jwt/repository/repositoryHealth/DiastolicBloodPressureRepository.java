package com.example.jwt.repository.repositoryHealth;

import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.DiastolicBloodPressure;
import com.example.jwt.entities.dashboardEntity.healthTrends.SystolicBloodPressure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DiastolicBloodPressureRepository extends JpaRepository<DiastolicBloodPressure, Long> {
    List<DiastolicBloodPressure> findByDiastolicId(Long diastolicId);

    List<DiastolicBloodPressure> findByUserAndLocalDate(User user, LocalDate date);


    List<DiastolicBloodPressure> findByUserAndLocalDateBetween(User user, LocalDate startDate, LocalDate endDate);
}
