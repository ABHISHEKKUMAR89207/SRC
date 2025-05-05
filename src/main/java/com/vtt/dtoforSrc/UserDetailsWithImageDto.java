package com.vtt.dtoforSrc;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;



@Data
public class UserDetailsWithImageDto {
    private MultipartFile profilePicture;

    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$", message = "Invalid PAN format")
    private String panNumber;

    @Pattern(regexp = "^[0-9]{12}$", message = "Aadhar number must be 12 digits")
    private String aadharNumber;

    private String address;

    @NotBlank(message = "Employment type is required")
    private String employmentType; // SALARIED or WAGES

    // Bank Details
    private String bankName;
    private String accountNumber;
    private String ifscCode;
    private String upiId;

    // Wallet Details
    private String paytmNumber;
    private String phonePeNumber;
}