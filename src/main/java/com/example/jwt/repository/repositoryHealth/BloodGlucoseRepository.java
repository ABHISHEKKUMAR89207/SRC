package com.example.jwt.repository.repositoryHealth;


import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.BloodGlucose;
import com.example.jwt.entities.dashboardEntity.healthTrends.HeartRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository

public interface BloodGlucoseRepository extends JpaRepository<BloodGlucose, Long> {
//    List<HeartRate> findAllByTimeStampBetween(LocalDateTime localDateTime, LocalDateTime localDateTime1);

    // You can define custom query methods here if needed


//        List<HeartRate> findByTimeStampBetween(LocalDateTime startTime, LocalDateTime endTime);


    List<BloodGlucose> findByBloodGlucoseId(Long bloodGlucoseId);

    List<BloodGlucose> findByUserAndLocalDate(User user, LocalDate date);

    List<BloodGlucose> findByUserAndLocalDateBetween(User user, LocalDate startDate, LocalDate endDate);


}
