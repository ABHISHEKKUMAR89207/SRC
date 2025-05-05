package com.vtt.repository;


import com.vtt.entities.Fabric;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FabricRepository extends MongoRepository<Fabric, String> {
    // Custom query methods can be added here
    Fabric findByFabricName(String fabricName);
    List<Fabric> findByMillFactory(String millFactory);
}