package com.vtt.controllers;

import com.vtt.entities.*;
import com.vtt.repository.CategoryPricingRepository;
import com.vtt.repository.LabelGeneratedRepository;
import com.vtt.repository.UserRepository;
import com.vtt.repository.WorkerKhataBookRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Objects;
@RestController
@RequestMapping("/api/user-work")
public class UserWorkController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkerKhataBookRepository workerKhataBookRepository;


    @Autowired
    private LabelGeneratedRepository labelGeneratedRepository;

    @Autowired
    private CategoryPricingRepository categoryPricingRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @GetMapping("/calculate-work/{userId}")
    public ResponseEntity<?> calculateUserWork(
            @PathVariable String userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        try {
            // 1. Parse dates
            LocalDate parsedStartDate = LocalDate.parse(startDate, DATE_FORMATTER);
            LocalDate parsedEndDate = LocalDate.parse(endDate, DATE_FORMATTER);

            // Convert to Instant
            Instant startInstant = parsedStartDate.atStartOfDay(ZoneId.of("UTC")).toInstant();
            Instant endInstant = parsedEndDate.plusDays(1).atStartOfDay(ZoneId.of("UTC")).toInstant();

            // 2. Validate user
            User user = userRepository.findByUserId(userId);
            if (user == null) return ResponseEntity.badRequest().body("User not found: " + userId);

            String userRole = user.getSubRole();
            if (userRole == null || userRole.isEmpty())
                return ResponseEntity.badRequest().body("User does not have a role");

            // 3. Fetch all labels where user assignment exists
            List<LabelGenerated> allLabels = labelGeneratedRepository.findAll()
                    .stream()
                    .filter(label -> label.getUsers() != null &&
                            label.getUsers().stream().anyMatch(u -> u.getUser() != null &&
                                    u.getUser().getUserId().equals(userId)))
                    .toList();

            // 4. Filter by USER assignment createdAt (NOT label createdAt)
            List<LabelGenerated> filteredLabels = allLabels.stream()
                    .filter(label -> label.getUsers().stream()
                            .anyMatch(assign ->
                                    assign.getUser().getUserId().equals(userId) &&
                                            assign.getCreatedAt() != null &&
                                            assign.getCreatedAt().isAfter(startInstant) &&
                                            assign.getCreatedAt().isBefore(endInstant)))
                    .toList();

            // 5. Prepare work summary
            List<UserWorkSummary> workSummaries = new ArrayList<>();

            for (LabelGenerated label : filteredLabels) {

                // Total size quantity
                int totalQuantityForLabel = label.getSizes().stream()
                        .mapToInt(LabelGenerated.SizeCompleted::getQuantity)
                        .sum();

                // Pricing
                double pricePerUnit = categoryPricingRepository
                        .findByCategoryAndSubCategory(label.getCategory(), label.getSubCategory())
                        .map(cat -> cat.getRolePrices().stream()
                                .filter(rp -> rp.getRole().getName().equals(userRole))
                                .mapToDouble(CategoryPricing.RoleWithPrice::getPrice)
                                .findFirst().orElse(0.0))
                        .orElse(0.0);

                // Paid status
                boolean isUserPaid = label.getUsers().stream()
                        .anyMatch(u -> u.getUser().getUserId().equals(userId) && u.isPaid());

                // Work status
                boolean userStatus = label.getUsers().stream()
                        .anyMatch(u -> u.getUser().getUserId().equals(userId) && u.isStatus());

                // Assignment date (formatted)
                String dateOfAssigned = label.getUsers().stream()
                        .filter(u -> u.getUser().getUserId().equals(userId))
                        .map(LabelGenerated.UserWorkAssign::getCreatedAt)
                        .filter(Objects::nonNull)
                        .findFirst()
                        .map(instant -> DATE_FORMATTER.format(
                                instant.atZone(ZoneId.systemDefault()).toLocalDate()))
                        .orElse("");

                // Fabric display
                String fabricDisplay = "";
                if (label.getFabrics() != null && !label.getFabrics().isEmpty() &&
                        label.getFabrics().get(0).getFabric() != null) {
                    fabricDisplay = label.getFabrics().get(0).getFabric().getDisplayName();
                }

                // Total amount
                double totalAmount = totalQuantityForLabel * pricePerUnit;

                // Build summary object
                UserWorkSummary summary = new UserWorkSummary();
                summary.setLabelNumber(label.getLabelNumber());
                summary.setCategory(label.getCategory());
                summary.setSubCategory(label.getSubCategory());
                summary.setSizes(label.getSizes());
                summary.setTotalQuantity(totalQuantityForLabel);
                summary.setPricePerUnit(pricePerUnit);
                summary.setTotalAmount(totalAmount);
                summary.setFabricDisplay(fabricDisplay);
                summary.setPaid(isUserPaid);
                summary.setStatus(userStatus);
                summary.setDate(dateOfAssigned);

                workSummaries.add(summary);
            }

            // 6. Total
            int overallQuantity = workSummaries.stream().mapToInt(UserWorkSummary::getTotalQuantity).sum();
            double overallAmount = workSummaries.stream().mapToDouble(UserWorkSummary::getTotalAmount).sum();

            // 7. Response
            UserWorkResponse response = new UserWorkResponse();
            response.setUserId(userId);
            response.setUserName(user.getUserName());
            response.setUserRole(userRole);
            response.setStartDate(startDate);
            response.setEndDate(endDate);
            response.setWorkSummaries(workSummaries);
            response.setOverallQuantity(overallQuantity);
            response.setOverallAmount(overallAmount);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

//    @GetMapping("/calculate-work/{userId}")
//    public ResponseEntity<?> calculateUserWork(
//            @PathVariable String userId,
//            @RequestParam String startDate,
//            @RequestParam String endDate) {
//
//        try {
//            // 1. Parse dates from dd-MM-yyyy format
//            LocalDate parsedStartDate = LocalDate.parse(startDate, DATE_FORMATTER);
//            LocalDate parsedEndDate = LocalDate.parse(endDate, DATE_FORMATTER);
//
//            // 2. Find the user and validate
//            User user = userRepository.findByUserId(userId);
//            if (user == null) {
//                return ResponseEntity.badRequest().body("User not found with ID: " + userId);
//            }
//
//            String userRole = user.getSubRole();
//            if (userRole == null || userRole.isEmpty()) {
//                return ResponseEntity.badRequest().body("User does not have a valid role assigned");
//            }
//
//            // 3. Convert LocalDate to Instant for query (using UTC timezone)
//            Instant startInstant = parsedStartDate.atStartOfDay(ZoneId.of("UTC")).toInstant();
//            Instant endInstant = parsedEndDate.plusDays(1).atStartOfDay(ZoneId.of("UTC")).toInstant();
//
//            // Debug output
//            System.out.println("Query parameters:");
//            System.out.println("User ID: " + userId);
//            System.out.println("Start Date: " + startDate + " -> " + startInstant);
//            System.out.println("End Date: " + endDate + " -> " + endInstant);
//
//            // 4. Find all labels where this user is assigned within the given date range
//            // First try with direct user reference
//            List<LabelGenerated> labels = labelGeneratedRepository.findByUsersUserAndCreatedAtBetween(
//                    user, startInstant, endInstant);
//
//            // If no results, try alternative approach with user ID
//            if (labels.isEmpty()) {
//                System.out.println("No results with direct user reference, trying alternative approach");
//                labels = labelGeneratedRepository.findByCreatedAtBetween(startInstant, endInstant).stream()
//                        .filter(label -> label.getUsers().stream()
//                                .anyMatch(u -> u.getUser().equals(userId)))
//                        .toList();
//            }
//
//            System.out.println("Found labels count: " + labels.size());
//            labels.forEach(label -> System.out.println(
//                    "Label: " + label.getLabelNumber() +
//                            ", Created: " + label.getCreatedAt() +
//                            ", Users: " + label.getUsers().stream()
//                            .map(u -> u.getUser())
//                            .toList()));
//
//            // 5. Process each label to calculate quantities and pricing
//            List<UserWorkSummary> workSummaries = new ArrayList<>();
//
//            for (LabelGenerated label : labels) {
//                // Find all size quantities for this label
//                int totalQuantityForLabel = label.getSizes().stream()
//                        .mapToInt(LabelGenerated.SizeCompleted::getQuantity)
//                        .sum();
//
//                // Find pricing information based on category and user's role
//                double pricePerUnit = 0.0;
//                Optional<CategoryPricing> pricingOptional = categoryPricingRepository
//                        .findByCategoryAndSubCategory(label.getCategory(), label.getSubCategory());
//
//                if (pricingOptional.isPresent()) {
//                    pricePerUnit = pricingOptional.get().getRolePrices().stream()
//                            .filter(rp -> rp.getRole().getName().equals(userRole))
//                            .mapToDouble(CategoryPricing.RoleWithPrice::getPrice)
//                            .findFirst()
//                            .orElse(0.0);
//                }
//                boolean isUserPaid = label.getUsers().stream()
//                        .anyMatch(assign -> assign.getUser() != null &&
//                                assign.getUser().getUserId().equals(userId) &&
//                                assign.isPaid());
//                boolean userStatus = label.getUsers().stream()
//                        .anyMatch(assign -> assign.getUser() != null &&
//                                assign.getUser().getUserId().equals(userId) &&
//                                assign.isStatus());
//
//
//                String dateOfAssigned = label.getUsers().stream()
//                        .filter(assign -> assign.getUser() != null &&
//                                assign.getUser().getUserId().equals(userId))
//                        .map(LabelGenerated.UserWorkAssign::getCreatedAt)
//                        .filter(Objects::nonNull) // ✅ prevents NPE
//                        .findFirst()
//                        .map(instant -> {
//                            LocalDate date = instant.atZone(ZoneId.systemDefault()).toLocalDate();
//                            return DATE_FORMATTER.format(date);
//                        })
//                        .orElse("");
//
//
//
//                String fabricDisplay = "";
//                if (label.getFabrics() != null && !label.getFabrics().isEmpty() &&
//                        label.getFabrics().get(0).getFabric() != null) {
//                    fabricDisplay = label.getFabrics().get(0).getFabric().getDisplayName();
//                }
//                // Calculate total amount for this label
//                double totalAmount = totalQuantityForLabel * pricePerUnit;
//
//                // Create summary for this label
//                UserWorkSummary summary = new UserWorkSummary();
//                summary.setLabelNumber(label.getLabelNumber());
//                summary.setCategory(label.getCategory());
//                summary.setPaid(isUserPaid);
//                summary.setStatus(userStatus);
//                summary.setSubCategory(label.getSubCategory());
//                summary.setSizes(label.getSizes());
//                summary.setTotalQuantity(totalQuantityForLabel);
//                summary.setPricePerUnit(pricePerUnit);
//                summary.setTotalAmount(totalAmount);
//                summary.setFabricDisplay(fabricDisplay);  // Set the fabric display name
//                summary.setDate(dateOfAssigned);
//
//                workSummaries.add(summary);
//            }
//
//            // 6. Calculate overall totals
//            int overallQuantity = workSummaries.stream()
//                    .mapToInt(UserWorkSummary::getTotalQuantity)
//                    .sum();
//
//            double overallAmount = workSummaries.stream()
//                    .mapToDouble(UserWorkSummary::getTotalAmount)
//                    .sum();
//
//            UserWorkResponse response = new UserWorkResponse();
//            response.setUserId(userId);
//            response.setUserName(user.getUserName());
//            response.setUserRole(userRole);
//            response.setStartDate(startDate); // Return in same format as input
//            response.setEndDate(endDate); // Return in same format as input
//            response.setWorkSummaries(workSummaries);
//            response.setOverallQuantity(overallQuantity);
//            response.setOverallAmount(overallAmount);
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.badRequest().body("Error processing request: " + e.getMessage());
//        }
//    }

    @GetMapping("/get-by-date/")
    public ResponseEntity<?> getUserLabelsByDateRange(
            @RequestParam String userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        try {
            // 1. Parse dates from dd-MM-yyyy format
            LocalDate parsedStartDate = LocalDate.parse(startDate, DATE_FORMATTER);
            LocalDate parsedEndDate = LocalDate.parse(endDate, DATE_FORMATTER);

            // 2. Find the user and validate
            User user = userRepository.findByUserId(userId);
            if (user == null) {
                return ResponseEntity.badRequest().body("User not found with ID: " + userId);
            }

            String userRole = user.getSubRole();
            if (userRole == null || userRole.isEmpty()) {
                return ResponseEntity.badRequest().body("User does not have a valid role assigned");
            }

            // 3. Convert LocalDate to Instant for query (using UTC timezone)
            Instant startInstant = parsedStartDate.atStartOfDay(ZoneId.of("UTC")).toInstant();
            Instant endInstant = parsedEndDate.plusDays(1).atStartOfDay(ZoneId.of("UTC")).toInstant();

            // Debug output
            System.out.println("Query parameters:");
            System.out.println("User ID: " + userId);
            System.out.println("Start Date: " + startDate + " -> " + startInstant);
            System.out.println("End Date: " + endDate + " -> " + endInstant);

            // 4. Find all labels where this user is assigned within the given date range
            // First try with direct user reference
            List<LabelGenerated> labels = labelGeneratedRepository.findByUsersUserAndCreatedAtBetween(
                    user, startInstant, endInstant);


            return ResponseEntity.ok(labels);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }



//    @PostMapping("/update-paid-status/{userId}")
//    public ResponseEntity<?> updatePaidStatusForUser(
//            @PathVariable String userId,
//            @RequestBody List<LabelPaymentStatus> labelStatusList) {
//
//        try {
//            User user = userRepository.findByUserId(userId);
//            if (user == null) {
//                return ResponseEntity.badRequest().body("User not found for ID: " + userId);
//            }
//
//            for (LabelPaymentStatus status : labelStatusList) {
//                Optional<LabelGenerated> labelOpt = labelGeneratedRepository.findByLabelNumber(status.getLabelNumber());
//
//                if (labelOpt.isPresent()) {
//                    LabelGenerated label = labelOpt.get();
//
//                    boolean updated = false;
//                    for (LabelGenerated.UserWorkAssign assign : label.getUsers()) {
//                        if (assign.getUser() != null && assign.getUser().getUserId().equals(userId)) {
//                            assign.setPaid(status.isPaid());
//                            updated = true;
//                        }
//                    }
//
//                    if (updated) {
//                        label.setUpdatedAt(Instant.now());
//                        labelGeneratedRepository.save(label);
//                    }
//                }
//            }
//
//            return ResponseEntity.ok("Paid statuses updated successfully for user: " + userId);
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
//        }
//    }

    // Response DTOs
    @Data
    public static class UserWorkSummary {
        private String labelNumber;
        private String category;
        private String subCategory;
        private String fabricDisplay;
        private List<LabelGenerated.SizeCompleted> sizes;
        private int totalQuantity;
        private double pricePerUnit;
        private boolean paid;
        private boolean status;
        private double totalAmount;
        private String date;
    }

    @Data
    public static class UserWorkResponse {
        private String userId;
        private String userName;
        private String userRole;
        private String startDate;
        private String endDate;
        private List<UserWorkSummary> workSummaries;
        private int overallQuantity;
        private double overallAmount;
    }

//    @Data
//    public static class LabelPaymentStatus {
//        private String labelNumber;
//        private boolean paid;
//    }


}