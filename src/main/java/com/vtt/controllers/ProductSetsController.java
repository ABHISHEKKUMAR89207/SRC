package com.vtt.controllers;

import com.vtt.dtos.ProductSetsDTO;
import com.vtt.entities.ApplySet;
import com.vtt.entities.DisplayNamesCat;
import com.vtt.entities.Fabric;
import com.vtt.entities.ProductSets;
import com.vtt.repository.ApplySetRepository;
import com.vtt.repository.DisplayNamesCatRepository;
import com.vtt.repository.FabricRepository;
import com.vtt.repository.ProductSetsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/product-sets")
public class ProductSetsController {

    @Autowired
    private ProductSetsRepository productSetsRepo;

    @Autowired
    private FabricRepository fabricRepo;

    @Autowired
    private DisplayNamesCatRepository displayNamesCatRepo;

    @Autowired
    private ApplySetRepository applySetRepo;

    @PostMapping("/bulk")
    public ResponseEntity<?> createMultipleProductSets(@RequestBody List<ProductSetsDTO> dtos) {
        return ResponseEntity.ok(
                dtos.stream().map(dto -> {
                    Fabric fabric = fabricRepo.findById(dto.getFabricId())
                            .orElseThrow(() -> new RuntimeException("Fabric not found"));
                    DisplayNamesCat displayNamesCat = displayNamesCatRepo.findById(dto.getDisplayNamesCatId())
                            .orElseThrow(() -> new RuntimeException("DisplayNamesCat not found"));
                    ApplySet applySet = applySetRepo.findById(dto.getApplySet())
                            .orElseThrow(() -> new RuntimeException("ApplySet not found"));

                    // Check if product set already exists with same applySet id
                    ProductSets productSet = (ProductSets) productSetsRepo.findByApplySet(dto.getApplySet()).orElse(null);

                    if (productSet != null) {
                        // ✅ Update existing
                        productSet.setTotalQuantity(productSet.getTotalQuantity() + dto.getTotalQuantity());
                    } else {
                        // ✅ Create new
                        productSet = new ProductSets();
                        productSet.setColor(dto.getColor());
                        productSet.setTotalQuantity(dto.getTotalQuantity());
                        productSet.setSetName(dto.getSetName());
                        productSet.setApplySet(dto.getApplySet()); // ApplySet id
                        productSet.setSizes(applySet.getSizes().stream()
                                .map(s -> new ProductSets.SizeQuantity(s.getLabel(), s.getQuantity()))
                                .collect(Collectors.toList()));
                        productSet.setFabric(fabric);
                        productSet.setDisplayNamesCat(displayNamesCat);
                    }

                    return productSetsRepo.save(productSet);
                }).collect(Collectors.toList())
        );
    }

    @PostMapping
    public ResponseEntity<ProductSets> createProductSet(@RequestBody ProductSetsDTO dto) {
        Fabric fabric = fabricRepo.findById(dto.getFabricId())
                .orElseThrow(() -> new RuntimeException("Fabric not found"));
        DisplayNamesCat displayNamesCat = displayNamesCatRepo.findById(dto.getDisplayNamesCatId())
                .orElseThrow(() -> new RuntimeException("DisplayNamesCat not found"));
        ApplySet applySet = applySetRepo.findById(dto.getApplySet())
                .orElseThrow(() -> new RuntimeException("ApplySet not found"));

        // Check if product set already exists with same applySet id
        ProductSets productSet = (ProductSets) productSetsRepo.findByApplySet(dto.getApplySet()).orElse(null);

        if (productSet != null) {
            // ✅ Update existing
            productSet.setTotalQuantity(productSet.getTotalQuantity() + dto.getTotalQuantity());


        } else {
            // ✅ Create new
            productSet = new ProductSets();
            productSet.setColor(dto.getColor());
            productSet.setTotalQuantity(dto.getTotalQuantity());
            productSet.setSetName(dto.getSetName());
            productSet.setApplySet(dto.getApplySet()); // ApplySet id
            productSet.setSizes(applySet.getSizes().stream()
                    .map(s -> new ProductSets.SizeQuantity(s.getLabel(), s.getQuantity()))
                    .collect(Collectors.toList()));
            productSet.setFabric(fabric);
            productSet.setDisplayNamesCat(displayNamesCat);
        }

        return ResponseEntity.ok(productSetsRepo.save(productSet));
    }

    @GetMapping
    public ResponseEntity<?> getAllProductSets() {
        return ResponseEntity.ok(productSetsRepo.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductSetById(@PathVariable String id) {
        return ResponseEntity.of(productSetsRepo.findById(id));
    }
    // ===================== 1️⃣ Update totalQuantity by ID =====================
    @PutMapping("/{id}/quantity")
    public ResponseEntity<?> updateTotalQuantity(@PathVariable String id, @RequestParam int totalQuantity) {
        ProductSets productSet = productSetsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductSet not found with id: " + id));

        productSet.setTotalQuantity(totalQuantity); // set new quantity
        return ResponseEntity.ok(productSetsRepo.save(productSet));
    }
    @GetMapping("/filter")
    public ResponseEntity<?> getProductSetsByFabricDisplayColor(
            @RequestParam String fabricId,
            @RequestParam String displayNamesCatId,
            @RequestParam String color) {

        Fabric fabric = fabricRepo.findById(fabricId)
                .orElseThrow(() -> new RuntimeException("Fabric not found"));

        DisplayNamesCat displayNamesCat = displayNamesCatRepo.findById(displayNamesCatId)
                .orElseThrow(() -> new RuntimeException("DisplayNamesCat not found"));

        // ✅ Use repository function
        List<ProductSets> filteredSets = productSetsRepo.findByFabricAndDisplayNamesCatAndColor(fabric, displayNamesCat, color);

        return ResponseEntity.ok(filteredSets);
    }

}
