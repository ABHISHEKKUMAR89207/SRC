package com.example.jwt.repository;

import com.example.jwt.entities.User;
//import com.google.api.gax.paging.Page;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


//import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

//import java.awt.print.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    List<User> findAllByUserProfile_GenderIgnoreCase(String gender);

    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.userName) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(u.mobileNo) LIKE LOWER(CONCAT('%', :searchQuery, '%'))")
    Page<User> findBySearchQuery(String searchQuery, Pageable pageable);

    @Query("SELECT COUNT(u) FROM User u WHERE " +
            "LOWER(u.userName) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(u.mobileNo) LIKE LOWER(CONCAT('%', :searchQuery, '%'))")
    long countBySearchQuery(String searchQuery);
    public Optional<User> findByEmail(String email);
//    List<User> findByRegistrationTimestampBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<User> findByEmailVerifiedFalseAndRegistrationTimestampBefore(LocalDateTime timestamp);

//    Page<User> findAll(Pageable pageable);
//long countByGender(String gender);

Integer countByUserProfileGender(String gender);

    List<User> findByLocalDateBetween(LocalDate startDateTime, LocalDate endDateTime);

    List<User> findByRegistrationTimestampBetween(LocalDateTime startOfMonth, LocalDateTime endOfMonth);

    User findByUserId(Long userId);
}
