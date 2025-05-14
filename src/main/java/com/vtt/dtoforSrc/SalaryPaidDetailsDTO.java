package com.vtt.dtoforSrc;



import lombok.Data;
import java.time.Instant;

@Data
public class SalaryPaidDetailsDTO {
    private String userId;
    private double totalAmountPaid;
    private String dateOfPayment;
    private String notes;
    private String paymentReferenceNumber;


}
