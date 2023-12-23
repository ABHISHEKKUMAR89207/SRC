package com.example.jwt.registration;

public record RegistrationRequest(
         String userName,
         String mobileNo,
         String email,
         String password,
         String deviceType,
         Double latitude,
         Double longitude,
         boolean registrationTermCondition) {
}
