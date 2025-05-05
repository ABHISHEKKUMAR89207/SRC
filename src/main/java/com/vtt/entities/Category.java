//package com.vtt.entities;
//
//import lombok.*;
//import org.bson.types.ObjectId;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Getter @Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Document(collection = "categories")
//public class Category {
//    @Id
//    private String id;
//    private String categoryName;
//    private LocalDateTime createdTimestamp = LocalDateTime.now();
//
//    public void setSubCategories(List<SubCategory> subCategories) {
//    }
//}