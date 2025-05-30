package com.vtt.repository;



import com.vtt.entities.WorkerKhataBook;
import com.vtt.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WorkerKhataBookRepository extends MongoRepository<WorkerKhataBook, String> {
    List<WorkerKhataBook> findByUser(User user);

    WorkerKhataBook findTopByUserOrderByCreatedAtDesc(User user);
}
