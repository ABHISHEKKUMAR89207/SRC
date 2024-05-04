package com.example.jwt.controler.controllerHealth;

import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.MenstrualCycle;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.repositoryHealth.MenstrualCycleRepository;
import com.example.jwt.request.MenstrualCycleRequest;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.UserService;
import com.example.jwt.service.serviceHealth.HealthTrendsService;
import com.example.jwt.service.serviceHealth.MenstrualCycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/menstrualCycle")
public class MenstrualCycleController {
    private final MenstrualCycleService menstrualCycleService;
    private final HealthTrendsService healthTrendsService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtHelper jwtHelper;
    private final MenstrualCycleRepository menstrualCycleRepository;

    @Autowired
    public MenstrualCycleController(MenstrualCycleService menstrualCycleService,
                                    HealthTrendsService healthTrendsService,
                                    JwtHelper jwtHelper,
                                    UserService userService,
                                    UserRepository userRepository,
                                    MenstrualCycleRepository menstrualCycleRepository
                                    ) {
        this.jwtHelper = jwtHelper;
        this.menstrualCycleRepository=menstrualCycleRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.menstrualCycleService = menstrualCycleService;
        this.healthTrendsService = healthTrendsService;
    }


//    @PostMapping
//    public ResponseEntity<MenstrualCycle> createUser(@RequestHeader("Auth") String tokenHeader,
//                                                     @RequestBody MenstrualCycle values) {
//        //MenstrualCycle createdUser = userService.createUser(user);
//
//        try {
//            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//            String token = tokenHeader.replace("Bearer ", "");
//
//            // Extract the username (email) from the token
//            String username = jwtHelper.getUsernameFromToken(token);
//
//            // Use the extracted username to associate the heart rate data with the user
//            // HeartRate heartRate = heartRateService.addHeartRate(heartRateValue, username);
////            MenstrualCycle menstrualDate =  menstrualCycleService.calculateNextPeriodStartDate(values,username);
//
//            MenstrualCycle menstrualDate =  menstrualCycleService.addMenstrualCycle(values,username);
//
//            return new ResponseEntity<>(menstrualDate, HttpStatus.CREATED);
//        } catch (Exception e) {
//            // Handle exceptions, e.g., validation errors or database errors
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(null); // You can customize the error response as needed
//        }
//
//    }

    //add meansture cycle only female
//    @PostMapping("/add")
//    public ResponseEntity<String> addMenstrualCycle(
//            @RequestHeader("Auth") String tokenHeader,
//            @RequestBody MenstrualCycle menstrualCycle) {
//        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        User user = userRepository.findByEmail(username)
//                .orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
//
//        if (user != null && user.getUserProfile() != null && user.getUserProfile().getGender().equalsIgnoreCase("Female")) {
//            // Set the user for the menstrualCycle
//            menstrualCycle.setUser(user);
//            // Set the userProfile for menstrualCycle
//            menstrualCycle.setUserProfile(user.getUserProfile());
//
//            // Save the menstrualCycle
//            MenstrualCycle savedMenstrualCycle = menstrualCycleRepository.save(menstrualCycle);
//
//            return ResponseEntity.ok("MenstrualCycle added successfully with ID: " + savedMenstrualCycle.getMenstrualCycle_id());
//        } else {
//            // Handle the case where the user is not found or has a gender other than "Female"
//            throw new UserNotFoundException("User not found for username: " + username + " or user's gender is not Female.");
//        }
//    }


    //calculate and get meansture cycle
    @GetMapping("/calculate-next-period-start-date")
    public ResponseEntity<MenstrualCycle> calculateNextPeriodStartDate(@RequestHeader("Auth") String tokenHeader) {
        try {
            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);

            // Calculate the next period start date
            MenstrualCycle nextPeriodStartDate = menstrualCycleService.calculateNextPeriodStartDate(username);

            return new ResponseEntity<>(nextPeriodStartDate, HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions, e.g., validation errors or database errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // You can customize the error response as needed
        }
    }



//calculate feature meansture cycle
//    @PutMapping("/calculate-next-period-start-date")
//    public ResponseEntity<String> calculateAndSetNextPeriodStartDate(@RequestHeader("Auth") String tokenHeader) {
//        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        User user = userRepository.findByEmail(username)
//                .orElseThrow(() -> new UserNotFoundException("User not found for email: " + username));
//
//        if (user.getUserProfile() != null && user.getUserProfile().getGender().equalsIgnoreCase("Female")) {
//            MenstrualCycle menstrualCycle = user.getUserProfile().getMenstrualCycle();
//
//            if (menstrualCycle != null) {
//                LocalDate lastPeriodStartDate = menstrualCycle.getLastPeriodStartDate();
//                int cycleLength = menstrualCycle.getAverageCycleLength();
//
//                // Calculate the next period start date
//                LocalDate nextPeriodStartDate = lastPeriodStartDate.plusDays(cycleLength);
//
//                // Check if the calculated date is in the future
//                LocalDate today = LocalDate.now();
//                if (nextPeriodStartDate.isAfter(today)) {
//                    // Update both last period start date and calculated date
//                    menstrualCycle.setLastPeriodStartDate(nextPeriodStartDate);
//                    menstrualCycle.setCalculatedDate(nextPeriodStartDate.plusDays(cycleLength));
//
//                    // Set the userProfile for menstrualCycle
//                    menstrualCycle.setUserProfile(user.getUserProfile());
//
//                    // Save the updated menstrualCycle
//                    menstrualCycleRepository.save(menstrualCycle);
//
//                    return ResponseEntity.ok("Next period start date calculated and updated successfully.");
//                } else {
//                    throw new IllegalArgumentException("Next period calculation resulted in a date in the past.");
//                }
//            }
//        }
//
//        throw new IllegalArgumentException("This service is only available for female users with a valid last period start date and cycle length.");
//    }


    @PutMapping("/update-mc")
    public ResponseEntity<String> updateMenstrualCycle(@RequestHeader("Auth") String tokenHeader) {
        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found for email: " + username));

        if (user.getUserProfile() != null && user.getUserProfile().getGender().equalsIgnoreCase("Female")) {
            MenstrualCycle menstrualCycle = user.getUserProfile().getMenstrualCycle();
            int cycleLength = menstrualCycle.getAverageCycleLength();

            if (menstrualCycle != null) {
                // Set both last period start date and calculated date to the current date
                LocalDate currentDate = LocalDate.now();
                menstrualCycle.setLastPeriodStartDate(currentDate);
                menstrualCycle.setCalculatedDate(currentDate.plusDays(cycleLength));

//                menstrualCycle.setCalculatedDate(currentDate);

                // Set the userProfile for menstrualCycle
                menstrualCycle.setUserProfile(user.getUserProfile());

                // Save the updated menstrualCycle
                menstrualCycleRepository.save(menstrualCycle);

                return ResponseEntity.ok("MenstrualCycle updated with the current date.");
            }
        }

        throw new IllegalArgumentException("This service is only available for female users with a valid last period start date and cycle length.");
    }



//    //update meansture cycle
//    @PutMapping("/update")
//    public ResponseEntity<String> updateMenstrualCycle(
//            @RequestHeader("Auth") String tokenHeader,
//            @RequestBody MenstrualCycle request) {
//
//        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        User user = userRepository.findByEmail(username)
//                .orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
//
//        MenstrualCycle existingMenstrualCycle = user.getUserProfile().getMenstrualCycle();
//
//        if (existingMenstrualCycle != null) {
//            if (request.getAverageCycleLength() != 0) {
//                existingMenstrualCycle.setAverageCycleLength(request.getAverageCycleLength());
//            }
//            if (request.getLastPeriodStartDate() != null) {
//                existingMenstrualCycle.setLastPeriodStartDate(request.getLastPeriodStartDate());
//            }
//
//            menstrualCycleRepository.save(existingMenstrualCycle);
//        } else {
//            MenstrualCycle newMenstrualCycle = new MenstrualCycle();
//            newMenstrualCycle.setAverageCycleLength(request.getAverageCycleLength());
//            newMenstrualCycle.setUser(user);
//            newMenstrualCycle.setUserProfile(user.getUserProfile());
//            if (request.getLastPeriodStartDate() != null) {
//                newMenstrualCycle.setLastPeriodStartDate(request.getLastPeriodStartDate());
//            }
//
//            user.getUserProfile().setMenstrualCycle(newMenstrualCycle);
//            userRepository.save(user);
//        }
//
//        return ResponseEntity.ok("MenstrualCycle updated successfully");
//    }
//
//


//    @PutMapping("/update")
//    public ResponseEntity<String> updateMenstrualCycle(
//            @RequestHeader("Auth") String tokenHeader,
//            @RequestBody MenstrualCycle request) {
//
//        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        User user = userRepository.findByEmail(username)
//                .orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
//
//        if (user.getUserProfile() != null && "Female".equalsIgnoreCase(user.getUserProfile().getGender())) {
//            MenstrualCycle existingMenstrualCycle = user.getUserProfile().getMenstrualCycle();
//
//
//
//            if (existingMenstrualCycle != null) {
//                if (request.getAverageCycleLength() != 0) {
//                    existingMenstrualCycle.setAverageCycleLength(request.getAverageCycleLength());
//                }
//                if (request.getLastPeriodStartDate() != null) {
//                    existingMenstrualCycle.setLastPeriodStartDate(request.getLastPeriodStartDate());
//                }
//
//                // Save the updated MenstrualCycle
//                menstrualCycleRepository.save(existingMenstrualCycle);
//            } else {
//                MenstrualCycle newMenstrualCycle = new MenstrualCycle();
//                newMenstrualCycle.setAverageCycleLength(request.getAverageCycleLength());
//                newMenstrualCycle.setUser(user);
//                newMenstrualCycle.setUserProfile(user.getUserProfile());
//                if (request.getLastPeriodStartDate() != null) {
//                    newMenstrualCycle.setLastPeriodStartDate(request.getLastPeriodStartDate());
//                }
//
//                // Save the new MenstrualCycle
//                existingMenstrualCycle = menstrualCycleRepository.save(newMenstrualCycle);
//                user.getUserProfile().setMenstrualCycle(existingMenstrualCycle);
//                userRepository.save(user);
//            }
//
//            return ResponseEntity.ok("MenstrualCycle updated successfully");
//        } else {
//            throw new IllegalArgumentException("This service is only available for female users.");
//        }
//    }

//    @PutMapping("/update")
//    public ResponseEntity<String> updateMenstrualCycle(
//            @RequestHeader("Auth") String tokenHeader,
//            @RequestBody MenstrualCycleRequest request) {
//
//        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        User user = userRepository.findByEmail(username)
//                .orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
//
//        if (user.getUserProfile() != null && "Female".equalsIgnoreCase(user.getUserProfile().getGender())) {
//            MenstrualCycle existingMenstrualCycle = user.getUserProfile().getMenstrualCycle();
//
//            if (existingMenstrualCycle != null) {
//                if (request.getAverageCycleLength() != 0) {
//                    existingMenstrualCycle.setAverageCycleLength(request.getAverageCycleLength());
//                }
//                if (request.getLastPeriodStartDate() != null) {
//                    existingMenstrualCycle.setLastPeriodStartDate(request.getLastPeriodStartDate());
//                }
//
//                // Calculate and set the calculatedDate based on lastPeriodStartDate and averageCycleLength
//                LocalDate calculatedDate = request.getLastPeriodStartDate().plusDays(request.getAverageCycleLength());
//                existingMenstrualCycle.setCalculatedDate(calculatedDate);
//
//                // Save the updated MenstrualCycle
//                menstrualCycleRepository.save(existingMenstrualCycle);
//            } else {
//                MenstrualCycle newMenstrualCycle = new MenstrualCycle();
//                newMenstrualCycle.setAverageCycleLength(request.getAverageCycleLength());
//                newMenstrualCycle.setUser(user);
//                newMenstrualCycle.setUserProfile(user.getUserProfile());
//                if (request.getLastPeriodStartDate() != null) {
//                    newMenstrualCycle.setLastPeriodStartDate(request.getLastPeriodStartDate());
//                }
//
//                // Calculate and set the calculatedDate based on lastPeriodStartDate and averageCycleLength
//                LocalDate calculatedDate = request.getLastPeriodStartDate().plusDays(request.getAverageCycleLength());
//                newMenstrualCycle.setCalculatedDate(calculatedDate);
//
//                // Save the new MenstrualCycle
//                existingMenstrualCycle = menstrualCycleRepository.save(newMenstrualCycle);
//                user.getUserProfile().setMenstrualCycle(existingMenstrualCycle);
//                userRepository.save(user);
//            }
//
//            return ResponseEntity.ok("MenstrualCycle updated successfully");
//        } else {
//            throw new IllegalArgumentException("This service is only available for female users.");
//        }
//    }


    @PutMapping("/update")
    public ResponseEntity<String> updateMenstrualCycle(
            @RequestHeader("Auth") String tokenHeader,
            @RequestBody MenstrualCycleRequest request) {

        return menstrualCycleService.updateMenstrualCycle(tokenHeader, request);
    }
    @PostMapping("/toggle-menstrual-cycle")
    public ResponseEntity<String> toggleMenstrualCycleStatus(
            @RequestHeader("Auth") String tokenHeader,
            @RequestHeader("status") String status
    ) {
        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found for email: " + username));

        if (user.getUserProfile() != null && user.getUserProfile().getGender().equalsIgnoreCase("Female")) {
            MenstrualCycle menstrualCycle = user.getUserProfile().getMenstrualCycle();

            // Check if MenstrualCycle is null
            if (menstrualCycle == null) {
                menstrualCycle = new MenstrualCycle();
                menstrualCycle.setUser(user);
                menstrualCycle.setUserProfile(user.getUserProfile());
            }

            // Update the isMenstrualCycleEnabled property
            if ("on".equalsIgnoreCase(status)) {
                menstrualCycle.setIsMenstrualCycleEnabled(true);
            } else if ("off".equalsIgnoreCase(status)) {
                menstrualCycle.setIsMenstrualCycleEnabled(false);
            }

            // Save the updated or new MenstrualCycle entity
            menstrualCycleRepository.save(menstrualCycle);

            return ResponseEntity.ok("Menstrual Cycle status updated.");
        }

        throw new IllegalArgumentException("This service is only available for female users with a valid gender.");
    }


//    // isEnableOrNot


    @GetMapping("/menstrual-cycle-status")
    public ResponseEntity<Boolean> getMenstrualCycleStatus(@RequestHeader("Auth") String tokenHeader) {
        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found for email: " + username));

        if (user.getUserProfile() != null && user.getUserProfile().getGender().equalsIgnoreCase("Female")) {
            MenstrualCycle menstrualCycle = user.getUserProfile().getMenstrualCycle();

            if (menstrualCycle != null) {
                // Retrieve the current menstrual cycle status from the database
                boolean isMenstrualCycleEnabled = menstrualCycle.isMenstrualCycleEnabled();
                return ResponseEntity.ok(isMenstrualCycleEnabled);
            } else {
                // Handle the case when there is no menstrual cycle information in the database
                throw new IllegalArgumentException("Menstrual cycle information not found.");
            }
        }

        throw new IllegalArgumentException("This service is only available for female users with a valid gender.");
    }



}