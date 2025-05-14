package com.vtt.controller;

import com.vtt.dtoforSrc.SalaryPaidDetailsDTO;
import com.vtt.entities.SalaryPaidDetails;
import com.vtt.entities.User;
import com.vtt.repository.SalaryPaidDetailsRepository;
import com.vtt.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/salary")
public class SalaryPaidDetailsController {

    @Autowired
    private SalaryPaidDetailsRepository salaryPaidDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // Convert Entity to DTO
    private SalaryPaidDetailsDTO toDTO(SalaryPaidDetails entity) {
        SalaryPaidDetailsDTO dto = new SalaryPaidDetailsDTO();

        dto.setUserId(entity.getUser().getUserId());
        dto.setTotalAmountPaid(entity.getTotalAmountPaid());

        dto.setDateOfPayment(entity.getDateOfPayment());
        dto.setNotes(entity.getNotes());
        dto.setPaymentReferenceNumber(entity.getPaymentReferenceNumber());
        return dto;
    }

    // Convert DTO to Entity
    private SalaryPaidDetails toEntity(SalaryPaidDetailsDTO dto) {
        SalaryPaidDetails entity = new SalaryPaidDetails();

        User user = userRepository.findByUserId(dto.getUserId());
        if (user != null) {
            entity.setUser(user);
        }

        entity.setTotalAmountPaid(dto.getTotalAmountPaid());
        entity.setDateOfPayment(dto.getDateOfPayment());


        entity.setNotes(dto.getNotes());
        entity.setPaymentReferenceNumber(dto.getPaymentReferenceNumber());
        return entity;
    }

    // GET all salary records
    @GetMapping("/all")
    public ResponseEntity<List<SalaryPaidDetailsDTO>> getAll() {
        List<SalaryPaidDetailsDTO> result = salaryPaidDetailsRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // GET salary records by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SalaryPaidDetailsDTO>> getByUser(@PathVariable String userId) {
        List<SalaryPaidDetailsDTO> result = salaryPaidDetailsRepository.findAll()
                .stream()
                .filter(s -> s.getUser() != null && userId.equals(s.getUser().getUserId()))
                .map(this::toDTO)
                .collect(Collectors.toList());

        if (result.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(result);
    }

    // POST: Add new salary record
    @PostMapping("/add")
    public ResponseEntity<SalaryPaidDetailsDTO> addSalary(@RequestBody SalaryPaidDetailsDTO dto) {
        SalaryPaidDetails entity = toEntity(dto);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        SalaryPaidDetails saved = salaryPaidDetailsRepository.save(entity);
        return ResponseEntity.ok(toDTO(saved));
    }

    // PUT: Update specific fields
    @PutMapping("/update/{id}")
    public ResponseEntity<SalaryPaidDetailsDTO> updateSalary(@PathVariable String id, @RequestBody SalaryPaidDetailsDTO dto) {
        Optional<SalaryPaidDetails> existingOpt = salaryPaidDetailsRepository.findById(id);
        if (existingOpt.isEmpty()) return ResponseEntity.notFound().build();

        SalaryPaidDetails existing = existingOpt.get();

        if (dto.getUserId() != null) {
            User user = userRepository.findByUserId(dto.getUserId());
            if (user != null) {
                existing.setUser(user);
            }
        }

        existing.setTotalAmountPaid(dto.getTotalAmountPaid());

        if (dto.getDateOfPayment() != null) {
            existing.setDateOfPayment(dto.getDateOfPayment());
        }

        existing.setNotes(dto.getNotes());
        existing.setPaymentReferenceNumber(dto.getPaymentReferenceNumber());
        existing.setUpdatedAt(Instant.now());

        SalaryPaidDetails updated = salaryPaidDetailsRepository.save(existing);
        return ResponseEntity.ok(toDTO(updated));
    }

    // DELETE: Delete record by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSalary(@PathVariable String id) {
        if (!salaryPaidDetailsRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        salaryPaidDetailsRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
