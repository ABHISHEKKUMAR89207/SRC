package com.example.jwt.controler.controllerHealth;//package com.practice.springbootimportcsvfileapp.controller;

import com.example.jwt.entities.dashboardEntity.healthTrends.SleepTarget;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.serviceHealth.SleepDurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class SleepDurationController {

    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private SleepDurationService sleepDurationService;
    private final UserRepository userRepository;

    public SleepDurationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Set or update user's sleep target
    @PutMapping("/sleepTarget")
    public ResponseEntity<SleepTarget> setOrUpdateSleepTarget(@RequestHeader("Auth") String tokenHeader,
                                                              @RequestBody SleepTarget sleepDuration) {
        try {
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);

            // Use the extracted username to associate the heart rate data with the user
            sleepDuration = sleepDurationService.SleepTarget(sleepDuration, username);
            System.out.println("Username is "+ username);
            return new ResponseEntity<>(sleepDuration, HttpStatus.CREATED);
        } catch(Exception e)
          {  System.out.println("Username is "+ e);
            // Handle exceptions, e.g., validation errors or database errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // You can customize the error response as needed

          }
    }
}







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




