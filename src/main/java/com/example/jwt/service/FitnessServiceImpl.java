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

//    private UserProfileRepository userProfileRepository;

//    public FitnessActivity saveFitnessActivity(FitnessActivity activity) {
//        // Calculate calories burnt and distance covered based on steps and activity type
//        // You can implement your own logic here
//
//        return activityRepository.save(activity);
//    }

    public FitnessActivity saveFitnessActivity(FitnessActivity activity) {
        // Calculate calories burnt and distance covered based on steps and activity type
        double caloriesBurnt = calculateCaloriesBurnt(activity.getActivityType(), activity.getSteps());
        double distanceCovered = calculateDistanceCovered(activity.getActivityType(), activity.getSteps());

        // Update the activity object with calculated values
        activity.setCaloriesBurnt(caloriesBurnt);
        activity.setDistanceCovered(distanceCovered);

//        calculateBMI(userProfile);


        // Calculate and set BMI based on the associated UserProfile
//        double bmi = calculateBMI(activity.getUserProfile());
//        activity.setCaloriesBurnt(caloriesBurnt);
//        activity.setDistanceCovered(distanceCovered);
//        activity.setBmi(bmi); // Set BMI in the FitnessActivity entity


        // Save the updated activity to the database
        return activityRepository.save(activity);
    }


//    public UserProfile saveBMI(UserProfile userProfile, Long profileId)
//    {
//        UserProfile userProfile1=this.userProfileRepository.findById(profileId)
//                .orElseThrow(()->new ResourceNotFoundException("User Profile","UserProfile id",profileId));
//
//        calculateBMI(userProfile);
//
//        return userProfileRepository.save(userProfile) ;
//    }


    public List<FitnessActivity> getActivitiesByType(String activityType) {
        return activityRepository.findByActivityType(activityType);
    }

    public List<FitnessActivity> getActivitiesBetween(LocalDateTime start, LocalDateTime end) {
        return activityRepository.findByTimestampBetween(start, end);
    }



        private double calculateCaloriesBurnt(String activityType, int steps) {
        // Implement your logic to calculate calories burnt based on activity type and steps
        // You may use MET values for different activities
        // Example: For walking, MET = 3.9; for running, MET = 7.0

        double metValue = 0.0;

        if ("Walking".equalsIgnoreCase(activityType)) {
            metValue = 3.9;
        } else if ("Running".equalsIgnoreCase(activityType)) {
            metValue = 7.0;
        }

        // Calories burnt per minute calculation (adjust for your use case)
        double caloriesPerMinute = metValue * (steps / 120.0);

        return caloriesPerMinute;
    }




    private double calculateDistanceCovered(String activityType, int steps) {
        // Implement your logic to calculate distance covered based on activity type and steps
        // You may use average stride length for different activities

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


    private double calculateBMI(UserProfile userProfile) {
        if (userProfile != null) {
            double heightInMeters = userProfile.getHeight() / 100; // Convert height to meters
            return userProfile.getWeight() / (heightInMeters * heightInMeters);
        } else {
            return 0.0; // Handle the case where userProfile is null
        }
    }

//    public UserProfile saveBMI(UserProfile userProfile)
//    {
//        calculateBMI(userProfile);
//
//        return userProfileRepository.save(userProfile) ;
//    }
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




//    public double calculateBMI(FitnessActivity fitnessActivity) {
//        UserProfile userProfile = fitnessActivity.getUserProfile();
//
//        if (userProfile != null) {
//            double height = userProfile.getHeight();
//            double weight = userProfile.getWeight();
//
//            // Calculate BMI (e.g., using the BMI formula)
//            double bmi = calculateBMI(height, weight);
//            return calculateBMI(height, weight);
//            // Set the calculated BMI in the FitnessActivity
////            fitnessActivity.setBmi(bmi);
//        }
//
//        return 0.0;  // Or handle the case when UserProfile is null
//    }

    // Define a method to calculate BMI based on height and weight
//    private double calculateBMI(double height, double weight) {
//        // Calculate BMI using the formula: BMI = weight (kg) / (height (m) * height (m))
//        return weight / (height * height);
//    }
}

