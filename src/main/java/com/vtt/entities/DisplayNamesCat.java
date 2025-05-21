package com.vtt.entities;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "displaynamescat")
public class DisplayNamesCat {



    @Id
    private String id;



    private String categoryName;
    private String subCategoryName;
    private String productName;
    private String productDescription;
    private String manufacturerName;
    private String websiteName;
    private String imageUrl;
    private int selectdTheme;
    @DBRef
    private User user;



}