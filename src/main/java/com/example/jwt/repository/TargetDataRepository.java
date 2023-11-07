package com.example.jwt.repository;

import com.example.jwt.entities.TargetData;
import com.example.jwt.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;

public interface TargetDataRepository extends CrudRepository<TargetData, Long> {

    TargetData findByUser(User user);

}
