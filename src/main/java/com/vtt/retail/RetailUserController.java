package com.vtt.retail;

import com.vtt.retail.entities.RetailUser;
import com.vtt.retail.repository.RetailUserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/retail/users")
@Tag(name = "Retail User Controller", description = "API for user profile management")
public class RetailUserController {

    private final RetailUserRepository retailUserRepository;

    /**
     * Create or update user profile
     */
    @PostMapping("/profile")
    public ResponseEntity<?> createOrUpdateProfile(
            @RequestHeader("Authorization") String token,
            @RequestBody UserProfileRequest profileRequest) {
        try {
            // In real implementation, extract userId from JWT token
            // For now, using userId from request
            String userId = profileRequest.getUserId();

            if (userId == null || userId.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("User ID is required", null, false));
            }

            Optional<RetailUser> existingUser = retailUserRepository.findByUserId(userId);
            RetailUser user;

            if (existingUser.isPresent()) {
                user = existingUser.get();
                // Update existing profile
                if (profileRequest.getName() != null) user.setName(profileRequest.getName());
                if (profileRequest.getEmail() != null) user.setEmail(profileRequest.getEmail());
                if (profileRequest.getMobileNo() != null) user.setMobileNo(profileRequest.getMobileNo());
                if (profileRequest.getPhone() != null) user.setPhone(profileRequest.getPhone());
                if (profileRequest.getAddressLine1() != null) user.setAddressLine1(profileRequest.getAddressLine1());
                if (profileRequest.getAddressLine2() != null) user.setAddressLine2(profileRequest.getAddressLine2());
                if (profileRequest.getCity() != null) user.setCity(profileRequest.getCity());
                if (profileRequest.getState() != null) user.setState(profileRequest.getState());
                if (profileRequest.getPostalCode() != null) user.setPostalCode(profileRequest.getPostalCode());
                if (profileRequest.getCountry() != null) user.setCountry(profileRequest.getCountry());
                if (profileRequest.getLandmark() != null) user.setLandmark(profileRequest.getLandmark());
                if (profileRequest.getGender() != null) user.setGender(profileRequest.getGender());
                if (profileRequest.getDateOfBirth() != null) user.setDateOfBirth(profileRequest.getDateOfBirth());
                
                user.setUpdatedAt(LocalDateTime.now());
            } else {
                // Create new profile
                user = new RetailUser();
                user.setUserId(userId);
                user.setName(profileRequest.getName());
                user.setEmail(profileRequest.getEmail());
                user.setMobileNo(profileRequest.getMobileNo());
                user.setPhone(profileRequest.getPhone());
                user.setAddressLine1(profileRequest.getAddressLine1());
                user.setAddressLine2(profileRequest.getAddressLine2());
                user.setCity(profileRequest.getCity());
                user.setState(profileRequest.getState());
                user.setPostalCode(profileRequest.getPostalCode());
                user.setCountry(profileRequest.getCountry());
                user.setLandmark(profileRequest.getLandmark());
                user.setGender(profileRequest.getGender());
                user.setDateOfBirth(profileRequest.getDateOfBirth());
                user.setCreatedAt(LocalDateTime.now());
                user.setUpdatedAt(LocalDateTime.now());
            }

            RetailUser savedUser = retailUserRepository.save(user);
            return ResponseEntity.ok(new ApiResponse<>("Profile saved successfully", savedUser, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get user profile by ID
     */
    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable String userId) {
        try {
            Optional<RetailUser> user = retailUserRepository.findByUserId(userId);

            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("User profile not found", null, false));
            }

            return ResponseEntity.ok(new ApiResponse<>("Success", user.get(), true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Update user address
     */
    @PutMapping("/profile/{userId}/address")
    public ResponseEntity<?> updateUserAddress(
            @PathVariable String userId,
            @RequestBody AddressRequest addressRequest) {
        try {
            Optional<RetailUser> userOptional = retailUserRepository.findByUserId(userId);

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("User not found", null, false));
            }

            RetailUser user = userOptional.get();
            user.setAddressLine1(addressRequest.getAddressLine1());
            user.setAddressLine2(addressRequest.getAddressLine2());
            user.setCity(addressRequest.getCity());
            user.setState(addressRequest.getState());
            user.setPostalCode(addressRequest.getPostalCode());
            user.setCountry(addressRequest.getCountry());
            user.setLandmark(addressRequest.getLandmark());
            user.setUpdatedAt(LocalDateTime.now());

            RetailUser updatedUser = retailUserRepository.save(user);
            return ResponseEntity.ok(new ApiResponse<>("Address updated successfully", updatedUser, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Check if user profile exists
     */
    @GetMapping("/exists/{userId}")
    public ResponseEntity<?> userExists(@PathVariable String userId) {
        try {
            boolean exists = retailUserRepository.existsByUserId(userId);
            
            Map<String, Boolean> response = new HashMap<>();
            response.put("exists", exists);
            
            return ResponseEntity.ok(new ApiResponse<>("Success", response, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Delete user profile
     */
    @DeleteMapping("/profile/{userId}")
    public ResponseEntity<?> deleteUserProfile(@PathVariable String userId) {
        try {
            Optional<RetailUser> userOptional = retailUserRepository.findByUserId(userId);

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("User not found", null, false));
            }

            retailUserRepository.deleteById(userOptional.get().getId());
            return ResponseEntity.ok(new ApiResponse<>("User profile deleted successfully", null, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    // DTOs
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserProfileRequest {
        private String userId;
        private String name;
        private String email;
        private String mobileNo;
        private String phone;
        private String addressLine1;
        private String addressLine2;
        private String city;
        private String state;
        private String postalCode;
        private String country;
        private String landmark;
        private String profileImage;
        private String gender;
        private String dateOfBirth;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressRequest {
        private String addressLine1;
        private String addressLine2;
        private String city;
        private String state;
        private String postalCode;
        private String country;
        private String landmark;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiResponse<T> {
        private String message;
        private T data;
        private boolean success;
    }
}
