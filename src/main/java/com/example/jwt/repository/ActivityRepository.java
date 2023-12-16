package com.example.jwt.repository;

import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.Activities;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activities, Long> {
    List<Activities> findByUserAndActivityDateBetween(User user, LocalDate startDate, LocalDate endDate);


    Activities findByUserAndActivityTypeAndActivityDate(User user, String activityType, LocalDate activityDate);

    Activities findActivityByUserAndActivityDateAndActivityType(User user, LocalDate activityDate, String activityType);

    Activities findByUserAndActivityDate(User user, LocalDate date);

    List<Activities> findByActivityDateAndUserUserId(LocalDate date, Long userId);
}
