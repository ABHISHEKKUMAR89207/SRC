package com.example.jwt.controler.controllerHealth;

import com.example.jwt.dtos.BloodSisGlo;
import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.BloodGlucose;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.serviceHealth.BloodGlucoseService;
import com.example.jwt.service.serviceHealth.HealthTrendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/blood-glucose")
public class BloodGlucoseController {

    private final BloodGlucoseService bloodGlucoseService;
    private final HealthTrendsService healthTrendsService;
    private final UserRepository userRepository;
    private final JwtHelper jwtHelper;

    @Autowired
    public BloodGlucoseController(BloodGlucoseService bloodGlucoseService, HealthTrendsService healthTrendsService, JwtHelper jwtHelper, UserRepository userRepository) {
        this.bloodGlucoseService = bloodGlucoseService;
        this.healthTrendsService = healthTrendsService;
        this.jwtHelper = jwtHelper;
        this.userRepository = userRepository;
    }


//    @PostMapping("/")
//    public ResponseEntity<BloodGlucose> addBlooadGlucose(@RequestHeader("Auth") String tokenHeader, @RequestBody BloodGlucose bloodGlucoseValue) {
//        try {
//            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//            String token = tokenHeader.replace("Bearer ", "");
//            // Extract the username (email) from the token
//            String username = jwtHelper.getUsernameFromToken(token);
//            // Use the extracted username to associate the heart rate data with the user
//            BloodGlucose bloodGlucose1 = bloodGlucoseService.addBloodGlucose(bloodGlucoseValue, username);
//            return new ResponseEntity<>(bloodGlucose1, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
@PostMapping("/")
public ResponseEntity<BloodSisGlo> addBloodGlucose(@RequestHeader("Auth") String tokenHeader, @RequestBody BloodSisGlo bloodSisGlo) {
    try {
        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Convert BloodSisGlo to BloodGlucose
        BloodGlucose bloodGlucose = bloodGlucoseService.convertToBloodGlucose(bloodSisGlo);

        // Use the extracted username to associate the blood glucose data with the user
         bloodGlucoseService.addBloodGlucose(bloodGlucose, username);

        // Return the added object along with status code 201 (Created)
//        return ResponseEntity.status(HttpStatus.OK);
        return ResponseEntity.ok(bloodSisGlo);
    } catch (Exception e) {
        // Handle exceptions, e.g., validation errors or database errors
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}

    //get blood glucose by date
    @GetMapping("/get/{date}")
    public ResponseEntity<List<BloodGlucose>> getHeartRateForUserAndDate(@RequestHeader("Auth") String tokenHeader, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        // Find the user by the username, and associate the heart rate with that user
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<BloodGlucose> bloodGlucoses = bloodGlucoseService.getBloodGlucoseForUserAndDate(user, date);
        return ResponseEntity.ok(bloodGlucoses);
    }

    //formated structured
//    @GetMapping("/get/{date}")
//    public ResponseEntity<List<BloodSisGlo>> getBloodGlucoseUserAndDate(@RequestHeader("Auth") String tokenHeader, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
//        String token = tokenHeader.replace("Bearer ", "");
//        String username = jwtHelper.getUsernameFromToken(token);
//        // Find the user by the username, and associate the blood glucose with that user
//        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
//        if (user == null) {
//            return ResponseEntity.notFound().build();
//        }
//        List<BloodGlucose> bloodGlucoses = bloodGlucoseService.getBloodGlucoseForUserAndDate(user, date);
//
//        // Map the BloodGlucose objects to BloodGlucoseDTO objects
//        List<BloodSisGlo> bloodGlucoseDTOs = bloodGlucoses.stream()
//                .map(glucose -> new BloodSisGlo(glucose.getValue(), glucose.getLocalDate()))
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(bloodGlucoseDTOs);
//    }


    // get the user's data of blood glucose of one week ago
    @GetMapping("/get/one-week-ago")
    public ResponseEntity<List<BloodGlucose>> getBloodGlucoseForUserOneWeekAgo(@RequestHeader("Auth") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        // Find the user by the username
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(7);
        List<BloodGlucose> bloodGlucoses = bloodGlucoseService.getBloodGlucoseForUserBetweenDates(user, startDate, endDate);
        return ResponseEntity.ok(bloodGlucoses);
    }

    // get the user's data of blood glucose of one month ago
    @GetMapping("/get/one-month-ago")
    public ResponseEntity<List<BloodGlucose>> getBloodGlucoseForUserOneMonthAgo(@RequestHeader("Auth") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        // Find the user by the username
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(1); // One month ago
        List<BloodGlucose> bloodGlucoses = bloodGlucoseService.getBloodGlucoseForUserBetweenDates(user, startDate, endDate);
        return ResponseEntity.ok(bloodGlucoses);
    }
}
