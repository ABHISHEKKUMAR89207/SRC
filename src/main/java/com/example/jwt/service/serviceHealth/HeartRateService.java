package com.example.jwt.service.serviceHealth;

import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.HeartRate;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.repositoryHealth.HealthTrendsRepository;
import com.example.jwt.repository.repositoryHealth.HeartRateRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HeartRateService {

    private final HeartRateRepository heartRateRepository;
    @Autowired
    private HealthTrendsRepository healthTrendsRepository;
    @Autowired
    private UserRepository userRepository;


    @Autowired
    public HeartRateService(HeartRateRepository heartRateRepository) {

        this.heartRateRepository = heartRateRepository;
        this.healthTrendsRepository = healthTrendsRepository;
    }

    public List<HeartRate> getHeartRatesByTime(LocalDateTime startTime, LocalDateTime endTime) {
        // Use the repository to fetch heart rates between the given time range
        return heartRateRepository.findByTimeStampBetween(startTime, endTime);
    }

    // add heart rate by token username
    public HeartRate addHeartRate(HeartRate heartRate, String username) {
        // Find the user by the username, and associate the heart rate with that user
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

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
    public HeartRate getHeartRateForUserAndDate(User user, LocalDate date) {
        return heartRateRepository.findByUserAndLocalDate(user, date);
    }


    //get heart rate by user beween dates
    public List<HeartRate> getHeartRateForUserBetweenDates(User user, LocalDate startDate, LocalDate endDate) {
        return heartRateRepository.findByUserAndLocalDateBetween(user, startDate, endDate);
    }


    public HeartRate createHeartRate(HeartRate heartRate) {
        return heartRateRepository.save(heartRate);
    }
    public HeartRate updateHeartRate(HeartRate heartRate) {
        // Check if the heart rate with the same ID already exists in the database
        Optional<HeartRate> existingHeartRate = heartRateRepository.findById(heartRate.getHeart_rate_Id());

        if (existingHeartRate.isPresent()) {
            // Update the existing heart rate
            return heartRateRepository.save(heartRate);
        } else {
            // Handle the case where the heart rate with the given ID is not found
            throw new EntityNotFoundException("Heart rate with ID " + heartRate.getHeart_rate_Id() + " not found");
        }
    }
}
