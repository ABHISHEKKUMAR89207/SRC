package com.vtt.repository;



import com.vtt.entities.MaterNumber;
import com.vtt.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MaterNumberRepository extends MongoRepository<MaterNumber, String> {
    boolean existsByMaterNumber(int materNumber);

    MaterNumber findByUser(User user);
}
