package com.example.jwt.service.serviceHealth;

import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.SleepDuration;
import com.example.jwt.entities.water.WaterEntity;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.repositoryHealth.SleepDurationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class SleepDurationService {
    private final SleepDurationRepository sleepDurationRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public SleepDurationService(SleepDurationRepository sleepDurationRepository) {
        this.sleepDurationRepository = sleepDurationRepository;
    }
//    public SleepDuration getSleepForUserAndDate(User user, LocalDate date) {
//        return sleepDurationRepository.findByUserAndDateOfSleep(user, date);
//    }
public SleepDuration getSleepForUserAndDate(User user, LocalDate date) {
    Optional<SleepDuration> optionalSleepDuration = sleepDurationRepository.findByUserAndDateOfSleep(user, date);
    return optionalSleepDuration.orElse(null);
}

    public SleepDuration updateSleep(SleepDuration sleepDuration) {
        // Check if the sleep duration with the same ID already exists in the database
        Optional<SleepDuration> existingSleep = sleepDurationRepository.findById(sleepDuration.getId());

        if (existingSleep.isPresent()) {
            // Update the existing sleep duration
            return sleepDurationRepository.save(sleepDuration);
        } else {
            // Handle the case where the sleep duration with the given ID is not found
            throw new EntityNotFoundException("Sleep duration with ID " + sleepDuration.getId() + " not found");
        }
    }

    public SleepDuration createSleep(SleepDuration sleepDuration) {
        return sleepDurationRepository.save(sleepDuration);
    }
    public SleepDuration addOrUpdateSleepDuration(SleepDuration sleepDuration, String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        Optional<SleepDuration> existingSleep = sleepDurationRepository.findByUserAndDateOfSleep(user, sleepDuration.getDateOfSleep());

        if (existingSleep.isPresent()) {
            // If sleep data already exists for the user and date, update the duration
            SleepDuration existing = existingSleep.get();
            existing.setDuration(sleepDuration.getDuration());
            existing.setManualDuration(existing.getManualDuration() + sleepDuration.getManualDuration());
            existing.setEfficiency(sleepDuration.getEfficiency());
            existing.setEndTime(sleepDuration.getEndTime());
            return sleepDurationRepository.save(existing);
        } else {
            // If sleep data doesn't exist, set the user and save the new sleep data
            if (user != null) {
                sleepDuration.setUser(user);
                return sleepDurationRepository.save(sleepDuration);
            } else {
                throw new UserNotFoundException("User not found for username: " + username);
            }
        }
    }

    // to get all sleep log of the specifc user
    public List<SleepDuration> getAllSleepLogs(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
        List<SleepDuration> sleepList = (List<SleepDuration>) sleepDurationRepository.findByUser(user);
        return sleepList;
    }

    // New method to get sleep logs by date
    public Optional<SleepDuration> getSleepLogsByDate(String username, LocalDate date) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        // Implement the logic to get sleep logs for a specific user and date
        return sleepDurationRepository.findByUserAndDateOfSleep(user, date);
    }

    // New method to get sleep logs for the last week
//    public List<SleepDuration> getSleepLogsBetweenDates(String username, LocalDate startDate, LocalDate endDate) {
//        User user = userRepository.findByEmail(username)
//                .orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
//
//        // Implement the logic to get sleep logs for a specific user and date range
//        return sleepDurationRepository.findByUserAndDateOfSleepBetween(user, startDate, endDate);
//    }
//    public Map<String, Double> sleepForLastWeek(User user) {
//        List<SleepDuration> sleepDurations = user.getSleepDurations();
//
//        // Create a map to store sleep duration for each day
//        Map<String, Double> sleepMap = new HashMap<>();
//
//        // Calculate the start date of the last week
//        LocalDate endDate = LocalDate.now();
//        LocalDate startDate = endDate.minusWeeks(1);
//
//        // Iterate over each day in the last week
//        for (LocalDate currentDate = startDate; currentDate.isBefore(endDate); currentDate = currentDate.plusDays(1)) {
//            double totalSleep = 0.0;
//
//            // Calculate total sleep duration for the current day
//            for (SleepDuration sleepEntity : sleepDurations) {
//                // Check if the sleep duration is on the current date
//                if (sleepEntity.getDateOfSleep().isEqual(currentDate)) {
//                    totalSleep += sleepEntity.getDuration();
//                }
//            }
//
//            // Store the result in the map with the day name
//            sleepMap.put(currentDate.getDayOfWeek().toString(), totalSleep);
//        }
//
//        return sleepMap;
//    }




    public Map<String, Double> sleepForLastWeek(User user) {
        List<SleepDuration> sleepDurations = user.getSleepDurations();

        // Create a map to store sleep duration for each day
        Map<String, Double> sleepMap = new HashMap<>();

        // Calculate the start date of the last week
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusWeeks(1);

        // Iterate over each day in the last week
        for (LocalDate currentDate = startDate; currentDate.isBefore(endDate); currentDate = currentDate.plusDays(1)) {
            double totalSleep = 0.0;

            // Calculate total sleep duration for the current day
            for (SleepDuration sleepEntity : sleepDurations) {
                // Check if the sleep duration is on the current date
                if (sleepEntity.getDateOfSleep().isEqual(currentDate)) {
                    // Add both duration and manualDuration in minutes
                    totalSleep += sleepEntity.getDuration() + sleepEntity.getManualDuration();
                }
            }

            // Store the result in the map with the day name
            sleepMap.put(currentDate.getDayOfWeek().toString(), totalSleep);
        }

        return sleepMap;
    }

}

