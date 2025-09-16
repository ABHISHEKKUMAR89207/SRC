package com.vtt.entities;



import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "cart")
public class Cart {

    @Id
    private String id;

    @DBRef
    private User user;  // Link to the user

    private List<CartProductItem> products; // List of product inventory items with selected sizes and quantities

    private List<CartSetItem> sets;         // List of product sets with quantities

    // Inner class to represent each product in the cart
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartProductItem {
        @DBRef
        private ProductInventory productInventory; // Reference to the product

        private List<SizeQuantity> selectedSizes; // Selected sizes and quantities

        // Inner class for size and quantity
        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class SizeQuantity {
            private String label;
            private int quantity;
        }
    }

    // Inner class to represent each set in the cart
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartSetItem {
        @DBRef
        private ProductSets productSet; // Reference to the set

        private int quantity; // Quantity of this set in the cart
    }
}
