package com.example.jwt.service;
import com.example.jwt.entities.User;
import com.example.jwt.entities.water.WaterEntity;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.WaterEntityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class WaterService {

    @Autowired
    private UserRepository userRepository;
    private final WaterEntityRepository waterEntityRepository;
    @Autowired
<<<<<<< HEAD:src/main/java/com/example/jwt/service/waterService.java
    public waterService(WaterEntityRepository waterEntityRepository) {
=======
    public WaterService(WaterEntityRepository waterEntityRepository) {
>>>>>>> 495700b4804df131a48b75088fdae4d03dbf4e57:src/main/java/com/example/jwt/service/WaterService.java
        this.waterEntityRepository = waterEntityRepository;
    }

// for calculating the water intake of the user based on the parameters
    public Double calculateWaterIntake(User user) {
        List<WaterEntity> waterEntities = user.getWaterEntities();

        // Assuming you want to calculate the total water intake for all WaterEntities
        double totalWaterIntake = 0.0;

        for (WaterEntity waterEntity : waterEntities) {
            totalWaterIntake += waterEntity.getCupCapacity() * waterEntity.getNoOfCups() / 1000.0;
        }

        return totalWaterIntake;
    }
// calculate water intake of last week
    public Map<String, Double> calculateWaterIntakeForLastWeek(User user) {
        List<WaterEntity> waterEntities = user.getWaterEntities();

        // Create a map to store water intake for each day
        Map<String, Double> waterIntakeMap = new HashMap<>();

        // Calculate the start date of the last week
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusWeeks(1);

        // Iterate over each day in the last week
        for (LocalDate currentDate = startDate; currentDate.isBefore(endDate); currentDate = currentDate.plusDays(1)) {
            double totalWaterIntake = 0.0;

            // Calculate total water intake for the current day
            for (WaterEntity waterEntity : waterEntities) {
                if (waterEntity.getLocalDate().isEqual(currentDate)) {
                    totalWaterIntake += waterEntity.getCupCapacity() * waterEntity.getNoOfCups() / 1000.0;
                }
            }
            // Store the result in the map with the day name
            waterIntakeMap.put(currentDate.getDayOfWeek().toString(), totalWaterIntake);
        }

        return waterIntakeMap;
    }

// calculate water intake of the user
    public Double calculateWaterIntake(User user, LocalDate date) {
        List<WaterEntity> waterEntities = user.getWaterEntities();

        // Assuming you want to calculate the total water intake for the specified date
        double totalWaterIntake = 0.0;

        for (WaterEntity waterEntity : waterEntities) {
            if (waterEntity.getLocalDate().isEqual(date)) {
                totalWaterIntake += waterEntity.getCupCapacity() * waterEntity.getNoOfCups() / 1000.0;
            }
        }
        return totalWaterIntake;
    }

// to save and update the water data of the user
    public WaterEntity saveOrUpdateWaterEntity(String username, WaterEntity waterEntity1) {
        // Find the user by username
        Optional<User> userOptional = userRepository.findByEmail(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Find or create the WaterEntity for the specified date and user
            LocalDate currentDate = LocalDate.now();
            WaterEntity existingWaterEntity = waterEntityRepository.findByUserAndLocalDate(user, currentDate);

            if (existingWaterEntity == null) {
                // If no entity exists for the current date and user, create a new one
                WaterEntity newWaterEntity = new WaterEntity();
                newWaterEntity.setCupCapacity(waterEntity1.getCupCapacity());
                newWaterEntity.setNoOfCups(waterEntity1.getNoOfCups());
                newWaterEntity.setLocalDate(currentDate);
                newWaterEntity.setUser(user);

                return waterEntityRepository.save(newWaterEntity);
            } else {
                // If an entity already exists for the current date and user, update the existing one
                existingWaterEntity.setCupCapacity(waterEntity1.getCupCapacity());
                existingWaterEntity.setNoOfCups(waterEntity1.getNoOfCups());
                return waterEntityRepository.save(existingWaterEntity);
            }
        } else {
            throw new UserNotFoundException("User not found for username: " + username);
        }
    }
}