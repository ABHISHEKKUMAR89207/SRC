package com.vtt.controllers;

import com.vtt.dtoforSrc.InventoryForApprovalDTO;
import com.vtt.entities.*;
import com.vtt.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private LabelGeneratedRepository labelGeneratedRepo;
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
                dto.getLabelNumber(),
                dto.getColor(),
                sizeList,
                displayNamesCat,
                fabric,
                dto.isApproved(),
                user,
                applySetList   // ✅ added
                , null
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
    @PatchMapping("/{id}/set-pre-sale-client/{userId}")
    public ResponseEntity<InventoryForApproval> setPreSaleClient(
            @PathVariable String id,
            @PathVariable String userId) {

        Optional<InventoryForApproval> inventoryOpt = inventoryRepo.findById(id);
        if (inventoryOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        InventoryForApproval inventory = inventoryOpt.get();
        User preSaleClient = userRepo.findByUserId(userId);
        if (preSaleClient == null) {
            return ResponseEntity.badRequest().body(null);
        }

        inventory.setPreSaleClient(preSaleClient);
        InventoryForApproval updated = inventoryRepo.save(inventory);
        return ResponseEntity.ok(updated);
    }
    @GetMapping("/presale-client/{userId}")
    public ResponseEntity<List<InventoryForApproval>> getByPreSaleClient(@PathVariable String userId) {
        User preSaleClient = userRepo.findByUserId(userId);
        if (preSaleClient == null) {
            return ResponseEntity.notFound().build();
        }

        List<InventoryForApproval> inventories = inventoryRepo.findByPreSaleClient(preSaleClient);
        return ResponseEntity.ok(inventories);
    }
    @GetMapping("/presale-client/assigned")
    public ResponseEntity<List<InventoryForApproval>> getAllWithPreSaleClient() {
        List<InventoryForApproval> inventories = inventoryRepo.findByPreSaleClientNotNull();
        return ResponseEntity.ok(inventories);
    }
    @GetMapping("/label-mapping-check")
    public ResponseEntity<List<Map<String, Object>>> checkLabelMappings() {
        List<LabelGenerated> allLabels = labelGeneratedRepo.findAll();
        List<InventoryForApproval> allInventories = inventoryRepo.findAll();

        List<Map<String, Object>> mappingResults = allLabels.stream()
                .map(label -> {
                    // Find inventories with matching label number
                    List<InventoryForApproval> matchingInventories = allInventories.stream()
                            .filter(inv -> label.getLabelNumber() != null &&
                                    label.getLabelNumber().equals(inv.getLabelNumber()))
                            .collect(Collectors.toList());

                    String mappingStatus = matchingInventories.isEmpty() ? "No" : "Yes";
                    int mappingCount = matchingInventories.size();

                    // Get inventory IDs for reference
                    List<String> inventoryIds = matchingInventories.stream()
                            .map(InventoryForApproval::getId)
                            .collect(Collectors.toList());

                    // Build a response map instead of DTO
                    Map<String, Object> result = new LinkedHashMap<>();
                    result.put("labelNumber", label.getLabelNumber());
                    result.put("mappingStatus", mappingStatus);
                    result.put("mappingCount", mappingCount);
                    result.put("inventoryIds", inventoryIds);
                    result.put("labelId", label.getId());

                    return result;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(mappingResults);
    }
    @GetMapping("/by-label/{labelNumber}")
    public ResponseEntity<List<InventoryForApproval>> getByLabelNumber(@PathVariable String labelNumber) {
        // Find inventories that have the given label number
        List<InventoryForApproval> inventories = inventoryRepo.findAll().stream()
                .filter(inv -> inv.getLabelNumber() != null &&
                        inv.getLabelNumber().equalsIgnoreCase(labelNumber.trim()))
                .collect(Collectors.toList());

        if (inventories.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(inventories);
    }

}
