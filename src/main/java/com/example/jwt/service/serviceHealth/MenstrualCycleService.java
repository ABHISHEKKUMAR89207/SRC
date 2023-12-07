package com.example.jwt.service.serviceHealth;

import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.MenstrualCycle;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserProfileRepository;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.repositoryHealth.MenstrualCycleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MenstrualCycleService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MenstrualCycleRepository menstrualCycleRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;


    public MenstrualCycle calculateNextPeriodStartDate(String username) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for email: " + username));

        if (user.getUserProfile() != null && user.getUserProfile().getGender().equalsIgnoreCase("Female")) {
            MenstrualCycle menstrualCycle = user.getUserProfile().getMenstrualCycle();


            if (menstrualCycle != null) {
                LocalDate lastPeriodStartDate = menstrualCycle.getLastPeriodStartDate();
                int cycleLength = menstrualCycle.getAverageCycleLength();

                // Calculate the next period start date
                LocalDate nextPeriodStartDate = lastPeriodStartDate.plusDays(cycleLength);

                // Update the calculatedDate
                menstrualCycle.setCalculatedDate(nextPeriodStartDate);

                // Set the userProfile for menstrualCycle
                menstrualCycle.setUserProfile(user.getUserProfile());

                // Save the updated menstrualCycle
                menstrualCycleRepository.save(menstrualCycle);

                return menstrualCycle;


//                return lastPeriodStartDate.plusDays(cycleLength);
            }
        }

        throw new IllegalArgumentException("This service is only available for female users with a valid last period start date and cycle length.");
    }


    public LocalDate caalculateNextPeriodStartDate(String username) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for email: " + username));

        if (user.getUserProfile() != null && user.getUserProfile().getGender().equalsIgnoreCase("Female")) {
            MenstrualCycle menstrualCycle = user.getUserProfile().getMenstrualCycle();

            if (menstrualCycle != null) {
                LocalDate lastPeriodStartDate = menstrualCycle.getLastPeriodStartDate();
                int cycleLength = menstrualCycle.getAverageCycleLength();

                // Calculate the next period start date
                LocalDate nextPeriodStartDate = lastPeriodStartDate.plusDays(cycleLength);

                // Check if the calculated date is in the future
                LocalDate today = LocalDate.now();
//                if (nextPeriodStartDate.isAfter(today)) {
//                    // Update both last period start date and calculated date
//                    menstrualCycle.setLastPeriodStartDate(nextPeriodStartDate);
//                    menstrualCycle.setCalculatedDate(nextPeriodStartDate.plusDays(cycleLength));
//                } else {
//                    throw new IllegalArgumentException("Next period calculation resulted in a date in the past.");
//                }

                if (nextPeriodStartDate.isAfter(today)) {
                    // Update both last period start date and calculated date
                    menstrualCycle.setLastPeriodStartDate(nextPeriodStartDate);
                    menstrualCycle.setCalculatedDate(nextPeriodStartDate.plusDays(cycleLength));
                } else {
                    throw new IllegalArgumentException("Next period calculation resulted in a date in the past.");
                }

                // Set the userProfile for menstrualCycle
                menstrualCycle.setUserProfile(user.getUserProfile());

                // Save the updated menstrualCycle
                menstrualCycleRepository.save(menstrualCycle);

                return nextPeriodStartDate;
            }
        }

        throw new IllegalArgumentException("This service is only available for female users with a valid last period start date and cycle length.");
    }


    public MenstrualCycle addMenstrualoCycle(MenstrualCycle menstrualCycle, String username) {

        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        if (user != null) {
            // Set the user for the heart rate
            menstrualCycle.setUser(user);
            // Set the userProfile for menstrualCycle
            menstrualCycle.setUserProfile(user.getUserProfile());

            // Save the heart rate
            return menstrualCycleRepository.save(menstrualCycle);
        } else {
            // Handle the case where the user is not found
            throw new UserNotFoundException("User not found for username: " + username);
        }

    }


    public MenstrualCycle addMenstrualCycle(MenstrualCycle menstrualCycle, String username) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        if (user != null && user.getUserProfile() != null && user.getUserProfile().getGender().equalsIgnoreCase("Female")) {
            // Set the user for the menstrualCycle
            menstrualCycle.setUser(user);
            // Set the userProfile for menstrualCycle
            menstrualCycle.setUserProfile(user.getUserProfile());

            // Save the menstrualCycle
            return menstrualCycleRepository.save(menstrualCycle);
        } else {
            // Handle the case where the user is not found or has a gender other than "Female"
            throw new UserNotFoundException("User not found for username: " + username + " or user's gender is not Female.");
        }
    }


}

