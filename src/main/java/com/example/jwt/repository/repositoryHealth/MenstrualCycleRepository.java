package com.example.jwt.repository.repositoryHealth;

//import com.example.menstrual_reminder.entity.menstrualCycle;
import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.HeartRate;
import com.example.jwt.entities.dashboardEntity.healthTrends.MenstrualCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MenstrualCycleRepository extends JpaRepository<MenstrualCycle, Long> {


//    static Optional<MenstrualCycle> findById(String username) {
//        return null;
//    }

//    List<MenstrualCycle> findByIsFemale(boolean b);

//    List<MenstrualCycle> findByTimeStampBetween(LocalDateTime startTime, LocalDateTime endTime);

//    MenstrualCycle findAllByLocalDate(LocalDate localDate);

//    List<MenstrualCycle> findByUserAndLocalDate(User user, LocalDate date);

}
