package com.example.jwt.service;

import com.example.jwt.entities.UserProfile;
import com.example.jwt.entities.dashboardEntity.FitnessActivity;
import com.example.jwt.exception.ResourceNotFoundException;
import com.example.jwt.repository.FitnessActivityRepository;
import com.example.jwt.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FitnessServiceImpl {
    @Autowired
    private FitnessActivityRepository activityRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;

    // to save fitness activity
    public FitnessActivity saveFitnessActivity(FitnessActivity activity) {
        // Calculate calories burnt and distance covered based on steps and activity type
        double caloriesBurnt = calculateCaloriesBurnt(activity.getActivityType(), activity.getSteps());
        double distanceCovered = calculateDistanceCovered(activity.getActivityType(), activity.getSteps());
        // Update the activity object with calculated values
        activity.setCaloriesBurnt(caloriesBurnt);
        activity.setDistanceCovered(distanceCovered);
        // Save the updated activity to the database
        return activityRepository.save(activity);
    }

    // get all the activities by activity type
    public List<FitnessActivity> getActivitiesByType(String activityType) {
        return activityRepository.findByActivityType(activityType);
    }

    // get all the activities between start time and end time
    public List<FitnessActivity> getActivitiesBetween(LocalDateTime start, LocalDateTime end) {
        return activityRepository.findByTimestampBetween(start, end);
    }

    // to calculate ca;ories burnt based on activity type
    private double calculateCaloriesBurnt(String activityType, int steps) {
        double metValue = 0.0;

        if ("Walking".equalsIgnoreCase(activityType)) {
            metValue = 3.9;
        } else if ("Running".equalsIgnoreCase(activityType)) {
            metValue = 7.0;
        }

        double caloriesPerMinute = metValue * (steps / 120.0);

        return caloriesPerMinute;
    }

    // to calculate the distance covered by the user based on steps
    private double calculateDistanceCovered(String activityType, int steps) {
        double strideLength = 0.0;

        if ("Walking".equalsIgnoreCase(activityType)) {
            strideLength = 0.76; // Example stride length for walking in meters
        } else if ("Running".equalsIgnoreCase(activityType)) {
            strideLength = 1.2; // Example stride length for running in meters
        }

        // Calculate distance covered
        double distanceMeters = steps * strideLength;
        double distanceKilometers = distanceMeters / 1000.0;

        return distanceKilometers;
    }

    // to calculate th BMI(Body Mass Index)
    private double calculateBMI(UserProfile userProfile) {
        if (userProfile != null) {
            double heightInMeters = userProfile.getHeight() / 100; // Convert height to meters
            return userProfile.getWeight() / (heightInMeters * heightInMeters);
        } else {
            return 0.0;
        }
    }

    // to calculate and save the BMI
    public UserProfile calculateAndSaveBMI(Long userProfileId) {
        Optional<UserProfile> userProfileOptional = userProfileRepository.findById(userProfileId);

        if (userProfileOptional.isPresent()) {
            UserProfile userProfile = userProfileOptional.get();
            double bmi = calculateBMI(userProfile);
            userProfile.setBmi(bmi);
            return userProfileRepository.save(userProfile);
        } else {
            throw new ResourceNotFoundException("UserProfile");
        }
    }
}