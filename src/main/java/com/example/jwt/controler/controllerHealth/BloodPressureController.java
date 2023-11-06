package com.example.jwt.controler.controllerHealth;

import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.BloodGlucose;
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
    public BloodPressureController(BloodPressureService bloodPressureService,
                                   HealthTrendsService healthTrendsService,
                                   JwtHelper jwtHelper,
                                   BloodPressureRepository bloodPressureRepository,
                                    UserRepository userRepository
    ) {
        this.bloodPressureService = bloodPressureService;
        this.healthTrendsService = healthTrendsService;
        this.jwtHelper = jwtHelper;
        this.bloodPressureRepository = bloodPressureRepository;
        this.userRepository = userRepository;
    }

    // Endpoint to fetch heart rate data by time
//    @GetMapping("/by-time")
//    public List<HeartRate> getHeartRatesByTime(@RequestParam LocalDateTime startTime, @RequestParam LocalDateTime endTime) {
//        // You can add validation and error handling here
//        return heartRateService.getHeartRatesByTime(startTime, endTime);
//    }

//
//    @PostMapping("/user/{userId}/posts")
//    public ResponseEntity<HealthTrends> createUserProfile(
//            @RequestBody HealthTrends healthTrends,
//            @PathVariable Long healthTrendId
//    ) {
////        return userProfileService.createUserProfile(userProfile);
//        HealthTrends userProfile1 = this.healthTrendsService.createUserProfile(healthTrends, healthTrendId);
//        return new ResponseEntity<HealthTrends>(userProfile1, HttpStatus.CREATED);
//    }


    //    @GetMapping("/daily")
//    public double calculateDailyHeartRate(@RequestParam("date") String date) {
//        LocalDate localDate = LocalDate.parse(date);
//        return heartRateService.calculateDailyHeartRate(localDate);
//    }


//    // Endpoint to fetch heart rate data by time
//    @GetMapping("/by-time")
//    public List<HeartRate> getHeartRatesByTime(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
//        // Use the existing method from HeartRateService to fetch heart rates between the given time range
//        return heartRateService.getHeartRatesByTime(startTime, endTime);
//    }
//
//    // Map to /createUserProfile/{healthTrendId} using POST
//    @PostMapping("/createUserProfile/{healthTrendId}")
//    public ResponseEntity<String> createUserProfile(
//            @RequestBody HeartRateRequest heartRateRequest,
//            @PathVariable Long healthTrendId
//    ) {
//        // Your implementation here
//        return ResponseEntity.ok("User profile created successfully.");
//    }

//    @PostMapping("/add/{healthTrendId}")
//    public ResponseEntity<BloodPressure> addBloodPressure(
//            @PathVariable Long healthTrendId,
//            @RequestBody BloodPressure bloodPressureValue
//    ) {
//        try {
//            // Save the heart rate data associated with the health trend
//            BloodPressure bloodPressure = bloodPressureService.addBloodPressure(bloodPressureValue, healthTrendId);
//            return new ResponseEntity<>(bloodPressure, HttpStatus.CREATED);
//        } catch (Exception e) {
//            // Handle exceptions, e.g., validation errors or database errors
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(null); // You can customize the error response as needed
//        }
//    }


    @PostMapping("/")
    public ResponseEntity<BloodPressure> addBloodPressure(@RequestHeader("Auth") String tokenHeader,
                                                  @RequestBody BloodPressure bloodPressureValue) {
        try {
            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);

            // Use the extracted username to associate the heart rate data with the user
            BloodPressure bloodPressure = bloodPressureService.addBloodPressure(bloodPressureValue, username);

            return new ResponseEntity<>(bloodPressure, HttpStatus.CREATED);
        } catch (Exception e) {
            // Handle exceptions, e.g., validation errors or database errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // You can customize the error response as needed
        }
    }



    //get blood pressure by date
    @GetMapping("/get/{date}")
    public ResponseEntity<List<BloodPressure>> getHeartRateForUserAndDate(@RequestHeader("Auth") String tokenHeader,
                                                                         @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

//        User user = userService.getUserByUsername(username);

        // Find the user by the username, and associate the heart rate with that user
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<BloodPressure> bloodPressures = bloodPressureService.getBloodPressureForUserAndDate(user, date);
        return ResponseEntity.ok(bloodPressures);
    }



    @GetMapping("/get/one-week-ago")
    public ResponseEntity<List<BloodPressure>> getBloodPressureForUserOneWeekAgo(@RequestHeader("Auth") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        // Find the user by the username
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(7); // One week ago

        List<BloodPressure> bloodPressure = bloodPressureService.getBloodPressureForUserBetweenDates(user, startDate, endDate);
        return ResponseEntity.ok(bloodPressure);
    }


    @GetMapping("/get/one-month-ago")
    public ResponseEntity<List<BloodPressure>> getBloodPressureForUserOneMonthAgo(@RequestHeader("Auth") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        // Find the user by the username
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(1); // One month ago

        List<BloodPressure> bloodPressure = bloodPressureService.getBloodPressureForUserBetweenDates(user, startDate, endDate);
        return ResponseEntity.ok(bloodPressure);
    }


}
