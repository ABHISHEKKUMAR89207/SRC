package com.example.jwt.entities.weight;



import com.example.jwt.entities.User;
import com.example.jwt.entities.weight.WeightManager;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/weight-manager")
public class WeightManagerController {

    @Autowired
    private WeightManagerService weightManagerService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper jwtHelper;

    // Save weight entry for a user on a specific date
//    @PostMapping("/save-weight")
//    public ResponseEntity<WeightManager> saveWeightEntry(
//            @RequestHeader("Auth") String tokenHeader,
//            @RequestParam Double weight,
//            @RequestParam String date
//    ) {
//        try {
//            String token = tokenHeader.replace("Bearer ", "");
//            String username = jwtHelper.getUsernameFromToken(token);
//            User user = userService.findByUsername(username);
//
//            // Parse the date string to LocalDate
//            LocalDate localDate = LocalDate.parse(date);
//
//            // Create a new WeightManager object
//            WeightManager weightManager = new WeightManager();
//            weightManager.setWeight(weight);
//            weightManager.setLocalDate(localDate);
//            weightManager.setUser(user);
//
//            // Save the weight entry
//            WeightManager savedWeightEntry = weightManagerService.saveWeightEntry(weightManager);
//
//            return new ResponseEntity<>(savedWeightEntry, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
    @PostMapping("/save-weight")
    public ResponseEntity<WeightManager> saveWeightEntry(
            @RequestHeader("Auth") String tokenHeader,
            @RequestParam Double weight,
            @RequestParam String date
    ) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            User user = userService.findByUsername(username);

            // Parse the date string to LocalDate
            LocalDate localDate = LocalDate.parse(date);

            // Check if an entry already exists for the given date
            WeightManager existingWeightEntry = weightManagerService.getWeightByUserAndDate(user, localDate);

            if (existingWeightEntry != null) {
                // Update the existing entry with the new weight
                existingWeightEntry.setWeight(weight);
                WeightManager updatedWeightEntry = weightManagerService.saveWeightEntry(existingWeightEntry);
                return new ResponseEntity<>(updatedWeightEntry, HttpStatus.OK);
            } else {
                // Create a new WeightManager object
                WeightManager weightManager = new WeightManager();
                weightManager.setWeight(weight);
                weightManager.setLocalDate(localDate);
                weightManager.setUser(user);

                // Save the weight entry
                WeightManager savedWeightEntry = weightManagerService.saveWeightEntry(weightManager);

                return new ResponseEntity<>(savedWeightEntry, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Autowired
    private WeightManagerRepository weightManagerRepository;
    public WeightManager getWeightByUserAndDate(User user, LocalDate date) {
        return weightManagerRepository.findByUserAndLocalDate(user, date);
    }


//    // Get all weight entries for a user
//    @GetMapping("/get-all-weights")
//    public ResponseEntity<List<WeightManager>> getAllWeightEntries(@RequestHeader("Auth") String tokenHeader) {
//        try {
//            String token = tokenHeader.replace("Bearer ", "");
//            String username = jwtHelper.getUsernameFromToken(token);
//            User user = userService.findByUsername(username);
//
//            // Get all weight entries for the user
//            List<WeightManager> weightEntries = weightManagerService.getAllWeightEntriesByUser(user);
//
//            return new ResponseEntity<>(weightEntries, HttpStatus.OK);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

    // WeightManagerController.java
    @GetMapping("/get-weight-by-custom-range")
    public ResponseEntity<List<WeightResponseDto>> getWeightByCustomRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestHeader("Auth") String tokenHeader) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            User user = userService.findByUsername(username);

            if (user != null) {
                List<WeightResponseDto> weightEntries = weightManagerService.getWeightByUserAndDateRange(user, startDate, endDate);
                return new ResponseEntity<>(weightEntries, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // WeightManagerController.java
    @GetMapping("/get-latest-weight")
    public ResponseEntity<WeightResponseDto> getLatestWeight(
            @RequestHeader("Auth") String tokenHeader) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            User user = userService.findByUsername(username);

            if (user != null) {
                WeightResponseDto latestWeightEntry = weightManagerService.getLatestWeightByUser(user);

                if (latestWeightEntry != null) {
                    return new ResponseEntity<>(latestWeightEntry, HttpStatus.OK);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
