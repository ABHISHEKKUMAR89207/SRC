package com.example.jwt.service;

import com.example.jwt.entities.User;
import com.example.jwt.entities.UserProfile;
import com.example.jwt.exception.ResourceNotFoundException;
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



    //create user profile
//    public UserProfile createUserProfile(UserProfile userProfile, Long userId) {
//
//
//        User user=this.userRepository.findById(userId)
//                .orElseThrow(()->new ResourceNotFoundException("User Profile","UserProfile id",userId));
//
//        UserProfile profile = this.modelMapper.map(userProfile,UserProfile.class);
////        post.setImageName("default.png");
////        post.setAddedDate(new Date());
//        profile.setUser(user);
////        post.setCategory(category);
//
//        UserProfile newProfile = this.userProfileRepository.save(profile);
//
////        return this.modelMapper.map(newProfile,UserProfile.class);
//        return userProfileRepository.save(userProfile);
//    }

    public UserProfile createUserProfile(UserProfile userProfile, Long userId) throws ParseException {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Profile", "UserProfile id"));

        // Set the user reference in the UserProfile
        userProfile.setUser(user);

        // Calculate and set the BMI
        double bmi = calculateBMI(userProfile.getHeight(), userProfile.getWeight());
        userProfile.setBmi(bmi);


        UserProfile newProfile = this.userProfileRepository.save(userProfile);

        return newProfile;
    }

    private double calculateBMI(double heightInFeet, double weight) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String str = String.valueOf(heightInFeet);
        String[] parts = str.split("\\.");
        double befDecimal = Double.parseDouble(parts[0]);
        double aftDecimal =Double.parseDouble(parts[1]);

        double heightInInches = (befDecimal*12) + aftDecimal;
        double heightInMeters = heightInInches * 0.0254; // Convert height to meters
        double bmis = weight / (heightInMeters * heightInMeters);
        String formatedBmi = decimalFormat.format(bmis);
        return Double.parseDouble(formatedBmi);
    }

//    public void updateUserProfile(Long id, UserProfile updatedProfile) {
//        if (userProfileRepository.existsById(id)) {
//            updatedProfile.setId(id);
//            userProfileRepository.save(updatedProfile);
//        }
//    }


//    public UserProfile findByUsername(String username) {
//        // Use the repository to find the user's profile by username
//        return userProfileRepository.findByUsername(username);
//    }
//


    //update user profile
    public UserProfile saveUserProfile(UserProfile userProfile) {
        // This method should save or update the user's profile data in the database
        return userProfileRepository.save(userProfile);
    }
    public UserProfile findByUsername(String username) {
        // Implement the logic to fetch health trends by the user's username
        return userProfileRepository.findByUserEmail(username);
    }

//    public void updateUserProfile(Long id, UserProfile updatedProfile) {
//        Optional<UserProfile> existingProfile = userProfileRepository.findById(id);
//        if (existingProfile.isPresent()) {
//            UserProfile userProfile = existingProfile.get();
//            userProfile.setEmail(updatedProfile.getEmail());
//            userProfile.setMobileNo(updatedProfile.getMobileNo());
//            // You can update other fields as well
//            userProfileRepository.save(userProfile);
//        }
//    }



//    public void updateUserProfile(Long id, UserProfile updatedProfile) {
//        Optional<UserProfile> existingProfile = userProfileRepository.findById(id);
//        if (existingProfile.isPresent()) {
//            UserProfile userProfile = existingProfile.get();
//            // Access and update email and mobileNo through the User entity
//            userProfile.getUser().setEmail(updatedProfile.getUser().getEmail());
//            userProfile.getUser().setMobileNo(updatedProfile.getUser().getMobileNo());
//            // You can update other fields as well
//            userProfileRepository.save(userProfile);
//        }


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





//    public void deleteUserProfile(Long id) {
//        userProfileRepository.deleteById(id);
//    }

    // Fetch the User and UserProfile

//    public void updateProfileInformation(Long userId, UserProfile userProfile) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
//
//        // Update User fields with UserProfile data
//        user.setEmail(userProfile.getEmail());
//        user.setMobileNo(userProfile.getMobileNo());
//        user.setGender(userProfile.getGender());
//        user.setDateOfBirth(userProfile.getDateOfBirth());
//
//        // Additional logic to calculate and update height, weight, or other fields if needed
//        // user.setHeight(userProfile.getHeight());
//        // user.setWeight(userProfile.getWeight());
//
//        // Save the updated User entity
//        userRepository.save(user);
//    }




}

