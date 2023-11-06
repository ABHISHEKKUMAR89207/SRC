package com.example.jwt.service.serviceHealth;


import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.BloodGlucose;
import com.example.jwt.entities.dashboardEntity.healthTrends.BloodPressure;
import com.example.jwt.entities.dashboardEntity.healthTrends.HealthTrends;
import com.example.jwt.exception.ResourceNotFoundException;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.repositoryHealth.BloodPressureRepository;
import com.example.jwt.repository.repositoryHealth.HealthTrendsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BloodPressureService {

    @Autowired
    private BloodPressureRepository bloodPressureRepository;
    @Autowired
    private HealthTrendsRepository healthTrendsRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    public BloodPressureService(BloodPressureRepository bloodPressureRepository) {

        this.bloodPressureRepository = bloodPressureRepository;
        this.healthTrendsRepository = healthTrendsRepository;
    }

    public BloodPressure saveHeartRate(BloodPressure bloodPressure) {
        return bloodPressureRepository.save(bloodPressure);
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


    public List<BloodPressure> getBloodPressureById(Long bloodPressureId) {
        // Use the repository to fetch heart rates between the given time range
        return bloodPressureRepository.findByBloodPressureId(bloodPressureId);
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



//    public BloodPressure addBloodPressure(BloodPressure bloodPressure, Long healthTrendId) {
//        HealthTrends healthTrends = this.healthTrendsRepository.findById(healthTrendId)
//                .orElseThrow(() -> new ResourceNotFoundException("HealthTrends", "ID", healthTrendId));
//
//        BloodPressure bloodPressure1 = this.modelMapper.map(bloodPressure, BloodPressure.class);
//        bloodPressure1.setHealthTrends(healthTrends);
//
//        // Save the HeartRate entity
//        return bloodPressureRepository.save(bloodPressure1);
//    }
//


    // add heart bloodPressure by token username
    public BloodPressure addBloodPressure(BloodPressure bloodPressure, String username) {
        // Find the user by the username, and associate the heart rate with that user
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        if (user != null) {
            // Set the user for the heart rate
            bloodPressure.setUser(user);

            // Save the heart rate
            return bloodPressureRepository.save(bloodPressure);
        } else {
            // Handle the case where the user is not found
            throw new UserNotFoundException("User not found for username: " + username);
        }
    }



    //get blood pressure by user and date
    public List<BloodPressure> getBloodPressureForUserAndDate(User user, LocalDate date) {
        return bloodPressureRepository.findByUserAndLocalDate(user, date);
    }

    // get blood pressure for one week and one month
    public List<BloodPressure> getBloodPressureForUserBetweenDates(User user, LocalDate startDate, LocalDate endDate) {
        return bloodPressureRepository.findByUserAndLocalDateBetween(user, startDate, endDate);
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
