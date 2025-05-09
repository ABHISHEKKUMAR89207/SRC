package com.vtt.entities;



import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "grouped_roles")
@Data
public class GroupedRole {
    @Id
    private String id;

    @DBRef
    private SRCRole role;

    @DBRef
    private List<SRCRole> downlinkedRoles;
}
