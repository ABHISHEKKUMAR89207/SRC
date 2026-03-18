package com.vtt.retail;

import com.vtt.entities.ProductInventory;
import com.vtt.retail.repository.RetailProductRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/retail/categories")
@Tag(name = "Retail Category Controller", description = "API for category and subcategory management")
public class RetailCategoryController {

    private final RetailProductRepository retailProductRepository;

    /**
     * Get all categories
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllCategories() {
        try {
            List<ProductInventory>allProducts = retailProductRepository.findAll();

            Set<String> categories = allProducts.stream()
                    .map(ProductInventory::getCategory)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            List<CategoryInfo> categoryList = new ArrayList<>();
            for (String category : categories) {
                CategoryInfo info = new CategoryInfo();
                info.setCategoryName(category);
                info.setProductCount(allProducts.stream()
                        .filter(p -> category.equals(p.getCategory()))
                        .count());
                categoryList.add(info);
            }

            categoryList.sort(Comparator.comparing(CategoryInfo::getProductCount).reversed());

            return ResponseEntity.ok(new ApiResponse<>("Success", categoryList, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get category with details
     */
    @GetMapping("/{categoryName}")
    public ResponseEntity<?> getCategoryDetails(@PathVariable String categoryName) {
        try {
            Page<ProductInventory>products = retailProductRepository.findByCategory(categoryName, Pageable.unpaged());

            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Category not found", null, false));
            }

            CategoryDetails details = new CategoryDetails();
            details.setCategoryName(categoryName);
            details.setTotalProducts(products.getTotalElements());

            // Get subcategories
            Set<String> subcategories = products.stream()
                    .map(ProductInventory::getSubcategory)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            details.setSubcategories(new ArrayList<>(subcategories));

            // Get colors
            Set<String> colors = products.stream()
                    .map(ProductInventory::getColor)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            details.setColors(new ArrayList<>(colors));

            // Get price range
            double minPrice = Double.MAX_VALUE;
            double maxPrice = 0;

            for (ProductInventory product : products) {
                if (product.getSizes() != null) {
                    for (ProductInventory.SizeQuantity size : product.getSizes()) {
                        if (size.getPrice() < minPrice) minPrice = size.getPrice();
                        if (size.getPrice() > maxPrice) maxPrice = size.getPrice();
                    }
                }
            }

            details.setMinPrice(minPrice == Double.MAX_VALUE ? 0 : minPrice);
            details.setMaxPrice(maxPrice);

            return ResponseEntity.ok(new ApiResponse<>("Success", details, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get all subcategories for a category
     */
    @GetMapping("/{categoryName}/subcategories")
    public ResponseEntity<?> getSubcategories(@PathVariable String categoryName) {
        try {
            Page<ProductInventory>products = retailProductRepository.findByCategory(categoryName, Pageable.unpaged());

            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Category not found", null, false));
            }

            Map<String, Long> subcategoryCount = products.stream()
                    .collect(Collectors.groupingBy(
                            ProductInventory::getSubcategory,
                            Collectors.counting()
                    ));

            List<SubcategoryInfo> subcategories = new ArrayList<>();
            for (Map.Entry<String, Long> entry : subcategoryCount.entrySet()) {
                SubcategoryInfo info = new SubcategoryInfo();
                info.setSubcategoryName(entry.getKey());
                info.setProductCount(entry.getValue());
                subcategories.add(info);
            }

            subcategories.sort(Comparator.comparing(SubcategoryInfo::getProductCount).reversed());

            return ResponseEntity.ok(new ApiResponse<>("Success", subcategories, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get products by category and subcategory
     */
    @GetMapping("/{categoryName}/subcategories/{subcategoryName}/products")
    public ResponseEntity<?> getSubcategoryProducts(
            @PathVariable String categoryName,
            @PathVariable String subcategoryName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            Page<ProductInventory>products = retailProductRepository
                    .findByCategoryAndSubcategory(categoryName, subcategoryName, pageable);

            return ResponseEntity.ok(new ApiResponse<>("Success", products, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Browse products by category with filters
     */
    @PostMapping("/browse")
    public ResponseEntity<?> browseCategory(
            @RequestBody BrowseRequest browseRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

            Page<ProductInventory>products;

            if (browseRequest.getSubcategoryName() != null && !browseRequest.getSubcategoryName().isEmpty()) {
                // Browse by category and subcategory
                if (browseRequest.getColor() != null && !browseRequest.getColor().isEmpty()) {
                    products = retailProductRepository.findByCategoryAndSubcategoryAndColor(
                            browseRequest.getCategoryName(),
                            browseRequest.getSubcategoryName(),
                            browseRequest.getColor(),
                            pageable
                    );
                } else {
                    products = retailProductRepository.findByCategoryAndSubcategory(
                            browseRequest.getCategoryName(),
                            browseRequest.getSubcategoryName(),
                            pageable
                    );
                }
            } else {
                // Browse by category only
                products = retailProductRepository.findByCategory(browseRequest.getCategoryName(), pageable);
            }

            return ResponseEntity.ok(new ApiResponse<>("Success", products, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get category hierarchy (categories with subcategories)
     */
    @GetMapping("/hierarchy")
    public ResponseEntity<?> getCategoryHierarchy() {
        try {
            List<ProductInventory>allProducts = retailProductRepository.findAll();

            Map<String, Set<String>> hierarchy = new LinkedHashMap<>();

            for (ProductInventory product : allProducts) {
                String category = product.getCategory();
                String subcategory = product.getSubcategory();

                if (category != null) {
                    hierarchy.computeIfAbsent(category, k -> new TreeSet()).add(subcategory);
                }
            }

            List<CategoryHierarchy> result = new ArrayList<>();
            for (Map.Entry<String, Set<String>> entry : hierarchy.entrySet()) {
                CategoryHierarchy ch = new CategoryHierarchy();
                ch.setCategoryName(entry.getKey());
                ch.setSubcategories(new ArrayList<>(entry.getValue()));
                result.add(ch);
            }

            return ResponseEntity.ok(new ApiResponse<>("Success", result, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Search categories by name
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchCategories(@RequestParam String query) {
        try {
            List<ProductInventory>allProducts = retailProductRepository.findAll();

            Set<String> categories = allProducts.stream()
                    .map(ProductInventory::getCategory)
                    .filter(Objects::nonNull)
                    .filter(cat -> cat.toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toSet());

            List<CategoryInfo> categoryList = new ArrayList<>();
            for (String category : categories) {
                CategoryInfo info = new CategoryInfo();
                info.setCategoryName(category);
                info.setProductCount(allProducts.stream()
                        .filter(p -> category.equals(p.getCategory()))
                        .count());
                categoryList.add(info);
            }

            return ResponseEntity.ok(new ApiResponse<>("Success", categoryList, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get popular categories (with most products)
     */
    @GetMapping("/popular")
    public ResponseEntity<?> getPopularCategories(
            @RequestParam(defaultValue = "5") int limit) {
        try {
            List<ProductInventory>allProducts = retailProductRepository.findAll();

            Set<String> categories = allProducts.stream()
                    .map(ProductInventory::getCategory)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            List<CategoryInfo> categoryList = new ArrayList<>();
            for (String category : categories) {
                CategoryInfo info = new CategoryInfo();
                info.setCategoryName(category);
                info.setProductCount(allProducts.stream()
                        .filter(p -> category.equals(p.getCategory()))
                        .count());
                categoryList.add(info);
            }

            categoryList.sort(Comparator.comparing(CategoryInfo::getProductCount).reversed());

            List<CategoryInfo> popularCategories = categoryList.stream()
                    .limit(limit)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new ApiResponse<>("Success", popularCategories, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    // DTOs
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryInfo {
        private String categoryName;
        private long productCount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubcategoryInfo {
        private String subcategoryName;
        private long productCount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryDetails {
        private String categoryName;
        private long totalProducts;
        private List<String> subcategories;
        private List<String> colors;
        private double minPrice;
        private double maxPrice;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryHierarchy {
        private String categoryName;
        private List<String> subcategories;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BrowseRequest {
        private String categoryName;
        private String subcategoryName;
        private String color;
        private Double minPrice;
        private Double maxPrice;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiResponse<T> {
        private String message;
        private T data;
        private boolean success;
    }
}
