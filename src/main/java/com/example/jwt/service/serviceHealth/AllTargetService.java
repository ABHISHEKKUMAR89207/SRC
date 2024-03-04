package com.example.jwt.service.serviceHealth;//package com.practice.springbootimportcsvfileapp.service;//package com.practice.springbootimportcsvfileapp.service;//package com.practice.springbootimportcsvfileapp.service;

import com.example.jwt.entities.User;

import com.example.jwt.entities.dashboardEntity.healthTrends.AllTarget;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.repositoryHealth.AllTargetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AllTargetService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AllTargetRepository allTargetRepository;
    public AllTargetService(UserRepository userRepository, AllTargetRepository sleepDurationRepository) {
        this.userRepository = userRepository;
        this.allTargetRepository = sleepDurationRepository;
    }

    // To set the sleep target of a particular user
    public AllTarget SleepTarget(AllTarget sleepTarget, String username) {
        // Find the user by the username, and associate the sleep duration with that user
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        if (user != null) {
            // Get the existing sleep duration (if exists) for this user
            AllTarget existingSleepDuration = allTargetRepository.findByUser(user);

            if (existingSleepDuration != null) {
                // Update the sleep target for the existing sleep duration
                existingSleepDuration.setSleepTarget(sleepTarget.getSleepTarget());
                return allTargetRepository.save(existingSleepDuration);
            } else {
                // If no existing sleep duration found, create a new one and set the sleep target
                sleepTarget.setUser(user);
                return allTargetRepository.save(sleepTarget);
            }
        } else {
            // Handle the case where the user is not found
            throw new UserNotFoundException("User not found for username: " + username);
        }
    }

    // To set the sleep target of a particular user
    public AllTarget weightGoal(AllTarget weightGoal, String username) {
        // Find the user by the username, and associate the sleep duration with that user
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        if (user != null) {
            // Get the existing sleep duration (if exists) for this user
            AllTarget existingWeightGoal = allTargetRepository.findByUser(user);

            if (existingWeightGoal != null) {
                // Update the sleep target for the existing sleep duration
                existingWeightGoal.setWeightGoal(weightGoal.getWeightGoal());
                return allTargetRepository.save(existingWeightGoal);
            } else {
                // If no existing sleep duration found, create a new one and set the sleep target
                weightGoal.setUser(user);
                return allTargetRepository.save(weightGoal);
            }
        } else {
            // Handle the case where the user is not found
            throw new UserNotFoundException("User not found for username: " + username);
        }
    }

    // to save and update the water goal of a specific user
    public AllTarget saveOrUpdateWaterGoal(String username, Double newWaterGoal) {
        // Find the user by username
        Optional<User> userOptional = userRepository.findByEmail(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Check if a goal already exists for the user's water entity
            AllTarget existingGoal = allTargetRepository.findByUser(user);

            if (existingGoal == null) {
                // If no goal exists, create a new one
                AllTarget newWaterGoalEntity = new AllTarget();
                newWaterGoalEntity.setWaterGoal(newWaterGoal);
                newWaterGoalEntity.setUser(user);

                return allTargetRepository.save(newWaterGoalEntity);
            } else {
                // If a goal already exists, update the existing one
                existingGoal.setWaterGoal(newWaterGoal);
                return allTargetRepository.save(existingGoal);
            }
        } else {
            // Handle the case where the user is not found
            // You can throw an exception, log a message, or handle it in another way based on your requirements.
            throw new UserNotFoundException("User not found for username: " + username);
        }
    }
}
