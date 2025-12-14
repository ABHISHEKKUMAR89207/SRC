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
@Document(collection = "dicont") // as per your spelling
public class Discount {

    @Id
    private String id;

    @DBRef
    private User user;

    private List<DiscountWithProduct> discountWithProducts;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiscountWithProduct {
        private String productId;
        private double price;
    }
}
