package com.example.jwt.AdminDashboard;

import lombok.Data;

@Data
public class SleepDurationStatsDTO {
    private double averageDuration;
    private String ageGroup;
    private String gender;
    private long userCount;  // New field for user count


}

