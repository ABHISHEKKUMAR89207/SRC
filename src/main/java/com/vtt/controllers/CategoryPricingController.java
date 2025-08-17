package com.vtt.controllers;

import com.vtt.dtoforSrc.CategoryPricingDTO;
import com.vtt.entities.CategoryPricing;
import com.vtt.entities.DisplayNamesCat;
import com.vtt.entities.SRCRole;
import com.vtt.repository.CategoryPricingRepository;
import com.vtt.repository.DisplayNamesCatRepository;
import com.vtt.repository.SRCRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category-pricing")
public class CategoryPricingController {
    @Autowired
    private DisplayNamesCatRepository displayNamesCatRepository;

    @Autowired
    private CategoryPricingRepository categoryPricingRepository;

    @Autowired
    private SRCRoleRepository srcRoleRepository;

    // 1. GET all category-pricings
    @GetMapping
    public ResponseEntity<List<CategoryPricing>> getAll() {
        return ResponseEntity.ok(categoryPricingRepository.findAll());
    }

    // 2. POST create new category-pricing
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CategoryPricingDTO dto) {
        CategoryPricing categoryPricing = new CategoryPricing();
        categoryPricing.setCategory(dto.getCategory());
        categoryPricing.setSubCategory(dto.getSubCategory());
        categoryPricing.setRolePrices(mapRolePrices(dto.getRolePrices()));

        CategoryPricing saved = categoryPricingRepository.save(categoryPricing);
        return ResponseEntity.ok(saved);
    }

    // 3. PUT update category-pricing by ID
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody CategoryPricingDTO dto) {
        Optional<CategoryPricing> optional = categoryPricingRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        CategoryPricing categoryPricing = optional.get();
        categoryPricing.setCategory(dto.getCategory());
        categoryPricing.setSubCategory(dto.getSubCategory());
        categoryPricing.setRolePrices(mapRolePrices(dto.getRolePrices()));

        CategoryPricing updated = categoryPricingRepository.save(categoryPricing);
        return ResponseEntity.ok(updated);
    }

    // Helper to convert RolePriceDTO to RoleWithPrice entity list
    private List<CategoryPricing.RoleWithPrice> mapRolePrices(List<CategoryPricingDTO.RolePriceDTO> dtos) {
        return dtos.stream()
                .map(dto -> {
                    Optional<SRCRole> roleOpt = srcRoleRepository.findById(dto.getRoleId());
                    if (roleOpt.isEmpty()) {
                        throw new RuntimeException("Role not found with ID: " + dto.getRoleId());
                    }
                    return new CategoryPricing.RoleWithPrice(roleOpt.get(), dto.getPrice());
                })
                .collect(Collectors.toList());
    }
    @GetMapping("/search")
    public ResponseEntity<?> getOrCreateByCategoryAndSubCategory(
            @RequestParam String category,
            @RequestParam String subCategory) {

        List<CategoryPricing> results = categoryPricingRepository
                .findAllByCategoryAndSubCategory(category, subCategory);

        if (!results.isEmpty()) {
            return ResponseEntity.ok(results.get(0)); // Return first if found
        }

        // Create new CategoryPricing if none found
        CategoryPricing newPricing = new CategoryPricing();
        newPricing.setCategory(category);
        newPricing.setSubCategory(subCategory);
        newPricing.setRolePrices(new ArrayList<>()); // or set defaults if needed

        CategoryPricing saved = categoryPricingRepository.save(newPricing);
        return ResponseEntity.ok(saved); // Return the newly created one
    }


    @GetMapping("/price")
    public ResponseEntity<?> getPriceByDisplayIdAndRoleName(
            @RequestParam String displayId,
            @RequestParam String roleName) {

        // Step 1: Find DisplayNamesCat using displayId
        Optional<DisplayNamesCat> displayOpt = displayNamesCatRepository.findById(displayId);
        if (displayOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Display ID not found: " + displayId);
        }

        DisplayNamesCat display = displayOpt.get();
        String category = display.getCategoryName();
        String subCategory = display.getSubCategoryName();

        // Step 2: Find CategoryPricing using category and subCategory
        List<CategoryPricing> results = categoryPricingRepository.findAllByCategoryAndSubCategory(category, subCategory);
        if (results.isEmpty()) {
            return ResponseEntity.status(404).body("No pricing found for category: " + category + " and subCategory: " + subCategory);
        }

        // Step 3: Find SRCRole by name
        Optional<SRCRole> roleOpt = srcRoleRepository.findByName(roleName);
        if (roleOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Role not found: " + roleName);
        }
        SRCRole role = roleOpt.get();

        // Step 4: Find price from rolePrices
        Optional<CategoryPricing.RoleWithPrice> match = results.get(0).getRolePrices().stream()
                .filter(rp -> rp.getRole().getId().equals(role.getId()))
                .findFirst();

        if (match.isEmpty()) {
            return ResponseEntity.status(404).body("Price for role '" + roleName + "' not found in this category/subCategory.");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("displayId", displayId);
        response.put("category", category);
        response.put("subCategory", subCategory);
        response.put("roleName", roleName);
        response.put("price", match.get().getPrice());

        return ResponseEntity.ok(response);
    }

}
