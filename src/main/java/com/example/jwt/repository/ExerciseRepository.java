package com.example.jwt.repository;

import com.example.jwt.entities.Exercise;
import com.example.jwt.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
//    List<Exercise> findByUserAndDateAndActivityType(User user, LocalDate date, String activityType);
List<Exercise> findByUserAndDateAndActivityType(User user, LocalDate date, String activityType);

    // You can add custom queries if needed
}
