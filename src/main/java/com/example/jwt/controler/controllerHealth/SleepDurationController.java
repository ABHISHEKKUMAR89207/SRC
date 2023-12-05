package com.example.jwt.controler.controllerHealth;

import com.example.jwt.entities.dashboardEntity.healthTrends.SleepDuration;
import com.example.jwt.repository.repositoryHealth.SleepDurationRepository;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.serviceHealth.SleepDurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/creatSleepLog")
    public ResponseEntity<SleepDuration> createSleepLog(@RequestHeader("Auth") String tokenHeader,
                                        @RequestBody SleepDuration sleepDuration) {

        try {
            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);
            sleepDuration = sleepDurationService.addOrUpdateSleepDuration(sleepDuration, username);

            return new ResponseEntity<>(sleepDuration, HttpStatus.CREATED);
        }catch(Exception e){
            System.out.println("Username is "+ e);
            // Handle exceptions, e.g., validation errors or database errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // You can customize the error response as needed
        }
    }

    @GetMapping("/getAllSleep")
    public ResponseEntity<List<SleepDuration>> getAllSleepLogs(@RequestHeader("Auth") String tokenHeader) {

        try {
            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);
            List<SleepDuration> sleepDuration = sleepDurationService.getAllSleepLogs(username);

            return new ResponseEntity<>(sleepDuration, HttpStatus.CREATED);
        }catch(Exception e){
            System.out.println("Username is "+ e);
            // Handle exceptions, e.g., validation errors or database errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // You can customize the error response as needed
        }
    }

    // Other endpoints for updating, deleting, or retrieving specific sleep logs
}

