package com.vtt.retail;

import com.vtt.entities.ProductInventory;
import com.vtt.entities.ProductSets;
import com.vtt.repository.ProductSetsRepository;
import com.vtt.retail.repository.RetailProductRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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


    private final MongoTemplate mongoTemplate;
    // NEW ENHANCED FILTER API
    @PostMapping("/advanced-filter")
    public ResponseEntity<?> advancedFilterProducts(
            @RequestBody AdvancedFilterRequest filterRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        try {
            Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            // Build dynamic query
            Query query = new Query();
            List<Criteria> criteriaList = new ArrayList<>();

            // Filter by category (men/women/kids)
            if (filterRequest.getCategory() != null && !filterRequest.getCategory().isEmpty()) {
                criteriaList.add(Criteria.where("category").is(filterRequest.getCategory()));
            }

            // Filter by subcategory (shirt/pant/saree)
            if (filterRequest.getSubcategory() != null && !filterRequest.getSubcategory().isEmpty()) {
                criteriaList.add(Criteria.where("subcategory").is(filterRequest.getSubcategory()));
            }

            // Filter by fabric name (cotton/lycra etc)
            if (filterRequest.getFabricName() != null && !filterRequest.getFabricName().isEmpty()) {
                criteriaList.add(Criteria.where("fabricName").is(filterRequest.getFabricName()));
            }

            // Filter by color
            if (filterRequest.getColor() != null && !filterRequest.getColor().isEmpty()) {
                criteriaList.add(Criteria.where("color").is(filterRequest.getColor()));
            }

            // Filter by color code
            if (filterRequest.getColorCode() != null && !filterRequest.getColorCode().isEmpty()) {
                criteriaList.add(Criteria.where("colorCode").is(filterRequest.getColorCode()));
            }

            // Apply all criteria
            if (!criteriaList.isEmpty()) {
                query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
            }

            // Add sorting
            query.with(pageable);

            // Execute query with pagination
            long total = mongoTemplate.count(query, ProductInventory.class);
            List<ProductInventory> products = mongoTemplate.find(query, ProductInventory.class);

            // Further filter by price if needed (since price is in nested array)
            // This ensures we only return products that actually have a size with price in range
            if (filterRequest.getMinPrice() != null || filterRequest.getMaxPrice() != null) {
                products = products.stream()
                        .filter(product -> {
                            if (product.getSizes() == null || product.getSizes().isEmpty()) {
                                return false;
                            }
                            // Renamed 'size' to 'sizeObj' to avoid conflict with pagination 'size' parameter
                            return product.getSizes().stream().anyMatch(sizeObj -> {
                                boolean meetsMin = filterRequest.getMinPrice() == null ||
                                        sizeObj.getPrice() >= filterRequest.getMinPrice();
                                boolean meetsMax = filterRequest.getMaxPrice() == null ||
                                        sizeObj.getPrice() <= filterRequest.getMaxPrice();
                                return meetsMin && meetsMax;
                            });
                        })
                        .collect(Collectors.toList());

                // Update total after price filtering
                total = products.size();
                // Apply pagination manually after price filtering
                int start = (int) pageable.getOffset();
                int end = Math.min((start + pageable.getPageSize()), products.size());
                if (start <= products.size()) {
                    products = products.subList(start, end);
                } else {
                    products = new ArrayList<>();
                }
            }

            // Merge with ProductSets quantities
            products = mergeSetQuantities(products);

            // Create page object
            Page<ProductInventory> productPage = new PageImpl<>(products, pageable, total);

            // Also return available filters for better UX
            Map<String, Object> response = new HashMap<>();
            response.put("products", productPage);
            response.put("availableFilters", getAvailableFilters(products));
            response.put("totalProducts", total);
            response.put("currentPage", page);
            response.put("totalPages", (int) Math.ceil((double) total / size));

            return ResponseEntity.ok(new ApiResponse<>("Success", response, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    // Helper method to get available filters based on current products
    private Map<String, Object> getAvailableFilters(List<ProductInventory> products) {
        Map<String, Object> availableFilters = new HashMap<>();

        // Get unique categories
        Set<String> categories = products.stream()
                .map(ProductInventory::getCategory)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        availableFilters.put("categories", categories);

        // Get unique subcategories
        Set<String> subcategories = products.stream()
                .map(ProductInventory::getSubcategory)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        availableFilters.put("subcategories", subcategories);

        // Get unique fabric names
        Set<String> fabricNames = products.stream()
                .map(ProductInventory::getFabricName)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        availableFilters.put("fabricNames", fabricNames);

        // Get unique colors
        Set<String> colors = products.stream()
                .map(ProductInventory::getColor)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        availableFilters.put("colors", colors);

        // Get unique color codes
        Set<String> colorCodes = products.stream()
                .map(ProductInventory::getColorCode)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        availableFilters.put("colorCodes", colorCodes);

        // Get price range
        double minPrice = products.stream()
                .flatMap(p -> p.getSizes() != null ? p.getSizes().stream() : java.util.stream.Stream.empty())
                .mapToDouble(ProductInventory.SizeQuantity::getPrice)
                .min()
                .orElse(0);

        double maxPrice = products.stream()
                .flatMap(p -> p.getSizes() != null ? p.getSizes().stream() : java.util.stream.Stream.empty())
                .mapToDouble(ProductInventory.SizeQuantity::getPrice)
                .max()
                .orElse(0);

        Map<String, Double> priceRange = new HashMap<>();
        priceRange.put("min", minPrice);
        priceRange.put("max", maxPrice);
        availableFilters.put("priceRange", priceRange);

        return availableFilters;
    }

    // Alternative: Simple filter API using query parameters
    @GetMapping("/filter")
    public ResponseEntity<?> filterProductsByParams(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String subcategory,
            @RequestParam(required = false) String fabricName,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String colorCode,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        try {
            AdvancedFilterRequest filterRequest = new AdvancedFilterRequest();
            filterRequest.setCategory(category);
            filterRequest.setSubcategory(subcategory);
            filterRequest.setFabricName(fabricName);
            filterRequest.setColor(color);
            filterRequest.setColorCode(colorCode);
            filterRequest.setMinPrice(minPrice);
            filterRequest.setMaxPrice(maxPrice);

            return advancedFilterProducts(filterRequest, page, size, sortBy, sortDirection);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    // Also add endpoint to get all available filter values
    @GetMapping("/filter-options")
    public ResponseEntity<?> getFilterOptions() {
        try {
            Map<String, Object> filterOptions = new HashMap<>();

            // Get all distinct categories
            List<String> categories = mongoTemplate.findDistinct("category", ProductInventory.class, String.class);
            filterOptions.put("categories", categories);

            // Get all distinct subcategories
            List<String> subcategories = mongoTemplate.findDistinct("subcategory", ProductInventory.class, String.class);
            filterOptions.put("subcategories", subcategories);

            // Get all distinct fabric names
            List<String> fabricNames = mongoTemplate.findDistinct("fabricName", ProductInventory.class, String.class);
            filterOptions.put("fabricNames", fabricNames);

            // Get all distinct colors
            List<String> colors = mongoTemplate.findDistinct("color", ProductInventory.class, String.class);
            filterOptions.put("colors", colors);

            // Get all distinct color codes
            List<String> colorCodes = mongoTemplate.findDistinct("colorCode", ProductInventory.class, String.class);
            filterOptions.put("colorCodes", colorCodes);

            // Get global price range
            Query query = new Query();
            query.fields().include("sizes");
            List<ProductInventory> allProducts = mongoTemplate.find(query, ProductInventory.class);

            double globalMinPrice = allProducts.stream()
                    .flatMap(p -> p.getSizes() != null ? p.getSizes().stream() : java.util.stream.Stream.empty())
                    .mapToDouble(ProductInventory.SizeQuantity::getPrice)
                    .min()
                    .orElse(0);

            double globalMaxPrice = allProducts.stream()
                    .flatMap(p -> p.getSizes() != null ? p.getSizes().stream() : java.util.stream.Stream.empty())
                    .mapToDouble(ProductInventory.SizeQuantity::getPrice)
                    .max()
                    .orElse(0);

            Map<String, Double> priceRange = new HashMap<>();
            priceRange.put("min", globalMinPrice);
            priceRange.put("max", globalMaxPrice);
            filterOptions.put("priceRange", priceRange);

            return ResponseEntity.ok(new ApiResponse<>("Success", filterOptions, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    // Request DTO for advanced filter
    @lombok.Getter
    @lombok.Setter
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class AdvancedFilterRequest {
        private String category;
        private String subcategory;
        private String fabricName;
        private String color;
        private String colorCode;
        private Double minPrice;
        private Double maxPrice;
    }
    @GetMapping("/{productId}/similar-options")
    public ResponseEntity<?> getSimilarProductOptions(
            @PathVariable String productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        try {
            // First, find the original product
            Optional<ProductInventory> originalProductOpt = retailProductRepository.findById(productId);

            if (originalProductOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Product not found with id: " + productId, null, false));
            }

            ProductInventory originalProduct = originalProductOpt.get();

            Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            List<ProductInventory> similarProducts = new ArrayList<>();
            String matchCriteria = "";

            // Priority 1: Try to find products with same DisplayNamesCat AND Fabric (both @DBRef)
            if (originalProduct.getDisplayNamesCat() != null && originalProduct.getFabric() != null) {
                similarProducts = retailProductRepository.findByDisplayNamesCatAndFabric(
                        originalProduct.getDisplayNamesCat(),
                        originalProduct.getFabric(),
                        pageable
                ).getContent();

                // Remove the original product from results
                similarProducts = similarProducts.stream()
                        .filter(product -> !product.getId().equals(productId))
                        .collect(Collectors.toList());

                if (!similarProducts.isEmpty()) {
                    matchCriteria = "Same Display Category and Fabric";
                }
            }

            // Priority 2: If no results from Priority 1, try matching by Category + Subcategory + FabricName
            if (similarProducts.isEmpty()) {
                if (originalProduct.getCategory() != null &&
                        originalProduct.getSubcategory() != null &&
                        originalProduct.getFabricName() != null) {

                    similarProducts = retailProductRepository.findByCategoryAndSubcategoryAndFabricName(
                            originalProduct.getCategory(),
                            originalProduct.getSubcategory(),
                            originalProduct.getFabricName(),
                            pageable
                    ).getContent();

                    // Remove the original product from results
                    similarProducts = similarProducts.stream()
                            .filter(product -> !product.getId().equals(productId))
                            .collect(Collectors.toList());

                    if (!similarProducts.isEmpty()) {
                        matchCriteria = "Same Category, Subcategory and Fabric Name";
                    }
                }
            }

            // Priority 3: Try matching by Category + Subcategory only
            if (similarProducts.isEmpty()) {
                if (originalProduct.getCategory() != null && originalProduct.getSubcategory() != null) {
                    similarProducts = retailProductRepository.findByCategoryAndSubcategory(
                            originalProduct.getCategory(),
                            originalProduct.getSubcategory(),
                            pageable
                    ).getContent();

                    // Remove the original product from results
                    similarProducts = similarProducts.stream()
                            .filter(product -> !product.getId().equals(productId))
                            .collect(Collectors.toList());

                    if (!similarProducts.isEmpty()) {
                        matchCriteria = "Same Category and Subcategory";
                    }
                }
            }

            // Priority 4: Try matching by Category only
            if (similarProducts.isEmpty()) {
                if (originalProduct.getCategory() != null) {
                    similarProducts = retailProductRepository.findByCategory(
                            originalProduct.getCategory(),
                            pageable
                    ).getContent();

                    // Remove the original product from results
                    similarProducts = similarProducts.stream()
                            .filter(product -> !product.getId().equals(productId))
                            .collect(Collectors.toList());

                    if (!similarProducts.isEmpty()) {
                        matchCriteria = "Same Category";
                    }
                }
            }

            // Merge quantities from ProductSets
            similarProducts = mergeSetQuantities(similarProducts);

            // Prepare response with additional information
            Map<String, Object> response = new HashMap<>();
            response.put("originalProduct", originalProduct);
            response.put("similarProducts", similarProducts);
            response.put("matchCriteria", matchCriteria);
            response.put("totalSimilarFound", similarProducts.size());
            response.put("currentPage", page);
            response.put("size", size);

            if (similarProducts.isEmpty()) {
                response.put("message", "No similar products found for this product");
            } else {
                response.put("message", "Found " + similarProducts.size() + " similar products");
            }

            return ResponseEntity.ok(new ApiResponse<>("Success", response, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }




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
