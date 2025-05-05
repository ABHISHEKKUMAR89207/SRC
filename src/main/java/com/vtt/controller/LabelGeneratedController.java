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
    private SerialNoProductRepository serialNoProductRepository;
    @Autowired
    private DisplayNamesCatRepository displayNamesCatRepository;
    @Autowired
    private TokenUtils tokenUtils;

    // CREATE Label
    @PostMapping("/create")
    public ResponseEntity<?> createLabel(
            @RequestHeader("Authorization") String tokenHeader,
            @RequestBody LabelGeneratedDTO labelDto) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN can access this endpoint");
            }

            // Get the order reference
            Optional<Order> optionalOrder = orderRepository.findById(labelDto.getOrderReference());
            if (optionalOrder.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Order not found with id: " + labelDto.getOrderReference());
            }

            LabelGenerated label = mapDtoToEntity(labelDto);
            label.setOrderReference(optionalOrder.get());

            // Generate Label Number
            String labelNumber = generateUniqueLabelNumber(labelDto.getMasterNumber());
            label.setLabelNumber(labelNumber);

            label.setCreatedAt(Instant.now());
            label.setUpdatedAt(Instant.now());
            LabelGenerated savedLabel = labelGeneratedRepository.save(label);

            // Update completed quantities in the order
            updateOrderCompletedQuantities(optionalOrder.get(), label);

            // Separated method call
            createSerialNoProduct(savedLabel);




            return ResponseEntity.ok(savedLabel);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating label: " + e.getMessage());
        }
    }

    private void createSerialNoProduct(LabelGenerated savedLabel) {
        Optional<DisplayNamesCat> defaultDisplayOpt = displayNamesCatRepository.findById(savedLabel.getDisplayId());
        if (defaultDisplayOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid Default DisplayNamesCat ID");
        }

        SerialNoProduct serialNoProduct = new SerialNoProduct();
        serialNoProduct.setReferredLabelNumber(savedLabel.getLabelNumber());
        serialNoProduct.setDefaultDisplayNameCat(defaultDisplayOpt.get());
        serialNoProduct.setLabelGenerated(savedLabel);
        serialNoProductRepository.save(serialNoProduct);
    }

    private void updateOrderCompletedQuantities(Order order, LabelGenerated label) {
        // Get all labels for this order
        List<LabelGenerated> orderLabels = labelGeneratedRepository.findByOrderReference(order);

        // Create a map to track completed quantities by size
        Map<String, Integer> completedQuantities = new HashMap<>();

        // Initialize with existing sizes from order
        for (Order.SizeQuantity size : order.getSizes()) {
            completedQuantities.put(size.getLabel(), size.getCompletedQuantity());
        }

        // Sum up quantities from all labels
        for (LabelGenerated orderLabel : orderLabels) {
            for (LabelGenerated.SizeCompleted size : orderLabel.getSizes()) {
                completedQuantities.merge(size.getSizeName(), size.getQuantity(), Integer::sum);
            }
        }

        // Update the order's completed quantities
        for (Order.SizeQuantity size : order.getSizes()) {
            size.setCompletedQuantity(completedQuantities.getOrDefault(size.getLabel(), 0));
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
//            if (requestingUser.getMainRole() != MainRole.ADMIN) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                        .body("Only ADMIN can access this endpoint");
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
                boolean workAlreadyAssigned = label.getUsers() != null &&
                        label.getUsers().stream()
                                .anyMatch(ua -> ua.getWorkAssigned().equals(assignDto.getWorkAssigned()) &&
                                        ua.isStatus());

                if (workAlreadyAssigned) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("This work is already assigned to someone else");
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
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLabel(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable String id) {
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

            labelGeneratedRepository.deleteById(id);
            return ResponseEntity.ok("Label deleted successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting label: " + e.getMessage());
        }
    }
    // GET All Labels
    @GetMapping("/all")
    public ResponseEntity<?> getAllLabels(
            @RequestHeader("Authorization") String tokenHeader) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN can access this endpoint");
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
            if (requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN can access this endpoint");
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

    // Helper method: DTO -> Entity
    private LabelGenerated mapDtoToEntity(LabelGeneratedDTO dto) {
        LabelGenerated label = new LabelGenerated();
        label.setMasterNumber(dto.getMasterNumber());
        label.setClientUserId(dto.getClientUserId());
        label.setCategory(dto.getCategory());
        label.setSubCategory(dto.getSubCategory());
        label.setDisplayId(dto.getDisplayId());
        label.setDisplayName(dto.getDisplayName());
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