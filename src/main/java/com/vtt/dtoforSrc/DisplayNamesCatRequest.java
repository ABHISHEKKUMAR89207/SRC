package com.vtt.dtoforSrc;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DisplayNamesCatRequest {
    private String categoryName;
    private String subCategoryName;
    private String productName;
    private String productDescription;
    private String manufacturerName;
    private String websiteName;
    private String imageUrl;
}