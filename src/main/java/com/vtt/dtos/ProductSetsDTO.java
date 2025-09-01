package com.vtt.dtos;


import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSetsDTO {

    private String color;
    private int totalQuantity;
    private String setName;
    private String applySet;

    private String displayNamesCatId; // just the ID
    private String fabricId;          // just the ID


}
