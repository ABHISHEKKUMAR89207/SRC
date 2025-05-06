package com.vtt.repository;
import com.vtt.entities.DisplayNamesCat;
import com.vtt.entities.Fabric;
import com.vtt.entities.ProductInventory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductInventoryRepository extends MongoRepository<ProductInventory, String> {
    Optional<ProductInventory> findByColorAndDisplayNamesCatAndFabric(String color, DisplayNamesCat displayNamesCat, Fabric fabric);
}