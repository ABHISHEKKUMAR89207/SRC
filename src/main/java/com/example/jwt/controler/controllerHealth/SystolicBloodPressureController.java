package com.example.jwt.controler.controllerHealth;

import com.example.jwt.dtos.FoodTodayDtos.BloodPressureResponse;
import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.BloodPressure;
import com.example.jwt.entities.dashboardEntity.healthTrends.DiastolicBloodPressure;
import com.example.jwt.entities.dashboardEntity.healthTrends.SystolicBloodPressure;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.repositoryHealth.BloodPressureRepository;
import com.example.jwt.repository.repositoryHealth.SystolicBloodPressureRepository;
import com.example.jwt.request.BloodPressureRequest;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.serviceHealth.BloodPressureService;
import com.example.jwt.service.serviceHealth.DiastolicBloodPressureService;
import com.example.jwt.service.serviceHealth.HealthTrendsService;
import com.example.jwt.service.serviceHealth.SystolicBloodPressureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

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
//    @PostMapping("/")
//    public ResponseEntity<SystolicBloodPressure> SystolicBloodPressure(@RequestHeader("Auth") String tokenHeader, @RequestBody SystolicBloodPressure systolicBloodPressureValue) {
//        try {
//            String token = tokenHeader.replace("Bearer ", "");
//            String username = jwtHelper.getUsernameFromToken(token);
//            SystolicBloodPressure systolicBloodPressure = systolicBloodPressureService.addSystolicBloodPressure(systolicBloodPressureValue, username);
//
//            return new ResponseEntity<>(systolicBloodPressure, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
    @Autowired
    private DiastolicBloodPressureService diastolicBloodPressureService;
//    @PostMapping("/bloodpressure")
//    public ResponseEntity<Map<String, Object>> addBloodPressure(@RequestHeader("Auth") String tokenHeader, @RequestBody BloodPressureRequest bloodPressureRequest) {
//        try {
//            String token = tokenHeader.replace("Bearer ", "");
//            String username = jwtHelper.getUsernameFromToken(token);
//
//            // Retrieve systolic and diastolic blood pressure values from the request
//            SystolicBloodPressure systolicBloodPressure = bloodPressureRequest.getSystolicBloodPressure();
//            DiastolicBloodPressure diastolicBloodPressure = bloodPressureRequest.getDiastolicBloodPressure();
//
//            // Add systolic blood pressure
//            SystolicBloodPressure savedSystolicBP = systolicBloodPressureService.addSystolicBloodPressure(systolicBloodPressure, username);
//
//            // Add diastolic blood pressure
//            DiastolicBloodPressure savedDiastolicBP = diastolicBloodPressureService.addDiastolicBloodPressure(diastolicBloodPressure, username);
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("systolicBloodPressure", savedSystolicBP);
//            response.put("diastolicBloodPressure", savedDiastolicBP);
//
//            return new ResponseEntity<>(response, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//}
//    @PostMapping("/bloodpressure")
//    public ResponseEntity<Map<String, Object>> addBloodPressure(@RequestHeader("Auth") String tokenHeader, @RequestBody BloodPressureRequest bloodPressureRequest) {
//        try {
//            String token = tokenHeader.replace("Bearer ", "");
//            String username = jwtHelper.getUsernameFromToken(token);
//
//            // Retrieve systolic and diastolic blood pressure values from the request
//            SystolicBloodPressure systolicBloodPressure = bloodPressureRequest.getSystolicBloodPressure();
//            DiastolicBloodPressure diastolicBloodPressure = bloodPressureRequest.getDiastolicBloodPressure();
//
//            // Add systolic blood pressure
//            SystolicBloodPressure savedSystolicBP = systolicBloodPressureService.addSystolicBloodPressure(systolicBloodPressure, username);
//
//            // Add diastolic blood pressure
////            DiastolicBloodPressure savedDiastolicBP = diastolicBloodPressureService.addDiastolicBloodPressure(diastolicBloodPressure, username);
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("systolicBloodPressure", savedSystolicBP);
////            response.put("diastolicBloodPressure", savedDiastolicBP);
//
//            return new ResponseEntity<>(response, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
@PostMapping("/bloodpressure")
public ResponseEntity<SystolicBloodPressure> addSisAndDis(@RequestHeader("Auth") String tokenHeader, @RequestBody BloodPressureRequest bloodPressureRequest) {
    try {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        // Fetch the user from the database
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        // Extract systolic and diastolic blood pressure values from the request
        double systolicValue = bloodPressureRequest.getSystolicValue();
        double diastolicValue = bloodPressureRequest.getDiastolicValue();
        LocalDate date = bloodPressureRequest.getLocalDate();

        // Create a new SystolicBloodPressure object
        SystolicBloodPressure systolicBloodPressure = new SystolicBloodPressure();
        systolicBloodPressure.setSystolicValue(systolicValue);
        systolicBloodPressure.setDiastolicValue(diastolicValue);
        systolicBloodPressure.setLocalDate(date);
        systolicBloodPressure.setUser(user);

        // Save the systolic blood pressure
        SystolicBloodPressure savedSystolicBP = systolicBloodPressureRepository.save(systolicBloodPressure);

        return new ResponseEntity<>(savedSystolicBP, HttpStatus.CREATED);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}


//    @GetMapping("/gett/{date}")
//    public ResponseEntity<BloodPressureResponse> getBloodPressureForUserAndDate(@RequestHeader("Auth") String tokenHeader, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
//        try {
//            String token = tokenHeader.replace("Bearer ", "");
//            String username = jwtHelper.getUsernameFromToken(token);
//            User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
//            if (user == null) {
//                return ResponseEntity.notFound().build();
//            }
//
//            List<SystolicBloodPressure> systolicBloodPressures = systolicBloodPressureService.getSystolicBloodPressureForUserAndDate(user, date);
////            List<DiastolicBloodPressure> diastolicBloodPressures = diastolicBloodPressureService.getDiastolicBloodPressureForUserAndDate(user, date);
//
//            BloodPressureResponse response = new BloodPressureResponse(systolicBloodPressures);
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

//    @GetMapping("/get-sis-dis/{date}")
//    public ResponseEntity<List<BloodPressureResponse>> getBloodPressureForUserAndDate(@RequestHeader("Auth") String tokenHeader, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
//        try {
//            String token = tokenHeader.replace("Bearer ", "");
//            String username = jwtHelper.getUsernameFromToken(token);
//            User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
//
//            if (user == null) {
//                return ResponseEntity.notFound().build();
//            }
//
//            List<SystolicBloodPressure> systolicBloodPressures = systolicBloodPressureService.getSystolicBloodPressureForUserAndDate(user, date);
//            List<BloodPressureResponse> responseList = new ArrayList<>();
//
//            for (SystolicBloodPressure systolicBloodPressure : systolicBloodPressures) {
//                BloodPressureResponse response = new BloodPressureResponse();
//                response.setTimeStamp(systolicBloodPressure.getTimeStamp());
//                response.setSystolicValue(systolicBloodPressure.getSystolicValue());
//                response.setDiastolicValue(systolicBloodPressure.getDiastolicValue());
//                responseList.add(response);
//            }
//
//            return ResponseEntity.ok(responseList);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

    @GetMapping("/get-sis-dis/{date}")
    public ResponseEntity<List<BloodPressureResponse>> getBloodPressureForUserAndDate(
            @RequestHeader("Auth") String tokenHeader,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            List<SystolicBloodPressure> systolicBloodPressures = systolicBloodPressureService.getSystolicBloodPressureForUserAndDate(user, date);
            List<BloodPressureResponse> responseList = new ArrayList<>();

            for (SystolicBloodPressure systolicBloodPressure : systolicBloodPressures) {
                BloodPressureResponse response = new BloodPressureResponse();
                response.setLocalDate(systolicBloodPressure.getLocalDate());
                response.setSystolicValue(systolicBloodPressure.getSystolicValue());
                response.setDiastolicValue(systolicBloodPressure.getDiastolicValue());
                responseList.add(response);
            }

            // Sort the responseList based on the timestamp in descending order
            responseList.sort(Comparator.comparing(BloodPressureResponse::getLocalDate).reversed());

            return ResponseEntity.ok(responseList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //    @GetMapping("/getByDateRange")
//    public ResponseEntity<BloodPressureResponse> getBloodPressureByDateRange(
//            @RequestHeader("Auth") String tokenHeader,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
//
//        try {
//            String token = tokenHeader.replace("Bearer ", "");
//            String username = jwtHelper.getUsernameFromToken(token);
//            User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
//
//            if (user == null) {
//                return ResponseEntity.notFound().build();
//            }
//
//            List<SystolicBloodPressure> systolicBloodPressures = systolicBloodPressureService.getSystolicBloodPressureByDateRange(user, startDate, endDate);
//            List<DiastolicBloodPressure> diastolicBloodPressures = diastolicBloodPressureService.getDiastolicBloodPressureByDateRange(user, startDate, endDate);
//
//            BloodPressureResponse response = new BloodPressureResponse(systolicBloodPressures, diastolicBloodPressures);
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//@GetMapping("/getByDateRange")
//public ResponseEntity<Map<LocalDate, BloodPressureResponse>> getBloodPressureByDateRange(
//        @RequestHeader("Auth") String tokenHeader,
//        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
//
//    try {
//        String token = tokenHeader.replace("Bearer ", "");
//        String username = jwtHelper.getUsernameFromToken(token);
//        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
//
//        if (user == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        Map<LocalDate, BloodPressureResponse> responseMap = new TreeMap<>(Comparator.reverseOrder()); // Use TreeMap with reverse order comparator
//        LocalDate currentDate = endDate; // Start from endDate
//        while (!currentDate.isBefore(startDate)) { // Continue until after startDate
//            List<SystolicBloodPressure> systolicBloodPressures = systolicBloodPressureService.getSystolicBloodPressureForUserAndDate(user, currentDate);
////            List<DiastolicBloodPressure> diastolicBloodPressures = diastolicBloodPressureService.getDiastolicBloodPressureForUserAndDate(user, currentDate);
////            BloodPressureResponse response = new BloodPressureResponse(systolicBloodPressures, diastolicBloodPressures);
//            BloodPressureResponse response = new BloodPressureResponse(systolicBloodPressures);
//            responseMap.put(currentDate, response);
//            currentDate = currentDate.minusDays(1); // Move to the previous date
//        }
//
//        return ResponseEntity.ok(responseMap);
//    } catch (Exception e) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//    }
//}


//    @GetMapping("/getByDateRange")
//    public ResponseEntity<List<BloodPressureResponse>> getBloodPressureByDateRange(
//            @RequestHeader("Auth") String tokenHeader,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
//
//        try {
//            String token = tokenHeader.replace("Bearer ", "");
//            String username = jwtHelper.getUsernameFromToken(token);
//            User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
//
//            if (user == null) {
//                return ResponseEntity.notFound().build();
//            }
//
//            List<BloodPressureResponse> responseList = new ArrayList<>();
//            LocalDate currentDate = endDate; // Start from endDate
//            while (!currentDate.isBefore(startDate)) { // Continue until after startDate
//                List<SystolicBloodPressure> systolicBloodPressures = systolicBloodPressureService.getSystolicBloodPressureForUserAndDate(user, currentDate);
//                for (SystolicBloodPressure systolicBloodPressure : systolicBloodPressures) {
//                    BloodPressureResponse response = new BloodPressureResponse(
//                            systolicBloodPressure.getTimeStamp(),
//                            systolicBloodPressure.getSystolicValue(),
//                            systolicBloodPressure.getDiastolicValue()
//                    );
//                    responseList.add(response);
//                }
//                currentDate = currentDate.minusDays(1); // Move to the previous date
//            }
//
//            return ResponseEntity.ok(responseList);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

//    @GetMapping("/getByDateRange")
//    public ResponseEntity<List<BloodPressureResponse>> getBloodPressureByDateRange(
//            @RequestHeader("Auth") String tokenHeader,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
//
//        try {
//            String token = tokenHeader.replace("Bearer ", "");
//            String username = jwtHelper.getUsernameFromToken(token);
//            User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
//
//            if (user == null) {
//                return ResponseEntity.notFound().build();
//            }
//
//            List<BloodPressureResponse> responseList = new ArrayList<>();
//            LocalDate currentDate = endDate; // Start from endDate
//            while (!currentDate.isBefore(startDate)) { // Continue until after startDate
//                List<SystolicBloodPressure> systolicBloodPressures = systolicBloodPressureService.getSystolicBloodPressureForUserAndDate(user, currentDate);
//                if (systolicBloodPressures.isEmpty()) {
//                    BloodPressureResponse response = new BloodPressureResponse(
//                            currentDate.atStartOfDay(), // Set timestamp to the start of the day
//                            0.0, // Default systolic value
//                            0.0  // Default diastolic value
//                    );
//                    responseList.add(response);
//                } else {
//                    for (SystolicBloodPressure systolicBloodPressure : systolicBloodPressures) {
//                        BloodPressureResponse response = new BloodPressureResponse(
//                                systolicBloodPressure.getTimeStamp(),
//                                systolicBloodPressure.getSystolicValue(),
//                                systolicBloodPressure.getDiastolicValue()
//                        );
//                        responseList.add(response);
//                    }
//                }
//                currentDate = currentDate.minusDays(1); // Move to the previous date
//            }
//
//            return ResponseEntity.ok(responseList);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

//    @GetMapping("/getByDateRange")
//    public ResponseEntity<List<BloodPressureResponse>> getBloodPressureByDateRange(
//            @RequestHeader("Auth") String tokenHeader,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
//
//        try {
//            String token = tokenHeader.replace("Bearer ", "");
//            String username = jwtHelper.getUsernameFromToken(token);
//            User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
//
//            if (user == null) {
//                return ResponseEntity.notFound().build();
//            }
//
//            List<BloodPressureResponse> responseList = new ArrayList<>();
//            LocalDate currentDate = endDate; // Start from endDate
//            while (!currentDate.isBefore(startDate)) { // Continue until after startDate
//                List<SystolicBloodPressure> systolicBloodPressures = systolicBloodPressureService.getSystolicBloodPressureForUserAndDate(user, currentDate);
//                List<BloodPressureResponse> dailyResponses = new ArrayList<>();
//
//                for (SystolicBloodPressure systolicBloodPressure : systolicBloodPressures) {
//                    BloodPressureResponse response = new BloodPressureResponse(
//                            systolicBloodPressure.getTimeStamp(),
//                            systolicBloodPressure.getSystolicValue(),
//                            systolicBloodPressure.getDiastolicValue()
//                    );
//                    dailyResponses.add(response);
//                }
//
//                // Sort the daily responses based on the timestamp in descending order
//                dailyResponses.sort(Comparator.comparing(BloodPressureResponse::getTimeStamp).reversed());
//                responseList.addAll(dailyResponses);
//
//                // If no blood pressure data available for the current date, add a default response
//                if (dailyResponses.isEmpty()) {
//                    BloodPressureResponse defaultResponse = new BloodPressureResponse(
//                            currentDate.atStartOfDay(),
//                            0.0,
//                            0.0
//                    );
//                    responseList.add(defaultResponse);
//                }
//
//                currentDate = currentDate.minusDays(1); // Move to the previous date
//            }
//
//            return ResponseEntity.ok(responseList);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
@GetMapping("/getByDateRange")
public ResponseEntity<List<BloodPressureResponse>> getBloodPressureByDateRange(
        @RequestHeader("Auth") String tokenHeader,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

    try {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<BloodPressureResponse> responseList = new ArrayList<>();
        LocalDate currentDate = endDate; // Start from endDate
        boolean dataFoundInRange = false; // Flag to check if data found within the range

        while (!currentDate.isBefore(startDate)) { // Continue until after startDate
            List<SystolicBloodPressure> systolicBloodPressures = systolicBloodPressureService.getSystolicBloodPressureForUserAndDate(user, currentDate);

            // If blood pressure data is found for the current date
            if (!systolicBloodPressures.isEmpty()) {
                dataFoundInRange = true; // Set flag to true
                List<BloodPressureResponse> dailyResponses = new ArrayList<>();

                for (SystolicBloodPressure systolicBloodPressure : systolicBloodPressures) {
                    BloodPressureResponse response = new BloodPressureResponse(
                            systolicBloodPressure.getLocalDate(),
                            systolicBloodPressure.getSystolicValue(),
                            systolicBloodPressure.getDiastolicValue()
                    );
                    dailyResponses.add(response);
                }

                // Sort the daily responses based on the timestamp in descending order
                dailyResponses.sort(Comparator.comparing(BloodPressureResponse::getLocalDate).reversed());
                responseList.addAll(dailyResponses);
            }

            currentDate = currentDate.minusDays(1); // Move to the previous date
        }

        // If no data found within the range, return an empty list
        if (!dataFoundInRange) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(responseList);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}


    //get blood pressure by date
//    @GetMapping("/get/{date}")
//    public ResponseEntity<List<SystolicBloodPressure>> getHeartRateForUserAndDate(@RequestHeader("Auth") String tokenHeader, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
//        String token = tokenHeader.replace("Bearer ", "");
//        String username = jwtHelper.getUsernameFromToken(token);
//        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
//        if (user == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        List<SystolicBloodPressure> systolicBloodPressures = systolicBloodPressureService.getSystolicBloodPressureForUserAndDate(user, date);
//        return ResponseEntity.ok(systolicBloodPressures);
//    }

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
