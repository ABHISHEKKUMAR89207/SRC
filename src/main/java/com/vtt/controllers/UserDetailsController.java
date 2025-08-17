package com.vtt.controllers;


import com.vtt.FileStorage.FileStorageService;
import com.vtt.dtoforSrc.UserDetailsResponse;
import com.vtt.dtoforSrc.UserDetailsWithImageDto;
import com.vtt.entities.User;
import com.vtt.entities.UserDetails;
import com.vtt.repository.UserDetailsRepository;
import com.vtt.repository.UserRepository;
import com.vtt.security.JwtHelper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-details")
public class UserDetailsController {

    private final UserDetailsRepository userDetailsRepository;
    private final UserRepository userRepository;
    private final JwtHelper jwtHelper;
    private final FileStorageService fileStorageService;
    private final Logger logger = LoggerFactory.getLogger(UserDetailsController.class);

    @Value("${file.base-url}")
    private String fileBaseUrl;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> createUserDetailsWithImage(
            @RequestHeader("Authorization") String tokenHeader,
            @ModelAttribute UserDetailsWithImageDto userDetailsDto) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            logger.info("Extracted Username: " + username);

            Optional<User> userOpt = userRepository.findByEmail(username);
            if (userOpt.isPresent()) {
                User user = userOpt.get();

                if (userDetailsRepository.findByUser(user).isPresent()) {
                    return ResponseEntity.badRequest().body("User details already exist");
                }

                UserDetails userDetails = new UserDetails();

                // Handle profile picture upload
                if (userDetailsDto.getProfilePicture() != null && !userDetailsDto.getProfilePicture().isEmpty()) {
                    String fileName = fileStorageService.storeFile(userDetailsDto.getProfilePicture());
                    String fileUrl = fileBaseUrl + fileName;
                    userDetails.setProfilePictureUrl(fileUrl);
                    user.setProfilePictureUrl(fileUrl);
                }

                // Map other fields
                userDetails.setMobileNumber(userDetailsDto.getMobileNumber());
                userDetails.setPanNumber(userDetailsDto.getPanNumber());
                userDetails.setAadharNumber(userDetailsDto.getAadharNumber());
                userDetails.setAddress(userDetailsDto.getAddress());
                userDetails.setEmploymentType(userDetailsDto.getEmploymentType());
                userDetails.setBankName(userDetailsDto.getBankName());
                userDetails.setAccountNumber(userDetailsDto.getAccountNumber());
                userDetails.setIfscCode(userDetailsDto.getIfscCode());
                userDetails.setUpiId(userDetailsDto.getUpiId());
                userDetails.setPaytmNumber(userDetailsDto.getPaytmNumber());
                userDetails.setPhonePeNumber(userDetailsDto.getPhonePeNumber());

                // Set user reference and timestamps
                userDetails.setUser(user);
                userDetails.setCreatedAt(Instant.now());
                userDetails.setUpdatedAt(Instant.now());
                userDetails.setMobileNumber(user.getMobileNo());

                UserDetails savedDetails = userDetailsRepository.save(userDetails);
                user.setBankDetailsStatus(true);

                User user1 = userRepository.save(user);
                return ResponseEntity.ok(UserDetailsResponse.fromEntity(savedDetails));
            } else {
                return ResponseEntity.status(404).body("User not found");
            }
        } catch (IOException e) {
            logger.error("Error while storing profile picture: ", e);
            return ResponseEntity.status(500).body("Error storing profile picture");
        } catch (Exception e) {
            logger.error("Error while creating user details: ", e);
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    @PutMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateUserDetailsWithImage(
            @RequestHeader("Authorization") String tokenHeader,
            @ModelAttribute UserDetailsWithImageDto updatedDetailsDto) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            logger.info("Extracted Username: " + username);

            Optional<User> userOpt = userRepository.findByEmail(username);
            if (userOpt.isPresent()) {
                User user = userOpt.get();

                Optional<UserDetails> existingDetailsOpt = userDetailsRepository.findByUser(user);
                if (existingDetailsOpt.isPresent()) {
                    UserDetails existingDetails = existingDetailsOpt.get();

                    // Handle profile picture update
                    if (updatedDetailsDto.getProfilePicture() != null && !updatedDetailsDto.getProfilePicture().isEmpty()) {
                        // Delete old file if exists
                        if (existingDetails.getProfilePictureUrl() != null) {
                            String oldFileName = existingDetails.getProfilePictureUrl().replace(fileBaseUrl, "");
                            fileStorageService.deleteFile(oldFileName);
                        }

                        // Store new file
                        String fileName = fileStorageService.storeFile(updatedDetailsDto.getProfilePicture());
                        String fileUrl = fileBaseUrl + fileName;
                        existingDetails.setProfilePictureUrl(fileUrl);
                    }

                    // Update other fields
                    if (updatedDetailsDto.getMobileNumber() != null) {
                        existingDetails.setMobileNumber(updatedDetailsDto.getMobileNumber());
                    }
                    if (updatedDetailsDto.getPanNumber() != null) {
                        existingDetails.setPanNumber(updatedDetailsDto.getPanNumber());
                    }
                    if (updatedDetailsDto.getAadharNumber() != null) {
                        existingDetails.setAadharNumber(updatedDetailsDto.getAadharNumber());
                    }
                    if (updatedDetailsDto.getAddress() != null) {
                        existingDetails.setAddress(updatedDetailsDto.getAddress());
                    }
                    if (updatedDetailsDto.getEmploymentType() != null) {
                        existingDetails.setEmploymentType(updatedDetailsDto.getEmploymentType());
                    }
                    if (updatedDetailsDto.getBankName() != null) {
                        existingDetails.setBankName(updatedDetailsDto.getBankName());
                    }
                    if (updatedDetailsDto.getAccountNumber() != null) {
                        existingDetails.setAccountNumber(updatedDetailsDto.getAccountNumber());
                    }
                    if (updatedDetailsDto.getIfscCode() != null) {
                        existingDetails.setIfscCode(updatedDetailsDto.getIfscCode());
                    }
                    if (updatedDetailsDto.getUpiId() != null) {
                        existingDetails.setUpiId(updatedDetailsDto.getUpiId());
                    }
                    if (updatedDetailsDto.getPaytmNumber() != null) {
                        existingDetails.setPaytmNumber(updatedDetailsDto.getPaytmNumber());
                    }
                    if (updatedDetailsDto.getPhonePeNumber() != null) {
                        existingDetails.setPhonePeNumber(updatedDetailsDto.getPhonePeNumber());
                    }

                    existingDetails.setUpdatedAt(Instant.now());

                    UserDetails savedDetails = userDetailsRepository.save(existingDetails);
                    return ResponseEntity.ok(UserDetailsResponse.fromEntity(savedDetails));
                } else {
                    return ResponseEntity.status(404).body("User details not found");
                }
            } else {
                return ResponseEntity.status(404).body("User not found");
            }
        } catch (IOException e) {
            logger.error("Error while updating profile picture: ", e);
            return ResponseEntity.status(500).body("Error updating profile picture");
        } catch (Exception e) {
            logger.error("Error while updating user details: ", e);
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    // Add this endpoint to serve the images
    @GetMapping("/images/{fileName:.+}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) {
        try {
            byte[] imageBytes = fileStorageService.loadFile(fileName);
            return ResponseEntity.ok()
                    .header("Content-Type", "image/jpeg") // Adjust based on your image type
                    .body(imageBytes);
        } catch (IOException e) {
            logger.error("Error loading image: ", e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<?> getUserDetails(@RequestHeader("Authorization") String tokenHeader) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            logger.info("Extracted Username: " + username);

            Optional<User> userOpt = userRepository.findByEmail(username);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                Optional<UserDetails> userDetailsOpt = userDetailsRepository.findByUser(user);

                if (userDetailsOpt.isPresent()) {
                    return ResponseEntity.ok(UserDetailsResponse.fromEntity(userDetailsOpt.get()));
                } else {
                    return ResponseEntity.status(404).body("User details not found");
                }
            } else {
                return ResponseEntity.status(404).body("User not found");
            }
        } catch (Exception e) {
            logger.error("Error while fetching user details: ", e);
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

}