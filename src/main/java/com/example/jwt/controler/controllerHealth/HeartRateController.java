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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

        List<HeartRate> heartRates = heartRateService.getHeartRateForUserAndDatee(user, date);
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

    @GetMapping("/get-heart-rate/custom-range")
    public ResponseEntity<List<Map<String, Object>>> getHeartRateForCustomRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestHeader("Auth") String tokenHeader) {
        try {
            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);

            // Use the username to fetch the userId from your user service
            User user = userService.findByUsername(username);

            if (user != null) {
                // Get heart rates for the custom date range and user
                List<HeartRate> heartRates = heartRateService.getHeartRateForUserBetweenDates(user, startDate, endDate);

                // Create a map to store average heart rate for each day
                Map<LocalDate, Double> averageHeartRateMap = new HashMap<>();

                // Initialize the map with all dates in the range
                startDate.datesUntil(endDate.plusDays(1)).forEach(date -> averageHeartRateMap.put(date, 0.0));

                // Calculate the average heart rate for each available day
                for (HeartRate heartRate : heartRates) {
                    LocalDate date = heartRate.getLocalDate();
                    double value = heartRate.getValue();

                    // Update the average heart rate for the day
                    averageHeartRateMap.merge(date, value, (existingValue, newValue) -> (existingValue + newValue) / 2);
                }

                // Create a list to store results
                List<Map<String, Object>> result = new ArrayList<>();

                // Convert the averageHeartRateMap to the desired format
                averageHeartRateMap.forEach((date, averageHeartRate) -> {
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("date", date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    resultMap.put("value", averageHeartRate);
                    result.add(resultMap);
                });

                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
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


    @PostMapping("/record-weekly")
    public Map<LocalDate, HeartRate> recordHeartRateForPreviousWeek(
            @RequestHeader("Auth") String tokenHeader,
            @RequestBody Map<String, Double> heartRateData) {

        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the username to fetch the userId from your user service
        User user = userService.findByUsername(username);

        Map<LocalDate, HeartRate> recordedHeartRates = new HashMap<>();

        if (user != null) {
            for (Map.Entry<String, Double> entry : heartRateData.entrySet()) {
                LocalDate currentDate = LocalDate.parse(entry.getKey());
                Double heartRateValue = entry.getValue();

                HeartRate existingRecord =  heartRateService.getHeartRateForUserAndDate(user, currentDate);

                if (existingRecord != null) {
                    // If a record for the user and the specified date exists, update it with the new heart rate data.
                    existingRecord.setValue(heartRateValue);
                    existingRecord.setTimeStamp(LocalTime.now());
                    heartRateService.updateHeartRate(existingRecord);
                    recordedHeartRates.put(currentDate, existingRecord); // Add the updated record to the map
                } else {
                    // If no record exists for the specified date, create a new one.
                    HeartRate newRecord = new HeartRate();
                    newRecord.setUser(user);
                    newRecord.setValue(heartRateValue);
                    newRecord.setLocalDate(currentDate);
                    newRecord.setTimeStamp(LocalTime.now());
                    // Save the new record as a separate row in the database.
                    heartRateService.createHeartRate(newRecord);
                    recordedHeartRates.put(currentDate, newRecord); // Add the newly created record to the map
                }
            }
        } else {
            // Handle the case where the user with the provided userId is not found.
            return Collections.singletonMap(LocalDate.now(), null); // Placeholder for error handling
        }

        return recordedHeartRates;
    }

    @GetMapping("/weekly-heart")
    public ResponseEntity<Map<String, Double>> StepsForLastWeek(@RequestHeader("Auth") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(username);

        Map<String, Double> heartMap = HeartForLastWeek(user);
        return new ResponseEntity<>(heartMap, HttpStatus.OK);
    }
    public Map<String, Double> HeartForLastWeek(User user) {
        List<HeartRate> heartRate = user.getHeartRates();

        // Create a map to store water intake for each day
        Map<String, Double> heartRateMap = new HashMap<>();

        // Calculate the start date of the last week
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusWeeks(1);

        // Iterate over each day in the last week
        for (LocalDate currentDate = startDate; currentDate.isBefore(endDate); currentDate = currentDate.plusDays(1)) {
            double totalHeartRate = 0.0;

            // Calculate total water intake for the current day
            for (HeartRate heartRate1 : heartRate) {
                if (heartRate1.getLocalDate().isEqual(currentDate)) {
//                    totalWaterIntake += waterEntity.getCupCapacity() * waterEntity.getNoOfCups() / 1000.0;
                    totalHeartRate += heartRate1.getValue();

                }
            }
            // Store the result in the map with the day name
            heartRateMap.put(currentDate.getDayOfWeek().toString(), totalHeartRate);
        }

        return heartRateMap;
    }
}
