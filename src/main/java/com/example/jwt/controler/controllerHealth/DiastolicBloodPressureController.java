package com.example.jwt.controler.controllerHealth;

import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.DiastolicBloodPressure;
import com.example.jwt.entities.dashboardEntity.healthTrends.SystolicBloodPressure;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.repositoryHealth.DiastolicBloodPressureRepository;
import com.example.jwt.repository.repositoryHealth.SystolicBloodPressureRepository;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.serviceHealth.DiastolicBloodPressureService;
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
@RequestMapping("/api/diastolic-blood-pressure")
public class DiastolicBloodPressureController {

    private final DiastolicBloodPressureService diastolicBloodPressureService;
    private final HealthTrendsService healthTrendsService;
    private final DiastolicBloodPressureRepository diastolicBloodPressureRepository;
    private final UserRepository userRepository;
    private final JwtHelper jwtHelper;

    @Autowired
    public DiastolicBloodPressureController(DiastolicBloodPressureService diastolicBloodPressureService, HealthTrendsService healthTrendsService, JwtHelper jwtHelper, DiastolicBloodPressureRepository diastolicBloodPressureRepository, UserRepository userRepository) {
        this.diastolicBloodPressureService = diastolicBloodPressureService;
        this.healthTrendsService = healthTrendsService;
        this.jwtHelper = jwtHelper;
        this.diastolicBloodPressureRepository = diastolicBloodPressureRepository;
        this.userRepository = userRepository;
    }

    // add blood pressure of the specific user
    @PostMapping("/")
    public ResponseEntity<DiastolicBloodPressure> DiastolicBloodPressure(@RequestHeader("Auth") String tokenHeader, @RequestBody DiastolicBloodPressure diastolicBloodPressureValue) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            DiastolicBloodPressure diastolicBloodPressure = diastolicBloodPressureService.addDiastolicBloodPressure(diastolicBloodPressureValue, username);

            return new ResponseEntity<>(diastolicBloodPressure, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //get blood pressure by date
    @GetMapping("/get/{date}")
    public ResponseEntity<List<DiastolicBloodPressure>> getHeartRateForUserAndDate(@RequestHeader("Auth") String tokenHeader, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<DiastolicBloodPressure> diastolicBloodPressures = diastolicBloodPressureService.getDiastolicBloodPressureForUserAndDate(user, date);
        return ResponseEntity.ok(diastolicBloodPressures);
    }

    // get the user's data of blood pressure of one week ago
    @GetMapping("/get/one-week-ago")
    public ResponseEntity<List<DiastolicBloodPressure>> getDiastolicBloodPressureForUserOneWeekAgo(@RequestHeader("Auth") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        // Find the user by the username
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(7); // One week ago

        List<DiastolicBloodPressure> diastolicBloodPressures = diastolicBloodPressureService.getDiastolicBloodPressureForUserBetweenDates(user, startDate, endDate);
        return ResponseEntity.ok(diastolicBloodPressures);
    }

    // get the user's data of blood pressure of one month ago
    @GetMapping("/get/one-month-ago")
    public ResponseEntity<List<DiastolicBloodPressure>> getDiastolicBloodPressureForUserOneMonthAgo(@RequestHeader("Auth") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        // Find the user by the username
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(1); // One month ago

        List<DiastolicBloodPressure> diastolicBloodPressures = diastolicBloodPressureService.getDiastolicBloodPressureForUserBetweenDates(user, startDate, endDate);
        return ResponseEntity.ok(diastolicBloodPressures);
    }
}
