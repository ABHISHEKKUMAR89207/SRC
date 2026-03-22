package com.vtt.retail;

import com.vtt.FileStorage.FileStorageService;
import com.vtt.entities.DisplayNamesCat;
import com.vtt.entities.Fabric;
import com.vtt.entities.ProductInventory;
import com.vtt.repository.DisplayNamesCatRepository;
import com.vtt.repository.FabricRepository;
import com.vtt.repository.ProductSetsRepository;
import com.vtt.retail.repository.RetailProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/retail/products")
@Tag(name = "Admin Retail Product Controller", description = "Admin API for managing retail products")
public class AdminRetailProductController {

    private final RetailProductRepository retailProductRepository;
    private final ProductSetsRepository productSetsRepository;
    private final FileStorageService fileStorageService;

    @Autowired
    private DisplayNamesCatRepository displayNamesCatRepository;

    @Autowired
    private FabricRepository fabricRepository;

    @Value("${file.base-url}")
    private String fileBaseUrl;

    // ================= ADD PRODUCT =================
    @PostMapping
    @Operation(summary = "Add new retail product", description = "Create a new product with category, fabric, and size-quantity mapping")
    public ResponseEntity<?> addProduct(@RequestBody AddProductRequest request) {
        try {
            // Basic validation
            if (request.getNameOfProduct() == null || request.getNameOfProduct().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Product name is required", null, false));
            }

            if (request.getColor() == null || request.getColor().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Color is required", null, false));
            }

            if (request.getSizes() == null || request.getSizes().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("At least one size is required", null, false));
            }

            // OPTIONAL FETCH - displayNamesCat and Fabric can be optional
            DisplayNamesCat displayNamesCat = null;
            Fabric fabric = null;

            if (request.getDisplayNamesCatId() != null && !request.getDisplayNamesCatId().isEmpty()) {
                displayNamesCat = displayNamesCatRepository
                        .findById(request.getDisplayNamesCatId())
                        .orElse(null);
            }

            if (request.getFabricId() != null && !request.getFabricId().isEmpty()) {
                fabric = fabricRepository
                        .findById(request.getFabricId())
                        .orElse(null);
            }

            // CREATE PRODUCT
            ProductInventory product = new ProductInventory();
            product.setId(UUID.randomUUID().toString());
            product.setNameOfProduct(request.getNameOfProduct());
            product.setActive(request.getActive() != null ? request.getActive() : "true");
            product.setColor(request.getColor());
            product.setColorCode(request.getColorCode());
            product.setProductImage(request.getProductImage());
            product.setProductImag2(request.getProductImag2());
            product.setProductImag3(request.getProductImag3());
            product.setProductLocation(request.getProductLocation());
            product.setArticleName(request.getArticleName());
            product.setIsNotOurBrand(request.getIsNotOurBrand() != null ? request.getIsNotOurBrand() : false);

            // Set description fields
            product.setProductDescription1(request.getProductDescription1());
            product.setProductDescription2(request.getProductDescription2());
            product.setProductDescription3(request.getProductDescription3());

            // SET OPTIONAL REFERENCES
            product.setDisplayNamesCat(displayNamesCat);
            product.setFabric(fabric);

            // SAFE DENORMALIZED FIELDS
            if (displayNamesCat != null) {
                product.setCategory(displayNamesCat.getCategoryName());
                product.setSubcategory(displayNamesCat.getSubCategoryName());
            }

            if (fabric != null) {
                product.setFabricName(fabric.getDisplayName());
            }

            // SIZES
            List<ProductInventory.SizeQuantity> sizes = new ArrayList<>();
            for (AddProductRequest.SizeQuantityRequest sizeReq : request.getSizes()) {
                sizes.add(new ProductInventory.SizeQuantity(
                        sizeReq.getLabel(),
                        sizeReq.getQuantity(),
                        sizeReq.getPrice(),
                        sizeReq.getWholesalePrice()
                ));
            }
            product.setSizes(sizes);

            // TIMESTAMPS
            product.setCreatedAt(LocalDateTime.now());
            product.setUpdatedAt(LocalDateTime.now());
            product.setLastedArrivedAt(LocalDateTime.now());

            // DEFAULT VALUES OR FROM REQUEST
            product.setRating(request.getRating() != null ? request.getRating() : 0.0);
            product.setTotalRatings(request.getTotalRatings() != null ? request.getTotalRatings() : 0);
            product.setTotalSales(request.getTotalSales() != null ? request.getTotalSales() : 0.0);

            ProductInventory savedProduct = retailProductRepository.save(product);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("Product created successfully", savedProduct, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    // ================= EDIT PRODUCT =================
    @PutMapping("/{productId}")
    @Operation(summary = "Edit existing retail product", description = "Update product details. Note: displayNamesCat, fabric, and color cannot be edited")
    public ResponseEntity<?> editProduct(@PathVariable String productId,
                                         @RequestBody EditProductRequest request) {
        try {
            Optional<ProductInventory> productOpt = retailProductRepository.findById(productId);
            if (productOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Product not found", null, false));
            }

            ProductInventory product = productOpt.get();

            // ✅ EDITABLE FIELDS
            if (request.getNameOfProduct() != null && !request.getNameOfProduct().isEmpty())
                product.setNameOfProduct(request.getNameOfProduct());

            if (request.getActive() != null)
                product.setActive(request.getActive());

            if (request.getProductImage() != null)
                product.setProductImage(request.getProductImage());

            if (request.getProductImag2() != null)
                product.setProductImag2(request.getProductImag2());

            if (request.getProductImag3() != null)
                product.setProductImag3(request.getProductImag3());

            if (request.getProductLocation() != null)
                product.setProductLocation(request.getProductLocation());

            if (request.getArticleName() != null)
                product.setArticleName(request.getArticleName());

            if (request.getIsNotOurBrand() != null)
                product.setIsNotOurBrand(request.getIsNotOurBrand());

            // Update description fields
            if (request.getProductDescription1() != null)
                product.setProductDescription1(request.getProductDescription1());

            if (request.getProductDescription2() != null)
                product.setProductDescription2(request.getProductDescription2());

            if (request.getProductDescription3() != null)
                product.setProductDescription3(request.getProductDescription3());

            // Update rating fields
            if (request.getRating() != null)
                product.setRating(request.getRating());

            if (request.getTotalRatings() != null)
                product.setTotalRatings(request.getTotalRatings());

            if (request.getTotalSales() != null)
                product.setTotalSales(request.getTotalSales());

            // UPDATE SIZES
            if (request.getSizes() != null && !request.getSizes().isEmpty()) {
                List<ProductInventory.SizeQuantity> updatedSizes = new ArrayList<>();
                for (EditProductRequest.SizeQuantityRequest sizeReq : request.getSizes()) {
                    updatedSizes.add(new ProductInventory.SizeQuantity(
                            sizeReq.getLabel(),
                            sizeReq.getQuantity(),
                            sizeReq.getPrice(),
                            sizeReq.getWholesalePrice()
                    ));
                }
                product.setSizes(updatedSizes);
            }

            product.setUpdatedAt(LocalDateTime.now());

            ProductInventory updatedProduct = retailProductRepository.save(product);

            return ResponseEntity.ok(new ApiResponse<>("Product updated successfully", updatedProduct, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    // ================= UPLOAD PRODUCT IMAGES =================
    @PutMapping(value = "/{productId}/images", consumes = {"multipart/form-data"})
    @Operation(summary = "Upload product images", description = "Upload up to 3 product images (image1, image2, image3)")
    public ResponseEntity<?> uploadProductImages(
            @PathVariable String productId,
            @RequestPart(value = "image1", required = false) MultipartFile image1,
            @RequestPart(value = "image2", required = false) MultipartFile image2,
            @RequestPart(value = "image3", required = false) MultipartFile image3) {
        try {
            Optional<ProductInventory> productOpt = retailProductRepository.findById(productId);
            if (productOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Product not found", null, false));
            }

            ProductInventory product = productOpt.get();

            // Update image1
            if (image1 != null && !image1.isEmpty()) {
                if (product.getProductImage() != null && !product.getProductImage().isEmpty()) {
                    String oldFileName = product.getProductImage().replace(fileBaseUrl, "");
                    try {
                        fileStorageService.deleteFile(oldFileName);
                    } catch (Exception e) {
                        System.err.println("Failed to delete old image1: " + e.getMessage());
                    }
                }
                String fileName = fileStorageService.storeFile(image1);
                product.setProductImage(fileBaseUrl + fileName);
            }

            // Update image2
            if (image2 != null && !image2.isEmpty()) {
                if (product.getProductImag2() != null && !product.getProductImag2().isEmpty()) {
                    String oldFileName = product.getProductImag2().replace(fileBaseUrl, "");
                    try {
                        fileStorageService.deleteFile(oldFileName);
                    } catch (Exception e) {
                        System.err.println("Failed to delete old image2: " + e.getMessage());
                    }
                }
                String fileName = fileStorageService.storeFile(image2);
                product.setProductImag2(fileBaseUrl + fileName);
            }

            // Update image3
            if (image3 != null && !image3.isEmpty()) {
                if (product.getProductImag3() != null && !product.getProductImag3().isEmpty()) {
                    String oldFileName = product.getProductImag3().replace(fileBaseUrl, "");
                    try {
                        fileStorageService.deleteFile(oldFileName);
                    } catch (Exception e) {
                        System.err.println("Failed to delete old image3: " + e.getMessage());
                    }
                }
                String fileName = fileStorageService.storeFile(image3);
                product.setProductImag3(fileBaseUrl + fileName);
            }

            // Check if at least one image was uploaded
            if ((image1 == null || image1.isEmpty()) &&
                    (image2 == null || image2.isEmpty()) &&
                    (image3 == null || image3.isEmpty())) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("At least one image must be provided", null, false));
            }

            product.setUpdatedAt(LocalDateTime.now());
            ProductInventory updatedProduct = retailProductRepository.save(product);

            return ResponseEntity.ok(new ApiResponse<>("Images uploaded successfully", updatedProduct, true));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Failed to upload images: " + e.getMessage(), null, false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    // ================= UPDATE PRODUCT DETAILS =================
    @PutMapping("/{productId}/details")
    @Operation(summary = "Update product details", description = "Update product information like location, name, active status, descriptions, ratings and sizes")
    public ResponseEntity<?> updateProductDetails(
            @PathVariable String productId,
            @RequestBody Map<String, Object> requestData) {
        try {
            Optional<ProductInventory> productOpt = retailProductRepository.findById(productId);
            if (productOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Product not found", null, false));
            }

            ProductInventory product = productOpt.get();

            // Update product location
            if (requestData.containsKey("productLocation")) {
                product.setProductLocation((String) requestData.get("productLocation"));
            }

            // Update product name
            if (requestData.containsKey("nameOfProduct")) {
                product.setNameOfProduct((String) requestData.get("nameOfProduct"));
            }

            // Update active status
            if (requestData.containsKey("active")) {
                product.setActive((String) requestData.get("active"));
            }

            // Update article name
            if (requestData.containsKey("articleName")) {
                product.setArticleName((String) requestData.get("articleName"));
            }

            // Update isNotOurBrand
            if (requestData.containsKey("isNotOurBrand")) {
                product.setIsNotOurBrand((Boolean) requestData.get("isNotOurBrand"));
            }

            // Update descriptions
            if (requestData.containsKey("productDescription1")) {
                product.setProductDescription1((String) requestData.get("productDescription1"));
            }

            if (requestData.containsKey("productDescription2")) {
                product.setProductDescription2((String) requestData.get("productDescription2"));
            }

            if (requestData.containsKey("productDescription3")) {
                product.setProductDescription3((String) requestData.get("productDescription3"));
            }

            // Update rating fields
            if (requestData.containsKey("rating")) {
                product.setRating(((Number) requestData.get("rating")).doubleValue());
            }

            if (requestData.containsKey("totalRatings")) {
                product.setTotalRatings(((Number) requestData.get("totalRatings")).intValue());
            }

            if (requestData.containsKey("totalSales")) {
                product.setTotalSales(((Number) requestData.get("totalSales")).doubleValue());
            }

            // Update sizes if present in request
            if (requestData.containsKey("sizes")) {
                List<Map<String, Object>> sizesData = (List<Map<String, Object>>) requestData.get("sizes");
                List<ProductInventory.SizeQuantity> sizes = new ArrayList<>();

                for (Map<String, Object> sizeData : sizesData) {
                    ProductInventory.SizeQuantity sizeQuantity = new ProductInventory.SizeQuantity();
                    sizeQuantity.setLabel((String) sizeData.get("label"));
                    sizeQuantity.setQuantity(((Number) sizeData.get("quantity")).intValue());

                    if (sizeData.containsKey("price")) {
                        sizeQuantity.setPrice(((Number) sizeData.get("price")).doubleValue());
                    }

                    if (sizeData.containsKey("wholesalePrice")) {
                        sizeQuantity.setWholesalePrice(((Number) sizeData.get("wholesalePrice")).doubleValue());
                    }

                    sizes.add(sizeQuantity);
                }
                product.setSizes(sizes);
            }

            product.setUpdatedAt(LocalDateTime.now());
            ProductInventory updatedProduct = retailProductRepository.save(product);

            return ResponseEntity.ok(new ApiResponse<>("Product details updated successfully", updatedProduct, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    // ================= DELETE PRODUCT =================
    @DeleteMapping("/{productId}")
    @Operation(summary = "Delete retail product", description = "Remove a product from inventory")
    public ResponseEntity<?> deleteProduct(@PathVariable String productId) {
        try {
            Optional<ProductInventory> productOpt = retailProductRepository.findById(productId);
            if (productOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Product not found", null, false));
            }

            ProductInventory product = productOpt.get();
            deleteProductImages(product);

            retailProductRepository.deleteById(productId);

            return ResponseEntity.ok(new ApiResponse<>("Product deleted successfully", null, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    // ================= GET PRODUCT BY ID =================
    @GetMapping("/{productId}")
    @Operation(summary = "Get product details", description = "Retrieve full product details for editing")
    public ResponseEntity<?> getProduct(@PathVariable String productId) {
        try {
            return retailProductRepository.findById(productId)
                    .map(p -> ResponseEntity.ok(new ApiResponse<>("Success", p, true)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ApiResponse<>("Product not found", null, false)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    // ================= UPDATE STATUS =================
    @PatchMapping("/{productId}/status")
    @Operation(summary = "Update product active status", description = "Toggle product active/inactive status")
    public ResponseEntity<?> updateStatus(@PathVariable String productId,
                                          @RequestParam String status) {
        try {
            if (!status.equals("true") && !status.equals("false")) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Status must be 'true' or 'false'", null, false));
            }

            Optional<ProductInventory> productOpt = retailProductRepository.findById(productId);
            if (productOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Product not found", null, false));
            }

            ProductInventory product = productOpt.get();
            product.setActive(status);
            product.setUpdatedAt(LocalDateTime.now());

            ProductInventory updatedProduct = retailProductRepository.save(product);

            return ResponseEntity.ok(new ApiResponse<>("Status updated successfully", updatedProduct, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    // ================= HELPER METHODS =================
    private void deleteProductImages(ProductInventory product) {
        if (product.getProductImage() != null && !product.getProductImage().isEmpty()) {
            try {
                String fileName = product.getProductImage().replace(fileBaseUrl, "");
                fileStorageService.deleteFile(fileName);
            } catch (Exception e) {
                System.err.println("Failed to delete image1: " + e.getMessage());
            }
        }

        if (product.getProductImag2() != null && !product.getProductImag2().isEmpty()) {
            try {
                String fileName = product.getProductImag2().replace(fileBaseUrl, "");
                fileStorageService.deleteFile(fileName);
            } catch (Exception e) {
                System.err.println("Failed to delete image2: " + e.getMessage());
            }
        }

        if (product.getProductImag3() != null && !product.getProductImag3().isEmpty()) {
            try {
                String fileName = product.getProductImag3().replace(fileBaseUrl, "");
                fileStorageService.deleteFile(fileName);
            } catch (Exception e) {
                System.err.println("Failed to delete image3: " + e.getMessage());
            }
        }
    }

    // ================= DTO CLASSES =================
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddProductRequest {
        private String nameOfProduct;
        private String active;
        private String color;
        private String colorCode;
        private String productImage;
        private String productImag2;
        private String productImag3;
        private String productLocation;
        private String articleName;
        private Boolean isNotOurBrand;
        private String productDescription1;
        private String productDescription2;
        private String productDescription3;

        private String displayNamesCatId;
        private String fabricId;

        private Double rating;
        private Integer totalRatings;
        private Double totalSales;

        private List<SizeQuantityRequest> sizes;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class SizeQuantityRequest {
            private String label;
            private int quantity;
            private double price;
            private double wholesalePrice;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EditProductRequest {
        // ✅ EDITABLE FIELDS
        private String nameOfProduct;
        private String active;
        private String productImage;
        private String productImag2;
        private String productImag3;
        private String productLocation;
        private String articleName;
        private Boolean isNotOurBrand;
        private String productDescription1;
        private String productDescription2;
        private String productDescription3;

        private Double rating;
        private Integer totalRatings;
        private Double totalSales;

        private List<SizeQuantityRequest> sizes;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class SizeQuantityRequest {
            private String label;
            private int quantity;
            private double price;
            private double wholesalePrice;
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApiResponse<T> {
        private String message;
        private T data;
        private boolean success;
    }
}