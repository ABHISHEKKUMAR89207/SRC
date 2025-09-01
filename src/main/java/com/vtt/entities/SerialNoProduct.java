package com.vtt.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Document(collection = "serialnoProduct")
public class SerialNoProduct {

    @Id
    private String id;

    private String referredLabelNumber;


    @DBRef
    private LabelGenerated labelGenerated;

    @DBRef
    private DisplayNamesCat defaultDisplayNameCat;
    private double commonMRP;
    private String commonColor;
    private String commonFabricId;
    private String commonFabricName;
    private String commonArticle;
    private List<DisplayWithSizes> displayNamesList;

    private Instant createdAt;
    private Instant updatedAt;
    private List<SizeSet> setAvailable;
    @Data
    public static class DisplayWithSizes {
        @DBRef
        private DisplayNamesCat displayNameCat;
        private double price;
        private String seprateColor;
        private List<SizeCompleted> sizes;
        private List<SizeSet> seprateSetAvailable;
    }

    @Data
    public static class SizeCompleted {
        private String sizeName;
        private int quantity;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SizeSet {
        private String setId;
        private String setName;
        private int setQuantity;
        // List of sizes and their quantities
        private List<SizeQuantity> sizes;

        // Inner class to hold size and quantity
        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class SizeQuantity {
            private String label;   // Size label (e.g., "M", "L", "XL")
            private int quantity;   // Available quantity for that size
        }
    }
}
