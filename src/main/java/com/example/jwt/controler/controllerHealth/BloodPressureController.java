package com.example.jwt.controler.controllerHealth;

import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.BloodPressure;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.repositoryHealth.BloodPressureRepository;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.serviceHealth.BloodPressureService;
import com.example.jwt.service.serviceHealth.HealthTrendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/blood-pressure")
public class BloodPressureController {

    private final BloodPressureService bloodPressureService;
    private final HealthTrendsService healthTrendsService;
    private final BloodPressureRepository bloodPressureRepository;
    private final UserRepository userRepository;
    private final JwtHelper jwtHelper;


    @Autowired
    public BloodPressureController(BloodPressureService bloodPressureService, HealthTrendsService healthTrendsService, JwtHelper jwtHelper, BloodPressureRepository bloodPressureRepository, UserRepository userRepository) {
        this.bloodPressureService = bloodPressureService;
        this.healthTrendsService = healthTrendsService;
        this.jwtHelper = jwtHelper;
        this.bloodPressureRepository = bloodPressureRepository;
        this.userRepository = userRepository;
    }

    // add blood pressure of the specific user
    @PostMapping("/")
    public ResponseEntity<BloodPressure> addBloodPressure(@RequestHeader("Auth") String tokenHeader, @RequestBody BloodPressure bloodPressureValue) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            BloodPressure bloodPressure = bloodPressureService.addBloodPressure(bloodPressureValue, username);

            return new ResponseEntity<>(bloodPressure, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //get blood pressure by date
    @GetMapping("/get/{date}")
    public ResponseEntity<List<BloodPressure>> getHeartRateForUserAndDate(@RequestHeader("Auth") String tokenHeader, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<BloodPressure> bloodPressures = bloodPressureService.getBloodPressureForUserAndDate(user, date);
        return ResponseEntity.ok(bloodPressures);
    }

    // get the user's data of blood pressure of one week ago
    @GetMapping("/get/one-week-ago")
    public ResponseEntity<List<BloodPressure>> getBloodPressureForUserOneWeekAgo(@RequestHeader("Auth") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        // Find the user by the username
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(7); // One week ago

        List<BloodPressure> bloodPressure = bloodPressureService.getBloodPressureForUserBetweenDates(user, startDate, endDate);
        return ResponseEntity.ok(bloodPressure);
    }

    // get the user's data of blood pressure of one month ago
    @GetMapping("/get/one-month-ago")
    public ResponseEntity<List<BloodPressure>> getBloodPressureForUserOneMonthAgo(@RequestHeader("Auth") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        // Find the user by the username
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(1); // One month ago

        List<BloodPressure> bloodPressure = bloodPressureService.getBloodPressureForUserBetweenDates(user, startDate, endDate);
        return ResponseEntity.ok(bloodPressure);
    }
}
