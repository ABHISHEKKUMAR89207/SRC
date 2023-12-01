package com.example.jwt.service;
import com.example.jwt.entities.User;
import com.example.jwt.entities.water.WaterEntity;
import com.example.jwt.entities.water.WaterGoal;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.WaterEntityRepository;
import com.example.jwt.repository.WaterGoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class waterService {

    @Autowired
    private UserRepository userRepository;

    private final WaterEntityRepository waterEntityRepository;
    private final WaterGoalRepository waterGoalRepository;


    @Autowired
    public waterService(WaterGoalRepository waterGoalRepository, WaterEntityRepository waterEntityRepository) {
        this.waterGoalRepository = waterGoalRepository;
        this.waterEntityRepository = waterEntityRepository;
    }



    public WaterGoal saveOrUpdateWaterGoal(String username, Double newWaterGoal) {
        // Find the user by username
        Optional<User> userOptional = userRepository.findByEmail(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Check if a goal already exists for the user's water entity
            WaterGoal existingGoal = waterGoalRepository.findByUser(user);

            if (existingGoal == null) {
                // If no goal exists, create a new one
                WaterGoal newWaterGoalEntity = new WaterGoal();
                newWaterGoalEntity.setWaterGoal(newWaterGoal);
                newWaterGoalEntity.setUser(user);

                return waterGoalRepository.save(newWaterGoalEntity);
            } else {
                // If a goal already exists, update the existing one
                existingGoal.setWaterGoal(newWaterGoal);
                return waterGoalRepository.save(existingGoal);
            }
        } else {
            // Handle the case where the user is not found
            // You can throw an exception, log a message, or handle it in another way based on your requirements.
            throw new UserNotFoundException("User not found for username: " + username);
        }
    }


//    private double calculateWaterIntake(int cupCapacity, int noOfCups) {
//        int ml = 0;
//            ml = cupCapacity * noOfCups;
//        return ml / 1000.0;
//    }
//
//


//    public Double calculateWaterIntake(User user) {
//
//
//        return user.getWaterEntity().getCupCapacity() * user.getWaterEntity().getNoOfCups()/1000.0;
//    }


    public Double calculateWaterIntake(User user) {
        List<WaterEntity> waterEntities = user.getWaterEntities();

        // Assuming you want to calculate the total water intake for all WaterEntities
        double totalWaterIntake = 0.0;

        for (WaterEntity waterEntity : waterEntities) {
            totalWaterIntake += waterEntity.getCupCapacity() * waterEntity.getNoOfCups() / 1000.0;
        }

        return totalWaterIntake;
    }




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



//    @Scheduled(fixedRate = 24 * 60 * 60 * 1000) // 24 hours in milliseconds
//    public void resetNumberOfCups() {
//        List<User> users = userRepository.findAll();
//
//        for (User user : users) {
//            WaterEntity userWater = user.getWater();
//            if (userWater != null) {
//                userWater.setNoOfCups(0);
//                // You can also update other attributes as needed
//                // e.g., update waterIntake, etc.
//                // userWater.setWaterIntake(calculateWaterIntake(userWater.getCupCapacity(), 0));
//                waterEntityRepository.save(userWater);
//            }
//        }
//    }



//    public void saveOrUpdateWaterEntity(String username, int cupCapacity, int noOfCups, LocalDate date) {
//        User user = userRepository.findByEmail(username)
//                .orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
//
//        WaterGoal waterGoal = user.getWaterGoal();
//
//        if (waterGoal == null) {
//            // If there is no WaterGoal for the user, create a new one
//            waterGoal = new WaterGoal();
//            waterGoal.setUser(user);
//            waterGoal.setWaterEntities(new ArrayList<>());
//            user.setWaterGoal(waterGoal);
//        }
//
//        // Check if there is already a WaterEntity for the given date
//        WaterEntity existingEntity = waterEntityRepository.findByWaterGoalAndLocalDate(waterGoal, date);
//
//        if (existingEntity == null) {
//            // If no entry exists for the given date, create a new one
//            WaterEntity newWaterEntity = new WaterEntity();
//            newWaterEntity.setCupCapacity(cupCapacity);
//            newWaterEntity.setNoOfCups(noOfCups);
//            newWaterEntity.setLocalDate(date);
//            newWaterEntity.setWaterGoal(waterGoal);
//
//            waterEntityRepository.save(newWaterEntity);
//        } else {
//            // If an entry already exists for the given date, update it
//            existingEntity.setCupCapacity(cupCapacity);
//            existingEntity.setNoOfCups(noOfCups);
//            waterEntityRepository.save(existingEntity);
//        }
//    }

//    public WaterEntity saveOrUpdateWaterEntity(String username, WaterEntity waterEntity1) {
//        // Find the user by username
//        Optional<User> userOptional = userRepository.findByEmail(username);
//
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//
//            // Find or create the WaterEntity for the specified date
//            LocalDate currentDate = LocalDate.now();
//            WaterEntity existingWaterEntity = waterEntityRepository.findByUserAndLocalDate(user, currentDate);
//
//            if (existingWaterEntity == null) {
//                // If no entity exists for the current date, create a new one
//                WaterEntity newWaterEntity = new WaterEntity();
//                newWaterEntity.setCupCapacity(waterEntity1.getCupCapacity());
//                newWaterEntity.setNoOfCups(waterEntity1.getNoOfCups());
//                newWaterEntity.setLocalDate(currentDate);
//                newWaterEntity.setUser(user);
//
//                // Create a new WaterGoal for the user
//                WaterGoal newWaterGoal = new WaterGoal();
//                // Set other WaterGoal properties as needed
//                newWaterGoal.setUser(user);
//
////                newWaterEntity.setWaterGoal(newWaterGoal);
//
//                return waterEntityRepository.save(newWaterEntity);
//            } else {
//                // If an entity already exists for the current date, update the existing one
//                existingWaterEntity.setCupCapacity(waterEntity1.getCupCapacity());
//                existingWaterEntity.setNoOfCups(waterEntity1.getNoOfCups());
//
//                return waterEntityRepository.save(existingWaterEntity);
//            }
//        } else {
//            throw new UserNotFoundException("User not found for username: " + username);
//        }
//    }


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

                // Set other WaterEntity properties as needed
                // ...

                return waterEntityRepository.save(newWaterEntity);
            } else {
                // If an entity already exists for the current date and user, update the existing one
                existingWaterEntity.setCupCapacity(waterEntity1.getCupCapacity());
                existingWaterEntity.setNoOfCups(waterEntity1.getNoOfCups());

                // Set other WaterEntity properties as needed
                // ...

                return waterEntityRepository.save(existingWaterEntity);
            }
        } else {
            throw new UserNotFoundException("User not found for username: " + username);
        }
    }


//    public WaterEntity saveOrUpdateWaterEntity(String username, WaterEntity waterEntity1) {
//        // Find the user by username
//        Optional<User> userOptional = userRepository.findByEmail(username);
//
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//
//            // Find the WaterGoal for the user
//            WaterGoal waterGoal = waterGoalRepository.findByUser(user);
//
//            // Find or create the WaterEntity for the specified date
//            LocalDate currentDate = LocalDate.now();
////            WaterEntity waterEntity = waterEntityRepository.findByWaterGoalAndLocalDate(waterGoal, currentDate);
//
//            if (waterEntity == null) {
//                // If no entity exists for the current date, create a new one
//                waterEntity = new WaterEntity();
//                waterEntity.setCupCapacity(waterEntity1.getCupCapacity()); // Set the cupCapacity in milliliters
//                waterEntity.setNoOfCups(waterEntity1.getNoOfCups());
//                waterEntity.setLocalDate(currentDate);
//                waterEntity.setUser(waterGoal.getUser());
//
//                return waterEntityRepository.save(waterEntity);
//            } else {
//                // If an entity already exists for the current date, update the existing one
//                waterEntity.setCupCapacity(waterEntity1.getCupCapacity()); // Update the cupCapacity in milliliters
//                waterEntity.setNoOfCups(waterEntity1.getNoOfCups());
//
//                return waterEntityRepository.save(waterEntity);
//            }
//        } else {
//            throw new UserNotFoundException("User not found for username: " + username);
//        }
//    }
//



//    public void processUserWaterIntake(String username, LocalDate currentDate) {
//        // Step 1: Retrieve the user's water goal information
//        WaterGoal waterGoal = waterGoalRepository.findByUser_email(username);
//
//        if (waterGoal != null) {
//            // Step 2: Check if there's a water entry for the current date
//            WaterEntity waterEntry = waterEntityRepository.findByLocalDateAndWaterGoal(currentDate, waterGoal);
//
//            if (waterEntry == null) {
//                // If no entry for the current date, create a new one
//                WaterEntity newWaterEntry = new WaterEntity();
//                newWaterEntry.setLocalDate(currentDate);
//                newWaterEntry.setWaterGoal(waterGoal);
//                // Set other properties as needed
//
//                waterEntityRepository.save(newWaterEntry);
//            } else {
//                // If an entry for the current date exists, update it
//                // Update properties as needed
//                waterEntityRepository.save(waterEntry);
//            }
//
//            // Step 3: Calculate the user's current water intake
//            int cupCapacity = waterGoal.getCupCapacity();
//            int noOfCups = waterGoal.getNoOfCups();
//            Double waterIntake = calculateWaterIntake(cupCapacity, noOfCups);
//
//            // Step 4: Display a completion message
//            if (waterIntake >= waterGoal.getWaterGoal()) {
//                System.out.println("Congratulations! You have completed your water goal for the day.");
//            }
//        } else {
//            throw new UserNotFoundException("User not found for username: " + username);
//        }
//    }
//
//    private Double calculateWaterIntake(int cupCapacity, int noOfCups) {
//        // Calculate the total water intake based on cup capacity and number of cups
//        return cupCapacity * noOfCups;
//    }
}









