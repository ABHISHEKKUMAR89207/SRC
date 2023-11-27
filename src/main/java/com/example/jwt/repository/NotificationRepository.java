package com.example.jwt.repository;



import com.example.jwt.entities.NotificationEntity;
import com.example.jwt.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;


public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
//    List<NotificationEntity> findByNotificationOnIsTrueAndStartTimeBefore(LocalDateTime time);

//    List<NotificationEntity> findByStartTimeBeforeAndLastTimeAfterAndNotificationOnIsTrue(
//            LocalDateTime startTime, LocalDateTime lastTime);

//    List<NotificationEntity> findByStartTime(LocalDateTime startTime);
//List<NotificationEntity> findByNotificationOnIsTrueAndStartTimeBefore(LocalTime startTime);
//    List<NotificationEntity> findByStartTime(LocalTime startTime);

    @Query("SELECT n FROM NotificationEntity n WHERE n.startTime = :startTime")
    List<NotificationEntity> findByStartTime(@Param("startTime") LocalTime startTime);

    List<NotificationEntity> findByStartTimeBetween(LocalTime startTime, LocalTime endTime);

    NotificationEntity findByUserAndNotificationType(User user, String notificationType);
//    List<NotificationEntity> findByStartTimeBeforeAndNotificationOnIsTrue(LocalTime currentTime);
//@Modifying
//@Transactional
//@Query("MERGE INTO NotificationEntity n USING :notification WHERE n.id = :#{#notification.id} WHEN MATCHED THEN UPDATE SET n = :notification WHEN NOT MATCHED THEN INSERT (/* fields here */) VALUES (/* values here */)")
//void merge(@Param("notification") NotificationEntity notification);

}