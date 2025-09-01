package com.vtt.controllers;

import com.vtt.dtoforSrc.InventoryForApprovalDTO;
import com.vtt.entities.*;
import com.vtt.repository.DisplayNamesCatRepository;
import com.vtt.repository.FabricRepository;
import com.vtt.repository.InventoryForApprovalRepository;
import com.vtt.repository.UserRepository;
import com.vtt.repository.ApplySetRepository;   // ✅ added
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventory")
public class InventoryForApprovalController {

    @Autowired
    private InventoryForApprovalRepository inventoryRepo;

    @Autowired
    private DisplayNamesCatRepository displayNamesCatRepo;

    @Autowired
    private FabricRepository fabricRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ApplySetRepository applySetRepo;   // ✅ added

    // GET all
    @GetMapping
    public List<InventoryForApproval> getAll() {
        return inventoryRepo.findAll();
    }

    // GET by ID
    @GetMapping("/{id}")
    public ResponseEntity<InventoryForApproval> getById(@PathVariable String id) {
        return inventoryRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST
    @PostMapping
    public ResponseEntity<InventoryForApproval> create(@RequestBody InventoryForApprovalDTO dto) {
        InventoryForApproval inventory = mapDtoToEntity(dto);
        return ResponseEntity.ok(inventoryRepo.save(inventory));
    }

    // PUT (Update)
    @PutMapping("/{id}")
    public ResponseEntity<InventoryForApproval> update(@PathVariable String id, @RequestBody InventoryForApprovalDTO dto) {
        Optional<InventoryForApproval> existing = inventoryRepo.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        InventoryForApproval updated = mapDtoToEntity(dto);
        updated.setId(id);  // retain the original ID
        return ResponseEntity.ok(inventoryRepo.save(updated));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!inventoryRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        inventoryRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Helper method to map DTO to entity
    private InventoryForApproval mapDtoToEntity(InventoryForApprovalDTO dto) {
        DisplayNamesCat displayNamesCat = displayNamesCatRepo.findById(dto.getDisplayNamesCatId()).orElse(null);
        Fabric fabric = fabricRepo.findById(dto.getFabricId()).orElse(null);
        User user = userRepo.findByUserId(dto.getUserId());

        List<InventoryForApproval.SizeQuantity> sizeList = dto.getSizes().stream()
                .map(s -> new InventoryForApproval.SizeQuantity(s.getLabel(), s.getQuantity()))
                .collect(Collectors.toList());

        // ✅ Map ApplySet DTOs -> Entity references
        List<InventoryForApproval.ApplySetWithQuantity> applySetList = dto.getApplySets() != null
                ? dto.getApplySets().stream()
                .map(asqDto -> new InventoryForApproval.ApplySetWithQuantity(
                        applySetRepo.findById(asqDto.getApplySetId()).orElse(null),
                        asqDto.getTotalQuantity()
                ))
                .collect(Collectors.toList())
                : List.of();

        return new InventoryForApproval(
                null,
                dto.getColor(),
                sizeList,
                displayNamesCat,
                fabric,
                dto.isApproved(),
                user,
                applySetList   // ✅ added
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<InventoryForApproval>> getByUserId(@PathVariable String userId) {
        User user = userRepo.findByUserId(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<InventoryForApproval> inventories = inventoryRepo.findByUser(user);
        return ResponseEntity.ok(inventories);
    }

    @PatchMapping("/{id}/approval")
    public ResponseEntity<InventoryForApproval> updateApprovalStatus(@PathVariable String id, @RequestBody boolean approved) {
        Optional<InventoryForApproval> existing = inventoryRepo.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        InventoryForApproval inventory = existing.get();
        inventory.setApproved(approved);
        return ResponseEntity.ok(inventoryRepo.save(inventory));
    }
}
