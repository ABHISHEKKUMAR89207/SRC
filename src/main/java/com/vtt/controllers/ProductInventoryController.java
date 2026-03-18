package com.vtt.controllers;



import com.vtt.FileStorage.FileStorageService;
import com.vtt.commonfunc.TokenUtils;
import com.vtt.dtoforSrc.ProductInventoryDTO;
import com.vtt.entities.ProductInventory;
import com.vtt.entities.DisplayNamesCat;
import com.vtt.entities.Fabric;
import com.vtt.entities.User;
import com.vtt.otherclass.MainRole;
import com.vtt.repository.ProductInventoryRepository;
import com.vtt.repository.DisplayNamesCatRepository;
import com.vtt.repository.FabricRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/productinventory")
public class ProductInventoryController {

    @Autowired
    private ProductInventoryRepository productInventoryRepository;

    @Autowired
    private DisplayNamesCatRepository displayNamesCatRepository;

    @Autowired
    private FabricRepository fabricRepository;

    @Autowired
    private TokenUtils tokenUtils;
    @Value("${file.base-url}")
    private String fileBaseUrl;

    private final FileStorageService fileStorageService;

    public ProductInventoryController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public ResponseEntity<List<ProductInventory>> getAllProductInventories(@RequestHeader("Authorization") String tokenHeader) {
//        if (!checkAdminRole(tokenHeader)) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Respond with a forbidden status
//        }
        List<ProductInventory> productInventories = productInventoryRepository.findAll(); // Fetch product inventories
        return ResponseEntity.ok(productInventories); // Return the list wrapped in ResponseEntity
    }


    // GET a single ProductInventory by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductInventory> getProductInventoryById(@PathVariable String id, @RequestHeader("Authorization") String tokenHeader) {
        if (!checkAdminRole(tokenHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<ProductInventory> productInventory = productInventoryRepository.findById(id);
        return productInventory.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST a new ProductInventory or update existing one if color, DisplayNamesCat, and Fabric match
    @PostMapping
    public ResponseEntity<ProductInventory> createOrUpdateProductInventory(@RequestBody ProductInventoryDTO productInventoryDTO,
                                                                           @RequestHeader("Authorization") String tokenHeader) {
        if (!checkAdminRole(tokenHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Get the associated DisplayNamesCat and Fabric entities
        Optional<DisplayNamesCat> displayNamesCat = displayNamesCatRepository.findById(productInventoryDTO.getDisplayNamesCatId());
        Optional<Fabric> fabric = fabricRepository.findById(productInventoryDTO.getFabricId());
        if (displayNamesCat.isPresent() && fabric.isPresent()) {
            List<Fabric> fabrics = fabricRepository.findByDisplayNameContainingIgnoreCase(fabric.get().getDisplayName());

            // Check if a ProductInventory with the same color, displayNamesCat, and fabric already exists
//            Optional<ProductInventory> existingProductInventory = productInventoryRepository
//                    .findByColorAndDisplayNamesCatAndFabric(productInventoryDTO.getColor(),
//                            displayNamesCat.get(), fabrics.get(0));
            Optional<ProductInventory> existingProductInventory = Optional.empty();
            if (productInventoryDTO.getColor() == null || productInventoryDTO.getColor().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .header("error", "Color is required")
                        .build();
            }

            if (productInventoryDTO.getArticleName() == null || productInventoryDTO.getArticleName().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .header("error", "Article name is required")
                        .build();
            }

            if (productInventoryDTO.getDisplayNamesCatId() == null || productInventoryDTO.getDisplayNamesCatId().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .header("error", "DisplayNamesCatId is required")
                        .build();
            }

            if (productInventoryDTO.getFabricId() == null || productInventoryDTO.getFabricId().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .header("error", "FabricId is required")
                        .build();
            }
            for (Fabric fab : fabrics) {
                existingProductInventory = productInventoryRepository
                        .findByColorAndArticleNameAndDisplayNamesCatAndFabric(
                                productInventoryDTO.getColor(),
                                productInventoryDTO.getArticleName(), // article name added
                                displayNamesCat.get(),
                                fab
                        );

                if (existingProductInventory.isPresent()) {
                    break; // jahan mil gaya wahan loop stop
                }
            }

            ProductInventory productInventory;
            if (existingProductInventory.isPresent()) {
                // If it exists, update the quantities for the existing sizes or add new size
                productInventory = existingProductInventory.get();
                updateSizeQuantities(productInventory, productInventoryDTO.getSizes());
            } else {
                // If it doesn't exist, create a new ProductInventory
                productInventory = new ProductInventory();
                productInventory.setColor(productInventoryDTO.getColor());
                productInventory.setSizes(mapSizeQuantityDTOs(productInventoryDTO.getSizes()));
                productInventory.setDisplayNamesCat(displayNamesCat.get());
                productInventory.setArticleName(productInventoryDTO.getArticleName());
                productInventory.setFabric(fabric.get());
            }

            ProductInventory savedProductInventory = productInventoryRepository.save(productInventory);
            return ResponseEntity.ok(savedProductInventory);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping(value = "/{id}/images", consumes = {"multipart/form-data"})
    public ResponseEntity<ProductInventory> updateProductInventoryImages(
            @PathVariable String id,
            @RequestPart(value = "image1", required = false) MultipartFile image1,
            @RequestPart(value = "image2", required = false) MultipartFile image2,
            @RequestPart(value = "image3", required = false) MultipartFile image3,
            @RequestHeader("Authorization") String tokenHeader) throws IOException {

        if (!checkAdminRole(tokenHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<ProductInventory> existingProductInventory = productInventoryRepository.findById(id);
        if (existingProductInventory.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ProductInventory productInventory = existingProductInventory.get();

        // Update image1
        if (image1 != null && !image1.isEmpty()) {
            if (productInventory.getProductImage() != null) {
                String oldFileName = productInventory.getProductImage().replace(fileBaseUrl, "");
                fileStorageService.deleteFile(oldFileName);
            }
            String fileName = fileStorageService.storeFile(image1);
            productInventory.setProductImage(fileBaseUrl + fileName);
        }

        // Update image2
        if (image2 != null && !image2.isEmpty()) {
            if (productInventory.getProductImag2() != null) {
                String oldFileName = productInventory.getProductImag2().replace(fileBaseUrl, "");
                fileStorageService.deleteFile(oldFileName);
            }
            String fileName = fileStorageService.storeFile(image2);
            productInventory.setProductImag2(fileBaseUrl + fileName);
        }

        // Update image3
        if (image3 != null && !image3.isEmpty()) {
            if (productInventory.getProductImag3() != null) {
                String oldFileName = productInventory.getProductImag3().replace(fileBaseUrl, "");
                fileStorageService.deleteFile(oldFileName);
            }
            String fileName = fileStorageService.storeFile(image3);
            productInventory.setProductImag3(fileBaseUrl + fileName);
        }

        ProductInventory updatedProductInventory = productInventoryRepository.save(productInventory);
        return ResponseEntity.ok(updatedProductInventory);
    }


//    @PutMapping(value = "/{id}/image", consumes = {"multipart/form-data"})
//    public ResponseEntity<ProductInventory> updateProductInventoryImage(
//            @PathVariable String id,
//            @RequestPart("image") MultipartFile image,
//            @RequestHeader("Authorization") String tokenHeader) throws IOException {
//
//        if (!checkAdminRole(tokenHeader)) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
//
//        Optional<ProductInventory> existingProductInventory = productInventoryRepository.findById(id);
//        if (existingProductInventory.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        ProductInventory productInventory = existingProductInventory.get();
//
//        // Delete old image if exists
//        if (productInventory.getProductImage() != null) {
//            String oldFileName = productInventory.getProductImage().replace(fileBaseUrl, "");
//            fileStorageService.deleteFile(oldFileName);
//        }
//
//        // Store new image
//        String fileName = fileStorageService.storeFile(image);
//        String fileUrl = fileBaseUrl + fileName;
//        productInventory.setProductImage(fileUrl);
//
//        ProductInventory updatedProductInventory = productInventoryRepository.save(productInventory);
//        return ResponseEntity.ok(updatedProductInventory);
//    }


    @PutMapping("/{id}/details")
    public ResponseEntity<ProductInventory> updateProductInventoryDetails(
            @PathVariable String id,
            @RequestBody Map<String, Object> requestData,
            @RequestHeader("Authorization") String tokenHeader) {

        if (!checkAdminRole(tokenHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<ProductInventory> existingProductInventory = productInventoryRepository.findById(id);
        if (existingProductInventory.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ProductInventory productInventory = existingProductInventory.get();

        // Update product location if present in request
        if (requestData.containsKey("productLocation")) {
            productInventory.setProductLocation((String) requestData.get("productLocation"));
        }
        if (requestData.containsKey("nameOfProduct")) {
            productInventory.setNameOfProduct((String) requestData.get("nameOfProduct"));
        }
        if (requestData.containsKey("active")) {
            productInventory.setActive((String) requestData.get("active"));
        }

        // Update sizes if present in request
        if (requestData.containsKey("sizes")) {
            List<Map<String, Object>> sizesData = (List<Map<String, Object>>) requestData.get("sizes");
            List<ProductInventory.SizeQuantity> sizes = new ArrayList<>();

            for (Map<String, Object> sizeData : sizesData) {
                ProductInventory.SizeQuantity sizeQuantity = new ProductInventory.SizeQuantity();
                sizeQuantity.setLabel((String) sizeData.get("label"));
                sizeQuantity.setQuantity(((Number) sizeData.get("quantity")).intValue());
                sizes.add(sizeQuantity);
            }
            productInventory.setSizes(sizes);
        }

        ProductInventory updatedProductInventory = productInventoryRepository.save(productInventory);
        return ResponseEntity.ok(updatedProductInventory);
    }
    // PUT update an existing ProductInventory
//    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
//    public ResponseEntity<ProductInventory> updateProductInventory(
//            @PathVariable String id,
//            @RequestPart(value = "image", required = false) MultipartFile image,
//            @RequestPart("productInventoryDTO") ProductInventoryDTO productInventoryDTO,
//            @RequestHeader("Authorization") String tokenHeader) throws IOException {
//        System.out.println("dsjkhcfdfjsdfbdshjfndsv-----------------------");
//        if (!checkAdminRole(tokenHeader)) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
//
//        Optional<ProductInventory> existingProductInventory = productInventoryRepository.findById(id);
//        if (existingProductInventory.isPresent()) {
//            Optional<DisplayNamesCat> displayNamesCat = displayNamesCatRepository.findById(productInventoryDTO.getDisplayNamesCatId());
//            Optional<Fabric> fabric = fabricRepository.findById(productInventoryDTO.getFabricId());
//
//            if (displayNamesCat.isPresent() && fabric.isPresent()) {
//                ProductInventory productInventory = existingProductInventory.get();
//
//                // Handle image upload if present
//                if (image != null && !image.isEmpty()) {
//                    // Delete old image if exists
//                    if (productInventory.getProductImage() != null) {
//                        String oldFileName = productInventory.getProductImage().replace(fileBaseUrl, "");
//                        fileStorageService.deleteFile(oldFileName);
//                    }
//
//                    // Store new image
//                    String fileName = fileStorageService.storeFile(image);
//                    String fileUrl = fileBaseUrl + fileName;
//                    productInventory.setProductImage(fileUrl);
//                }
//
//                productInventory.setColor(productInventoryDTO.getColor());
//                productInventory.setSizes(mapSizeQuantityDTOs(productInventoryDTO.getSizes()));
//                productInventory.setProductLocation(productInventoryDTO.getProductLocation());
//
//                ProductInventory updatedProductInventory = productInventoryRepository.save(productInventory);
//                return ResponseEntity.ok(updatedProductInventory);
//            } else {
//                return ResponseEntity.badRequest().build();
//            }
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//    @PutMapping("/{id}")
//    public ResponseEntity<ProductInventory> updateProductInventory(@PathVariable String id, @RequestBody ProductInventoryDTO productInventoryDTO,
//                                                                   @RequestHeader("Authorization") String tokenHeader) {
//        if (!checkAdminRole(tokenHeader)) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
//
//        Optional<ProductInventory> existingProductInventory = productInventoryRepository.findById(id);
//        if (existingProductInventory.isPresent()) {
//            Optional<DisplayNamesCat> displayNamesCat = displayNamesCatRepository.findById(productInventoryDTO.getDisplayNamesCatId());
//            Optional<Fabric> fabric = fabricRepository.findById(productInventoryDTO.getFabricId());
//
//            if (displayNamesCat.isPresent() && fabric.isPresent()) {
//                ProductInventory productInventory = existingProductInventory.get();
//                productInventory.setColor(productInventoryDTO.getColor());
//                productInventory.setSizes(mapSizeQuantityDTOs(productInventoryDTO.getSizes()));
//                productInventory.setDisplayNamesCat(displayNamesCat.get());
//                productInventory.setFabric(fabric.get());
//
//                ProductInventory updatedProductInventory = productInventoryRepository.save(productInventory);
//                return ResponseEntity.ok(updatedProductInventory);
//            } else {
//                return ResponseEntity.badRequest().build();
//            }
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    // DELETE a ProductInventory by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductInventory(@PathVariable String id, @RequestHeader("Authorization") String tokenHeader) {



        if (!checkAdminRole(tokenHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (productInventoryRepository.existsById(id)) {
            productInventoryRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Utility method to convert SizeQuantityDTO list to ProductInventory.SizeQuantity list
    private List<ProductInventory.SizeQuantity> mapSizeQuantityDTOs(List<ProductInventoryDTO.SizeQuantityDTO> sizeQuantityDTOs) {
        return sizeQuantityDTOs.stream()
                .map(dto -> new ProductInventory.SizeQuantity(dto.getLabel(), dto.getQuantity(),0))
                .toList();
    }

    // Utility method to update size quantities or add new size
    private void updateSizeQuantities(ProductInventory productInventory, List<ProductInventoryDTO.SizeQuantityDTO> newSizes) {
        for (ProductInventoryDTO.SizeQuantityDTO newSize : newSizes) {
            boolean sizeUpdated = false;
            for (ProductInventory.SizeQuantity existingSize : productInventory.getSizes()) {
                if (existingSize.getLabel().equals(newSize.getLabel())) {
                    // If size exists, update the quantity
                    existingSize.setQuantity(existingSize.getQuantity() + newSize.getQuantity());
                    sizeUpdated = true;
                    break;
                }
            }
            // If size doesn't exist, add a new size
            if (!sizeUpdated) {
                productInventory.getSizes().add(new ProductInventory.SizeQuantity(newSize.getLabel(), newSize.getQuantity(),0));
            }
        }
    }

    // Method to check if the user is ADMIN
    private boolean checkAdminRole(String tokenHeader) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            return requestingUser.getMainRole() == MainRole.ADMIN;
        } catch (Exception e) {
            return false;
        }
    }


}
