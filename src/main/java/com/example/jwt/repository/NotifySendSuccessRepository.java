package com.example.jwt.repository;

import com.example.jwt.entities.NotificationEntity;
import com.example.jwt.entities.NotifySendSuccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NotifySendSuccessRepository extends JpaRepository<NotifySendSuccess, Long> {
    List<NotifySendSuccess> findByUserEmail(String email);
    List<NotifySendSuccess> findByNotificationEntity(NotificationEntity notificationEntity);



}
