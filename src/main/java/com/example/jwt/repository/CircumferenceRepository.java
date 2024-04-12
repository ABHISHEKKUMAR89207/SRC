package com.example.jwt.repository;

import com.example.jwt.entities.Circumference;
import com.example.jwt.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CircumferenceRepository extends JpaRepository<Circumference, Long> {

    Circumference findByUserAndDate(User user, LocalDate date);

    List<Circumference> findByUserAndDateBetweenAndWaistCircumferenceNotNull(User user, LocalDate startDate, LocalDate endDate);

    List<Circumference> findByUserAndDateBetweenAndHipCircumferenceNotNull(User user, LocalDate startDate, LocalDate endDate);
}