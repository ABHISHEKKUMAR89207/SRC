package com.vtt.controller;

import com.vtt.commonfunc.TokenUtils;
import com.vtt.dtoforSrc.LabelGeneratedDTO;
import com.vtt.entities.*;
import com.vtt.otherclass.MainRole;
import com.vtt.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/label")
@RequiredArgsConstructor
public class LabelGeneratedController {

    private final LabelGeneratedRepository labelGeneratedRepository;
    private final FabricRepository fabricRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    @Autowired
    private  MaterNumberRepository materNumberRepo;
    @Autowired
    private SerialNoProductRepository serialNoProductRepository;
    @Autowired
    private DisplayNamesCatRepository displayNamesCatRepository;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private SRCRoleRepository srcRoleRepository;
    @Autowired
    private GroupedRoleRepository groupedRoleRepository;
    // Add these methods to your LabelGeneratedController class

    /**
     * Checks if all fabrics in the label have sufficient quantity available
     */
    private boolean checkFabricAvailability(LabelGenerated label) {
        if (label.getFabrics() == null || label.getFabrics().isEmpty()) {
            return true; // No fabrics to check
        }

        for (LabelGenerated.LabelFabric labelFabric : label.getFabrics()) {
            Fabric fabric = labelFabric.getFabric();
            if (fabric == null) {
                throw new IllegalArgumentException("Fabric reference is null");
            }

            // Refresh fabric data from DB to ensure we have latest quantity
            Fabric currentFabric = fabricRepository.findById(fabric.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Fabric not found with id: " + fabric.getId()));

            if (currentFabric.getQuantityinMeter() < labelFabric.getUsedQuantity()) {
                return false; // Insufficient quantity
            }
        }

        return true; // All fabrics have sufficient quantity
    }

    /**
     * Updates fabric quantities by subtracting used amounts
     */
    private void updateFabricQuantities(LabelGenerated label) {
        if (label.getFabrics() == null || label.getFabrics().isEmpty()) {
            return; // No fabrics to update
        }

        for (LabelGenerated.LabelFabric labelFabric : label.getFabrics()) {
            Fabric fabric = labelFabric.getFabric();
            if (fabric == null) {
                continue; // Skip if fabric reference is null
            }

            // Refresh fabric data from DB
            Fabric currentFabric = fabricRepository.findById(fabric.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Fabric not found with id: " + fabric.getId()));

            // Calculate new quantity
            double newQuantity = currentFabric.getQuantityinMeter() - labelFabric.getUsedQuantity();
            if (newQuantity < 0) {
                throw new IllegalStateException("Negative fabric quantity would result for fabric: " + currentFabric.getId());
            }

            // Update and save
            currentFabric.setQuantityinMeter(newQuantity);
            currentFabric.setUpdatedAt(Instant.now());
            fabricRepository.save(currentFabric);
        }
    }

    /**
     * Restores fabric quantities by adding back used amounts (for delete operations)
     */
    private void restoreFabricQuantities(LabelGenerated label) {
        if (label.getFabrics() == null || label.getFabrics().isEmpty()) {
            return; // No fabrics to restore
        }

        for (LabelGenerated.LabelFabric labelFabric : label.getFabrics()) {
            Fabric fabric = labelFabric.getFabric();
            if (fabric == null) {
                continue; // Skip if fabric reference is null
            }

            // Refresh fabric data from DB
            Fabric currentFabric = fabricRepository.findById(fabric.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Fabric not found with id: " + fabric.getId()));

            // Calculate new quantity by adding back
            double newQuantity = currentFabric.getQuantityinMeter() + labelFabric.getUsedQuantity();

            // Update and save
            currentFabric.setQuantityinMeter(newQuantity);
            currentFabric.setUpdatedAt(Instant.now());
            fabricRepository.save(currentFabric);
        }
    }
    // CREATE Label
    // Modified createLabel method
    @PostMapping("/create")
    public ResponseEntity<?> createLabel(
            @RequestHeader("Authorization") String tokenHeader,
            @RequestBody LabelGeneratedDTO labelDto) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN && requestingUser.getMainRole() != MainRole.WORKER) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN or WORKER can access this endpoint");
            }

            // Get the order reference
            Optional<Order> optionalOrder = orderRepository.findById(labelDto.getOrderReference());
            if (optionalOrder.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Order not found with id: " + labelDto.getOrderReference());
            }

            LabelGenerated label = mapDtoToEntity(labelDto);
            label.setOrderReference(optionalOrder.get());

            // Check fabric availability before proceeding
            if (!checkFabricAvailability(label)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Insufficient fabric quantity available");
            }

            // Generate Label Number
            String labelNumber = generateUniqueLabelNumber(labelDto.getMasterNumber());
            label.setLabelNumber(labelNumber);

            label.setCreatedAt(Instant.now());
            label.setUpdatedAt(Instant.now());

            // First save the label to get its ID
            LabelGenerated savedLabel = labelGeneratedRepository.save(label);

            // Then update fabric quantities
            updateFabricQuantities(savedLabel);

            // Update completed quantities in the order
            updateOrderCompletedQuantities(optionalOrder.get(), savedLabel);

            // Create serial number product
            createSerialNoProduct(savedLabel);

            return ResponseEntity.ok(savedLabel);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating label: " + e.getMessage());
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLabel(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable String id) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN && requestingUser.getMainRole() != MainRole.WORKER) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN or WORKER can access this endpoint");
            }

            Optional<LabelGenerated> optionalLabel = labelGeneratedRepository.findById(id);
            if (optionalLabel.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Label not found with id: " + id);
            }

            LabelGenerated label = optionalLabel.get();
            Order order = label.getOrderReference();

            // First restore fabric quantities
            restoreFabricQuantities(label);
            serialNoProductRepository.deleteByLabelGenerated(label);
            // Then delete the label
            labelGeneratedRepository.deleteById(id);

            // Finally, update the order's completed quantities by removing this label's quantities
            revertOrderCompletedQuantities(order, label);
// Delete the SerialNoProduct linked to this label

            return ResponseEntity.ok("Label deleted successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting label: " + e.getMessage());
        }
    }

    @GetMapping("/next-label-number")
    public ResponseEntity<?> getNextLabelNumber(
            @RequestHeader("Authorization") String tokenHeader) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN && requestingUser.getMainRole() != MainRole.WORKER) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN or WORKER can access this endpoint");
            }

            MaterNumber materNumber = materNumberRepo.findByUser(requestingUser);
            if (materNumber == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No master number associated with this user");
            }

            String masterNumber = String.valueOf(materNumber.getMaterNumber());
            String nextLabelNumber = generateUniqueLabelNumber(masterNumber);
            return ResponseEntity.ok(Collections.singletonMap("nextLabelNumber", nextLabelNumber));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating next label number: " + e.getMessage());
        }
    }
    @GetMapping("/getlable-by-date/{date}")
    public ResponseEntity<?> getLabelsByMasterDate(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable String date) {
        try {
            // Fetch the user from the token
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);

            // Check if the user has the required role
            if (requestingUser.getMainRole() != MainRole.ADMIN && requestingUser.getMainRole() != MainRole.WORKER) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN or WORKER can access this endpoint");
            }

            // Parse the input date
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy");
            java.time.LocalDate localDate = java.time.LocalDate.parse(date, formatter);

            // Convert to Instant range (start and end of the day)
            Instant startOfDay = localDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant();
            Instant endOfDay = localDate.plusDays(1).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant();

            // Find labels created on that date
            List<LabelGenerated> labels = labelGeneratedRepository.findByCreatedAtBetween(startOfDay, endOfDay);

            // If no labels found, return a not found response
            if (labels.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No labels found for the date: " + date);
            }

            // Return the labels found
            return ResponseEntity.ok(labels);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching labels: " + e.getMessage());
        }
    }


    /**
     * Reverts the completed quantities in the order by subtracting the quantities from the deleted label
     */
    private void revertOrderCompletedQuantities(Order order, LabelGenerated deletedLabel) {
        // Create a map to track the quantities from the deleted label
        Map<String, Integer> deletedQuantities = new HashMap<>();

        // Sum quantities from the deleted label's sizes
        if (deletedLabel.getSizes() != null) {
            for (LabelGenerated.SizeCompleted size : deletedLabel.getSizes()) {
                deletedQuantities.merge(size.getSizeName(), size.getQuantity(), Integer::sum);
            }
        }

        // Subtract only the sizes that exist in the deleted label
        for (Order.SizeQuantity orderSize : order.getSizes()) {
            if (deletedQuantities.containsKey(orderSize.getLabel())) {
                // Only update if this size exists in the deleted label
                int newQuantity = orderSize.getCompletedQuantity() - deletedQuantities.get(orderSize.getLabel());
                orderSize.setCompletedQuantity(Math.max(newQuantity, 0)); // Prevent negative quantities
            }
        }

        orderRepository.save(order);
    }
//    @PostMapping("/create")
//    public ResponseEntity<?> createLabel(
//            @RequestHeader("Authorization") String tokenHeader,
//            @RequestBody LabelGeneratedDTO labelDto) {
//        try {
//            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
//            if (requestingUser.getMainRole() != MainRole.ADMIN&& requestingUser.getMainRole() != MainRole.WORKER) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                        .body("Only ADMIN can access this endpoint");
//            }
//
//            // Get the order reference
//            Optional<Order> optionalOrder = orderRepository.findById(labelDto.getOrderReference());
//            if (optionalOrder.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                        .body("Order not found with id: " + labelDto.getOrderReference());
//            }
//
//            LabelGenerated label = mapDtoToEntity(labelDto);
//            label.setOrderReference(optionalOrder.get());
//
//            // Generate Label Number
//            String labelNumber = generateUniqueLabelNumber(labelDto.getMasterNumber());
//            label.setLabelNumber(labelNumber);
//
//            label.setCreatedAt(Instant.now());
//            label.setUpdatedAt(Instant.now());
//            LabelGenerated savedLabel = labelGeneratedRepository.save(label);
//
//            // Update completed quantities in the order
//            updateOrderCompletedQuantities(optionalOrder.get(), label);
//
//            // Separated method call
//            createSerialNoProduct(savedLabel);
//
//
//
//
//            return ResponseEntity.ok(savedLabel);
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error creating label: " + e.getMessage());
//        }
//    }

    private void createSerialNoProduct(LabelGenerated savedLabel) {
        Optional<DisplayNamesCat> defaultDisplayOpt = displayNamesCatRepository.findById(savedLabel.getDisplayId());
        if (defaultDisplayOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid Default DisplayNamesCat ID");
        }

        if (savedLabel.getFabrics() == null || savedLabel.getFabrics().isEmpty()) {
            throw new IllegalArgumentException("No fabric data found in LabelGenerated");
        }

        LabelGenerated.LabelFabric firstFabric = savedLabel.getFabrics().get(0);
        Fabric fabric = firstFabric.getFabric();

        if (fabric == null) {
            throw new IllegalArgumentException("Fabric reference is null in first LabelFabric");
        }

        SerialNoProduct serialNoProduct = new SerialNoProduct();
        serialNoProduct.setReferredLabelNumber(savedLabel.getLabelNumber());
        serialNoProduct.setDefaultDisplayNameCat(defaultDisplayOpt.get());
        serialNoProduct.setCommonArticle(fabric.getDisplayName());   // Assuming article = displayName
        serialNoProduct.setCommonMRP(fabric.getRetailPrice());
        serialNoProduct.setCommonFabricName(fabric.getFabricName());
        serialNoProduct.setCommonColor(firstFabric.getColor());      // Color comes from LabelFabric

        serialNoProduct.setLabelGenerated(savedLabel);
        serialNoProductRepository.save(serialNoProduct);
    }

    private void updateOrderCompletedQuantities(Order order, LabelGenerated label) {
        // Create a map to track the quantities from this specific label
        Map<String, Integer> labelQuantities = new HashMap<>();

        // Sum quantities from this label's sizes
        if (label.getSizes() != null) {
            for (LabelGenerated.SizeCompleted size : label.getSizes()) {
                labelQuantities.merge(size.getSizeName(), size.getQuantity(), Integer::sum);
            }
        }

        // Update only the sizes that exist in this label
        for (Order.SizeQuantity orderSize : order.getSizes()) {
            if (labelQuantities.containsKey(orderSize.getLabel())) {
                // Only update if this size exists in the label
                orderSize.setCompletedQuantity(
                        orderSize.getCompletedQuantity() + labelQuantities.get(orderSize.getLabel())
                );
            }
        }

        orderRepository.save(order);
    }
    // ... [keep all other existing methods] ...

    @GetMapping("/get-by-label-number/{labelNumber}")
    public ResponseEntity<?> getLabelByLabelNumber(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable String labelNumber) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
//            if (requestingUser.getMainRole() != MainRole.ADMIN && requestingUser.getMainRole() != MainRole.WORKER) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                        .body("Only ADMIN or WORKER can access this endpoint");
//            }

            Optional<LabelGenerated> optionalLabel = labelGeneratedRepository.findByLabelNumber(labelNumber);
            if (optionalLabel.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Label not found with labelNumber: " + labelNumber);
            }

            return ResponseEntity.ok(optionalLabel.get());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching label: " + e.getMessage());
        }
    }
    @PostMapping("/assign-work/{labelId}")
    public ResponseEntity<?> assignWorkToUser(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable String labelId,
            @RequestBody LabelGeneratedDTO.UserWorkAssignDTO assignDto) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);

            Optional<LabelGenerated> optionalLabel = labelGeneratedRepository.findById(labelId);
            if (optionalLabel.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Label not found with id: " + labelId);
            }

            User optionalUser = userRepository.findByUserId(assignDto.getUserId());
            if (optionalUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found with id: " + assignDto.getUserId());
            }

            LabelGenerated label = optionalLabel.get();
            User user = optionalUser;

            // Check if the same work is already assigned to the same user
            boolean existingAssignmentFound = false;

            if (label.getUsers() != null) {
                for (LabelGenerated.UserWorkAssign existingAssign : label.getUsers()) {
                    if (existingAssign.getUser().getUserId().equals(assignDto.getUserId()) &&
                            existingAssign.getWorkAssigned().equals(assignDto.getWorkAssigned())) {

                        // Update the status of existing assignment
                        existingAssign.setStatus(assignDto.isStatus());
                        existingAssignmentFound = true;
                        break;
                    }
                }
            }

            if (!existingAssignmentFound) {
                // Check if the same work is already assigned to someone else
//                boolean workAlreadyAssigned = label.getUsers() != null &&
//                        label.getUsers().stream()
//                                .anyMatch(ua -> ua.getWorkAssigned().equals(assignDto.getWorkAssigned()) &&
//                                        ua.isStatus());
//
//                if (workAlreadyAssigned) {
//                    return ResponseEntity.status(HttpStatus.CONFLICT)
//                            .body("This work is already assigned to someone else");
//                }


                Optional<SRCRole> optionalRole = srcRoleRepository.findByName(assignDto.getWorkAssigned());

                if (optionalRole.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Invalid role name provided");
                }


                SRCRole inputRole = optionalRole.get();
                Set<String> matchedRoleNames = new HashSet<>();
//                List<GroupedRole> allGroups = groupedRoleRepository.findAll();

                List<GroupedRole> allGroups = groupedRoleRepository.findByRole(inputRole);




                for (GroupedRole group : allGroups) {
                    if (group.getRole() != null && group.getRole().getId().equals(inputRole.getId())) {
                        matchedRoleNames.add(group.getRole().getName());
                        if (group.getDownlinkedRoles() != null) {
                            for (SRCRole downlinked : group.getDownlinkedRoles()) {
                                matchedRoleNames.add(downlinked.getName());
                            }
                        }
                    } else if (group.getDownlinkedRoles() != null) {
                        for (SRCRole downlinked : group.getDownlinkedRoles()) {
                            if (downlinked.getId().equals(inputRole.getId())) {
                                matchedRoleNames.add(downlinked.getName());
                                if (group.getRole() != null) {
                                    matchedRoleNames.add(group.getRole().getName());
                                }
                                matchedRoleNames.addAll(group.getDownlinkedRoles().stream()
                                        .map(SRCRole::getName)
                                        .collect(Collectors.toSet()));
                            }
                        }
                    }
                }

//                if (matchedRoleNames.isEmpty()) {
//                    return ResponseEntity.ok("No groups found with this role");
//                }
//                System.out.println("dfsgfdgfdhfdjfgj"+matchedRoleNames);
                if (matchedRoleNames.stream().anyMatch(role -> role.equalsIgnoreCase("PACKING"))) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("This label is completed");
                }

                for (String roleNameInGroup : matchedRoleNames) {
                    boolean workAlreadyAssignedingroup = label.getUsers() != null &&
                            label.getUsers().stream()
                                    .anyMatch(ua -> ua.getWorkAssigned().equals(roleNameInGroup) &&
                                            ua.isStatus());

                    if (workAlreadyAssignedingroup) {
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body("This work is already assigned to someone else");
                    }

                }

                if (matchedRoleNames.isEmpty()) {
                    boolean workAlreadyAssigned = label.getUsers() != null &&
                            label.getUsers().stream()
                                    .anyMatch(ua -> ua.getWorkAssigned().equals(assignDto.getWorkAssigned()) &&
                                            ua.isStatus());

                    if (workAlreadyAssigned) {
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body("This work is already assigned to someone else");
                    }

                }

                // Create new assignment if not found
                LabelGenerated.UserWorkAssign userAssign = new LabelGenerated.UserWorkAssign();
                userAssign.setUser(user);
                userAssign.setWorkAssigned(assignDto.getWorkAssigned());
                userAssign.setStatus(assignDto.isStatus());

                if (label.getUsers() == null) {
                    label.setUsers(new ArrayList<>());
                }
                label.getUsers().add(userAssign);
            }

            label.setUpdatedAt(Instant.now());
            LabelGenerated updatedLabel = labelGeneratedRepository.save(label);
            return ResponseEntity.ok(updatedLabel);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error assigning work: " + e.getMessage());
        }
    }

    // Helper method to generate unique label number
    private String generateUniqueLabelNumber(String masterNumber) {
        String datePart = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("ddMMyy"));
        int counter = 1;

        String labelNumber;
        do {
            String counterFormatted = String.format("%02d", counter); // Always 2 digits (01,02,03,...)
            labelNumber = datePart + masterNumber + counterFormatted;

            boolean exists = labelGeneratedRepository.existsByLabelNumber(labelNumber);
            if (!exists) {
                break;
            }
            counter++;
        } while (true);

        return labelNumber;
    }


    // UPDATE Label
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateLabel(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable String id,
            @RequestBody LabelGeneratedDTO labelDto) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN can access this endpoint");
            }

            Optional<LabelGenerated> optionalLabel = labelGeneratedRepository.findById(id);
            if (optionalLabel.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Label not found with id: " + id);
            }

            LabelGenerated label = mapDtoToEntity(labelDto);
            label.setId(id);
            label.setUpdatedAt(Instant.now());
            LabelGenerated updatedLabel = labelGeneratedRepository.save(label);

            return ResponseEntity.ok(updatedLabel);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating label: " + e.getMessage());
        }
    }

    // DELETE Label
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<?> deleteLabel(
//            @RequestHeader("Authorization") String tokenHeader,
//            @PathVariable String id) {
//        try {
//            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
//            if (requestingUser.getMainRole() != MainRole.ADMIN) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                        .body("Only ADMIN can access this endpoint");
//            }
//
//            Optional<LabelGenerated> optionalLabel = labelGeneratedRepository.findById(id);
//            if (optionalLabel.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body("Label not found with id: " + id);
//            }
//
//            labelGeneratedRepository.deleteById(id);
//            return ResponseEntity.ok("Label deleted successfully");
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error deleting label: " + e.getMessage());
//        }
//    }
    // GET All Labels
    @GetMapping("/all")
    public ResponseEntity<?> getAllLabels(
            @RequestHeader("Authorization") String tokenHeader) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN && requestingUser.getMainRole() != MainRole.WORKER) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN or WORKER can access this endpoint");
            }

            // Fetch all labels from the repository
            Iterable<LabelGenerated> labels = labelGeneratedRepository.findAll();

            return ResponseEntity.ok(labels);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching labels: " + e.getMessage());
        }
    }

    @GetMapping("/get-by-date/{date}")
    public ResponseEntity<?> getLabelsByDate(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable String date) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN && requestingUser.getMainRole() != MainRole.WORKER) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN or WORKER can access this endpoint");
            }

            // Parse input date
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy");
            java.time.LocalDate localDate = java.time.LocalDate.parse(date, formatter);

            // Convert to Instant range (start and end of day)
            Instant startOfDay = localDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant();
            Instant endOfDay = localDate.plusDays(1).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant();

            // Find labels created on that date
            List<LabelGenerated> labels = labelGeneratedRepository.findByCreatedAtBetween(startOfDay, endOfDay);

            return ResponseEntity.ok(labels);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching labels by date: " + e.getMessage());
        }
    }
    @GetMapping("/noauth/get-by-date/{date}")
    public ResponseEntity<?> getLabelsByDateNoAUth(

            @PathVariable String date) {
        try {
//            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
//            if (requestingUser.getMainRole() != MainRole.ADMIN && requestingUser.getMainRole() != MainRole.WORKER) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                        .body("Only ADMIN or WORKER can access this endpoint");
//            }

            // Parse input date
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy");
            java.time.LocalDate localDate = java.time.LocalDate.parse(date, formatter);

            // Convert to Instant range (start and end of day)
            Instant startOfDay = localDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant();
            Instant endOfDay = localDate.plusDays(1).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant();

            // Find labels created on that date
            List<LabelGenerated> labels = labelGeneratedRepository.findByCreatedAtBetween(startOfDay, endOfDay);

            return ResponseEntity.ok(labels);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching labels by date: " + e.getMessage());
        }
    }


    @GetMapping("/get-by-user/{userId}")
    public ResponseEntity<?> getLabelsByAssignedUser(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable String userId) {
        try {
            // Verify requesting user has proper permissions
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN &&
                    requestingUser.getMainRole() != MainRole.WORKER &&
                    !requestingUser.getUserId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You don't have permission to access this data");
            }

            // Verify the target user exists
            User targetUser = userRepository.findByUserId(userId);
            if (targetUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found with id: " + userId);
            }

            // Find all labels where this user has work assignments
            List<LabelGenerated> labels = labelGeneratedRepository.findByUsersUser(targetUser);

            if (labels.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No labels found with work assigned to user: " + userId);
            }

            return ResponseEntity.ok(labels);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching labels: " + e.getMessage());
        }
    }

    // Helper method: DTO -> Entity
    private LabelGenerated mapDtoToEntity(LabelGeneratedDTO dto) {
        LabelGenerated label = new LabelGenerated();
        label.setMasterNumber(dto.getMasterNumber());
        label.setClientUserId(dto.getClientUserId());
        label.setCategory(dto.getCategory());
        label.setSubCategory(dto.getSubCategory());
        label.setDisplayId(dto.getDisplayId());
        label.setDisplayName(dto.getDisplayName());
        label.setLayers(dto.getLayers());
        label.setStatus(dto.getStatus());
        label.setTotalQuantity(dto.getTotalQuantity());

        if (dto.getFabrics() != null) {
            label.setFabrics(dto.getFabrics().stream().map(fabricDto -> {
                LabelGenerated.LabelFabric fabric = new LabelGenerated.LabelFabric();
                fabric.setUsedQuantity(fabricDto.getUsedQuantity());
                fabric.setColor(fabricDto.getColor());
                Optional<Fabric> optionalFabric = fabricRepository.findById(fabricDto.getFabricId());
                optionalFabric.ifPresent(fabric::setFabric);
                return fabric;
            }).collect(Collectors.toList()));
        }

        if (dto.getSizes() != null) {
            label.setSizes(dto.getSizes().stream().map(sizeDto -> {
                LabelGenerated.SizeCompleted size = new LabelGenerated.SizeCompleted();
                size.setSizeName(sizeDto.getSizeName());
                size.setQuantity(sizeDto.getQuantity());
                return size;
            }).collect(Collectors.toList()));
        }



        return label;
    }
}