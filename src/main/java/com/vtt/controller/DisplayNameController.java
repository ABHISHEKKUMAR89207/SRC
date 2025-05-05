package com.vtt.controller;


import com.vtt.commonfunc.TokenUtils;
import com.vtt.entities.DisplayName;
import com.vtt.entities.User;
import com.vtt.otherclass.MainRole;
import com.vtt.repository.DisplayNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/displaynames")
public class DisplayNameController {

    @Autowired
    private DisplayNameRepository displayNameRepository;

    @Autowired
    private TokenUtils tokenUtils;

    private boolean isAdmin(String tokenHeader) {
        User user = tokenUtils.getUserFromToken(tokenHeader);
        return user.getMainRole() == MainRole.ADMIN;
    }

    @GetMapping
    public ResponseEntity<?> getAllDisplayNames(@RequestHeader("Authorization") String tokenHeader) {
        try {
            if (!isAdmin(tokenHeader)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only ADMIN can access this endpoint");
            }
            List<DisplayName> names = displayNameRepository.findAll();
            return ResponseEntity.ok(names);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token or unauthorized access");
        }
    }

    @PostMapping
    public ResponseEntity<?> addDisplayName(@RequestBody DisplayName displayName,
                                            @RequestHeader("Authorization") String tokenHeader) {
        try {
            if (!isAdmin(tokenHeader)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only ADMIN can access this endpoint");
            }

            Optional<DisplayName> existing = displayNameRepository.findByName(displayName.getName());
            if (existing.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Display name already exists: " + displayName.getName());
            }

            displayNameRepository.save(displayName);
            return ResponseEntity.status(HttpStatus.CREATED).body("Display name added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDisplayName(@PathVariable String id,
                                               @RequestHeader("Authorization") String tokenHeader) {
        try {
            if (!isAdmin(tokenHeader)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only ADMIN can access this endpoint");
            }

            if (displayNameRepository.existsById(id)) {
                displayNameRepository.deleteById(id);
                return ResponseEntity.ok("Display name deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Display name not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}
