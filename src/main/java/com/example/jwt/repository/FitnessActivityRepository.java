package com.example.jwt.repository;


import com.example.jwt.entities.dashboardEntity.FitnessActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FitnessActivityRepository extends JpaRepository<FitnessActivity, Long> {
    List<FitnessActivity> findByActivityType(String activityType);

    List<FitnessActivity> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

//    public interface FitnessActivityRepository extends JpaRepository<FitnessActivity, Long> {
//        @Query("SELECT fa FROM FitnessActivity fa JOIN FETCH fa.userProfile WHERE fa.id = :activityId")
//        FitnessActivity findByIdWithUserProfile(@Param("activityId") Long activityId);
//    }


    // You can add more custom queries as needed
}
