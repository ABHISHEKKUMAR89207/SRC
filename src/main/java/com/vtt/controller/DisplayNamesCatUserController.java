package com.vtt.controller;



import com.vtt.dtoforSrc.DisplayNamesCatRequest;
import com.vtt.entities.DisplayNamesCat;
import com.vtt.entities.User;

import com.vtt.repository.DisplayNamesCatRepository;
import com.vtt.repository.UserRepository;

import com.vtt.security.JwtHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/displaynamescat")
public class DisplayNamesCatUserController {

    private static final Logger logger = LoggerFactory.getLogger(DisplayNamesCatUserController.class);
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DisplayNamesCatRepository displayNamesCatRepository;

    // Create a new DisplayNamesCat entry
    @PostMapping
    public ResponseEntity<?> createDisplayNamesCat(
            @RequestHeader("Authorization") String tokenHeader,
            @RequestBody DisplayNamesCatRequest request) {

        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);

            Optional<User> userOpt = userRepository.findByEmail(username);
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("User not found");
            }

            User user = userOpt.get();
            DisplayNamesCat newEntry = new DisplayNamesCat();
            newEntry.setCategoryName(request.getCategoryName());
            newEntry.setSubCategoryName(request.getSubCategoryName());
            newEntry.setProductName(request.getProductName());
            newEntry.setProductDescription(request.getProductDescription());
            newEntry.setManufacturerName(request.getManufacturerName());
            newEntry.setWebsiteName(request.getWebsiteName());
            newEntry.setSelectdTheme(request.getSelectdTheme());
            newEntry.setImageUrl(request.getImageUrl());
            newEntry.setUser(user);

            DisplayNamesCat savedEntry = displayNamesCatRepository.save(newEntry);
            return ResponseEntity.ok(savedEntry);

        } catch (Exception e) {
            logger.error("Error creating DisplayNamesCat entry", e);
            return ResponseEntity.internalServerError().body("Error creating entry");
        }
    }

    // Get all DisplayNamesCat entries for the current user
    @GetMapping
    public ResponseEntity<?> getAllDisplayNamesCatForUser(
            @RequestHeader("Authorization") String tokenHeader) {

        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);

            Optional<User> userOpt = userRepository.findByEmail(username);
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("User not found");
            }

            User user = userOpt.get();
            List<DisplayNamesCat> entries = displayNamesCatRepository.findByUser(user);
            return ResponseEntity.ok(entries);

        } catch (Exception e) {
            logger.error("Error fetching DisplayNamesCat entries", e);
            return ResponseEntity.internalServerError().body("Error fetching entries");
        }
    }

    // Get a specific DisplayNamesCat entry by ID (only if owned by user)
    @GetMapping("/{id}")
    public ResponseEntity<?> getDisplayNamesCatById(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable String id) {

        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);

            Optional<User> userOpt = userRepository.findByEmail(username);
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("User not found");
            }

            Optional<DisplayNamesCat> entryOpt = displayNamesCatRepository.findById(id);
            if (entryOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            DisplayNamesCat entry = entryOpt.get();
            if (!entry.getUser().getEmail().equals(username)) {
                return ResponseEntity.status(403).body("Not authorized to access this resource");
            }

            return ResponseEntity.ok(entry);

        } catch (Exception e) {
            logger.error("Error fetching DisplayNamesCat entry", e);
            return ResponseEntity.internalServerError().body("Error fetching entry");
        }
    }

    // Update a DisplayNamesCat entry
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDisplayNamesCat(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable String id,
            @RequestBody DisplayNamesCatRequest request) {

        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);

            Optional<User> userOpt = userRepository.findByEmail(username);
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("User not found");
            }

            Optional<DisplayNamesCat> existingEntryOpt = displayNamesCatRepository.findById(id);
            if (existingEntryOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            DisplayNamesCat existingEntry = existingEntryOpt.get();
            if (!existingEntry.getUser().getEmail().equals(username)) {
                return ResponseEntity.status(403).body("Not authorized to update this resource");
            }

            // Update fields from request
            existingEntry.setCategoryName(request.getCategoryName());
            existingEntry.setSubCategoryName(request.getSubCategoryName());
            existingEntry.setProductName(request.getProductName());
            existingEntry.setProductDescription(request.getProductDescription());
            existingEntry.setManufacturerName(request.getManufacturerName());
            existingEntry.setWebsiteName(request.getWebsiteName());
            existingEntry.setSelectdTheme(request.getSelectdTheme());
            existingEntry.setImageUrl(request.getImageUrl());

            DisplayNamesCat savedEntry = displayNamesCatRepository.save(existingEntry);
            return ResponseEntity.ok(savedEntry);

        } catch (Exception e) {
            logger.error("Error updating DisplayNamesCat entry", e);
            return ResponseEntity.internalServerError().body("Error updating entry");
        }
    }


    // Get all display name categories associated with a specific user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getDisplayNamesCatsByUserId(@PathVariable String userId) {
        try {
            User user = userRepo.findByUserId(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }


            List<DisplayNamesCat> entries = displayNamesCatRepository.findByUser(user);
            return ResponseEntity.ok(entries);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving data");
        }
    }
    // Delete a DisplayNamesCat entry
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDisplayNamesCat(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable String id) {

        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);

            Optional<User> userOpt = userRepository.findByEmail(username);
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("User not found");
            }

            Optional<DisplayNamesCat> entryOpt = displayNamesCatRepository.findById(id);
            if (entryOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            DisplayNamesCat entry = entryOpt.get();
            if (!entry.getUser().getEmail().equals(username)) {
                return ResponseEntity.status(403).body("Not authorized to delete this resource");
            }

            displayNamesCatRepository.delete(entry);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            logger.error("Error deleting DisplayNamesCat entry", e);
            return ResponseEntity.internalServerError().body("Error deleting entry");
        }
    }
}