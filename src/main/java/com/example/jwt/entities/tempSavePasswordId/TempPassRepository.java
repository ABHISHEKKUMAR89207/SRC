package com.example.jwt.entities.tempSavePasswordId;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TempPassRepository extends JpaRepository<tempSaveIdPass, Long> {
    Optional<tempSaveIdPass> findByUsername(String username);

}
