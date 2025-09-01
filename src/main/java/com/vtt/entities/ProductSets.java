
package com.vtt.entities;

import lombok.*;
        import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "productSets")
public class ProductSets {

    @Id
    private String id;

    private String color;
    private int totalQuantity;
    private String setName;
    private String applySet;


    // List of sizes and their quantities
    private List<SizeQuantity> sizes;

    @DBRef
    private DisplayNamesCat displayNamesCat;

    // Add a reference to the Fabric entity
    @DBRef
    private Fabric fabric;

    // Inner class to hold size and quantity
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SizeQuantity {
        private String label; // Size label (e.g., "M", "L", "XL")
        private int quantity; // Available quantity for that size
    }

}
