package com.example.jwt.repository;


import com.example.jwt.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepo extends JpaRepository<Role,Integer> {


}
