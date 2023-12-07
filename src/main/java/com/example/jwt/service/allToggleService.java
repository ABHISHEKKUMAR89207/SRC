package com.example.jwt.service;

import com.example.jwt.entities.User;
import com.example.jwt.entities.AllToggle;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.allToggleRepository;
import com.example.jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class allToggleService {

    private final allToggleRepository settingsRepository;

    @Autowired
    public allToggleService(allToggleRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    @Autowired
    private UserRepository userRepository;


    //get notification
    public AllToggle getUserNotificationSettings(String username) {
        Optional<User> userOptional = userRepository.findByEmail(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return settingsRepository.findByUser(user);
        }

        return null;
    }


    // update notification onn/off
    public AllToggle updateUserNotificationSettings(String username, boolean notificationOn) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        AllToggle settings = settingsRepository.findByUser(user);

        if (settings == null) {
            // Create new settings if not found
            settings = new AllToggle(notificationOn, user);
        } else {
            settings.setNotificationOn(notificationOn);
        }

        return settingsRepository.save(settings);
    }

}

