package com.example.jwt.registration;


public record RegistrationRequest(
         // String firstName,
         // String lastName,
         String userName,
         String mobileNo,
         String email,
         String password) {
}
