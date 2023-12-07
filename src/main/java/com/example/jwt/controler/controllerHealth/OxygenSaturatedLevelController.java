package com.example.jwt.controler.controllerHealth;

import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.OxygenSaturatedLevel;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.repositoryHealth.OxygenSaturatedLevelRepository;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.serviceHealth.HealthTrendsService;
import com.example.jwt.service.serviceHealth.OxygenSaturatedLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/oxygen-saturated-level")
public class OxygenSaturatedLevelController {

    private final OxygenSaturatedLevelService oxygenSaturatedLevelService;
    private final HealthTrendsService healthTrendsService;
    private final UserRepository userRepository;
    private final OxygenSaturatedLevelRepository oxygenSaturatedLevelRepository;
    private final JwtHelper jwtHelper;

    @Autowired
    public OxygenSaturatedLevelController(OxygenSaturatedLevelService oxygenSaturatedLevelService, HealthTrendsService healthTrendsService, JwtHelper jwtHelper, UserRepository userRepository, OxygenSaturatedLevelRepository oxygenSaturatedLevelRepository) {
        this.jwtHelper = jwtHelper;
        this.oxygenSaturatedLevelService = oxygenSaturatedLevelService;
        this.healthTrendsService = healthTrendsService;
        this.oxygenSaturatedLevelRepository = oxygenSaturatedLevelRepository;
        this.userRepository = userRepository;
    }

    // add heart rate of the user
    @PostMapping("/")
    public ResponseEntity<OxygenSaturatedLevel> addHeartRate(@RequestHeader("Auth") String tokenHeader, @RequestBody OxygenSaturatedLevel oxygenSaturatedLevelValue) {
        try {
            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);

            // Use the extracted username to associate the heart rate data with the user
            OxygenSaturatedLevel oxygenSaturatedLevel = oxygenSaturatedLevelService.addOxygenSaturatedLevel(oxygenSaturatedLevelValue, username);

            return new ResponseEntity<>(oxygenSaturatedLevel, HttpStatus.CREATED);
        } catch (Exception e) {
            // Handle exceptions, e.g., validation errors or database errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // You can customize the error response as needed
        }
    }

    //get blood glucose by date
    @GetMapping("/get/{date}")
    public ResponseEntity<List<OxygenSaturatedLevel>> getHeartRateForUserAndDate(@RequestHeader("Auth") String tokenHeader, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        // Find the user by the username, and associate the heart rate with that user
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<OxygenSaturatedLevel> oxygenSaturatedLevels = oxygenSaturatedLevelService.getOxygenSaturatedLevelForUserAndDate(user, date);
        return ResponseEntity.ok(oxygenSaturatedLevels);
    }

    // get the oxygen saturation for user of one week ago
    @GetMapping("/get/one-week-ago")
    public ResponseEntity<List<OxygenSaturatedLevel>> getOxygenSaturatedForUserOneWeekAgo(@RequestHeader("Auth") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(7); // One week ago

        List<OxygenSaturatedLevel> oxygenSaturatedLevel = oxygenSaturatedLevelService.getOxygenSaturatedLevelForUserBetweenDates(user, startDate, endDate);
        return ResponseEntity.ok(oxygenSaturatedLevel);
    }

    // get the oxygen saturation for user of one month ago
    @GetMapping("/get/one-month-ago")
    public ResponseEntity<List<OxygenSaturatedLevel>> getOxygenSaturatedForUserOneMonthAgo(@RequestHeader("Auth") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        // Find the user by the username
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(1); // One month ago

        List<OxygenSaturatedLevel> oxygenSaturatedLevel = oxygenSaturatedLevelService.getOxygenSaturatedLevelForUserBetweenDates(user, startDate, endDate);
        return ResponseEntity.ok(oxygenSaturatedLevel);
    }
}
