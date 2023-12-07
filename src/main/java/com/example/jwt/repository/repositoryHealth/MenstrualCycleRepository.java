package com.example.jwt.repository.repositoryHealth;

import com.example.jwt.entities.dashboardEntity.healthTrends.MenstrualCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenstrualCycleRepository extends JpaRepository<MenstrualCycle, Long> {

}
