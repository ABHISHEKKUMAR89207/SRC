package com.example.jwt.controler.controllerHealth;

import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.Activities;
import com.example.jwt.entities.dashboardEntity.healthTrends.SleepDuration;
import com.example.jwt.repository.repositoryHealth.SleepDurationRepository;
import com.example.jwt.request.SleepRequest;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.UserService;
import com.example.jwt.service.serviceHealth.SleepDurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    // creeate the sleep goal of the user
    @PutMapping("/creatSleepLog")
    public ResponseEntity<SleepDuration> createSleepLog(@RequestHeader("Auth") String tokenHeader, @RequestBody SleepDuration sleepDuration) {
        try {
            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);
            sleepDuration = sleepDurationService.addOrUpdateSleepDuration(sleepDuration, username);

            return new ResponseEntity<>(sleepDuration, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("Username is " + e);
            // Handle exceptions, e.g., validation errors or database errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // You can customize the error response as needed
        }
    }

    //get all the sleep recrd of the specific user
    @GetMapping("/getAllSleep")
    public ResponseEntity<List<SleepDuration>> getAllSleepLogs(@RequestHeader("Auth") String tokenHeader) {

        try {
            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);
            List<SleepDuration> sleepDuration = sleepDurationService.getAllSleepLogs(username);

            return new ResponseEntity<>(sleepDuration, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("Username is " + e);
            // Handle exceptions, e.g., validation errors or database errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // You can customize the error response as needed
        }
    }


    @GetMapping("/getSleepByDate")
    public ResponseEntity<SleepDuration> getSleepLogsByDate(
            @RequestHeader("Auth") String tokenHeader,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);

            // Call service method to get sleep logs by date
            Optional<SleepDuration> sleepDurationOptional = sleepDurationService.getSleepLogsByDate(username, date);

            if (sleepDurationOptional.isPresent()) {
                // Return sleep logs if present
                SleepDuration sleepDuration = sleepDurationOptional.get();
                return new ResponseEntity<>(sleepDuration, HttpStatus.OK);
            } else {
                // Return a not found response if sleep logs are not present
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
            // Handle exceptions, e.g., validation errors or database errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

//    @GetMapping("/getSleepByDate")
//    public ResponseEntity<Double> getSleepLogsByDate(
//            @RequestHeader("Auth") String tokenHeader,
//            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
//        try {
//            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//            String token = tokenHeader.replace("Bearer ", "");
//
//            // Extract the username (email) from the token
//            String username = jwtHelper.getUsernameFromToken(token);
//
//            // Call service method to get sleep logs by date
//            Optional<SleepDuration> sleepDurationOptional = sleepDurationService.getSleepLogsByDate(username, date);
//
//            double totalSleep = 0.0; // Default value
//
//            if (sleepDurationOptional.isPresent()) {
//                // If sleep logs are present, calculate total sleep duration
//                SleepDuration sleepDuration = sleepDurationOptional.get();
//                totalSleep = sleepDuration.getDuration() + sleepDuration.getManualDuration();
//            }
//
//            return new ResponseEntity<>(totalSleep, HttpStatus.OK);
//        } catch (Exception e) {
//            System.out.println("Error: " + e);
//            // Handle exceptions, e.g., validation errors or database errors
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }


//    @GetMapping("/getSleepByLastWeek")
//    public ResponseEntity<List<SleepDuration>> getSleepLogsForLastWeek(
//            @RequestHeader("Auth") String tokenHeader) {
//        try {
//            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//            String token = tokenHeader.replace("Bearer ", "");
//
//            // Extract the username (email) from the token
//            String username = jwtHelper.getUsernameFromToken(token);
//
//            // Call service method to get sleep logs for the last week
//            LocalDate endDate = LocalDate.now();
//            LocalDate startDate = endDate.minusDays(6); // Assuming you want data for the last 7 days
//
//            List<SleepDuration> sleepDuration = sleepDurationService.getSleepLogsBetweenDates(username, startDate, endDate);
//
//            return new ResponseEntity<>(sleepDuration, HttpStatus.OK);
//        } catch (Exception e) {
//            System.out.println("Error: " + e);
//            // Handle exceptions, e.g., validation errors or database errors
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }


    @Autowired
    private UserService userService;

    @GetMapping("/calculate-sleep-last-week")
    public ResponseEntity<Map<String, Double>> calculateWaterIntakeForLastWeek(@RequestHeader("Auth") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(username);

        Map<String, Double> waterIntakeMap = sleepDurationService.sleepForLastWeek(user);
        return new ResponseEntity<>(waterIntakeMap, HttpStatus.OK);
    }


    @GetMapping("/get-sleep-duration/custom-range")
    public ResponseEntity<List<Map<String, Object>>> getUserSleepDurationForCustomRange(
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
                // Get sleep durations for the custom date range and user
                List<SleepDuration> sleepDurations = sleepDurationService.findByUserAndDateOfSleepBetween(user, startDate, endDate);

                // Create a list to store sleep details for each day
                List<Map<String, Object>> sleepForRange = new ArrayList<>();

                // Convert each sleep duration to a map with formatted date and total sleep duration
                for (LocalDate currentDate = startDate; currentDate.isBefore(endDate.plusDays(1)); currentDate = currentDate.plusDays(1)) {
                    // Create a final variable to capture the value of currentDate within the lambda expression
                    final LocalDate finalCurrentDate = currentDate;

                    double totalSleep = sleepDurations.stream()
                            .filter(sleepEntity -> sleepEntity.getDateOfSleep().isEqual(finalCurrentDate))
                            .mapToDouble(sleepEntity -> sleepEntity.getDuration() + sleepEntity.getManualDuration())
                            .sum();

                    Map<String, Object> sleepMap = new HashMap<>();
                    sleepMap.put("date", finalCurrentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    sleepMap.put("value", totalSleep);
                    sleepForRange.add(sleepMap);
                }

                return ResponseEntity.ok(sleepForRange);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


//    @PostMapping("/record-by-steps-weekly")
//    public List<Activities> recordStepsForPreviousWeek(@RequestHeader("Auth") String tokenHeader, @RequestParam List<Integer> stepsList, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
//        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        // Use the username to fetch the userId from your user service
//        User user = userService.findByUsername(username);
//
//        List<Activities> recordedActivities = new ArrayList<>();
//
//        if (user != null) {
//            for (int i = stepsList.size() - 1; i >= 0; i--) {
//                LocalDate currentDate = startDate.minusDays(stepsList.size() - 1 - i);
//
//                Activities existingRecord = activityService.getActivitiesForUserAndDate(user, currentDate);
//
//                if (existingRecord != null) {
//                    // If a record for the user and the specified date exists, update it with the new steps.
//                    existingRecord.setSteps(stepsList.get(i));
//                    activityService.updateActivities(existingRecord);
//                    recordedActivities.add(existingRecord); // Add the updated record to the list
//                } else {
//                    // If no record exists for the specified date, create a new one.
//                    Activities newRecord = new Activities();
//                    newRecord.setUser(user);
//                    newRecord.setSteps(stepsList.get(i));
//                    newRecord.setActivityDate(currentDate);
//                    // Save the new record as a separate row in the database.
//                    activityService.createActivities(newRecord);
//                    recordedActivities.add(newRecord); // Add the newly created record to the list
//                }
//            }
//        } else {
//            // Handle the case where the user with the provided userId is not found.
//            return null; // Placeholder for error handling
//        }
//
//        return recordedActivities;
//    }
//@PostMapping("/record-sleep")
//public ResponseEntity<Map<LocalDate, Map<String, Integer>>> recordSleepData(@RequestHeader("Auth") String tokenHeader, @RequestBody Map<LocalDate, Map<String, Integer>> sleepData) {
//    try {
//        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        // Use the username to fetch the userId from your user service
//        User user = userService.findByUsername(username);
//
//        Map<LocalDate, Map<String, Integer>> recordedSleepData = new HashMap<>();
//
//        if (user != null) {
//            for (Map.Entry<LocalDate, Map<String, Integer>> entry : sleepData.entrySet()) {
//                LocalDate currentDate = entry.getKey();
//                Map<String, Integer> sleepDetails = entry.getValue();
//
//                Sleep existingRecord = sleepService.getSleepForUserAndDate(user, currentDate);
//
//                if (existingRecord != null) {
//                    // If a record for the user and the specified date exists, update it with the new sleep data.
//                    existingRecord.setTotalSleep(sleepDetails.get("totalSleep"));
//                    existingRecord.setTotalEfficiency(sleepDetails.get("totalEfficiency"));
//                    sleepService.updateSleep(existingRecord);
//                    recordedSleepData.put(currentDate, sleepDetails); // Add the updated record to the map
//                } else {
//                    // If no record exists for the specified date, create a new one.
//                    Sleep newRecord = new Sleep();
//                    newRecord.setUser(user);
//                    newRecord.setTotalSleep(sleepDetails.get("totalSleep"));
//                    newRecord.setTotalEfficiency(sleepDetails.get("totalEfficiency"));
//                    newRecord.setSleepDate(currentDate);
//                    // Save the new record as a separate row in the database.
//                    sleepService.createSleep(newRecord);
//                    recordedSleepData.put(currentDate, sleepDetails); // Add the newly created record to the map
//                }
//            }
//
//            return ResponseEntity.ok(recordedSleepData);
//        } else {
//            // Handle the case where the user with the provided userId is not found.
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//    } catch (Exception e) {
//        // Handle other exceptions as needed
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//    }
//}


//    @PostMapping("/record-sleep-weekly")
//    public Map<LocalDate, SleepDuration> recordSleepForPreviousWeek(@RequestHeader("Auth") String tokenHeader, @RequestBody Map<LocalDate, SleepData> sleepDataMap) {
//        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        // Use the username to fetch the userId from your user service
//        User user = userService.findByUsername(username);
//
//        Map<LocalDate, SleepData> recordedSleepData = new HashMap<>();
//
//        if (user != null) {
//            for (Map.Entry<LocalDate, SleepData> entry : sleepDataMap.entrySet()) {
//                LocalDate currentDate = entry.getKey();
//                SleepData sleepData = entry.getValue();
//
//                SleepRecord existingRecord = sleepService.getSleepRecordForUserAndDate(user, currentDate);
//
//                if (existingRecord != null) {
//                    // If a record for the user and the specified date exists, update it with the new sleep data.
//                    existingRecord.setTotalSleep(sleepData.getTotalSleep());
//                    existingRecord.setTotalEfficiency(sleepData.getTotalEfficiency());
//                    sleepService.updateSleepRecord(existingRecord);
//                    recordedSleepData.put(currentDate, sleepData); // Add the updated record to the map
//                } else {
//                    // If no record exists for the specified date, create a new one.
//                    SleepRecord newRecord = new SleepRecord();
//                    newRecord.setUser(user);
//                    newRecord.setTotalSleep(sleepData.getTotalSleep());
//                    newRecord.setTotalEfficiency(sleepData.getTotalEfficiency());
//                    newRecord.setSleepDate(currentDate);
//                    // Save the new record as a separate row in the database.
//                    sleepService.createSleepRecord(newRecord);
//                    recordedSleepData.put(currentDate, sleepData); // Add the newly created record to the map
//                }
//            }
//        } else {
//            // Handle the case where the user with the provided userId is not found.
//            return null; // Placeholder for error handling
//        }
//
//        return recordedSleepData;
//    }


//    @PostMapping("/record-weekly")
//    public Map<LocalDate, Map<String, Object>> recordSleepForPreviousWeek(
//            @RequestHeader("Auth") String tokenHeader,
//            @RequestBody Map<LocalDate, Map<String, Object>> sleepData
//    ) {
//        String token = tokenHeader.replace("Bearer ", "");
//        String username = jwtHelper.getUsernameFromToken(token);
//        User user = userService.findByUsername(username);
//
//        Map<LocalDate, Map<String, Object>> recordedSleepData = new HashMap<>();
//
//        if (user != null) {
//            for (Map.Entry<LocalDate, Map<String, Object>> entry : sleepData.entrySet()) {
//                LocalDate currentDate = entry.getKey();
//                Map<String, Object> sleepDetails = entry.getValue();
//
//                SleepDuration existingRecord = sleepDurationService.getSleepForUserAndDate(user, currentDate);
//
//                if (existingRecord != null) {
//                    // If a record for the user and the specified date exists, update it with the new sleep details.
//                    existingRecord.setTotalSleep((long) sleepDetails.get("totalSleep"));
//                    existingRecord.setTotalEfficiency((int) sleepDetails.get("totalEfficiency"));
//                    sleepDurationService.updateSleep(existingRecord);
//                    recordedSleepData.put(currentDate, sleepDetails); // Add the updated record to the response
//                } else {
//                    // If no record exists for the specified date, create a new one.
//                    SleepDuration newRecord = new SleepDuration();
//                    newRecord.setUser(user);
//                    newRecord.setTotalSleep((long) sleepDetails.get("totalSleep"));
//                    newRecord.setTotalEfficiency((int) sleepDetails.get("totalEfficiency"));
//                    newRecord.setDateOfSleep(currentDate);
//                    // Save the new record as a separate row in the database.
//                    sleepService.createSleep(newRecord);
//                    recordedSleepData.put(currentDate, sleepDetails); // Add the newly created record to the response
//                }
//            }
//        } else {
//            // Handle the case where the user with the provided userId is not found.
//            return null; // Placeholder for error handling
//        }
//
//        return recordedSleepData;
//    }
@PostMapping("/record-sleep-weekly")
public Map<LocalDate, SleepDuration> recordSleepForPreviousWeek(@RequestHeader("Auth") String tokenHeader, @RequestBody Map<LocalDate, SleepRequest> sleepData) {
    // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
    String token = tokenHeader.replace("Bearer ", "");

    // Extract the username (email) from the token
    String username = jwtHelper.getUsernameFromToken(token);

    // Use the username to fetch the userId from your user service
    User user = userService.findByUsername(username);

    Map<LocalDate, SleepDuration> recordedSleep = new HashMap<>();

    if (user != null) {
        for (Map.Entry<LocalDate, SleepRequest> entry : sleepData.entrySet()) {
            LocalDate currentDate = entry.getKey();
            SleepRequest sleepRequest = entry.getValue();

            SleepDuration existingRecord = sleepDurationService.getSleepForUserAndDate(user, currentDate);

            if (existingRecord != null) {
                // If a record for the user and the specified date exists, update it with the new sleep data.
                existingRecord.setDuration(sleepRequest.getTotalMinutesAsleep());
                existingRecord.setEfficiency(sleepRequest.getTotalEfficiency());
                existingRecord.setEndTime(LocalDateTime.now()); // You might want to set the end time here
                sleepDurationService.updateSleep(existingRecord);
                recordedSleep.put(currentDate, existingRecord); // Add the updated record to the map
            } else {
                // If no record exists for the specified date, create a new one.
                SleepDuration newRecord = new SleepDuration();
                newRecord.setUser(user);
                newRecord.setDuration(sleepRequest.getTotalMinutesAsleep());
                newRecord.setEfficiency(sleepRequest.getTotalEfficiency());
                newRecord.setDateOfSleep(currentDate);
                newRecord.setEndTime(LocalDateTime.now()); // You might want to set the end time here
                // Save the new record as a separate row in the database.
                sleepDurationService.createSleep(newRecord);
                recordedSleep.put(currentDate, newRecord); // Add the newly created record to the map
            }
        }
    } else {
        // Handle the case where the user with the provided userId is not found.
        return Collections.singletonMap(LocalDate.now(), null); // Placeholder for error handling
    }

    return recordedSleep;
}

    @PostMapping("/sleep-duration-by-googlefit")
    public ResponseEntity<String> saveSleepDurations(@RequestHeader("Auth") String tokenHeader,
                                                     @RequestBody Map<String, Long> sleepData) {
        try {
            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);
            User user = userService.findByUsername(username);
            // Convert keys from String to LocalDate and save
            Map<LocalDate, Long> parsedSleepData = new HashMap<>();
            sleepData.forEach((key, value) -> parsedSleepData.put(LocalDate.parse(key), value));

            sleepDurationService.saveSleepDurations(parsedSleepData, user);
            return ResponseEntity.status(HttpStatus.CREATED).body("Sleep durations saved successfully.");
        } catch (Exception e) {
            // Handle exceptions, e.g., validation errors or database errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save sleep durations.");
        }
    }




    @GetMapping("/weekly-steps")
    public ResponseEntity<Map<String, Double>> StepsForLastWeek(@RequestHeader("Auth") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(username);

        Map<String, Double> waterIntakeMap = StepsForLastWeek(user);
        return new ResponseEntity<>(waterIntakeMap, HttpStatus.OK);
    }
    public Map<String, Double> StepsForLastWeek(User user) {
        List<Activities> activities = user.getActivities();

        // Create a map to store water intake for each day
        Map<String, Double> waterIntakeMap = new HashMap<>();

        // Calculate the start date of the last week
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusWeeks(1);

        // Iterate over each day in the last week
        for (LocalDate currentDate = startDate; currentDate.isBefore(endDate); currentDate = currentDate.plusDays(1)) {
            double totalWaterIntake = 0.0;

            // Calculate total water intake for the current day
            for (Activities activities1 : activities) {
                if (activities1.getActivityDate().isEqual(currentDate)) {
//                    totalWaterIntake += waterEntity.getCupCapacity() * waterEntity.getNoOfCups() / 1000.0;
                    totalWaterIntake += activities1.getSteps();

                }
            }
            // Store the result in the map with the day name
            waterIntakeMap.put(currentDate.getDayOfWeek().toString(), totalWaterIntake);
        }

        return waterIntakeMap;
    }

}

