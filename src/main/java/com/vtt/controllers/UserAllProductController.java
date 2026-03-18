package com.vtt.controllers;




import com.vtt.dtos.UserAllProductDTO;
import com.vtt.entities.DisplayNamesCat;
import com.vtt.entities.Fabric;
import com.vtt.entities.ProductInventory;
import com.vtt.entities.ProductSets;
import com.vtt.repository.DisplayNamesCatRepository;
import com.vtt.repository.FabricRepository;
import com.vtt.repository.ProductInventoryRepository;
import com.vtt.repository.ProductSetsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user-all-products")
public class UserAllProductController {

    @Autowired
    private ProductInventoryRepository productInventoryRepo;

    @Autowired
    private ProductSetsRepository productSetsRepo;

    @Autowired
    private DisplayNamesCatRepository displayNamesCatRepo;

    @Autowired
    private FabricRepository fabricRepo;

    @GetMapping
    public ResponseEntity<List<UserAllProductDTO>> getAllProducts() {
        List<ProductInventory> inventories = productInventoryRepo.findAll();

        List<UserAllProductDTO> dtos = inventories.stream().map(inventory -> {

            // Fetch related entities
            DisplayNamesCat display = inventory.getDisplayNamesCat() != null ?
                    displayNamesCatRepo.findById(inventory.getDisplayNamesCat().getId()).orElse(null) : null;

            Fabric fabric = inventory.getFabric() != null ?
                    fabricRepo.findById(inventory.getFabric().getId()).orElse(null) : null;

            // Find matching sets by color, fabric.id, and displayNamesCat.id
            List<ProductSets> matchingSets = productSetsRepo.findByColorAndFabricIdAndDisplayNamesCatId(
                    inventory.getColor(),
                    fabric != null ? fabric.getId() : null,
                    display != null ? display.getId() : null
            );

            List<UserAllProductDTO.ProductSetDTO> setDTOs = matchingSets.stream().map(set -> {
                // Map sizes from ProductSets.SizeQuantity to ProductInventory.SizeQuantity
                List<ProductInventory.SizeQuantity> sizes = set.getSizes().stream()
                        .map(s -> new ProductInventory.SizeQuantity(s.getLabel(), s.getQuantity(),fabric.getRetailPrice()))
                        .collect(Collectors.toList());

                return new UserAllProductDTO.ProductSetDTO(
                        set.getId(),
                        set.getColor(),
                        set.getTotalQuantity(),
                        set.getSetName(),
                        set.getApplySet(),
                        sizes
                );
            }).collect(Collectors.toList());


            // Build UserAllProductDTO
            UserAllProductDTO dto = new UserAllProductDTO();
            dto.setId(inventory.getId());
            dto.setNameOfProduct(inventory.getNameOfProduct());
            dto.setActive(inventory.getActive());
            dto.setColor(inventory.getColor());
            dto.setProductImage(inventory.getProductImage());
            dto.setProductImag2(inventory.getProductImag2());
            dto.setProductImag3(inventory.getProductImag3());
            dto.setProductLocation(inventory.getProductLocation());
            dto.setSizes(inventory.getSizes());

            if (display != null) {
                dto.setDisplaynamecatid(display.getId());
                dto.setCategoryName(display.getCategoryName());
                dto.setSubCategoryName(display.getSubCategoryName());
//                dto.setProductName(inventory.getNameOfProduct());
                dto.setProductName(
                        (inventory.getNameOfProduct() != null && !inventory.getNameOfProduct().trim().isEmpty())
                                ? inventory.getNameOfProduct()
                                : display.getProductName()
                );

                dto.setProductDescription(display.getProductDescription());
                dto.setManufacturerName(display.getManufacturerName());
                dto.setWebsiteName(display.getWebsiteName());
                dto.setImageUrl(display.getImageUrl());
                dto.setSelectdTheme(display.getSelectdTheme());
            }

            if (fabric != null) {
                dto.setFabricId(fabric.getId());
                dto.setFabricName(fabric.getFabricName());
                dto.setFabricDisplayName(fabric.getDisplayName());
                dto.setBuyingPrice(fabric.getBuyingPrice());
                dto.setWholesalePrice(fabric.getWholesalePrice());
                dto.setMaximumPrice(fabric.getMaximumPrice());
                dto.setRetailPrice(fabric.getRetailPrice());
            }

            dto.setSets(setDTOs);

            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}

