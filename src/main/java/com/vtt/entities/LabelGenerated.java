package com.vtt.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "label_generated")
@Data
public class LabelGenerated {
    @Id
    private String id;
    private String labelNumber;
    private String masterNumber;
    private String clientUserId;
    private String category;
    private String subCategory;
    private String displayId;
    private String displayName;  // Corrected "DisplayName" to "displayName" for consistency

    @DBRef
    private Order orderReference;  // Reference to the original Order (optional but helpful)

    private List<LabelFabric> fabrics;
    private List<SizeCompleted> sizes;

    private List<UserWorkAssign> users;

    private int totalQuantity;
    private String status; // created, in-progress, completed, etc.
    private Instant createdAt;
    private Instant updatedAt;

    @Data
    public static class LabelFabric {
        @DBRef
        private Fabric fabric;  // Reference to Fabric
        private double usedQuantity;  // How much fabric used
        private String color;          // Color used
    }

    @Data
    public static class SizeCompleted {
        private String sizeName;  // Size label (example: S, M, L, XL)
        private int quantity;     // Quantity for this label
    }

    @Data
    public static class UserWorkAssign {
        @Id
        private String id;  // MongoDB will generate this
        @DBRef
        private User user;
        private String workAssigned;
        private boolean status;
    }
}
