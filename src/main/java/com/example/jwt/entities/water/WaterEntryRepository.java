package com.example.jwt.entities.water;

import com.example.jwt.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WaterEntryRepository extends JpaRepository<WaterEntry,Long> {
    List<WaterEntry> findByWaterEntity_UserAndLocalDateBetween(User user, LocalDate startDate, LocalDate endDate);
    @Query("SELECT w FROM WaterEntry w WHERE w.entryId = :entryId AND w.waterEntity.user = :user")
    Optional<WaterEntry> findByIdAndUser(@Param("entryId") Long entryId, @Param("user") User user);
    List<WaterEntry> findAllByLocalDate(LocalDate localDate);

}
