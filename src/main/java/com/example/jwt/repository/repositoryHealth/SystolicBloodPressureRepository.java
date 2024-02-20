package com.example.jwt.repository.repositoryHealth;


import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.BloodPressure;
import com.example.jwt.entities.dashboardEntity.healthTrends.SystolicBloodPressure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SystolicBloodPressureRepository extends JpaRepository<SystolicBloodPressure, Long> {
    List<SystolicBloodPressure> findBySystolicId(Long systolicId);

    List<SystolicBloodPressure> findByUserAndLocalDate(User user, LocalDate date);


    List<SystolicBloodPressure> findByUserAndLocalDateBetween(User user, LocalDate startDate, LocalDate endDate);
}
