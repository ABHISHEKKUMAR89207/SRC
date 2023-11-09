package com.example.jwt.service;
import com.example.jwt.entities.User;
import com.example.jwt.entities.waterEntity;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.exception.WaterEntityNotFoundException;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.waterEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class waterService {
    @Autowired
    private waterEntityRepository waterEntityRepository;
    @Autowired
    private UserRepository userRepository;



//    public waterEntity saveWaterActivity(waterEntity waterEntityactivity, String username) {
//
//
//        User user = userRepository.findByEmail(username)
//                .orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
//
//        if (user != null) {
//            // Set the user for the heart rate
//            waterEntityactivity.setUser(user);
//            Double updatedGoal = waterEntityactivity.getWaterGoal();
//            waterEntityactivity.setWaterGoal(updatedGoal);
//
//            int updatesNoOfCups = waterEntityactivity.getCupCapacity();
//            waterEntityactivity.setNoOfCups(updatesNoOfCups);
//
//            double waterIntake = calculateWaterIntake(waterEntityactivity.getCupCapacity(), waterEntityactivity.getNoOfCups());
//            waterEntityactivity.setWaterIntake(waterIntake);
//            return waterEntityRepository.save(waterEntityactivity);
//        } else {
//            // Handle the case where the user is not found
//            throw new UserNotFoundException("User not found for username: " + username);
//        }
//
//    }

    public waterEntity saveWaterActivity(waterEntity waterEntityactivity, String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        waterEntity existingWaterEntity = waterEntityRepository.findByUser(user);

        if (existingWaterEntity != null) {
            // Update the waterGoal and cupCapacity with new values
            existingWaterEntity.setWaterGoal(waterEntityactivity.getWaterGoal());
            existingWaterEntity.setCupCapacity(waterEntityactivity.getCupCapacity());

            // Calculate the updated waterIntake
            double waterIntake = calculateWaterIntake(existingWaterEntity.getCupCapacity(), existingWaterEntity.getNoOfCups());
            existingWaterEntity.setWaterIntake(waterIntake);

            return waterEntityRepository.save(existingWaterEntity);
        } else {
            // Create a new waterEntity for the user
            waterEntity newWaterEntity = new waterEntity();
            newWaterEntity.setUser(user);
            newWaterEntity.setWaterGoal(waterEntityactivity.getWaterGoal());
            newWaterEntity.setCupCapacity(waterEntityactivity.getCupCapacity());
            newWaterEntity.setNoOfCups(waterEntityactivity.getNoOfCups());

            // Calculate the initial waterIntake
            double waterIntake = calculateWaterIntake(newWaterEntity.getCupCapacity(), newWaterEntity.getNoOfCups());
            newWaterEntity.setWaterIntake(waterIntake);

            return waterEntityRepository.save(newWaterEntity);
        }
    }



    public waterEntity updateWaterActivity( waterEntity water, String username) {

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));


        // Update the noOfCups with the new value
        //existingWaterEntity.setNoOfCups(water.getNoOfCups()+existingWaterEntity.getNoOfCups());
        user.getWater().setNoOfCups(water.getNoOfCups()+user.getWater().getNoOfCups());

        // Recalculate the waterIntake
        double waterIntake = calculateWaterIntake(user.getWater().getCupCapacity(), user.getWater().getNoOfCups());
        user.getWater().setWaterIntake(waterIntake);

        // Save the updated entity
        return waterEntityRepository.save(user.getWater());
    }

    private double calculateWaterIntake(int cupCapacity, int noOfCups) {
        int ml = 0;
            ml = cupCapacity * noOfCups;
        return ml / 1000.0;
    }


//    public waterEntity getWaterDataForUser(String username) {
//        // Assuming you have a waterEntityRepository, you can fetch data for the user.
//        User user = userRepository.findByEmail(username)
//                .orElseThrow(() -> new UserNotFoundException("User not found for usernam"+ username));
//        waterEntity water = new waterEntity();
//        if (water != null) {
//            // Create a DTO to represent the water-related data
//            waterEntity waterData = new waterEntity();
//            waterData.setWaterGoal(water.getWaterGoal());
//            waterData.setNoOfCups(water.getNoOfCups());
//            waterData.setWaterIntake(water.getWaterIntake());
//
//            return waterData;
//        } else {
//            return null; // Data not found for the user
//        }
//    }

    public waterEntity getWaterDataForUser(String username) {
        // Fetch the user by their username
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        // Assuming that water-related data is associated with the user, you can access it via the user object
        waterEntity waterData = user.getWater();

        // Check if water-related data exists
        if (waterData != null) {
            return waterData;
        } else {
            // If water-related data doesn't exist, you can return a new waterEntity with default values
            waterEntity defaultWaterData = new waterEntity();
            defaultWaterData.setWaterGoal(null); // Set the default value for waterGoal
            defaultWaterData.setNoOfCups(0);      // Set the default value for noOfCups
            defaultWaterData.setWaterIntake(null); // Set the default value for waterIntake

            return defaultWaterData;
        }
    }


    @Scheduled(fixedRate = 24 * 60 * 60 * 1000) // 24 hours in milliseconds
    public void resetNumberOfCups() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            waterEntity userWater = user.getWater();
            if (userWater != null) {
                userWater.setNoOfCups(0);
                // You can also update other attributes as needed
                // e.g., update waterIntake, etc.
                // userWater.setWaterIntake(calculateWaterIntake(userWater.getCupCapacity(), 0));
                waterEntityRepository.save(userWater);
            }
        }
    }

}









