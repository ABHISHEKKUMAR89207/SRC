package com.example.jwt.service.serviceHealth;


import com.example.jwt.entities.User;
import com.example.jwt.entities.UserProfile;
import com.example.jwt.entities.dashboardEntity.healthTrends.*;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserProfileRepository;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.repositoryHealth.HealthTrendsRepository;
import com.example.jwt.repository.repositoryHealth.HeartRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Component
public class HealthTrendsService {

    private final HealthTrendsRepository healthTrendsRepository;
    private final HeartRateRepository heartRateRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository; // Import the UserProfileRepository

    @Autowired
    public HealthTrendsService(HealthTrendsRepository healthTrendsRepository, HeartRateRepository heartRateRepository, UserProfileRepository userProfileRepository, UserRepository userRepository) {
        this.healthTrendsRepository = healthTrendsRepository;
        this.heartRateRepository = heartRateRepository;
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
    }
//    public HealthTrends saveHealthTrends(HealthTrends healthTrends) {
//        return healthTrendsRepository.save(healthTrends);
//    }



    public HealthTrends findByUsername(String username) {
        // Implement the logic to fetch health trends by the user's username
        return healthTrendsRepository.findByUserEmail(username);
    }



//    public List<HealthTrends> getHealthTrendsForLoggedInUser(String username) {
//        // Find the user by their username (or another identifier)
//        User user = userRepository.findByUserName(username);
//
//        if (user != null) {
//            // Retrieve the health trends associated with the user
//            return healthTrendsRepository.findByUser(user);
//        } else {
//            // User not found, handle appropriately
//            return Collections.emptyList();
//        }
//    }




//    public List<HealthTrends> getHealthTrendsForLoggedInUser(String username) {
//        User user = userRepository.findByUserName(username); // Assuming you have a method in UserRepository to find by username
//        if (user != null) {
//            return user.getHealthTrends();
//        }
//        return Collections.emptyList();
//    }




//    public List<HealthTrends> getHealthTrendsForLoggedInUser(String username) {
//        User user = userRepository.findByUserName(username); // Assuming you have a method in UserRepository to find by username
//        if (user != null) {
//            return user.getHealthTrends();
//        } else {
//            throw new UserNotFoundException(username);
//        }
//    }



    public HealthTrends saveHealthTrends(HealthTrends healthTrends) {
        // Implement any additional logic before saving if needed
        return healthTrendsRepository.save(healthTrends);
    }



    //Save Health Trends
//    public HealthTrends saveHealthTrends(HealthTrends healthTrends) {
//        // Fetch the corresponding UserProfile
//        Optional<UserProfile> userProfileOptional = userProfileRepository.findById(healthTrends.getUserProfile().getId());
//
//        if (userProfileOptional.isPresent()) {
//            UserProfile userProfile = userProfileOptional.get();
//            healthTrends.setBmi(userProfile.getBmi());
//            healthTrends.setWeight(userProfile.getWeight());
//        }
//
//        return healthTrendsRepository.save(healthTrends);
//    }
//
//    public List<HealthTrends> getAllHealthTrends() {
//        return healthTrendsRepository.findAll();
//    }

    public Optional<HealthTrends> getHealthTrendsById(Long id) {
        return healthTrendsRepository.findById(id);
    }




    //Heart Rate
    public List<HeartRate> getHeartRatesByHealthTrend(Long healthTrendId) {
        Optional<HealthTrends> healthTrendsOptional = healthTrendsRepository.findById(healthTrendId);

        if (healthTrendsOptional.isPresent()) {
            HealthTrends healthTrends = healthTrendsOptional.get();
            return healthTrends.getHeartRates();
        } else {
            return null;
        }
    }

    //Blood Pressure

    public List<BloodPressure> getbloodPressureByHealthTrend(Long healthTrendId) {
        Optional<HealthTrends> healthTrendsOptional = healthTrendsRepository.findById(healthTrendId);

        if (healthTrendsOptional.isPresent()) {
            HealthTrends healthTrends = healthTrendsOptional.get();
            return healthTrends.getBloodPressures();
        } else {
            return null;
        }
    }


    //bloog glucose
    public List<BloodGlucose> getbloodGlucoseByHealthTrend(Long healthTrendId) {
        Optional<HealthTrends> healthTrendsOptional = healthTrendsRepository.findById(healthTrendId);

        if (healthTrendsOptional.isPresent()) {
            HealthTrends healthTrends = healthTrendsOptional.get();
            return healthTrends.getBloodGlucose();
        } else {
            return null;
        }
    }


    //bloog OxygenSaturatedLevel
    public List<OxygenSaturatedLevel> getOxygenSaturatedLevelByHealthTrend(Long healthTrendId) {
        Optional<HealthTrends> healthTrendsOptional = healthTrendsRepository.findById(healthTrendId);

        if (healthTrendsOptional.isPresent()) {
            HealthTrends healthTrends = healthTrendsOptional.get();
            return healthTrends.getOxygenSaturatedLevels();
        } else {
            return null;
        }
    }




//    // Create a new HealthTrends instance
//    HealthTrends healthTrend = new HealthTrends();
//    healthTrend.setDate(LocalDate.now()); // Set the date to the current date
//    // Set other health trend properties (e.g., weight, BMI)
//
//    // Create a new HeartRate instance
//    HeartRate heartRate = new HeartRate();
//    heartRate.setTimeStamp(LocalDateTime.now()); // Set the timestamp to the current time
//    heartRate.setValue(75.5); // Set the heart rate value
//
//    // Link the HeartRate to the HealthTrends
//    heartRate.setHealthTrends(healthTrend);
//
//    // Add the HeartRate to the list of heart rates in HealthTrends
//    healthTrend.getHeartRates().add(heartRate);
//
//    // Save the HealthTrends instance, which will cascade the save to HeartRate
//    healthTrendRepository.save(healthTrend);

}
