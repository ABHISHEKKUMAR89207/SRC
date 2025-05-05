package com.vtt.repository;


import com.vtt.entities.DisplayNamesCat;
import com.vtt.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisplayNamesCatRepository extends MongoRepository<DisplayNamesCat, String> {
    List<DisplayNamesCat> findByUser(User user);
}
