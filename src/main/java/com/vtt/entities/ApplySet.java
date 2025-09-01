package com.vtt.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "applyset")
public class ApplySet {

    @Id
    private String id;

    private String setName;

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
