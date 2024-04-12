package com.example.jwt.AdminDashboard;

import lombok.Data;

@Data
public class AverageDTOs {
    private String average;
    private String ageGroup;
    private String gender;
    private long userCount;  // New field for user count

//    public SleepDurationStatsDTO(String averageWater, String ageGroup, String gender, int userCount) {
//        this.average = averageWater;
//        this.ageGroup = ageGroup;
//        this.gender = gender;
//        this.userCount = userCount;
//    }
public AverageDTOs() {
    // no-arg constructor
}
    public AverageDTOs(String average, String ageGroup, String gender, long userCount) {
        this.average = average;
        this.ageGroup = ageGroup;
        this.gender = gender;
        this.userCount = userCount;
    }
}

