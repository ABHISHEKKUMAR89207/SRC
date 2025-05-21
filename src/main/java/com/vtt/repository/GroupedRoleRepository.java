package com.vtt.repository;



import com.vtt.entities.GroupedRole;
import com.vtt.entities.SRCRole;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GroupedRoleRepository extends MongoRepository<GroupedRole, String> {
    List<GroupedRole> findByRole(SRCRole inputRole);
}
