package com.vtt.dtoforSrc;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterNumberDTO {
    private String id;          // MaterNumber id
    private int materNumber;    // 9 digit number
    private String userName;    // User name
    private String mobileNo;    // User mobile number
    private String userId;      // User id
}
