package com.example.jwt.service;

import com.example.jwt.entities.TargetData;
import com.example.jwt.entities.User;
import com.example.jwt.entities.waterEntity;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.TargetDataRepository;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.request.TargetDataRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TargetDataService {
    @Autowired
    private TargetDataRepository targetDataRepository;
    @Autowired
    private UserRepository userRepository;
    public TargetData saveOrUpdateTargetData(String username, TargetDataRequest request) {

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        TargetData existingData = targetDataRepository.findByUser(user);

        if (existingData == null) {
            TargetData newTargetData = new TargetData();
            newTargetData.setUser(user);
            newTargetData.setTargetCalories(request.getCalories());
            newTargetData.setTargetCarbs(request.getCarbs());
            newTargetData.setTargetFat(request.getFats());
            newTargetData.setTargetProteins(request.getProteins());
            newTargetData.setTargetFibers(request.getFiber());

            // Save the new target data
            return targetDataRepository.save(newTargetData);
        } else {
            if (request.getCalories() != null) {
                existingData.setTargetCalories(request.getCalories());
            }
            if (request.getCarbs() != null) {
                existingData.setTargetCarbs(request.getCarbs());
            }
            if (request.getFats() != null) {
                existingData.setTargetFat(request.getFats());
            }
            if (request.getProteins() != null) {
                existingData.setTargetProteins(request.getProteins());
            }
            if (request.getFiber() != null) {
                existingData.setTargetFibers(request.getFiber());
            }

            // Save the updated target data
            return targetDataRepository.save(existingData);
        }
    }

    public TargetData getTargetData() {
        return targetDataRepository.findById(1L).orElse(null);
    }
}
