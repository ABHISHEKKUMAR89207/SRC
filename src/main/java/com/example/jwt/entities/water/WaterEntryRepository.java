package com.example.jwt.entities.water;

import com.example.jwt.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WaterEntryRepository extends JpaRepository<WaterEntry,Long> {
    List<WaterEntry> findByWaterEntity_UserAndLocalDateBetween(User user, LocalDate startDate, LocalDate endDate);

    List<WaterEntry> findAllByLocalDate(LocalDate localDate);

}
