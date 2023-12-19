package com.example.jwt.repository;

import com.example.jwt.entities.UnitsDatabase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitsDatabaseRepository extends JpaRepository<UnitsDatabase, Integer> {
    UnitsDatabase findByNutrientName(String nutrientName);
}
