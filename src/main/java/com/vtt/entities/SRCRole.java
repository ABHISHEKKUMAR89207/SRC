package com.vtt.entities;



import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "src_roles")
@Data
public class SRCRole {
    @Id
    private String id;
    private String name;
}
