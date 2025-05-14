package com.vtt.repository;


import com.vtt.entities.LabelGenerated;
import com.vtt.entities.Order;
import com.vtt.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface LabelGeneratedRepository extends MongoRepository<LabelGenerated, String> {
    boolean existsByLabelNumber(String labelNumber);

    List<LabelGenerated> findByOrderReference(Order order);

    Optional<LabelGenerated> findByLabelNumber(String labelNumber);

    List<LabelGenerated> findByCreatedAtBetween(Instant startOfDay, Instant endOfDay);

    LabelGenerated findByMasterNumber(String masterNumber);

    List<LabelGenerated> findByUsersUserAndCreatedAtBetween(User user, Instant start, Instant end);

}
