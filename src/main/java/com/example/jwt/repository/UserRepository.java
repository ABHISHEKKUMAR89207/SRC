package com.example.jwt.repository;

import com.example.jwt.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

//    public Optional<User> findByEmail(String email);

    public Optional<User> findByEmail(String email);

//    User findByEmail(String email);

}
