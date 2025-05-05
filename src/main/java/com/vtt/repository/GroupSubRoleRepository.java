package com.vtt.repository;


import com.vtt.entities.GroupSubRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupSubRoleRepository extends MongoRepository<GroupSubRole, String> {
    GroupSubRole findByRoleName(String roleName);
}
