package com.vtt.dtoforSrc;


import lombok.Data;

import java.time.Instant;

@Data
public class UserDetailsResponse {
    private String id;
    private String profilePictureUrl;
    private String mobileNumber;
    private String panNumber;
    private String aadharNumber;
    private String address;
    private String employmentType;
    private String bankName;
    private String accountNumber;
    private String ifscCode;
    private String upiId;
    private String paytmNumber;
    private String phonePeNumber;
    private Instant createdAt;
    private Instant updatedAt;

    public static UserDetailsResponse fromEntity(com.vtt.entities.UserDetails userDetails) {
        UserDetailsResponse response = new UserDetailsResponse();
        response.setId(userDetails.getId());
        response.setProfilePictureUrl(userDetails.getProfilePictureUrl());
        response.setMobileNumber(userDetails.getMobileNumber());
        response.setPanNumber(userDetails.getPanNumber());
        response.setAadharNumber(userDetails.getAadharNumber());
        response.setAddress(userDetails.getAddress());
        response.setEmploymentType(userDetails.getEmploymentType());
        response.setBankName(userDetails.getBankName());
        response.setAccountNumber(userDetails.getAccountNumber());
        response.setIfscCode(userDetails.getIfscCode());
        response.setUpiId(userDetails.getUpiId());
        response.setPaytmNumber(userDetails.getPaytmNumber());
        response.setPhonePeNumber(userDetails.getPhonePeNumber());
        response.setCreatedAt(userDetails.getCreatedAt());
        response.setUpdatedAt(userDetails.getUpdatedAt());
        return response;
    }
}