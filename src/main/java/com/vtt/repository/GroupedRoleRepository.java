package com.vtt.repository;



import com.vtt.entities.GroupedRole;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupedRoleRepository extends MongoRepository<GroupedRole, String> {
}
