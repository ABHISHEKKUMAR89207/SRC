package com.vtt.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId")
@Document
public class UserError {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long errorId;
    private String error;
    private Timestamp timestamp;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id") // Assuming this is the foreign key column in UserError table
    @DBRef
    private User user;


}
