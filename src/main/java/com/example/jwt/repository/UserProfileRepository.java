package com.example.jwt.repository;

import com.example.jwt.entities.User;
import com.example.jwt.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    List<UserProfile> findAllByUser(User user);
    List<UserProfile> findByGender(String user);
    UserProfile findByUserEmail(String email);
    List<UserProfile> findAllUserByGender(String gender);
    Integer countByGender(String gender);

    UserProfile findByUserUserId(Long id);
}
