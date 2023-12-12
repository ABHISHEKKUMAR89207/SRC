package com.example.jwt.AdminDashboard;

public class AuthenticationResponse {

    private String jwt;

    // Constructors, getters, setters

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
