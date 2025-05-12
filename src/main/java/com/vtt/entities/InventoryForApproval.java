package com.vtt.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "inventoryForApproval")
public class InventoryForApproval {

    @Id
    private String id;

    private String color;

    // List of sizes and their quantities
    private List<SizeQuantity> sizes;

    @DBRef
    private DisplayNamesCat displayNamesCat;

    @DBRef
    private Fabric fabric;

    // Field to indicate if the inventory is approved or not
    private boolean approved;

    // Reference to the user who submitted this inventory
    @DBRef
    private User user;

    // Inner class to hold size and quantity
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SizeQuantity {
        private String label; // Size label (e.g., "M", "L", "XL")
        private int quantity; // Available quantity for that size
    }
}
