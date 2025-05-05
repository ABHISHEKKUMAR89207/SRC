package com.vtt.entities;



import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.Date;

@Document(collection = "fabric_history")
@Data
public class FabricHistory {
    @Id
    private String id;

    @DBRef
    private Fabric fabric;  // Reference to main Fabric document using @DBRef

    private String invoiceNo;
    private Date invoiceDate;
    private double invoiceAmount;
    private double quantityinMeter;
    private Double credit;  // can be null
    private Double debit;   // can be null
    private Instant createdAt;
}