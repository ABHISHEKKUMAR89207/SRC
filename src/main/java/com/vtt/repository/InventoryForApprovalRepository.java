package com.vtt.repository;


import com.vtt.entities.InventoryForApproval;
import com.vtt.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InventoryForApprovalRepository extends MongoRepository<InventoryForApproval, String> {
    List<InventoryForApproval> findByUser(User user);
}
