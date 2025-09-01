package com.vtt.repository;



import com.vtt.entities.ApplySet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplySetRepository extends MongoRepository<ApplySet, String> {

    // You can add custom query methods here if needed, for example:

    // Find by setName
    ApplySet findBySetName(String setName);
}
