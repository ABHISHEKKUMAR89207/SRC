package com.vtt.dtoforSrc;


import lombok.Data;

@Data
public class KhataBookRequestDTO {
    private String userId;
    private double amount;
    private String type; // "credit" or "debit"
    private String note;
    private String date; //dd-mm-yyyy

}
