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
@Document(collection = "category_pricing")
public class CategoryPricing {

    @Id
    private String id;

    private String category;
    private String subCategory;

    @DBRef
    private List<RoleWithPrice> rolePrices;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoleWithPrice {
        @DBRef
        private SRCRole role;
        private double price;
    }
}
