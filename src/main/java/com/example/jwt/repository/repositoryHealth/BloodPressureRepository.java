package com.example.jwt.repository.repositoryHealth;


import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.BloodPressure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository

public interface BloodPressureRepository extends JpaRepository<BloodPressure, Long> {

    List<BloodPressure> findByBloodPressureId(Long bloodPressureId);

    List<BloodPressure> findByUserAndLocalDate(User user, LocalDate date);


    List<BloodPressure> findByUserAndLocalDateBetween(User user, LocalDate startDate, LocalDate endDate);
}
