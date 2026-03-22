package com.vtt.repository;
import com.vtt.entities.DisplayNamesCat;
import com.vtt.entities.Fabric;
import com.vtt.entities.ProductInventory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductInventoryRepository extends MongoRepository<ProductInventory, String> {
    Optional<ProductInventory> findByColorAndDisplayNamesCatAndFabric(String color, DisplayNamesCat displayNamesCat, Fabric fabric);

    ProductInventory findFirstByFabricAndDisplayNamesCatAndColor(Fabric fabric, DisplayNamesCat displayNamesCat, String color);
    Optional<ProductInventory> findByColorAndArticleNameAndDisplayNamesCatAndFabric(
            String color,
            String articleName,
            DisplayNamesCat displayNamesCat,
            Fabric fabric
    );
    @Query("{ 'isOurBrand': { $ne: true } }")
    List<ProductInventory> findAllWhereIsOurBrandNotTrue();
}