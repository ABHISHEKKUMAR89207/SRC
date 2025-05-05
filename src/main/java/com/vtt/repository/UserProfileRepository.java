package com.vtt.repository;

import com.vtt.entities.User;
import com.vtt.entities.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserProfileRepository extends MongoRepository<UserProfile, Long> {

    List<UserProfile> findAllByUser(User user);
    List<UserProfile> findByGender(String user);
    UserProfile findByUserEmail(String email);
    List<UserProfile> findAllUserByGender(String gender);
    Integer countByGender(String gender);

    UserProfile findByUserUserId(Long id);
}
