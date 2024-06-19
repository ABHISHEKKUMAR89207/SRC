package com.example.jwt.repository;

import com.example.jwt.entities.Exercise;
import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.Activities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
//    List<Exercise> findByUserAndDateAndActivityType(User user, LocalDate date, String activityType);
List<Exercise> findByUserAndDateAndActivityType(User user, LocalDate date, String activityType);
    List<Exercise> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);

    List<Exercise> findByUserAndDate(User user, LocalDate date);
//    List<Exercise> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);

//    List<Exercise> findByUserAndDate(User user, LocalDate date);
    // You can add custom queries if needed


    @Query("SELECT e FROM Exercise e WHERE e.user.userId = :userId AND (e.startTime >= :startDateTime AND e.endTime <= :endDateTime)")
    List<Exercise> findExercisesWithin24HourCycle(@Param("userId") Long userId,
                                                  @Param("startDateTime") LocalDateTime startDateTime,
                                                  @Param("endDateTime") LocalDateTime endDateTime);

//    @Query("SELECT SUM(e.caloriesBurned) FROM Exercise e WHERE e.user.userId = :userId AND (e.startTime >= :startDateTime AND e.endTime <= :endDateTime)")
//    Double sumCaloriesBurnedWithin24HourCycle(@Param("userId") Long userId,
//                                              @Param("startDateTime") LocalDateTime startDateTime,
//                                              @Param("endDateTime") LocalDateTime endDateTime);
//@Query("SELECT SUM(e.caloriesBurned) FROM Exercise e WHERE e.user.userId = :userId AND (e.startTime >= :startDateTime AND e.endTime <= :endDateTime)")
//Double sumCaloriesBurnedWithin24HourCycle(@Param("userId") Long userId,
//                                          @Param("startDateTime") LocalTime startDateTime,
//                                          @Param("endDateTime") LocalTime endDateTime);
//@Query("SELECT SUM(e.caloriesBurned) FROM Exercise e WHERE e.user.userId = :userId " +
//        "AND (e.startTime >= :startDateTime AND e.endTime <= :endDateTime)")
//Double sumCaloriesBurnedWithin24HourCycle(@Param("userId") Long userId,
//                                          @Param("startDateTime") LocalTime startDateTime,
//                                          @Param("endDateTime") LocalTime endDateTime);
//@Query("SELECT COALESCE(SUM(e.caloriesBurned), 0) FROM Exercise e WHERE e.user.userId = :userId AND e.startTime >= :startDateTime AND e.endTime <= :endDateTime")
//Double sumCaloriesBurnedWithin24HourCycle(@Param("userId") Long userId,
//                                          @Param("startDateTime") LocalTime startDateTime,
//                                          @Param("endDateTime") LocalTime endDateTime);
//@Query("SELECT COALESCE(SUM(e.caloriesBurned), 0) FROM Exercise e WHERE e.user.userId = :userId AND e.startTime >= :startDateTime AND e.startTime < :endDateTime")
//Double sumCaloriesBurnedWithin24HourCycle(@Param("userId") Long userId,
//                                          @Param("startDateTime") LocalTime startDateTime,
//                                          @Param("endDateTime") LocalTime endDateTime);

    List<Exercise> findByUserUserIdAndStartTimeBetween(Long userId, LocalTime startTime, LocalTime endTime);

}
