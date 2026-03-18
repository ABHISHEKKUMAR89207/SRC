package com.vtt.retail.repository;

import com.vtt.entities.ProductInventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RetailProductRepository extends MongoRepository<ProductInventory, String> {

    // Find products by category
    Page<ProductInventory>findByCategory(String category, Pageable pageable);

    // Find products by subcategory
    Page<ProductInventory>findBySubcategory(String subcategory, Pageable pageable);

    // Find products by color
    Page<ProductInventory>findByColor(String color, Pageable pageable);

    // Find products by category and subcategory
    Page<ProductInventory>findByCategoryAndSubcategory(String category, String subcategory, Pageable pageable);

    // Find products by category, subcategory, and color
    Page<ProductInventory>findByCategoryAndSubcategoryAndColor(String category, String subcategory, String color, Pageable pageable);

    // Find products by price range - query method for custom logic
    @Query("{ 'sizes.price': { $gte: ?0, $lte: ?1 } }")
    Page<ProductInventory>findByPriceRange(Double minPrice, Double maxPrice, Pageable pageable);

    // Find products by size availability
    @Query("{ 'sizes.label': ?0 }")
    Page<ProductInventory>findBySize(String size, Pageable pageable);

    // Find products by size, color, and price range
    @Query("{ 'sizes.label': ?0, 'color': ?1, 'sizes.price': { $gte: ?2, $lte: ?3 } }")
    Page<ProductInventory>findBySizeColorAndPrice(String size, String color, Double minPrice, Double maxPrice, Pageable pageable);

    // Find trending products (top 10 by sales)
    List<ProductInventory>findTop10ByOrderByTotalSalesDesc();

    // Find latest arrived products (top 10 by arrival date)
    List<ProductInventory>findTop10ByOrderByLastedArrivedAtDesc();

    // Find all available sizes for a category
    @Query(value = "{ 'category': ?0 }", fields = "{ 'sizes.label': 1 }")
    List<ProductInventory>findAllSizesByCategory(String category);

    // Find all colors for a category
    @Query(value = "{ 'category': ?0 }", fields = "{ 'color': 1 }")
    List<ProductInventory>findAllColorsByCategory(String category);

    // Find by active status
    List<ProductInventory>findByActive(String active);

    // Search by product name
    Page<ProductInventory>findByNameOfProductContainingIgnoreCase(String name, Pageable pageable);

    // Find by fabric name
    Page<ProductInventory>findByFabricName(String fabricName, Pageable pageable);

    // Complex query for all filters
    @Query("{ 'category': ?0, 'subcategory': ?1, 'color': ?2, 'sizes.label': ?3, 'sizes.price': { $gte: ?4, $lte: ?5 } }")
    Page<ProductInventory>findWithAllFilters(String category, String subcategory, String color, String size, Double minPrice, Double maxPrice, Pageable pageable);
}
