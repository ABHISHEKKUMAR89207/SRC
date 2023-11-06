package com.example.jwt.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data

public class Role {

    @Id
    //  @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String name;
}
