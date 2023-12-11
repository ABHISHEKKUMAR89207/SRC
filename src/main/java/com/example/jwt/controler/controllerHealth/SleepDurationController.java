package com.example.jwt.controler.controllerHealth;

import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.SleepDuration;
import com.example.jwt.repository.repositoryHealth.SleepDurationRepository;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.UserService;
import com.example.jwt.service.serviceHealth.SleepDurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/sleep-logs")
public class SleepDurationController {
    private final SleepDurationRepository sleepDurationRepository;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private SleepDurationService sleepDurationService;

    @Autowired
    public SleepDurationController(SleepDurationRepository sleepDurationRepository) {
        this.sleepDurationRepository = sleepDurationRepository;
    }

    // creeate the sleep goal of the user
    @PutMapping("/creatSleepLog")
    public ResponseEntity<SleepDuration> createSleepLog(@RequestHeader("Auth") String tokenHeader, @RequestBody SleepDuration sleepDuration) {
        try {
            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);
            sleepDuration = sleepDurationService.addOrUpdateSleepDuration(sleepDuration, username);

            return new ResponseEntity<>(sleepDuration, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("Username is " + e);
            // Handle exceptions, e.g., validation errors or database errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // You can customize the error response as needed
        }
    }

    //get all the sleep recrd of the specific user
    @GetMapping("/getAllSleep")
    public ResponseEntity<List<SleepDuration>> getAllSleepLogs(@RequestHeader("Auth") String tokenHeader) {

        try {
            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);
            List<SleepDuration> sleepDuration = sleepDurationService.getAllSleepLogs(username);

            return new ResponseEntity<>(sleepDuration, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("Username is " + e);
            // Handle exceptions, e.g., validation errors or database errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // You can customize the error response as needed
        }
    }


    @GetMapping("/getSleepByDate")
    public ResponseEntity<SleepDuration> getSleepLogsByDate(
            @RequestHeader("Auth") String tokenHeader,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);

            // Call service method to get sleep logs by date
            Optional<SleepDuration> sleepDurationOptional = sleepDurationService.getSleepLogsByDate(username, date);

            if (sleepDurationOptional.isPresent()) {
                // Return sleep logs if present
                SleepDuration sleepDuration = sleepDurationOptional.get();
                return new ResponseEntity<>(sleepDuration, HttpStatus.OK);
            } else {
                // Return a not found response if sleep logs are not present
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
            // Handle exceptions, e.g., validation errors or database errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

//    @GetMapping("/getSleepByLastWeek")
//    public ResponseEntity<List<SleepDuration>> getSleepLogsForLastWeek(
//            @RequestHeader("Auth") String tokenHeader) {
//        try {
//            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//            String token = tokenHeader.replace("Bearer ", "");
//
//            // Extract the username (email) from the token
//            String username = jwtHelper.getUsernameFromToken(token);
//
//            // Call service method to get sleep logs for the last week
//            LocalDate endDate = LocalDate.now();
//            LocalDate startDate = endDate.minusDays(6); // Assuming you want data for the last 7 days
//
//            List<SleepDuration> sleepDuration = sleepDurationService.getSleepLogsBetweenDates(username, startDate, endDate);
//
//            return new ResponseEntity<>(sleepDuration, HttpStatus.OK);
//        } catch (Exception e) {
//            System.out.println("Error: " + e);
//            // Handle exceptions, e.g., validation errors or database errors
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }


    @Autowired
    private UserService userService;

    @GetMapping("/calculate-sleep-last-week")
    public ResponseEntity<Map<String, Double>> calculateWaterIntakeForLastWeek(@RequestHeader("Auth") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(username);

        Map<String, Double> waterIntakeMap = sleepDurationService.sleepForLastWeek(user);
        return new ResponseEntity<>(waterIntakeMap, HttpStatus.OK);
    }


}

