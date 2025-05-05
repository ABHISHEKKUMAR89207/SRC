package com.vtt.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.List;

@Document(collection = "orders")
@Data
public class Order {
    @Id
    private String id;
    private String masterNumber;
    private String clientUserId;
    private String category;
    private String subCategory;
    private String displayId;
    private String DisplayName;

    @DBRef
    private List<Fabric> fabrics;

    private List<SizeQuantity> sizes;
    private int totalQuantity;
    private String status; // created, in-progress, completed, etc.
    private Instant createdAt;
    private Instant updatedAt;

    @Data
    public static class SizeQuantity {
        private String label;
        private int quantity;
        private int completedQuantity;
    }
}