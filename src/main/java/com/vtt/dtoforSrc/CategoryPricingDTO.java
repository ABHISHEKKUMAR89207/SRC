package com.vtt.dtoforSrc;

import lombok.Data;

import java.util.List;

@Data
public class CategoryPricingDTO {
    private String category;
    private String subCategory;
    private List<RolePriceDTO> rolePrices;

    @Data
    public static class RolePriceDTO {
        private String roleId;
        private double price;
    }
}
