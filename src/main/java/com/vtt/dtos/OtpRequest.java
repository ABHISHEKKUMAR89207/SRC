package com.vtt.dtos;

public class OtpRequest {
    private String mobileNo; // Phone number to which the OTP was sent
    private String otp; // OTP entered by the user

    // Getters and setters
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
