package com.example.jwt.repository;

import com.example.jwt.entities.User;
import com.example.jwt.entities.water.WaterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WaterEntityRepository extends JpaRepository<WaterEntity, Long> {
    List<WaterEntity> findByUser(User user);
    WaterEntity findByUserAndLocalDate(User user, LocalDate localDate);

    @Query("SELECT w FROM WaterEntity w WHERE w.user = :user AND w.localDate = :localDate")
    List<WaterEntity> findByUserAndLocalDatee(@Param("user") User user, @Param("localDate") LocalDate localDate);
    List<WaterEntity> findAllByLocalDate(LocalDate localDate);
    List<WaterEntity> findByUserAndLocalDateBetween(User user, LocalDate startDate, LocalDate endDate);
//    List<WaterEntity> findByUserAndLocalDate(User user, LocalDate localDate);
@Query("SELECT w FROM WaterEntity w WHERE w.waterId = :waterId AND w.user = :user")
Optional<WaterEntity> findByIdAndUser(@Param("waterId") Long waterId, @Param("user") User user);
    List<WaterEntity> findByLocalDate(LocalDate localDate);
}
