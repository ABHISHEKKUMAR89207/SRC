package com.vtt.retail.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "homebanners")
public class HomeBanner {

    @Id
    private String id;

    private String imageUrl;
    private String title;
    private String subtitle;
    private String buttonText;   // e.g. "Shop Now"
    private String linkUrl;      // e.g. "/retail/shop?category=Men"

    private Integer displayOrder;  // 1-5, controls carousel order
    private Boolean active = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}