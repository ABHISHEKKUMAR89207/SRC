package com.vtt.repository;



import com.vtt.entities.CategoryPricing;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryPricingRepository extends MongoRepository<CategoryPricing, String> {
    List<CategoryPricing> findAllByCategoryAndSubCategory(String category, String subCategory);

    Optional<CategoryPricing> findByCategoryAndSubCategory(String category, String subCategory);
}
