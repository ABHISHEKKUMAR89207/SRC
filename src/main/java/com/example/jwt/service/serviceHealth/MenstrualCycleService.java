package com.example.jwt.service.serviceHealth;

import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.MenstrualCycle;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserProfileRepository;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.repositoryHealth.MenstrualCycleRepository;
import com.example.jwt.request.MenstrualCycleRequest;
import com.example.jwt.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class MenstrualCycleService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MenstrualCycleRepository menstrualCycleRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private JwtHelper jwtHelper;

    @Scheduled(cron = "0 0 0 * * ?") // Run at midnight every day
    public void updateMenstrualCycle() {
        // Fetch all female users
        List<User> femaleUsers = userRepository.findAllByUserProfile_GenderIgnoreCase("Female");

        for (User user : femaleUsers) {
            MenstrualCycle menstrualCycle = user.getUserProfile().getMenstrualCycle();

            if (menstrualCycle != null) {
                // Calculate the new last period start date
                LocalDate lastPeriodStartDate = menstrualCycle.getLastPeriodStartDate().plusDays(menstrualCycle.getAverageCycleLength());

                // Calculate the difference in days between the current date and the new last period start date
                long daysUntilLastPeriodStart = ChronoUnit.DAYS.between(LocalDate.now(), lastPeriodStartDate);

                // If the new last period start date is today or in the past, update the menstrual cycle
                if (daysUntilLastPeriodStart <= 0) {
                    // Calculate and set the calculated date
                    LocalDate calculatedDate = lastPeriodStartDate.plusDays(menstrualCycle.getAverageCycleLength());
                    menstrualCycle.setCalculatedDate(calculatedDate);

                    // Save the updated menstrual cycle
                    menstrualCycleRepository.save(menstrualCycle);
                }
            }
        }
    }

    public ResponseEntity<String> updateMenstrualCycle(String tokenHeader, MenstrualCycleRequest request) {
        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        if (user.getUserProfile() != null && "Female".equalsIgnoreCase(user.getUserProfile().getGender())) {
            MenstrualCycle existingMenstrualCycle = user.getUserProfile().getMenstrualCycle();

            if (existingMenstrualCycle != null) {
                if (request.getAverageCycleLength() != 0) {
                    existingMenstrualCycle.setAverageCycleLength(request.getAverageCycleLength());
                }
                if (request.getLastPeriodStartDate() != null) {
                    existingMenstrualCycle.setLastPeriodStartDate(request.getLastPeriodStartDate());
                }

                // Calculate and set the calculatedDate based on lastPeriodStartDate and averageCycleLength
                LocalDate calculatedDate = request.getLastPeriodStartDate().plusDays(request.getAverageCycleLength());
                existingMenstrualCycle.setCalculatedDate(calculatedDate);

                // Save the updated MenstrualCycle
                menstrualCycleRepository.save(existingMenstrualCycle);
            } else {
                MenstrualCycle newMenstrualCycle = new MenstrualCycle();
                newMenstrualCycle.setAverageCycleLength(request.getAverageCycleLength());
                newMenstrualCycle.setUser(user);
                newMenstrualCycle.setUserProfile(user.getUserProfile());
                if (request.getLastPeriodStartDate() != null) {
                    newMenstrualCycle.setLastPeriodStartDate(request.getLastPeriodStartDate());
                }

                // Calculate and set the calculatedDate based on lastPeriodStartDate and averageCycleLength
                LocalDate calculatedDate = request.getLastPeriodStartDate().plusDays(request.getAverageCycleLength());
                newMenstrualCycle.setCalculatedDate(calculatedDate);

                // Save the new MenstrualCycle
                existingMenstrualCycle = menstrualCycleRepository.save(newMenstrualCycle);
                user.getUserProfile().setMenstrualCycle(existingMenstrualCycle);
                userRepository.save(user);
            }

            return ResponseEntity.ok("MenstrualCycle updated successfully");
        } else {
            throw new IllegalArgumentException("This service is only available for female users.");
        }
    }

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

