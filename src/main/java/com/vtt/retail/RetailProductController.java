package com.vtt.retail;

import com.vtt.entities.ProductInventory;
import com.vtt.entities.ProductSets;
import com.vtt.repository.ProductSetsRepository;
import com.vtt.retail.repository.RetailProductRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/retail/products")
@Tag(name = "Retail Product Controller", description = "API for retail product operations")
public class RetailProductController {

    private final RetailProductRepository retailProductRepository;
    @Autowired
    private ProductSetsRepository productSetsRepository;
    @GetMapping
    public ResponseEntity<?> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
            
            Page<ProductInventory>products = retailProductRepository.findAll(pageable);
            products = mergeSetQuantities(products);
            
            return ResponseEntity.ok(new ApiResponse<>("Success", products, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable String productId) {
        try {
            Optional<ProductInventory>product = retailProductRepository.findById(productId);
            product = mergeSetQuantities(product);
            if (product.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Product not found", null, false));
            }
            return ResponseEntity.ok(new ApiResponse<>("Success", product.get(), true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<?> getProductsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<ProductInventory>products = retailProductRepository.findByCategory(category, pageable);
            products = mergeSetQuantities(products);
            return ResponseEntity.ok(new ApiResponse<>("Success", products, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    @GetMapping("/subcategory/{subcategory}")
    public ResponseEntity<?> getProductsBySubcategory(
            @PathVariable String subcategory,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<ProductInventory>products = retailProductRepository.findBySubcategory(subcategory, pageable);
            products = mergeSetQuantities(products);
            return ResponseEntity.ok(new ApiResponse<>("Success", products, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    @PostMapping("/filter")
    public ResponseEntity<?> filterProducts(
            @RequestBody FilterRequest filterRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
            
            Page<ProductInventory>products;

            if (filterRequest.getCategory() != null && filterRequest.getSubcategory() != null && 
                filterRequest.getColor() != null && filterRequest.getSize() != null && 
                filterRequest.getMinPrice() != null && filterRequest.getMaxPrice() != null) {
                
                products = retailProductRepository.findWithAllFilters(
                    filterRequest.getCategory(),
                    filterRequest.getSubcategory(),
                    filterRequest.getColor(),
                    filterRequest.getSize(),
                    filterRequest.getMinPrice(),
                    filterRequest.getMaxPrice(),
                    pageable
                );
            } else if (filterRequest.getMinPrice() != null && filterRequest.getMaxPrice() != null) {
                products = retailProductRepository.findByPriceRange(
                    filterRequest.getMinPrice(),
                    filterRequest.getMaxPrice(),
                    pageable
                );
            } else if (filterRequest.getColor() != null) {
                products = retailProductRepository.findByColor(filterRequest.getColor(), pageable);
            } else if (filterRequest.getSize() != null) {
                products = retailProductRepository.findBySize(filterRequest.getSize(), pageable);
            } else if (filterRequest.getCategory() != null && filterRequest.getSubcategory() != null) {
                products = retailProductRepository.findByCategoryAndSubcategory(
                    filterRequest.getCategory(),
                    filterRequest.getSubcategory(),
                    pageable
                );
            } else {
                products = retailProductRepository.findAll(pageable);
            }
            products = mergeSetQuantities(products);
            return ResponseEntity.ok(new ApiResponse<>("Success", products, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    @GetMapping("/trending/top10")
    public ResponseEntity<?> getTrendingProducts() {
        try {
            List<ProductInventory>trendingProducts = retailProductRepository.findTop10ByOrderByTotalSalesDesc();
            trendingProducts = mergeSetQuantities(trendingProducts);
            if (trendingProducts.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse<>("No trending products found", new ArrayList<>(), true));
            }
            
            return ResponseEntity.ok(new ApiResponse<>("Success", trendingProducts, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    @GetMapping("/latest/top10")
    public ResponseEntity<?> getLatestProducts() {
        try {
            List<ProductInventory>latestProducts = retailProductRepository.findTop10ByOrderByLastedArrivedAtDesc();
            
            if (latestProducts.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse<>("No latest products found", new ArrayList<>(), true));
            }
            
            return ResponseEntity.ok(new ApiResponse<>("Success", latestProducts, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<ProductInventory>products = retailProductRepository.findByNameOfProductContainingIgnoreCase(keyword, pageable);
            
            return ResponseEntity.ok(new ApiResponse<>("Success", products, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    @GetMapping("/sizes/{category}")
    public ResponseEntity<?> getAvailableSizes(@PathVariable String category) {
        try {
            List<ProductInventory>products = retailProductRepository.findAllSizesByCategory(category);
            
            Set<String> sizes = new HashSet<>();
            for (ProductInventory product : products) {
                if (product.getSizes() != null) {
                    sizes.addAll(product.getSizes().stream()
                            .map(ProductInventory.SizeQuantity::getLabel)
                            .collect(Collectors.toSet()));
                }
            }
            
            return ResponseEntity.ok(new ApiResponse<>("Success", new ArrayList<>(sizes), true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    @GetMapping("/colors/{category}")
    public ResponseEntity<?> getAvailableColors(@PathVariable String category) {
        try {
            List<ProductInventory>products = retailProductRepository.findAllColorsByCategory(category);
            
            Set<String> colors = new HashSet<>();
            for (ProductInventory product : products) {
                if (product.getColor() != null) {
                    colors.add(product.getColor());
                }
            }
            
            return ResponseEntity.ok(new ApiResponse<>("Success", new ArrayList<>(colors), true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    @GetMapping("/price-range/{category}")
    public ResponseEntity<?> getPriceRange(@PathVariable String category) {
        try {
            List<ProductInventory>products = retailProductRepository.findByCategory(category, Pageable.unpaged())
                    .getContent();
            
            if (products.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse<>("No products found", null, false));
            }
            
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
            
            Map<String, Double> priceRange = new HashMap<>();
            priceRange.put("minPrice", minPrice == Double.MAX_VALUE ? 0 : minPrice);
            priceRange.put("maxPrice", maxPrice);
            
            return ResponseEntity.ok(new ApiResponse<>("Success", priceRange, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }
    /**
     * 👉 Overloaded method for List<ProductInventory>
     */
    public List<ProductInventory> mergeSetQuantities(List<ProductInventory> products) {

        return products.stream().map(product -> {

            List<ProductSets> sets = productSetsRepository
                    .findByColorAndDisplayNamesCatAndFabric(
                            product.getColor(),
                            product.getDisplayNamesCat(),
                            product.getFabric()
                    );

            if (sets != null && !sets.isEmpty()) {

                for (ProductSets set : sets) {

                    if (set.getSizes() == null) continue;

                    for (ProductSets.SizeQuantity setSize : set.getSizes()) {

                        boolean found = false;

                        if (product.getSizes() != null) {
                            for (ProductInventory.SizeQuantity prodSize : product.getSizes()) {

                                if (prodSize.getLabel().equalsIgnoreCase(setSize.getLabel())) {

                                    // ✅ add quantities (e.g. 10 + 5 = 15)
                                    prodSize.setQuantity(
                                            prodSize.getQuantity() + (set.getTotalQuantity()*setSize.getQuantity())
                                    );
                                    found = true;
                                    break;
                                }
                            }
                        }

                        // 👉 if size not exists, add new
                        if (!found) {
                            if (product.getSizes() == null) {
                                product.setSizes(new ArrayList<>());
                            }

                            product.getSizes().add(
                                    new ProductInventory.SizeQuantity(
                                            setSize.getLabel(),
                                            (set.getTotalQuantity()*setSize.getQuantity()),
                                            set.getFabric().getRetailPrice(),
                                            set.getFabric().getWholesalePrice()
                                    )
                            );
                        }
                    }
                }
            }

            return product;

        }).toList();
    }
    /**
     * 👉 Overloaded method for Optional<ProductInventory>
     */
    public Optional<ProductInventory> mergeSetQuantities(Optional<ProductInventory> productOptional) {

        if (productOptional.isEmpty()) return Optional.empty();

        ProductInventory product = productOptional.get();

        List<ProductSets> sets = productSetsRepository
                .findByColorAndDisplayNamesCatAndFabric(
                        product.getColor(),
                        product.getDisplayNamesCat(),
                        product.getFabric()
                );

        if (sets != null && !sets.isEmpty()) {

            for (ProductSets set : sets) {

                if (set.getSizes() == null) continue;

                for (ProductSets.SizeQuantity setSize : set.getSizes()) {

                    boolean found = false;

                    if (product.getSizes() != null) {
                        for (ProductInventory.SizeQuantity prodSize : product.getSizes()) {

                            if (prodSize.getLabel().equalsIgnoreCase(setSize.getLabel())) {

                                // ✅ add quantities
                                prodSize.setQuantity(
                                        prodSize.getQuantity() + (set.getTotalQuantity()*setSize.getQuantity())
                                );
                                found = true;
                                break;
                            }
                        }
                    }

                    // 👉 if size not present, add new
                    if (!found) {
                        if (product.getSizes() == null) {
                            product.setSizes(new ArrayList<>());
                        }

                        product.getSizes().add(
                                new ProductInventory.SizeQuantity(
                                        setSize.getLabel(),
                                        (set.getTotalQuantity()*setSize.getQuantity()),
                                        set.getFabric().getRetailPrice(),
                                        set.getFabric().getWholesalePrice()
                                )
                        );
                    }
                }
            }
        }

        return Optional.of(product);
    }
    /**
     * 👉 Separate reusable function
     * Pass Page<ProductInventory> and it will merge ProductSets quantities
     */
    public Page<ProductInventory> mergeSetQuantities(Page<ProductInventory> products) {

        List<ProductInventory> updatedList = products.getContent().stream().map(product -> {

            // 🔹 Filter sets based on color + category + fabric
            List<ProductSets> sets = productSetsRepository
                    .findByColorAndDisplayNamesCatAndFabric(
                            product.getColor(),
                            product.getDisplayNamesCat(),
                            product.getFabric()
                    );

            if (sets != null && !sets.isEmpty()) {

                for (ProductSets set : sets) {

                    if (set.getSizes() == null) continue;

                    for (ProductSets.SizeQuantity setSize : set.getSizes()) {

                        boolean found = false;

                        if (product.getSizes() != null) {
                            for (ProductInventory.SizeQuantity prodSize : product.getSizes()) {

                                if (prodSize.getLabel().equalsIgnoreCase(setSize.getLabel())) {

                                    // ✅ Example: 10 (inventory) + 5 (set) = 15
                                    prodSize.setQuantity(
                                            prodSize.getQuantity() + (set.getTotalQuantity()*setSize.getQuantity())
                                    );
                                    found = true;
                                    break;
                                }
                            }
                        }

                        // 👉 If size not present in product, add it
                        if (!found) {
                            if (product.getSizes() == null) {
                                product.setSizes(new ArrayList<>());
                            }

                            product.getSizes().add(
                                    new ProductInventory.SizeQuantity(
                                            setSize.getLabel(),
                                            (set.getTotalQuantity()*setSize.getQuantity()),
                                            set.getFabric().getRetailPrice(),
                                            set.getFabric().getWholesalePrice()
                                    )
                            );
                        }
                    }
                }
            }

            return product;

        }).toList();

        return new PageImpl<>(updatedList, products.getPageable(), products.getTotalElements());
    }
    @lombok.Getter
    @lombok.Setter
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class FilterRequest {
        private String category;
        private String subcategory;
        private String color;
        private String size;
        private Double minPrice;
        private Double maxPrice;
    }

    @lombok.Getter
    @lombok.Setter
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class ApiResponse<T> {
        private String message;
        private T data;
        private boolean success;
    }
}
