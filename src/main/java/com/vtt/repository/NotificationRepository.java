package com.vtt.repository;



import com.vtt.entities.NotificationEntity;
import com.vtt.entities.User;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByUser(User user);

    List<NotificationEntity> findByUserEmail(String email);

    void deleteByIdAndUser_UserId(Long id, Long userId);

    @Query("SELECT n FROM NotificationEntity n WHERE n.startTime = :startTime")
    List<NotificationEntity> findByStartTime(@Param("startTime") LocalTime startTime);

    List<NotificationEntity> findByStartTimeBetween(LocalTime startTime, LocalTime endTime);

    NotificationEntity findByUserAndNotificationType(User user, String notificationType);


//    Optional<NotificationEntity> findByUserAndNotificationType(String username, String notificationType);

}