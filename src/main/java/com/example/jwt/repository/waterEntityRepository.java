package com.example.jwt.repository;
import com.example.jwt.entities.User;
import com.example.jwt.entities.waterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface waterEntityRepository extends JpaRepository<waterEntity, Long> {


    waterEntity findByUser(User user);

}
