package com.example.jwt.dtos;


import com.example.jwt.entities.dashboardEntity.Activities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivitiesDTO {

    private Long id;
    private String activityType;
    private Long runningDurationInSeconds;
    private Long joggingDurationInSeconds;

    // Getters and setters for the above properties

    public static ActivitiesDTO fromActivities(Activities activities) {
        ActivitiesDTO dto = new ActivitiesDTO();
        dto.setId(activities.getId());
        dto.setActivityType(activities.getActivityType());
        dto.setRunningDurationInSeconds(activities.getRunningDurationInSeconds());
        dto.setJoggingDurationInSeconds(activities.getJoggingDurationInSeconds());
        return dto;
    }
}
