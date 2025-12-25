package com.vtt.dtos;


import com.vtt.entities.ProductInventory.SizeQuantity;
import com.vtt.entities.ProductSets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAllProductDTO {

    private String id;
    private String nameOfProduct;
    private String active;
    private String color;
    private String productImage;
    private String productImag2;
    private String productImag3;
    private String productLocation;
    private List<SizeQuantity> sizes;

    // DisplayNamesCat info
    private String displaynamecatid;
    private String categoryName;
    private String subCategoryName;
    private String productName;
    private String productDescription;
    private String manufacturerName;
    private String websiteName;
    private String imageUrl;
    private int selectdTheme;

    // Fabric info
    private String fabricId;
    private String fabricName;
    private String fabricDisplayName;
    private double buyingPrice;
    private double wholesalePrice;
    private double maximumPrice;
    private double retailPrice;

    // List of ProductSets matching this inventory item
    private List<ProductSetDTO> sets;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductSetDTO {
        private String id;
        private String color;
        private int totalQuantity;
        private String setName;
        private String applySet;
        private List<SizeQuantity> sizes;
    }
}
