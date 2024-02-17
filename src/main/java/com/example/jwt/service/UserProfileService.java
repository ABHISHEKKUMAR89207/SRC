package com.example.jwt.service;

import com.example.jwt.entities.User;
import com.example.jwt.entities.UserProfile;
import com.example.jwt.exception.ResourceNotFoundException;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserProfileRepository;
import com.example.jwt.repository.UserRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Optional;

@Service
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public Iterable<UserProfile> getAllUserProfiles() {
        return userProfileRepository.findAll();
    }

    public Optional<UserProfile> getUserProfileById(Long id) {
        return userProfileRepository.findById(id);
    }



    public UserProfile saveOrUpdateVariableType(String username, String variableType) {
        // Fetch the UserProfile based on the userId
        Optional<UserProfile> userProfileOptional = Optional.ofNullable(userProfileRepository.findByUserEmail(username));

        UserProfile userProfile = userProfileOptional.orElseThrow(() ->
                new UserNotFoundException("User with email " + username + " not found"));

        // Update the variableType
        userProfile.setVariableType(variableType);

        // Save and return the updated UserProfile
        return userProfileRepository.save(userProfile);
    }


    // to create the user profile
    public UserProfile createUserProfile(UserProfile userProfile, Long userId) throws ParseException {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Profile"));

        // Set the user reference in the UserProfile
        userProfile.setUser(user);

        // Calculate and set the BMI
        double heightInInches = userProfile.getHeightFt() * 12 + userProfile.getHeightIn();
        double heightInDecimal = heightInInches / 12.0; // Convert total inches to feet in decimal format
         System.out.println("height in decimal .........."+heightInDecimal);
        double bmi = calculateBMI(userProfile.getGender(),userProfile.getHeightFt(),userProfile.getHeightIn(), userProfile.getWeight());
        userProfile.setBmi(bmi);
        UserProfile newProfile = this.userProfileRepository.save(userProfile);
        return newProfile;
    }

    // to calculate the BMI of the user
//    private double calculateBMI(double heightInFeet, double weight) {
//        DecimalFormat decimalFormat = new DecimalFormat("#.##");
//        String str = String.valueOf(heightInFeet);
//        String[] parts = str.split("\\.");
//        double befDecimal = Double.parseDouble(parts[0]);
//        double aftDecimal =Double.parseDouble(parts[1]);
//
//        double heightInInches = (befDecimal*12) + aftDecimal;
//        double heightInMeters = heightInInches * 0.0254; // Convert height to meters
//        double bmis = weight / (heightInMeters * heightInMeters);
//        String formatedBmi = decimalFormat.format(bmis);
//        return Double.parseDouble(formatedBmi);
//    }

//    private double calculateBMI(String gender, double heightInFeet, double weight) {
//        // Convert height from feet to meters
//        double heightInMeters = heightInFeet * 0.3048; // 1 foot = 0.3048 meters
//
//        // Calculate BMI based on gender
//        double bmi;
//        if (gender.equalsIgnoreCase("male")) {
//            bmi = weight / (heightInMeters * heightInMeters);
//        } else if (gender.equalsIgnoreCase("female")) {
//            // Adjusted calculation for females
//            bmi = (weight / (heightInMeters * heightInMeters)) * 1.07 - (148 * (weight / heightInMeters)) + 4.5;
//        } else {
//            // Default to a generic BMI calculation if gender is not specified or recognized
//            bmi = weight / (heightInMeters * heightInMeters);
//        }
//        return bmi;
//    }

//    public double calculateBMI(String gender, double heightInFeet, double weight) {
//        // Convert height from feet to centimeters
//        double heightInCM = heightInFeet * 30.48; // 1 foot = 30.48 centimeters
//
//        // Convert height from centimeters to meters for BMI calculation
//        double heightInMeters = heightInCM / 100; // Convert centimeters to meters
//
//        // Calculate BMI based on gender
//        double bmi;
//        if (gender.equalsIgnoreCase("male")) {
//            bmi = weight / (heightInMeters * heightInMeters);
//        } else if (gender.equalsIgnoreCase("female")) {
//            // Adjusted calculation for females
////            bmi = (weight / (heightInMeters * heightInMeters)) * 1.07 - (148 * (weight / heightInMeters)) + 4.5;
//            bmi = weight / (heightInMeters * heightInMeters);
//        } else {
//            // Default to a generic BMI calculation if gender is not specified or recognized
//            bmi = weight / (heightInMeters * heightInMeters);
//        }
//        return bmi;
//    }
//

    public double calculateBMI(String gender, int heightFt, int heightIn, double weight) {
        // Convert height to centimeters
        double heightInCM = (heightFt * 12 + heightIn) * 2.54; // 1 inch = 2.54 centimeters

        // Convert height from centimeters to meters for BMI calculation
        double heightInMeters = heightInCM / 100; // Convert centimeters to meters

        // Calculate BMI based on gender
        double bmi;
        if (gender.equalsIgnoreCase("male")) {
            bmi = weight / (heightInMeters * heightInMeters);
        } else if (gender.equalsIgnoreCase("female")) {
            // Adjusted calculation for females
            // bmi = (weight / (heightInMeters * heightInMeters)) * 1.07 - (148 * (weight / heightInMeters)) + 4.5;
            bmi = weight / (heightInMeters * heightInMeters);
        } else {
            // Default to a generic BMI calculation if gender is not specified or recognized
            bmi = weight / (heightInMeters * heightInMeters);
        }
        return bmi;
    }


    //update user profile
    public UserProfile saveUserProfile(UserProfile userProfile) {
        // This method should save or update the user's profile data in the database
        return userProfileRepository.save(userProfile);
    }
    public UserProfile findByUsername(String username) {
        // Implement the logic to fetch health trends by the user's username
        return userProfileRepository.findByUserEmail(username);
    }


// to update the user profile
    public void updateUserProfile(Long id, UserProfile updatedProfile) {
        Optional<UserProfile> existingProfile = userProfileRepository.findById(id);
        if (existingProfile.isPresent()) {
            UserProfile userProfile = existingProfile.get();
            User user = userProfile.getUser();
            if (user != null) {
                user.setEmail(updatedProfile.getUser().getEmail());
                user.setMobileNo(updatedProfile.getUser().getMobileNo());
                // You can update other fields as well
                userProfileRepository.save(userProfile);
            } else {
                // Handle the case where the user is not associated with the UserProfile
                // Return an error response or take appropriate action
            }
        }
    }

}

