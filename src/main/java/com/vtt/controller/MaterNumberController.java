package com.vtt.controller;

import com.vtt.commonfunc.TokenUtils;
import com.vtt.dtoforSrc.MaterNumberDTO;
import com.vtt.entities.MaterNumber;
import com.vtt.entities.User;

import com.vtt.otherclass.MainRole;
import com.vtt.repository.MaterNumberRepository;
import com.vtt.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mater-numbers")
@RequiredArgsConstructor
public class MaterNumberController {

    private final MaterNumberRepository materNumberRepo;
    private final UserRepository userRepo;
    private final TokenUtils tokenUtils;

    private boolean isAdmin(String token) {
        User user = tokenUtils.getUserFromToken(token);
        return user.getMainRole() == MainRole.ADMIN;
    }

    @PostMapping
    public ResponseEntity<?> addMaterNumber(@RequestHeader("Authorization") String token,
                                            @RequestParam String userId,
                                            @RequestParam int materNumber) {
        if (!isAdmin(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only ADMIN can access this endpoint");

        User userOpt = userRepo.findByUserId(userId);
        if (userOpt == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

        // CHECK if materNumber already exists
        if (materNumberRepo.existsByMaterNumber(materNumber)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Mater number already exists");
        }

        try {
            MaterNumber mn = new MaterNumber();
            mn.setUser(userOpt);
            mn.setMaterNumber(materNumber);
            return ResponseEntity.ok(materNumberRepo.save(mn));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMaterNumber(@RequestHeader("Authorization") String token,
                                               @PathVariable String id,
                                               @RequestParam String userId) {
        if (!isAdmin(token))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only ADMIN can access this endpoint");

        Optional<MaterNumber> mnOpt = materNumberRepo.findById(id);
        if (mnOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mater number not found");

        MaterNumber mn = mnOpt.get();

        try {
            // Only update user if userId is provided and not empty
            if (userId != null && !userId.trim().isEmpty()) {
                User newUser = userRepo.findByUserId(userId);
                if (newUser == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("New user not found");
                }

                mn.setUser(newUser);
            }else { mn.setUser(null);}
                // Mater Number will NOT change, only user can change
            return ResponseEntity.ok(materNumberRepo.save(mn));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMaterNumber(@RequestHeader("Authorization") String token,
                                               @PathVariable String id) {
        if (!isAdmin(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only ADMIN can access this endpoint");

        if (!materNumberRepo.existsById(id)) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mater number not found");

        materNumberRepo.deleteById(id);
        return ResponseEntity.ok("Mater number deleted");
    }

    @GetMapping
    public ResponseEntity<?> getAllMaterNumbers(@RequestHeader("Authorization") String token) {
        if (!isAdmin(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only ADMIN can access this endpoint");

        return ResponseEntity.ok(materNumberRepo.findAll());
    }

    @GetMapping("/all-with-user")
    public ResponseEntity<?> getAllMaterNumberDTOs(@RequestHeader("Authorization") String token) {
        if (!isAdmin(token))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only ADMIN can access this endpoint");

        List<MaterNumber> materNumbers = materNumberRepo.findAll();
        List<MaterNumberDTO> dtos = materNumbers.stream().map(mn -> {
            MaterNumberDTO dto = new MaterNumberDTO();
            dto.setId(mn.getId());
            dto.setMaterNumber(mn.getMaterNumber());
            if (mn.getUser() != null) {
                dto.setUserId(mn.getUser().getUserId());
                dto.setUserName(mn.getUser().getUserName());
                dto.setMobileNo(mn.getUser().getMobileNo());
            }
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }



    @GetMapping("/my-mater-number")
    public ResponseEntity<?> getMaterNumberForCurrentUser(@RequestHeader("Authorization") String token) {
        User user = tokenUtils.getUserFromToken(token);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        // Find MaterNumber associated with this user
        MaterNumber materNumber = materNumberRepo.findByUser(user);
        if (materNumber == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Mater number associated with this user");
        }

        return ResponseEntity.ok(materNumber);
    }

}
