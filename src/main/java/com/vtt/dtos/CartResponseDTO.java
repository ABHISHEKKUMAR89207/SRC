package com.vtt.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDTO {

    private String cartId;
    private String userId;
    private List<ProductItemResponse> products;
    private List<SetItemResponse> sets;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductItemResponse {
        private String productInventoryId;
        private List<SizeQuantityResponse> selectedSizes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SizeQuantityResponse {
        private String label;
        private int quantity;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SetItemResponse {
        private String productSetId;
        private int quantity;
    }
}
