package com.vtt.dtos;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartRequestDTO {

    private String userId;

    private List<ProductItemDTO> products;

    private List<SetItemDTO> sets;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductItemDTO {
        private String productInventoryId;
        private List<SizeQuantityDTO> selectedSizes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SizeQuantityDTO {
        private String label;
        private int quantity;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SetItemDTO {
        private String productSetId;
        private int quantity;
    }
}
