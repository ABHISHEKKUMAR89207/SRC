package com.vtt.repository;

import com.vtt.entities.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepo extends MongoRepository<Role,Integer> {
}
