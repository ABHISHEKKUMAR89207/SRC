package com.example.jwt.service.serviceHealth;

import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.DiastolicBloodPressure;
import com.example.jwt.entities.dashboardEntity.healthTrends.SystolicBloodPressure;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.repositoryHealth.DiastolicBloodPressureRepository;
import com.example.jwt.repository.repositoryHealth.HealthTrendsRepository;
import com.example.jwt.repository.repositoryHealth.SystolicBloodPressureRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DiastolicBloodPressureService {

    @Autowired
    private DiastolicBloodPressureRepository diastolicBloodPressureRepository;
    @Autowired
    private HealthTrendsRepository healthTrendsRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;


    @Autowired
    public DiastolicBloodPressureService(DiastolicBloodPressureRepository diastolicBloodPressureRepository) {

        this.diastolicBloodPressureRepository = diastolicBloodPressureRepository;
        this.healthTrendsRepository = healthTrendsRepository;
    }

    public List<DiastolicBloodPressure> getDiastolicBloodPressureByDateRange(User user, LocalDate startDate, LocalDate endDate) {
        return diastolicBloodPressureRepository.findByUserAndLocalDateBetween(user, startDate, endDate);
    }

    public DiastolicBloodPressure addDiastolicBloodPressure(DiastolicBloodPressure diastolicBloodPressure, String username) {
        // Find the user by the username, and associate the heart rate with that user
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        if (user != null) {
            // Set the user for the heart rate
            diastolicBloodPressure.setUser(user);
            // Save the heart rate
            return diastolicBloodPressureRepository.save(diastolicBloodPressure);
        } else {
            // Handle the case where the user is not found
            throw new UserNotFoundException("User not found for username: " + username);
        }
    }

    public List<DiastolicBloodPressure> getDiastolicBloodPressureForUserAndDate(User user, LocalDate date) {
        return diastolicBloodPressureRepository.findByUserAndLocalDate(user, date);
    }

    public List<DiastolicBloodPressure> getDiastolicBloodPressureForUserBetweenDates(User user, LocalDate startDate, LocalDate endDate) {
        return diastolicBloodPressureRepository.findByUserAndLocalDateBetween(user, startDate, endDate);
    }
}
