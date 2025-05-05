package com.vtt.repository;


import com.vtt.entities.DisplayNamesCat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisplayNamesCatRepository extends MongoRepository<DisplayNamesCat, String> {
}
