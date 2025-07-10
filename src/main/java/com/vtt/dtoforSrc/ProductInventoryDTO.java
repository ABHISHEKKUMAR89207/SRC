package com.vtt.dtoforSrc;


import lombok.Data;
import java.util.List;

@Data
public class ProductInventoryDTO {

    private String color;
    private List<SizeQuantityDTO> sizes;
    private String displayNamesCatId;  // For DBRef link to DisplayNamesCat
    private String fabricId;
    private String productLocation;// For DBRef link to Fabric

    @Data
    public static class SizeQuantityDTO {
        private String label; // Size label (e.g., "M", "L", "XL")
        private int quantity; // Available quantity for that size
    }
}
