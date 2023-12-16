package com.example.jwt.repository;



import com.example.jwt.entities.NotificationEntity;
import com.example.jwt.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    List<NotificationEntity> findByUserEmail(String email);

    void deleteByIdAndUser_UserId(Long id, Long userId);

    @Query("SELECT n FROM NotificationEntity n WHERE n.startTime = :startTime")
    List<NotificationEntity> findByStartTime(@Param("startTime") LocalTime startTime);

    List<NotificationEntity> findByStartTimeBetween(LocalTime startTime, LocalTime endTime);

    NotificationEntity findByUserAndNotificationType(User user, String notificationType);


//    Optional<NotificationEntity> findByUserAndNotificationType(String username, String notificationType);

}