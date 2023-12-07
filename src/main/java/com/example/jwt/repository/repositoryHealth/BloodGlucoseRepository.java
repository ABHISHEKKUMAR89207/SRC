package com.example.jwt.repository.repositoryHealth;


import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.BloodGlucose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository

public interface BloodGlucoseRepository extends JpaRepository<BloodGlucose, Long> {

    List<BloodGlucose> findByBloodGlucoseId(Long bloodGlucoseId);

    List<BloodGlucose> findByUserAndLocalDate(User user, LocalDate date);

    List<BloodGlucose> findByUserAndLocalDateBetween(User user, LocalDate startDate, LocalDate endDate);
}
