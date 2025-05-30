package com.vtt.entities;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "worker_khatabook")
@Data
public class WorkerKhataBook {

    @Id
    private String id;

    @DBRef
    private User user; // Reference to the User document

    private double amount;
    private double Balance;

    private String type; // "credit" or "debit"

    private String note; // Optional note.
    private String date; //dd-mm-yyyy

    @CreatedDate
    private Instant createdAt;  // Date when this record was created

    @LastModifiedDate
    private Instant updatedAt;  // Date when this record was last updated
}
