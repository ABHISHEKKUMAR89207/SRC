//package com.vtt.dtoforSrc;
//
//
//import lombok.Data;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Data
//public class CategoryHierarchyDto {
//    private String id;
//    private String categoryName;
//    private LocalDateTime createdTimestamp;
//    private List<SubCategoryDto> subCategories;
//
//    @Data
//    public static class SubCategoryDto {
//        private String id;
//        private String subCategoryName;
//        private LocalDateTime createdTimestamp;
//        private List<DisplayNamesCatDto> displayNames;
//    }
//
//    @Data
//    public static class DisplayNamesCatDto {
//        private String id;
//        private String productName;
//        private String productDescription;
//        private String manufacturerName;
//        private String websiteName;
//        private String imageUrl;
//    }
//}