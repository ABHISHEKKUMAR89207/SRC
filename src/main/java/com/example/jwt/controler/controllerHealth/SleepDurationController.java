package com.example.jwt.controler.controllerHealth;//package com.practice.springbootimportcsvfileapp.controller;
//
//import com.practice.springbootimportcsvfileapp.entities.SleepDuration;
//import com.practice.springbootimportcsvfileapp.service.SleepDurationService;
//import jakarta.persistence.EntityNotFoundException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/health-trends/{healthTrendId}/sleep-durations")
//public class SleepDurationController {
//
//    @Autowired
//    private SleepDurationService sleepDurationService;
//
//    // GET Request to retrieve all sleep durations for a health trend by its ID
//    @GetMapping
//    public ResponseEntity<List<SleepDuration>> getSleepDurationsByHealthTrendId(
//            @PathVariable Long healthTrendId) {
//        try {
//            List<SleepDuration> sleepDurations = sleepDurationService.calculateAndSaveTotalSleepDuration(healthTrendId);
//            return ResponseEntity.ok(sleepDurations);
//        } catch (EntityNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//    }
//
//    // POST Request to add a new sleep duration for a health trend by its ID
//    @PostMapping
//    public ResponseEntity<Double> addSleepDurationToHealthTrend(
//            @PathVariable Long healthTrendId,
//            @RequestBody SleepDuration sleepDuration) {
//        try {
//            // You can use the healthTrendId to associate the sleep duration with the health trend
//            // Assuming that sleepDuration has the necessary fields set, including the health trend association.
//            // You should also validate and save the sleep duration.
//            double newSleepDuration = sleepDurationService.calculateAndSaveTotalSleepDuration(sleepDuration);
//            return ResponseEntity.ok(newSleepDuration);
//        } catch (EntityNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//    }
//}


import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.SleepDuration;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.UserService;
import com.example.jwt.service.serviceHealth.SleepDurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
    @RequestMapping("/api/sleep-durations")
public class SleepDurationController {

    @Autowired
    private SleepDurationService sleepDurationService;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserService userService;

//    @GetMapping("/calculate/{date}")
//    public ResponseEntity<Double> calculateAndSaveSleepDurationForDate(@PathVariable String date) {
//        try {
//            LocalDate parsedDate = LocalDate.parse(date);
//            double totalSleepDuration = sleepDurationService.calculateAndSaveTotalSleepDurationForDate(parsedDate);
//            return ResponseEntity.ok(totalSleepDuration);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//    }
//
//    @GetMapping("/by-date/{date}")
//    public ResponseEntity<List<SleepDuration>> getSleepDurationsForDate(@PathVariable String date) {
//        try {
//            LocalDate parsedDate = LocalDate.parse(date);
//            List<SleepDuration> sleepDurations = sleepDurationService.getSleepDurationsForDate(parsedDate);
//            return ResponseEntity.ok(sleepDurations);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//    }

//    @PostMapping
//    public ResponseEntity<Double> addSleepDuration(@RequestBody SleepDuration sleepDuration) {
//        try {
//            double newSleepDuration = sleepDurationService.calculateAndSaveTotalSleepDuration(sleepDuration);
//            return ResponseEntity.ok(newSleepDuration);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//    }

//    @PostMapping("/")
//    public ResponseEntity<SleepDuration> addHeartRate(@RequestHeader("Auth") String tokenHeader,
//                                                  @RequestBody SleepDuration sleepDurationValue) {
//        try {
//            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//            String token = tokenHeader.replace("Bearer ", "");
//
//            // Extract the username (email) from the token
//            String username = jwtHelper.getUsernameFromToken(token);
//
//            // Use the extracted username to associate the heart rate data with the user
//            SleepDuration sleepDuration = sleepDurationService.addSleepDuration(sleepDurationValue, username);
//
//            return new ResponseEntity<>(sleepDuration, HttpStatus.CREATED);
//        } catch (Exception e) {
//            // Handle exceptions, e.g., validation errors or database errors
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(null); // You can customize the error response as needed
//        }
//    }

//    @PostMapping ("/enable-sleep-time-recording")
//    public ResponseEntity<String> enableSleepTimeRecording(@RequestHeader("Auth") String tokenHeader) {
//        try {
//            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//            String token = tokenHeader.replace("Bearer ", "");
//
//            // Extract the username (email) from the token
//            String username = jwtHelper.getUsernameFromToken(token);
//
//            // Find the user by username
//            User user = userService.findByUsername(username);
//
//            if (user != null) {
//                // Toggle the sleep time recording status
//                user.setSleepTimeRecordingEnabled(!user.isSleepTimeRecordingEnabled());
//
//                // Save the updated user
//                userService.saveUser(user);
//
//                return ResponseEntity.ok("Sleep time recording is now " + (user.isSleepTimeRecordingEnabled() ? "enabled" : "disabled"));
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//            }
//        } catch (Exception e) {
//            // Handle exceptions, e.g., token validation failure
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token validation failed");
//        }
//    }


}

