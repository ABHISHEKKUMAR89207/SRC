package com.example.jwt.service.serviceHealth;

import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.BloodPressure;
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

    public List<BloodPressure> getBloodPressureById(Long bloodPressureId) {
        // Use the repository to fetch heart rates between the given time range
        return bloodPressureRepository.findByBloodPressureId(bloodPressureId);
    }

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
}
