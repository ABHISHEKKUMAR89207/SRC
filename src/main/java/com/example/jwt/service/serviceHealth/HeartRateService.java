package com.example.jwt.service.serviceHealth;


import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.HealthTrends;
import com.example.jwt.entities.dashboardEntity.healthTrends.HeartRate;
import com.example.jwt.exception.ResourceNotFoundException;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.repositoryHealth.HealthTrendsRepository;
import com.example.jwt.repository.repositoryHealth.HeartRateRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HeartRateService {

    private final HeartRateRepository heartRateRepository;
    @Autowired
    private HealthTrendsRepository healthTrendsRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;


    @Autowired
    public HeartRateService(HeartRateRepository heartRateRepository) {

        this.heartRateRepository = heartRateRepository;
        this.healthTrendsRepository = healthTrendsRepository;
    }

    public HeartRate saveHeartRate(HeartRate heartRate) {
        return heartRateRepository.save(heartRate);
    }





//    @Transactional
//    public HeartRate saveHeartRateData(double value, HealthTrends healthTrends) {
//        HeartRate heartRate = new HeartRate();
//        heartRate.setTimeStamp(LocalDateTime.now());
//        heartRate.setValue(value);
//        heartRate.setHealthTrends(healthTrends);
//
////        entityManager.persist(heartRate);
//        return healthTrendsRepository.save(heartRate);
//    }


    public List<HeartRate> getHeartRatesByTime(LocalDateTime startTime, LocalDateTime endTime) {
        // Use the repository to fetch heart rates between the given time range
        return heartRateRepository.findByTimeStampBetween(startTime, endTime);
    }

//    public HeartRate addHeartRate(HeartRate heartRate, Long healthTrendId) {
//
//        HealthTrends healthTrends=this.healthTrendsRepository.findById(healthTrendId)
//                .orElseThrow(()->new ResourceNotFoundException("HealthTrends not found with ID: " + healthTrendId));
//
//
//            HeartRate heartRate1 =this.modelMapper.map(heartRate,HeartRate.class);
//            heartRate1.setHealthTrends(healthTrends);
//
//            return healthTrendsRepository.save(heartRate);
//    }


//    public HeartRate addHeartRate(HeartRate heartRate, Long healthTrendId) {
//        HealthTrends healthTrends = this.healthTrendsRepository.findById(healthTrendId)
//                .orElseThrow(() -> new ResourceNotFoundException("HealthTrends", "ID", healthTrendId));
//
//        HeartRate heartRate1 = this.modelMapper.map(heartRate, HeartRate.class);
//        heartRate1.setHealthTrends(healthTrends);
//
//        // Save the HeartRate entity
//        return heartRateRepository.save(heartRate1);
//    }

    // add heart rate by token username
    public HeartRate addHeartRate(HeartRate heartRate, String username) {
        // Find the user by the username, and associate the heart rate with that user
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        if (user != null) {
            // Set the user for the heart rate
            heartRate.setUser(user);

            // Save the heart rate
            return heartRateRepository.save(heartRate);
        } else {
            // Handle the case where the user is not found
            throw new UserNotFoundException("User not found for username: " + username);
        }
    }


//get heart rate by user and date
    public List<HeartRate> getHeartRateForUserAndDate(User user, LocalDate date) {
        return heartRateRepository.findByUserAndLocalDate(user, date);
    }



    public List<HeartRate> getHeartRateForUserBetweenDates(User user, LocalDate startDate, LocalDate endDate) {
        return heartRateRepository.findByUserAndLocalDateBetween(user, startDate, endDate);
    }




//    public HeartRate createUserProfile(HeartRate heartRate, Long userId) {
//
//
//        HealthTrends user=this.healthTrendsRepository.findByDate(userId)
//                .orElseThrow(()->new ResourceNotFoundException("User Profile","UserProfile id",userId));
//
//        HeartRate profile = this.modelMapper.map(heartRate,HeartRate.class);
////        post.setImageName("default.png");
////        post.setAddedDate(new Date());
//        profile.setHealthTrends(user);
////        post.setCategory(category);
//
//        HeartRate heartRate1 = this.healthTrendsRepository.save(profile);
//
////        return this.modelMapper.map(newProfile,UserProfile.class);
//        return healthTrendsRepository.save(heartRate1);
//    }



}
