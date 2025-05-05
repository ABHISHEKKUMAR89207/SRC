package com.vtt.dtoforSrc;


import lombok.Data;
import java.util.List;

@Data
public class OrderRequestDTO {
    private String masterNumber;
    private String clientUserId;
    private String category;
    private String subCategory;
    private String displayId;
    private String DisplayName;
    private List<String> fabrics; // Just fabric IDs
    private List<SizeQuantityDTO> sizes;
    private Integer totalQuantity;
    private String status;

    @Data
    public static class SizeQuantityDTO {
        private String label;
        private Integer quantity;
        private Integer completedQuantity;
    }
}