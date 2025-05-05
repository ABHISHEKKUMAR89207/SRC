package com.vtt.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "fabrics")
@Data
public class Fabric {
    @Id
    private String id;
    private String fabricName;
    private String millFactory;
    private String displayName;
    private double quantityinMeter;
    private double buyingPrice;
    private double wholesalePrice;  // WSP
    private double retailPrice;     // RSP
    private double totalAmount;     // INVOICE Total AMOUNT
    private double paymentDone;
    private Instant createdAt;
    private Instant updatedAt;
}