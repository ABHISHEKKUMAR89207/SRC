package com.example.jwt.repository;

import com.example.jwt.entities.User;
import com.example.jwt.entities.water.WaterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface WaterEntityRepository extends JpaRepository<WaterEntity, Long> {
    List<WaterEntity> findByUser(User user);
    WaterEntity findByUserAndLocalDate(User user, LocalDate localDate);
}
