package com.example.jwt.service.serviceHealth;

import com.example.jwt.dtos.BloodSisGlo;
import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.BloodGlucose;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.repositoryHealth.BloodGlucoseRepository;
import com.example.jwt.repository.repositoryHealth.HealthTrendsRepository;
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
    private UserRepository userRepository;

    @Autowired
    public BloodGlucoseService(BloodGlucoseRepository bloodGlucoseRepository) {

        this.bloodGlucoseRepository = bloodGlucoseRepository;
        this.healthTrendsRepository = healthTrendsRepository;
    }

    public List<BloodGlucose> getBloodGlucoseById(Long bloodGlucoseId) {
        // Use the repository to fetch heart rates between the given time range
        return bloodGlucoseRepository.findByBloodGlucoseId(bloodGlucoseId);
    }

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

    public BloodGlucose convertToBloodGlucose(BloodSisGlo bloodSisGlo) {
        BloodGlucose bloodGlucose = new BloodGlucose();
        bloodGlucose.setValue(bloodSisGlo.getValue());
        bloodGlucose.setLocalDate(bloodSisGlo.getLocalDate());
        // Set other properties as needed
        return bloodGlucose;
    }

    //get blood glucose by user and date
    public List<BloodGlucose> getBloodGlucoseForUserAndDate(User user, LocalDate date) {
        return bloodGlucoseRepository.findByUserAndLocalDate(user, date);
    }

    // get blood glucose for one week and one month
    public List<BloodGlucose> getBloodGlucoseForUserBetweenDates(User user, LocalDate startDate, LocalDate endDate) {
        return bloodGlucoseRepository.findByUserAndLocalDateBetween(user, startDate, endDate);
    }
}
