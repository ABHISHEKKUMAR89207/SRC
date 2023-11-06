package com.example.jwt.model;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class JwtResponse {

    private String jwtToken;
    private String username;
    private String userId;

}
