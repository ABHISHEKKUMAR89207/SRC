package com.vtt.retail.repository;

import com.vtt.retail.entities.HomeBanner;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HomeBannerRepository extends MongoRepository<HomeBanner, String> {
    List<HomeBanner> findByActiveTrueOrderByDisplayOrderAsc();
    List<HomeBanner> findAllByOrderByDisplayOrderAsc();
}