package com.example.jwt.service.serviceHealth;

import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.SleepDuration;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.repositoryHealth.SleepDurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
}

