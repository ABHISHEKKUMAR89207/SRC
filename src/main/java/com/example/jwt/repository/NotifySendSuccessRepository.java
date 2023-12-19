package com.example.jwt.repository;

import com.example.jwt.entities.NotifySendSuccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface NotifySendSuccessRepository extends JpaRepository<NotifySendSuccess, Long> {
}
