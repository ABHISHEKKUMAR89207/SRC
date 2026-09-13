package com.vtt.repository;

import com.vtt.entities.SizeChart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for SizeChart entity operations.
 * Provides CRUD operations and custom finder methods for size charts.
 */
@Repository
public interface SizeChartRepository extends MongoRepository<SizeChart, String> {

    /**
     * Find a size chart by product ID.
     * Each product can have at most one size chart.
     *
     * @param productId the product ID to search for
     * @return Optional containing the SizeChart if found, or empty if not
     */
    Optional<SizeChart> findByProductId(String productId);

    /**
     * Delete a size chart by product ID.
     * Used when removing a chart from a product.
     *
     * @param productId the product ID whose chart should be deleted
     */
    void deleteByProductId(String productId);

    /**
     * Check if a size chart exists for a given product.
     *
     * @param productId the product ID to check
     * @return true if a size chart exists for the product, false otherwise
     */
    boolean existsByProductId(String productId);
}