package com.vtt.dtoforSrc;

import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderDTO {

    private List<ProductEntryDTO> productEntries;

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductEntryDTO {
        private String productInventoryId;
        private List<OrderedSizeQuantityDTO> orderedSizes;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderedSizeQuantityDTO {
        private String label;
        private int quantity;
    }
}
