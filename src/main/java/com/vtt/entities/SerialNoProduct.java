package com.vtt.entities;

import lombok.Data;
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
    private String commonFabricName;
    private String commonArticle;
    private List<DisplayWithSizes> displayNamesList;

    private Instant createdAt;
    private Instant updatedAt;

    @Data
    public static class DisplayWithSizes {
        @DBRef
        private DisplayNamesCat displayNameCat;
        private double price;
        private List<SizeCompleted> sizes;
    }

    @Data
    public static class SizeCompleted {
        private String sizeName;
        private int quantity;
    }
}
