package com.example.jwt.AdminDashboard;

import lombok.Data;

@Data
public class ActivitiesStatsDTO {
    private String averageCalories;
    private String ageGroup;
    private String gender;
    private long userCount;
    private long activeUsers;
    private long activityCount;  // New field for activity count

}
