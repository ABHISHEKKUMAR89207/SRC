package com.vtt.controllers;


import com.vtt.commonfunc.TokenUtils;
import com.vtt.entities.SRCRole;
import com.vtt.entities.User;

import com.vtt.otherclass.MainRole;
import com.vtt.repository.SRCRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/src-roles")
@RequiredArgsConstructor
public class SRCRoleController {

    private final SRCRoleRepository roleRepository;
    private final TokenUtils tokenUtils;
    @GetMapping
    public ResponseEntity<?> getAllRoles() {
        try {
//            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
//            if (requestingUser.getMainRole() != MainRole.ADMIN) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                        .body("Only ADMIN can access this endpoint");
//            }

            return ResponseEntity.ok(roleRepository.findAll());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addRole(@RequestHeader("Authorization") String tokenHeader,
                                     @RequestBody SRCRole role) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN can access this endpoint");
            }

            if (roleRepository.findByName(role.getName()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Role with this name already exists");
            }

            SRCRole savedRole = roleRepository.save(role);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRole);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@RequestHeader("Authorization") String tokenHeader,
                                        @PathVariable String id) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN can access this endpoint");
            }

            if (!roleRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found");
            }

            roleRepository.deleteById(id);
            return ResponseEntity.ok("Role deleted successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}
