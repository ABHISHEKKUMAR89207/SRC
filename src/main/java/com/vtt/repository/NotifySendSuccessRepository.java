package com.vtt.repository;

import com.vtt.entities.NotificationEntity;
import com.vtt.entities.NotifySendSuccess;
import com.vtt.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotifySendSuccessRepository extends MongoRepository<NotifySendSuccess, Long> {
    List<NotifySendSuccess> findByUserEmail(String email);
    List<NotifySendSuccess> findByNotificationEntity(NotificationEntity notificationEntity);

    void deleteByUser(User user);


    Optional<NotifySendSuccess> findByIdAndUser(Long id, User user);


}
