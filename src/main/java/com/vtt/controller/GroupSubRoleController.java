package com.vtt.controller;


import com.vtt.commonfunc.TokenUtils;
import com.vtt.entities.GroupSubRole;
import com.vtt.entities.User;
import com.vtt.otherclass.MainRole;
import com.vtt.repository.GroupSubRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/subroles")
public class GroupSubRoleController {

    @Autowired
    private GroupSubRoleRepository subRoleRepository;

    @Autowired
    private TokenUtils tokenUtils;

    private boolean isAdmin(String tokenHeader) {
        User user = tokenUtils.getUserFromToken(tokenHeader);
        return user.getMainRole() == MainRole.ADMIN;
    }

    // Check for existing role names
    private List<String> getExistingRoleNames(List<String> inputRoles) {
        List<GroupSubRole> allSubRoles = subRoleRepository.findAll();
        Set<String> existingRoles = allSubRoles.stream()
                .flatMap(g -> g.getRoleName().stream())
                .collect(Collectors.toSet());

        return inputRoles.stream()
                .filter(existingRoles::contains)
                .collect(Collectors.toList());
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String tokenHeader) {
        try {
//            if (!isAdmin(tokenHeader)) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only ADMIN can access this endpoint");
//            }
            List<GroupSubRole> all = subRoleRepository.findAll();
            return ResponseEntity.ok(all);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token or unauthorized access");
        }
    }

    @PostMapping
    public ResponseEntity<?> addSubRole(@RequestBody GroupSubRole subRole,
                                        @RequestHeader("Authorization") String tokenHeader) {
        try {
            if (!isAdmin(tokenHeader)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only ADMIN can access this endpoint");
            }

            List<String> duplicates = getExistingRoleNames(subRole.getRoleName());
            if (!duplicates.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Duplicate roleNames found: " + String.join(", ", duplicates));
            }

            subRoleRepository.save(subRole);
            return ResponseEntity.status(HttpStatus.CREATED).body("SubRole created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSubRole(@PathVariable String id,
                                           @RequestBody GroupSubRole updatedSubRole,
                                           @RequestHeader("Authorization") String tokenHeader) {
        try {
            if (!isAdmin(tokenHeader)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only ADMIN can access this endpoint");
            }

            Optional<GroupSubRole> existing = subRoleRepository.findById(id);
            if (existing.isPresent()) {
                List<String> duplicates = getExistingRoleNames(updatedSubRole.getRoleName());

                // Allow roleNames if they belong to the current subrole
                Set<String> currentRoleNames = new HashSet<>(existing.get().getRoleName());
                duplicates.removeIf(currentRoleNames::contains);

                if (!duplicates.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Duplicate roleNames found: " + String.join(", ", duplicates));
                }

                updatedSubRole.setId(id);
                subRoleRepository.save(updatedSubRole);
                return ResponseEntity.ok("SubRole updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SubRole not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSubRole(@PathVariable String id,
                                           @RequestHeader("Authorization") String tokenHeader) {
        try {
            if (!isAdmin(tokenHeader)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only ADMIN can access this endpoint");
            }

            if (subRoleRepository.existsById(id)) {
                subRoleRepository.deleteById(id);
                return ResponseEntity.ok("SubRole deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SubRole not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}
