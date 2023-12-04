package com.example.jwt.repository;

import com.example.jwt.entities.User;
import com.example.jwt.registration.token.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    //    public Optional<User> findByEmail(String email);
    Optional<User> findByUserName(String userName);


    public Optional<User> findByEmail(String email);

    List<User> findByEmailVerified(boolean emailVerified);

    List<User> findByEmailVerifiedFalseAndRegistrationTimestampBefore(LocalDateTime timestamp);

//    User findByEmail(String email);

//    List<User> findByVerificationTokens(VerificationToken verificationToken);
//    Optional<User> findByVerificationTokensContains(VerificationToken verificationToken);

    Optional<User> findByVerificationTokens_Token(String verificationToken);





}
