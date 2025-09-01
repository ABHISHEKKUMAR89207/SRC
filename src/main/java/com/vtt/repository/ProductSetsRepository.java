package com.vtt.repository;



import com.vtt.entities.DisplayNamesCat;
import com.vtt.entities.Fabric;
import com.vtt.entities.ProductSets;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductSetsRepository extends MongoRepository<ProductSets, String> {
    Optional<Object> findByApplySet(String applySet);

    List<ProductSets> findByFabricAndDisplayNamesCatAndColor(Fabric fabric, DisplayNamesCat displayNamesCat, String color);
    // You can add custom queries here if needed
}

