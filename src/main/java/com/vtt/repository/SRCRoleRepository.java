package com.vtt.repository;



import com.vtt.entities.SRCRole;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SRCRoleRepository extends MongoRepository<SRCRole, String> {
    Optional<SRCRole> findByName(String name);
}
