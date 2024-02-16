package com.example.jwt.dtos;

// WaterIntakeResponse.java


public class WaterIntakeResponse {
    private final String title;
    private final String subtitle;

    public WaterIntakeResponse(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }
}
