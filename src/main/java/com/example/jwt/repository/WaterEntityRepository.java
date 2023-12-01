package com.example.jwt.repository;
import com.example.jwt.entities.User;
import com.example.jwt.entities.water.WaterEntity;
import com.example.jwt.entities.water.WaterGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface WaterEntityRepository extends JpaRepository<WaterEntity, Long> {
//    WaterEntity findByUser_email(String username);


//    WaterEntity findByUser(User user);
//WaterEntity findByWaterGoalAndLocalDate(WaterGoal waterGoal, LocalDate localDate);


    WaterEntity findByUserAndLocalDate(User user, LocalDate localDate);




}
