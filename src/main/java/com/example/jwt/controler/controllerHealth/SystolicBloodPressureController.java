package com.example.jwt.controler.controllerHealth;

import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.BloodPressure;
import com.example.jwt.entities.dashboardEntity.healthTrends.SystolicBloodPressure;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.repositoryHealth.BloodPressureRepository;
import com.example.jwt.repository.repositoryHealth.SystolicBloodPressureRepository;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.serviceHealth.BloodPressureService;
import com.example.jwt.service.serviceHealth.HealthTrendsService;
import com.example.jwt.service.serviceHealth.SystolicBloodPressureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/systolic-blood-pressure")
public class SystolicBloodPressureController {

    private final SystolicBloodPressureService systolicBloodPressureService;
    private final HealthTrendsService healthTrendsService;
    private final SystolicBloodPressureRepository systolicBloodPressureRepository;
    private final UserRepository userRepository;
    private final JwtHelper jwtHelper;


    @Autowired
    public SystolicBloodPressureController(SystolicBloodPressureService systolicBloodPressureService, HealthTrendsService healthTrendsService, JwtHelper jwtHelper, SystolicBloodPressureRepository systolicBloodPressureRepository, UserRepository userRepository) {
        this.systolicBloodPressureService = systolicBloodPressureService;
        this.healthTrendsService = healthTrendsService;
        this.jwtHelper = jwtHelper;
        this.systolicBloodPressureRepository = systolicBloodPressureRepository;
        this.userRepository = userRepository;
    }

    // add blood pressure of the specific user
    @PostMapping("/")
    public ResponseEntity<SystolicBloodPressure> SystolicBloodPressure(@RequestHeader("Auth") String tokenHeader, @RequestBody SystolicBloodPressure systolicBloodPressureValue) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            SystolicBloodPressure systolicBloodPressure = systolicBloodPressureService.addSystolicBloodPressure(systolicBloodPressureValue, username);

            return new ResponseEntity<>(systolicBloodPressure, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //get blood pressure by date
    @GetMapping("/get/{date}")
    public ResponseEntity<List<SystolicBloodPressure>> getHeartRateForUserAndDate(@RequestHeader("Auth") String tokenHeader, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<SystolicBloodPressure> systolicBloodPressures = systolicBloodPressureService.getSystolicBloodPressureForUserAndDate(user, date);
        return ResponseEntity.ok(systolicBloodPressures);
    }

    // get the user's data of blood pressure of one week ago
    @GetMapping("/get/one-week-ago")
    public ResponseEntity<List<SystolicBloodPressure>> getSystolicBloodPressureForUserOneWeekAgo(@RequestHeader("Auth") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        // Find the user by the username
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(7); // One week ago

        List<SystolicBloodPressure> systolicBloodPressures = systolicBloodPressureService.getSystolicBloodPressureForUserBetweenDates(user, startDate, endDate);
        return ResponseEntity.ok(systolicBloodPressures);
    }

    // get the user's data of blood pressure of one month ago
    @GetMapping("/get/one-month-ago")
    public ResponseEntity<List<SystolicBloodPressure>> getSystolicBloodPressureForUserOneMonthAgo(@RequestHeader("Auth") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        // Find the user by the username
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(1); // One month ago

        List<SystolicBloodPressure> systolicBloodPressures = systolicBloodPressureService.getSystolicBloodPressureForUserBetweenDates(user, startDate, endDate);
        return ResponseEntity.ok(systolicBloodPressures);
    }
}
