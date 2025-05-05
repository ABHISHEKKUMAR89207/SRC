package com.vtt.repository;




import com.vtt.entities.SerialNoProduct;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SerialNoProductRepository extends MongoRepository<SerialNoProduct, String> {
    Optional<SerialNoProduct> findByReferredLabelNumber(String referredLabelNumber);
}
