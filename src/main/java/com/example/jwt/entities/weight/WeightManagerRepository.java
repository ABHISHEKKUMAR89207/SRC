package com.example.jwt.entities.weight;



import com.example.jwt.entities.User;
import com.example.jwt.entities.weight.WeightManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeightManagerRepository extends JpaRepository<WeightManager, Long> {
    Optional<WeightManager> findByLocalDateAndUser(LocalDate localDate, User user);
    List<WeightManager> findByUserAndLocalDateBetween(User user, LocalDate startDate, LocalDate endDate);

    @Query("SELECT w FROM WeightManager w WHERE w.user = :user ORDER BY w.localDate DESC")
    List<WeightManager> findLatestWeightByUser(@Param("user") User user);

}
