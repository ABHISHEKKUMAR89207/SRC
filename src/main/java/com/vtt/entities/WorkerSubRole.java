package com.vtt.entities;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "worker_sub_roles")
public class WorkerSubRole {
    @Id
    private String id;
    private String name;
    private String description;
    private boolean active = true;
    private LocalDateTime createdAt = LocalDateTime.now();
}