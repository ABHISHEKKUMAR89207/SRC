package com.example.jwt.service.serviceHealth;

import com.example.jwt.dtos.BloodSisGlo;
import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.OxygenSaturatedLevel;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.repositoryHealth.HealthTrendsRepository;
import com.example.jwt.repository.repositoryHealth.OxygenSaturatedLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OxygenSaturatedLevelService {

    @Autowired
    private OxygenSaturatedLevelRepository oxygenSaturatedLevelRepository;
    @Autowired
    private HealthTrendsRepository healthTrendsRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public OxygenSaturatedLevelService(OxygenSaturatedLevelRepository oxygenSaturatedLevelRepository) {

        this.oxygenSaturatedLevelRepository = oxygenSaturatedLevelRepository;
        this.healthTrendsRepository = healthTrendsRepository;
    }

    public OxygenSaturatedLevel saveOxygenSaturatedLevel(OxygenSaturatedLevel oxygenSaturatedLevel) {
        return oxygenSaturatedLevelRepository.save(oxygenSaturatedLevel);
    }

    public List<OxygenSaturatedLevel> getOxygenSaturatedLevelById(Long oxygenSaturatedLevelId) {
        // Use the repository to fetch heart rates between the given time range
        return oxygenSaturatedLevelRepository.findByOxygenSaturatedLevelId(oxygenSaturatedLevelId);
    }

    // add heart oxygenSaturatedLevel by token username
    public OxygenSaturatedLevel addOxygenSaturatedLevel(OxygenSaturatedLevel oxygenSaturatedLevel, String username) {
        // Find the user by the username, and associate the heart rate with that user
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        if (user != null) {
            // Set the user for the heart rate
            oxygenSaturatedLevel.setUser(user);

            // Save the heart rate
            return oxygenSaturatedLevelRepository.save(oxygenSaturatedLevel);
        } else {
            // Handle the case where the user is not found
            throw new UserNotFoundException("User not found for username: " + username);
        }
    }

    public OxygenSaturatedLevel convertToOxygenSaturatedLevel(BloodSisGlo bloodSisGlo) {
        OxygenSaturatedLevel oxygenSaturatedLevel = new OxygenSaturatedLevel();
        oxygenSaturatedLevel.setValue(bloodSisGlo.getValue());
        oxygenSaturatedLevel.setLocalDate(bloodSisGlo.getLocalDate());
        // You might need to set other properties as well if they exist
        return oxygenSaturatedLevel;
    }

    //get heart rate by user and date
    public List<OxygenSaturatedLevel> getOxygenSaturatedLevelForUserAndDate(User user, LocalDate date) {
        return oxygenSaturatedLevelRepository.findByUserAndLocalDate(user, date);
    }

    // get blood glucose for one week and one month
    public List<OxygenSaturatedLevel> getOxygenSaturatedLevelForUserBetweenDates(User user, LocalDate startDate, LocalDate endDate) {
        return oxygenSaturatedLevelRepository.findByUserAndLocalDateBetween(user, startDate, endDate);
    }
}
