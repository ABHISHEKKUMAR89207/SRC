package com.example.jwt.service.serviceHealth;//package com.practice.springbootimportcsvfileapp.service;//package com.practice.springbootimportcsvfileapp.service;//package com.practice.springbootimportcsvfileapp.service;
import com.example.jwt.entities.User;

import com.example.jwt.entities.dashboardEntity.healthTrends.SleepTarget;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.repositoryHealth.SleepDurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SleepDurationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SleepDurationRepository sleepDurationRepository;

    public SleepDurationService(UserRepository userRepository, SleepDurationRepository sleepDurationRepository) {
        this.userRepository = userRepository;
        this.sleepDurationRepository = sleepDurationRepository;
    }

    public SleepTarget SleepTarget(SleepTarget sleepDuration, String username) {
        // Find the user by the username, and associate the sleep duration with that user
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        if (user != null) {
            // Get the existing sleep duration (if exists) for this user
            SleepTarget existingSleepDuration = sleepDurationRepository.findByUser(user);

            if (existingSleepDuration != null) {
                // Update the sleep target for the existing sleep duration
                existingSleepDuration.setSleepTarget(sleepDuration.getSleepTarget());
                return sleepDurationRepository.save(existingSleepDuration);
            } else {
                // If no existing sleep duration found, create a new one and set the sleep target
                sleepDuration.setUser(user);
                return sleepDurationRepository.save(sleepDuration);
            }
        } else {
            // Handle the case where the user is not found
            throw new UserNotFoundException("User not found for username: " + username);
        }
    }
}
