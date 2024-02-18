package com.example.jwt.service;
import com.example.jwt.entities.User;
import com.example.jwt.entities.water.WaterEntity;
import com.example.jwt.entities.water.WaterEntry;
import com.example.jwt.entities.water.WaterEntryRepository;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.WaterEntityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class WaterService {

    @Autowired
    private UserRepository userRepository;
    private final WaterEntityRepository waterEntityRepository;
    @Autowired
    public WaterService(WaterEntityRepository waterEntityRepository) {
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
//    public Map<String, Double> calculateWaterIntakeForLastWeek(User user) {
//        List<WaterEntity> waterEntities = user.getWaterEntities();
//
//        // Create a map to store water intake for each day
//        Map<String, Double> waterIntakeMap = new HashMap<>();
//
//        // Calculate the start date of the last week
//        LocalDate endDate = LocalDate.now();
//        LocalDate startDate = endDate.minusWeeks(1);
//
//        // Iterate over each day in the last week
//        for (LocalDate currentDate = startDate; currentDate.isBefore(endDate); currentDate = currentDate.plusDays(1)) {
//            double totalWaterIntake = 0.0;
//
//
//            // Calculate total water intake for the current day
//            for (WaterEntity waterEntity : waterEntities) {
//                if (waterEntity.getLocalDate().isEqual(currentDate)) {
////                    totalWaterIntake += waterEntity.getCupCapacity() * waterEntity.getNoOfCups() / 1000.0;
//                    totalWaterIntake += waterEntity.getWaterIntake();
//
//                }
//            }
//            // Store the result in the map with the day name
//            waterIntakeMap.put(currentDate.getDayOfWeek().toString(), totalWaterIntake);
//        }
//
//        return waterIntakeMap;
//    }
@Autowired
private WaterEntryRepository waterEntryRepository;
    public Map<String, Double> calculateWaterIntakeForLastWeek(User user) {
        // Retrieve water entries from the WaterEntry table
        List<WaterEntry> waterEntries = waterEntryRepository.findByWaterEntity_UserAndLocalDateBetween(
                user, LocalDate.now().minusWeeks(1), LocalDate.now());

        // Create a map to store water intake for each day
        Map<String, Double> waterIntakeMap = new HashMap<>();

        // Calculate the start date of the last week
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusWeeks(1);

        // Iterate over each day in the last week
        for (LocalDate currentDate = startDate; currentDate.isBefore(endDate); currentDate = currentDate.plusDays(1)) {
            double totalWaterIntake = 0.0;

            // Calculate total water intake for the current day
            for (WaterEntry waterEntry : waterEntries) {
                if (waterEntry.getLocalDate().isEqual(currentDate)) {
                    totalWaterIntake += waterEntry.getWaterIntake();
                }
            }

            // Store the result in the map with the day name
            waterIntakeMap.put(currentDate.getDayOfWeek().toString(), totalWaterIntake);
        }

        return waterIntakeMap;
    }


//    public Map<String, Double> calculateWaterIntakeForLastWeek(User user) {
//        List<WaterEntity> waterEntities = user.getWaterEntities();
//
//        // Create a map to store water intake for each day with a predictable order starting from Monday
//        Map<String, Double> waterIntakeMap = new LinkedHashMap<>();
//
//        // Calculate the start date of the last week
//        LocalDate endDate = LocalDate.now();
//        LocalDate startDate = endDate.minusWeeks(1);
//
//        // Create a list to define the order of days starting from Monday
//        List<String> dayOrder = Arrays.asList("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY");
//
//        // Iterate over each day in the last week
//        for (String day : dayOrder) {
//            // Find the corresponding LocalDate for the current day
//            LocalDate currentDate = startDate;
//            while (!currentDate.getDayOfWeek().toString().equals(day)) {
//                currentDate = currentDate.plusDays(1);
//            }
//
//            double totalWaterIntake = 0.0;
//
//            // Calculate total water intake for the current day
//            for (WaterEntity waterEntity : waterEntities) {
//                if (waterEntity.getLocalDate().isEqual(currentDate)) {
//                    totalWaterIntake += waterEntity.getWaterIntake();
//                }
//            }
//
//            // Store the result in the map with the day name
//            waterIntakeMap.put(currentDate.getDayOfWeek().toString(), totalWaterIntake);
//        }
//
//        return waterIntakeMap;
//    }


//    public Double getWaterIntakeForCurrentDate(User user, LocalDate currentDate) {
//        WaterEntity waterEntity = waterEntityRepository.findByUserAndLocalDate(user, currentDate);
//        return (waterEntity != null) ? waterEntity.getWaterIntake() : 0.0;
//    }

    public Double getWaterIntakeForCurrentDate(User user, LocalDate currentDate) {
        WaterEntity waterEntity = waterEntityRepository.findByUserAndLocalDate(user, currentDate);
        return (waterEntity != null) ? waterEntity.calculateTotalWaterIntake() : 0.0;
    }
    // calculate water intake of the user
//    public Double calculateWaterIntake(User user, LocalDate date) {
//        List<WaterEntity> waterEntities = user.getWaterEntities();
//
//        // Assuming you want to calculate the total water intake for the specified date
//        double totalWaterIntake = 0.0;
//
//        for (WaterEntity waterEntity : waterEntities) {
//            if (waterEntity.getLocalDate().isEqual(date)) {
//                totalWaterIntake += waterEntity.getWaterIntake();
//            }
//        }
//        return totalWaterIntake;
//    }

// to save and update the water data of the user
//    public WaterEntity saveOrUpdateWaterEntity(String username, WaterEntity waterEntity1) {
//        // Find the user by username
//        Optional<User> userOptional = userRepository.findByEmail(username);
//
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//
//            // Find or create the WaterEntity for the specified date and user
//            LocalDate currentDate = LocalDate.now();
//            WaterEntity existingWaterEntity = waterEntityRepository.findByUserAndLocalDate(user, currentDate);
//
//            if (existingWaterEntity == null) {
//                // If no entity exists for the current date and user, create a new one
//                WaterEntity newWaterEntity = new WaterEntity();
//                newWaterEntity.setCupCapacity(waterEntity1.getCupCapacity());
//                newWaterEntity.setNoOfCups(waterEntity1.getNoOfCups());
//                newWaterEntity.setLocalDate(currentDate);
//                newWaterEntity.setUser(user);
//
//                return waterEntityRepository.save(newWaterEntity);
//            } else {
//                // If an entity already exists for the current date and user, update the existing one
//                existingWaterEntity.setCupCapacity(waterEntity1.getCupCapacity());
//                existingWaterEntity.setNoOfCups(waterEntity1.getNoOfCups());
//                return waterEntityRepository.save(existingWaterEntity);
//            }
//        } else {
//            throw new UserNotFoundException("User not found for username: " + username);
//        }
//    }

//    public WaterEntity saveOrUpdateWaterEntity1(String username, WaterEntity newWaterEntity) {
//        // Find the user by username
//        Optional<User> userOptional = userRepository.findByEmail(username);
//
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//
//            // Find or create the WaterEntity for the specified date and user
//            LocalDate currentDate = LocalDate.now();
//            LocalTime currentTime = LocalTime.now();
//
//            // Define the format for 12-hour clock
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
//
//            // Format the current time using the defined format
//            String formattedTime = currentTime.format(formatter);
//
//            WaterEntity existingWaterEntity = waterEntityRepository.findByUserAndLocalDate(user, currentDate);
//
//            if (existingWaterEntity == null) {
//                // If no entity exists for the current date and user, create a new one
//                WaterEntity waterEntity = new WaterEntity();
//                waterEntity.setCupCapacity(newWaterEntity.getCupCapacity());
//                waterEntity.setNoOfCups(newWaterEntity.getNoOfCups());
//                waterEntity.setUser(user);
//
//                // Create a new WaterEntry
//                WaterEntry entry = new WaterEntry();
//                entry.setLocalDate(currentDate);
//                entry.setLocalTime(formattedTime);
//                entry.setWaterIntake(calculateWaterIntake(waterEntity));
//
//                // Add the entry to the waterEntity and set water intake
//                waterEntity.addWaterEntry(entry);
//
//                // Save the new WaterEntity
//                WaterEntity savedWaterEntity = waterEntityRepository.save(waterEntity);
//
//                // Return the saved WaterEntity
//                return savedWaterEntity;
//            } else {
//                // If an entity already exists for the current date and user
//                // Create a new WaterEntry for the current time
//                WaterEntry entry = new WaterEntry();
//                entry.setLocalDate(currentDate);
//                entry.setLocalTime(formattedTime);
//                entry.setWaterIntake(calculateWaterIntake(existingWaterEntity));
//
//                // Add the entry to the existingWaterEntity and set water intake
//                existingWaterEntity.addWaterEntry(entry);
//
//                // Update cup capacity and number of cups
//                existingWaterEntity.setCupCapacity(newWaterEntity.getCupCapacity());
//                existingWaterEntity.setNoOfCups(newWaterEntity.getNoOfCups());
//
//                // Save the updated WaterEntity
//                WaterEntity savedWaterEntity = waterEntityRepository.save(existingWaterEntity);
//
//                // Return the saved WaterEntity
//                return savedWaterEntity;
//            }
//        } else {
//            throw new UserNotFoundException("User not found for username: " + username);
//        }
//    }

    public WaterEntity saveOrUpdateWaterEntity1(String username, WaterEntity newWaterEntity) {
        // Find the user by username
        Optional<User> userOptional = userRepository.findByEmail(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Find or create the WaterEntity for the specified date and user
            LocalDate currentDate = LocalDate.now();
            LocalTime currentTime = LocalTime.now();

            // Define the format for 12-hour clock
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");

            // Format the current time using the defined format
            String formattedTime = currentTime.format(formatter);

            WaterEntity existingWaterEntity = waterEntityRepository.findByUserAndLocalDate(user, currentDate);

            if (existingWaterEntity == null) {
                // If no entity exists for the current date and user, create a new one
                WaterEntity waterEntity = new WaterEntity();
                waterEntity.setCupCapacity(newWaterEntity.getCupCapacity());
                waterEntity.setNoOfCups(newWaterEntity.getNoOfCups());
                waterEntity.setUser(user);

                // Create a new WaterEntry
                WaterEntry entry = new WaterEntry();
                entry.setLocalDate(currentDate);
                entry.setLocalTime(formattedTime);
                entry.setWaterIntake(calculateWaterIntake(waterEntity));

                // Add the entry to the waterEntity and set water intake
                waterEntity.addWaterEntry(entry);

                // Save the new WaterEntity
                WaterEntity savedWaterEntity = waterEntityRepository.save(waterEntity);

                // Return the saved WaterEntity
                return savedWaterEntity;
            } else {
                // If an entity already exists for the current date and user
                // Create a new WaterEntry for the current time
                WaterEntry entry = new WaterEntry();
                entry.setLocalDate(currentDate);
                entry.setLocalTime(formattedTime);

                // Update cup capacity and number of cups
                existingWaterEntity.setCupCapacity(newWaterEntity.getCupCapacity());
                existingWaterEntity.setNoOfCups(newWaterEntity.getNoOfCups());

                // Recalculate water intake based on the updated cup capacity
                entry.setWaterIntake(calculateWaterIntake(existingWaterEntity));

                // Add the entry to the existingWaterEntity and set water intake
                existingWaterEntity.addWaterEntry(entry);

                // Save the updated WaterEntity
                WaterEntity savedWaterEntity = waterEntityRepository.save(existingWaterEntity);

                // Return the saved WaterEntity
                return savedWaterEntity;
            }
        } else {
            throw new UserNotFoundException("User not found for username: " + username);
        }
    }

    public WaterEntity saveOrUpdateWaterEntity(String username, WaterEntity newWaterEntity) {
        // Find the user by username
        Optional<User> userOptional = userRepository.findByEmail(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Find or create the WaterEntity for the specified date and user
            LocalDate currentDate = LocalDate.now();
            LocalTime localTime = LocalTime.now();
            WaterEntity existingWaterEntity = waterEntityRepository.findByUserAndLocalDate(user, currentDate);
            LocalTime currentTime = LocalTime.now();

            // Define the format for 12-hour clock
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");

            // Format the current time using the defined format
            String formattedTime = currentTime.format(formatter);

            if (existingWaterEntity == null) {
                // If no entity exists for the current date and user, create a new one
                WaterEntity waterEntity = new WaterEntity();
                waterEntity.setCupCapacity(newWaterEntity.getCupCapacity());
                waterEntity.setNoOfCups(newWaterEntity.getNoOfCups());
                waterEntity.setLocalDate(currentDate);
                waterEntity.setLocalTime(formattedTime);
                waterEntity.setUser(user);

                // Calculate and set the water intake based on the new data
                waterEntity.setWaterIntake(calculateWaterIntake(waterEntity));

                // Save the new WaterEntity
                WaterEntity savedWaterEntity = waterEntityRepository.save(waterEntity);

                // Return the saved WaterEntity
                return savedWaterEntity;
            } else {
                // If an entity already exists for the current date and user
                // Update cup capacity, number of cups, and recalculate water intake
                existingWaterEntity.setCupCapacity(newWaterEntity.getCupCapacity());
                existingWaterEntity.setNoOfCups(newWaterEntity.getNoOfCups());

                // Calculate and set the updated water intake
                existingWaterEntity.setWaterIntake(calculateWaterIntake(existingWaterEntity));

                // Save the updated WaterEntity
                WaterEntity savedWaterEntity = waterEntityRepository.save(existingWaterEntity);

                // Return the saved WaterEntity
                return savedWaterEntity;
            }
        } else {
            throw new UserNotFoundException("User not found for username: " + username);
        }
    }

    // Calculate water intake based on cup capacity and updated number of cups
    private Double calculateWaterIntake(WaterEntity waterEntity) {
        // Check for null and provide a default value of 0.0
        Double waterIntake1 = waterEntity.getWaterIntake() != null ? waterEntity.getWaterIntake() : 0.0;
        return waterIntake1 + (waterEntity.getCupCapacity() * waterEntity.getNoOfCups() / 1000.0);
    }





}