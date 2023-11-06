package com.example.jwt.service.serviceHealth;


import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.BloodGlucose;
import com.example.jwt.entities.dashboardEntity.healthTrends.HealthTrends;

import com.example.jwt.entities.dashboardEntity.healthTrends.HeartRate;
import com.example.jwt.exception.ResourceNotFoundException;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.repositoryHealth.BloodGlucoseRepository;
import com.example.jwt.repository.repositoryHealth.HealthTrendsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BloodGlucoseService {

    @Autowired
    private BloodGlucoseRepository bloodGlucoseRepository;
    @Autowired
    private HealthTrendsRepository healthTrendsRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    public BloodGlucoseService(BloodGlucoseRepository bloodGlucoseRepository) {

        this.bloodGlucoseRepository = bloodGlucoseRepository;
        this.healthTrendsRepository = healthTrendsRepository;
    }

    public BloodGlucose saveBloodGlucose(BloodGlucose bloodGlucose) {
        return bloodGlucoseRepository.save(bloodGlucose);
    }


    @Autowired
    private UserRepository userRepository;




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


    public List<BloodGlucose> getBloodGlucoseById(Long bloodGlucoseId) {
        // Use the repository to fetch heart rates between the given time range
        return bloodGlucoseRepository.findByBloodGlucoseId(bloodGlucoseId);
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



//    public BloodGlucose addBloodGlucose(BloodGlucose bloodGlucose, Long healthTrendId) {
//        HealthTrends healthTrends = this.healthTrendsRepository.findById(healthTrendId)
//                .orElseThrow(() -> new ResourceNotFoundException("HealthTrends", "ID", healthTrendId));
//
//        BloodGlucose bloodGlucose1 = this.modelMapper.map(bloodGlucose, BloodGlucose.class);
//        bloodGlucose1.setHealthTrends(healthTrends);
//
//        // Save the HeartRate entity
//        return bloodGlucoseRepository.save(bloodGlucose1);
//    }


    // add heart bloodGlucose by token username
    public BloodGlucose addBloodGlucose(BloodGlucose bloodGlucose, String username) {
        // Find the user by the username, and associate the heart rate with that user
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        if (user != null) {
            // Set the user for the heart rate
            bloodGlucose.setUser(user);

            // Save the heart rate
            return bloodGlucoseRepository.save(bloodGlucose);
        } else {
            // Handle the case where the user is not found
            throw new UserNotFoundException("User not found for username: " + username);
        }
    }


    //get blood glucose by user and date
    public List<BloodGlucose> getBloodGlucoseForUserAndDate(User user, LocalDate date) {
        return bloodGlucoseRepository.findByUserAndLocalDate(user, date);
    }

 // get blood glucose for one week and one month
    public List<BloodGlucose> getBloodGlucoseForUserBetweenDates(User user, LocalDate startDate, LocalDate endDate) {
        return bloodGlucoseRepository.findByUserAndLocalDateBetween(user, startDate, endDate);
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
