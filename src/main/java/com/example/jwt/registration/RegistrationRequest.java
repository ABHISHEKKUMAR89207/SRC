package com.example.jwt.registration;

public record RegistrationRequest(
         String userName,
         String mobileNo,
         String email,
         String password,
         boolean registrationTermCondition) {
}
