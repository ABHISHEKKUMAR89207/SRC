package com.vtt.dtoforSrc;



import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DisplayNameHierarchyDto {
    private String id;
    private String productName;
    private String productDescription;
    private String manufacturerName;
    private String websiteName;
    private String imageUrl;
    private SubCategoryInfo subCategory;
    private CategoryInfo category;

    @Data
    public static class SubCategoryInfo {
        private String id;
        private String subCategoryName;
        private LocalDateTime createdTimestamp;
    }

    @Data
    public static class CategoryInfo {
        private String id;
        private String categoryName;
        private LocalDateTime createdTimestamp;
    }
}