package com.example.jwt.service.serviceHealth;

import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.BloodPressure;
import com.example.jwt.entities.dashboardEntity.healthTrends.SystolicBloodPressure;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.repositoryHealth.BloodPressureRepository;
import com.example.jwt.repository.repositoryHealth.HealthTrendsRepository;
import com.example.jwt.repository.repositoryHealth.SystolicBloodPressureRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SystolicBloodPressureService {

    @Autowired
    private SystolicBloodPressureRepository systolicBloodPressureRepository;
    @Autowired
    private HealthTrendsRepository healthTrendsRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public SystolicBloodPressureService(SystolicBloodPressureRepository systolicBloodPressureRepository) {

        this.systolicBloodPressureRepository = systolicBloodPressureRepository;
        this.healthTrendsRepository = healthTrendsRepository;
    }

    public List<SystolicBloodPressure> getSystolicBloodPressureByDateRange(User user, LocalDate startDate, LocalDate endDate) {
        return systolicBloodPressureRepository.findByUserAndLocalDateBetween(user, startDate, endDate);
    }

    public SystolicBloodPressure addSystolicBloodPressure(SystolicBloodPressure systolicBloodPressure, String username) {
        // Find the user by the username, and associate the heart rate with that user
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        if (user != null) {
            // Set the user for the heart rate
            systolicBloodPressure.setUser(user);
            // Save the heart rate
            return systolicBloodPressureRepository.save(systolicBloodPressure);
        } else {
            // Handle the case where the user is not found
            throw new UserNotFoundException("User not found for username: " + username);
        }
    }

    public List<SystolicBloodPressure> getSystolicBloodPressureForUserAndDate(User user, LocalDate date) {
        return systolicBloodPressureRepository.findByUserAndLocalDate(user, date);
    }

    public List<SystolicBloodPressure> getSystolicBloodPressureForUserBetweenDates(User user, LocalDate startDate, LocalDate endDate) {
        return systolicBloodPressureRepository.findByUserAndLocalDateBetween(user, startDate, endDate);
    }
}
