package com.example.jwt.repository;

import com.example.jwt.entities.User;
import com.example.jwt.entities.water.WaterGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WaterGoalRepository extends JpaRepository<WaterGoal, Long> {
//    WaterGoal findByWaterEntity_WaterId(Long waterEntityId);
//
//    WaterGoal findByWaterEntity(WaterEntity waterEntity);

    WaterGoal findByUser(User user);
    WaterGoal findByUser_email(String username);



}
