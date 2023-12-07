package com.example.jwt.controler.controllerHealth;

import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.HeartRate;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.UserService;
import com.example.jwt.service.serviceHealth.HealthTrendsService;
import com.example.jwt.service.serviceHealth.HeartRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/heart-rates")
public class HeartRateController {

    private final HeartRateService heartRateService;
    private final HealthTrendsService healthTrendsService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtHelper jwtHelper;

    @Autowired
    public HeartRateController(HeartRateService heartRateService, HealthTrendsService healthTrendsService, JwtHelper jwtHelper, UserService userService, UserRepository userRepository) {
        this.jwtHelper = jwtHelper;
        this.userRepository = userRepository;
        this.userService = userService;
        this.heartRateService = heartRateService;
        this.healthTrendsService = healthTrendsService;
    }

    //save heart rate by login user
    @PostMapping("/")
    public ResponseEntity<HeartRate> addHeartRate(@RequestHeader("Auth") String tokenHeader, @RequestBody HeartRate heartRateValue) {
        try {
            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);

            // Use the extracted username to associate the heart rate data with the user
            HeartRate heartRate = heartRateService.addHeartRate(heartRateValue, username);

            return new ResponseEntity<>(heartRate, HttpStatus.CREATED);
        } catch (Exception e) {
            // Handle exceptions, e.g., validation errors or database errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // You can customize the error response as needed
        }
    }

    //get heart rate by date
    @GetMapping("/get/{date}")
    public ResponseEntity<List<HeartRate>> getHeartRateForUserAndDate(@RequestHeader("Auth") String tokenHeader, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

//        User user = userService.getUserByUsername(username);

        // Find the user by the username, and associate the heart rate with that user
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<HeartRate> heartRates = heartRateService.getHeartRateForUserAndDate(user, date);
        return ResponseEntity.ok(heartRates);
    }

    // to get heath rate of the specific user of one week
    @GetMapping("/get/one-week-ago")
    public ResponseEntity<List<HeartRate>> getHeartRateForUserOneWeekAgo(@RequestHeader("Auth") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        // Find the user by the username
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(7); // One week ago

        List<HeartRate> heartRates = heartRateService.getHeartRateForUserBetweenDates(user, startDate, endDate);
        return ResponseEntity.ok(heartRates);
    }

    // to get  heath rate of the specific user of one month
    @GetMapping("/get/one-month-ago")
    public ResponseEntity<List<HeartRate>> getHeartRateForUserOneMonthAgo(@RequestHeader("Auth") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        // Find the user by the username
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(1); // One month ago

        List<HeartRate> heartRates = heartRateService.getHeartRateForUserBetweenDates(user, startDate, endDate);
        return ResponseEntity.ok(heartRates);
    }
}
