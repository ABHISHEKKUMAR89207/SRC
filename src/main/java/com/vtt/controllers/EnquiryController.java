package com.vtt.controllers;

import com.vtt.entities.Enquiry;
import com.vtt.entities.User;
import com.vtt.repository.EnquiryRepository;
import com.vtt.repository.UserRepository;
import com.vtt.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/enquiry")
public class EnquiryController {

    @Autowired
    private EnquiryRepository enquiryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtHelper jwtHelper;

    // Create new enquiry - NO AUTHENTICATION REQUIRED
    @PostMapping("/create")
    public ResponseEntity<?> createEnquiry(@RequestBody Enquiry enquiryRequest) {
        try {
            // Create new enquiry without user authentication
            Enquiry enquiry = new Enquiry();
            enquiry.setProductName(enquiryRequest.getProductName());
            enquiry.setBuyRequirements(enquiryRequest.getBuyRequirements());
            enquiry.setQuantityValue(enquiryRequest.getQuantityValue());
            enquiry.setQuantityType(enquiryRequest.getQuantityType());
            enquiry.setFrequency(enquiryRequest.getFrequency());
            enquiry.setCustomerName(enquiryRequest.getCustomerName());
            enquiry.setEmail(enquiryRequest.getEmail());
            enquiry.setMobileNo(enquiryRequest.getMobileNo());
            enquiry.setCountry(enquiryRequest.getCountry());
            enquiry.setCity(enquiryRequest.getCity());
            enquiry.setCompanyName(enquiryRequest.getCompanyName());

            // Set userId to null or empty since no user is logged in
            enquiry.setUserId(null);
            enquiry.setStatus("Pending");

            Enquiry savedEnquiry = enquiryRepository.save(enquiry);

            return ResponseEntity.ok(savedEnquiry);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

//    // Get all enquiries for logged-in user - AUTHENTICATION REQUIRED
//    @GetMapping("/my-enquiries")
//    public ResponseEntity<?> getMyEnquiries(@RequestHeader("Authorization") String tokenHeader) {
//        try {
//            String token = tokenHeader.replace("Bearer ", "");
//            String username = jwtHelper.getUsernameFromToken(token);
//
//            User user = userRepository.findByEmail(username)
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//
//            List<Enquiry> enquiries = enquiryRepository.findByUserIdOrderByEnquiryDateDesc(user.getUserId());
//
//            return ResponseEntity.ok(enquiries);
//
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
//        }
//    }

    // Get all enquiries (Admin only) - AUTHENTICATION REQUIRED
    @GetMapping("/all")
    public ResponseEntity<?> getAllEnquiries(@RequestHeader("Authorization") String tokenHeader) {
        try {
            // Verify admin access if needed
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);

            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Add admin check if required
            // if (!user.getRole().equals("ADMIN")) {
            //     return ResponseEntity.status(403).body("Access denied");
            // }

            List<Enquiry> enquiries = enquiryRepository.findAllByOrderByEnquiryDateDesc();

            return ResponseEntity.ok(enquiries);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Get enquiry by ID - AUTHENTICATION REQUIRED
    @GetMapping("/{enquiryId}")
    public ResponseEntity<?> getEnquiryById(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable String enquiryId) {

        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);

            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Optional<Enquiry> enquiry = enquiryRepository.findById(enquiryId);

            if (enquiry.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Check if user owns the enquiry or is admin
            if (!enquiry.get().getUserId().equals(user.getUserId())) {
                // Add admin check here if needed
                // if (!user.getRole().equals("ADMIN")) {
                //     return ResponseEntity.status(403).body("Access denied to this enquiry");
                // }
            }

            return ResponseEntity.ok(enquiry.get());

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Update enquiry status (Admin only) - AUTHENTICATION REQUIRED
    @PutMapping("/update-status")
    public ResponseEntity<?> updateEnquiryStatus(
            @RequestHeader("Authorization") String tokenHeader,
            @RequestParam String enquiryId,
            @RequestParam String status) {

        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);

            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Add admin check if required
            // if (!user.getRole().equals("ADMIN")) {
            //     return ResponseEntity.status(403).body("Access denied");
            // }

            Enquiry enquiry = enquiryRepository.findById(enquiryId)
                    .orElseThrow(() -> new RuntimeException("Enquiry not found"));

            // Validate status
            if (!isValidStatus(status)) {
                return ResponseEntity.badRequest().body("Error: Invalid status");
            }

            enquiry.setStatus(status);
            Enquiry updatedEnquiry = enquiryRepository.save(enquiry);

            return ResponseEntity.ok(updatedEnquiry);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Delete enquiry - AUTHENTICATION REQUIRED
    @DeleteMapping("/delete/{enquiryId}")
    public ResponseEntity<?> deleteEnquiry(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable String enquiryId) {

        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);

            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Enquiry enquiry = enquiryRepository.findById(enquiryId)
                    .orElseThrow(() -> new RuntimeException("Enquiry not found"));

            // Check if user owns the enquiry
            if (!enquiry.getUserId().equals(user.getUserId())) {
                return ResponseEntity.status(403).body("Access denied to delete this enquiry");
            }

            enquiryRepository.delete(enquiry);

            return ResponseEntity.ok("Enquiry deleted successfully");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Get enquiries by status - AUTHENTICATION REQUIRED
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getEnquiriesByStatus(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable String status) {

        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);

            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Validate status
            if (!isValidStatus(status)) {
                return ResponseEntity.badRequest().body("Error: Invalid status");
            }

            List<Enquiry> enquiries;

            // If user is not admin, only show their enquiries
            // if (!user.getRole().equals("ADMIN")) {
            enquiries = enquiryRepository.findByUserIdAndStatus(user.getUserId(), status);
            // } else {
            //     enquiries = enquiryRepository.findByStatus(status);
            // }

            return ResponseEntity.ok(enquiries);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Update enquiry details - AUTHENTICATION REQUIRED
    @PutMapping("/update/{enquiryId}")
    public ResponseEntity<?> updateEnquiry(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable String enquiryId,
            @RequestBody Enquiry enquiryRequest) {

        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);

            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Enquiry enquiry = enquiryRepository.findById(enquiryId)
                    .orElseThrow(() -> new RuntimeException("Enquiry not found"));

            // Check if user owns the enquiry
            if (!enquiry.getUserId().equals(user.getUserId())) {
                return ResponseEntity.status(403).body("Access denied to update this enquiry");
            }

            // Update fields
            enquiry.setProductName(enquiryRequest.getProductName());
            enquiry.setBuyRequirements(enquiryRequest.getBuyRequirements());
            enquiry.setQuantityValue(enquiryRequest.getQuantityValue());
            enquiry.setQuantityType(enquiryRequest.getQuantityType());
            enquiry.setFrequency(enquiryRequest.getFrequency());
            enquiry.setCustomerName(enquiryRequest.getCustomerName());
            enquiry.setEmail(enquiryRequest.getEmail());
            enquiry.setMobileNo(enquiryRequest.getMobileNo());
            enquiry.setCountry(enquiryRequest.getCountry());
            enquiry.setCity(enquiryRequest.getCity());
            enquiry.setCompanyName(enquiryRequest.getCompanyName());
            enquiry.setLastUpdated(LocalDateTime.now());

            Enquiry updatedEnquiry = enquiryRepository.save(enquiry);

            return ResponseEntity.ok(updatedEnquiry);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Helper method to validate status
    private boolean isValidStatus(String status) {
        return status.equals("Pending") ||
                status.equals("Approved") ||
                status.equals("Rejected") ||
                status.equals("Completed");
    }
}