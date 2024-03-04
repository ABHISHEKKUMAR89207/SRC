package com.example.jwt.AdminDashboard;

import lombok.Data;

import java.util.Map;

@Data
public class UsersByWorkLevelDTO {
    private Map<String, Long> usersByWorkLevel;
}
