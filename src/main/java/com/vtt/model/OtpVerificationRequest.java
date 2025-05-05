package com.vtt.model;

import lombok.Data;

@Data
public class OtpVerificationRequest {
    private String mobileNo;
    private String otp;
}