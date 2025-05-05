//package com.vtt.entities;
//
//import lombok.*;
//import org.bson.types.ObjectId;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.DBRef;
//import org.springframework.data.mongodb.core.mapping.Document;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Getter @Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Document(collection = "subcategories")
//public class SubCategory {
//    @Id
//    private String id;
//    private String subCategoryName;
//    private LocalDateTime createdTimestamp = LocalDateTime.now();
//
//    @DBRef
//    private Category category;
//
//    public void setDisplayNames(List<DisplayNamesCat> displayNames) {
//    }
//}