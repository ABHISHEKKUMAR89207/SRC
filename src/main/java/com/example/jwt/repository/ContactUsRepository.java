package com.example.jwt.repository;

import com.example.jwt.entities.ContactUs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactUsRepository extends JpaRepository<ContactUs, Long> {
    // Add custom queries if needed
}