package com.example.jwt.controler;

import com.example.jwt.dtos.ActivitiesDTO;
import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.Activities;
import com.example.jwt.repository.ActivityRepository;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.ActivityService;
import com.example.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.time.YearMonth;

@RestController
@RequestMapping("/activities")
public class ActivityController {
    @Autowired
    private ActivityService activityService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private ActivityRepository activityRepository;

    //By steps
//    @PostMapping("/record-by-steps")
//    public Activities recordStep(@RequestHeader("Auth") String tokenHeader, @RequestParam int steps) {
//        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        // Use the username to fetch the userId from your user service
//        User user = userService.findByUsername(username);
//
//        if (user != null) {
//            LocalDate currentDate = LocalDate.now();
//            Activities existingRecord = activityService.getActivitiesForUserAndDate(user, currentDate);
//
//            if (existingRecord != null) {
//                // If a record for the user and the current date exists, update it with the new steps.
//                existingRecord.setSteps(steps);
//                activityService.updateActivities(existingRecord);
//                return existingRecord; // Return the updated record
//            } else {
//                // If no record exists for the current date, create a new one.
//                Activities newRecord = new Activities();
//                newRecord.setUser(user);
//                newRecord.setSteps(steps);
//                newRecord.setActivityDate(currentDate);
//                // Save the new record as a separate row in the database.
//                activityService.createActivities(newRecord);
//                return newRecord; // Return the newly created record
//            }
//        } else {
//            // Handle the case where the user with the provided userId is not found.
//            return null; // Placeholder for error handling
//        }
//    }

//    @PostMapping("/record-by-steps")
//    public Activities recordStep(@RequestHeader("Auth") String tokenHeader, @RequestParam int steps, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate recordingDate) {
//        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        // Use the username to fetch the userId from your user service
//        User user = userService.findByUsername(username);
//
//        if (user != null) {
//            // Use the specified recordingDate instead of the current date
//            LocalDate currentDate = recordingDate;
//
//            Activities existingRecord = activityService.getActivitiesForUserAndDate(user, currentDate);
//
//            if (existingRecord != null) {
//                // If a record for the user and the specified date exists, update it with the new steps.
//                existingRecord.setSteps(steps);
//                activityService.updateActivities(existingRecord);
//                return existingRecord; // Return the updated record
//            } else {
//                // If no record exists for the specified date, create a new one.
//                Activities newRecord = new Activities();
//                newRecord.setUser(user);
//                newRecord.setSteps(steps);
//                newRecord.setActivityDate(currentDate);
//                // Save the new record as a separate row in the database.
//                activityService.createActivities(newRecord);
//                return newRecord; // Return the newly created record
//            }
//        } else {
//            // Handle the case where the user with the provided userId is not found.
//            return null; // Placeholder for error handling
//        }
//    }

//    @PostMapping("/record-by-steps")
//    public List<Activities> recordStepsForWeek(@RequestHeader("Auth") String tokenHeader, @RequestParam List<Integer> stepsList, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
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
//            for (int i = 0; i < stepsList.size(); i++) {
//                LocalDate currentDate = startDate.plusDays(i);
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

    @PostMapping("/record-by-steps")
    public List<Activities> recordStepsForPreviousWeek(@RequestHeader("Auth") String tokenHeader, @RequestParam List<Integer> stepsList, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the username to fetch the userId from your user service
        User user = userService.findByUsername(username);

        List<Activities> recordedActivities = new ArrayList<>();

        if (user != null) {
            for (int i = stepsList.size() - 1; i >= 0; i--) {
                LocalDate currentDate = startDate.minusDays(stepsList.size() - 1 - i);

                Activities existingRecord = activityService.getActivitiesForUserAndDate(user, currentDate);

                if (existingRecord != null) {
                    // If a record for the user and the specified date exists, update it with the new steps.
                    existingRecord.setSteps(stepsList.get(i));
                    activityService.updateActivities(existingRecord);
                    recordedActivities.add(existingRecord); // Add the updated record to the list
                } else {
                    // If no record exists for the specified date, create a new one.
                    Activities newRecord = new Activities();
                    newRecord.setUser(user);
                    newRecord.setSteps(stepsList.get(i));
                    newRecord.setActivityDate(currentDate);
                    // Save the new record as a separate row in the database.
                    activityService.createActivities(newRecord);
                    recordedActivities.add(newRecord); // Add the newly created record to the list
                }
            }
        } else {
            // Handle the case where the user with the provided userId is not found.
            return null; // Placeholder for error handling
        }

        return recordedActivities;
    }


    @PostMapping("/record-by-calory")
    public List<Activities> recordCaloryForPreviousWeek(@RequestHeader("Auth") String tokenHeader, @RequestParam List<Integer> stepsList, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the username to fetch the userId from your user service
        User user = userService.findByUsername(username);

        List<Activities> recordedActivities = new ArrayList<>();

        if (user != null) {
            for (int i = stepsList.size() - 1; i >= 0; i--) {
                LocalDate currentDate = startDate.minusDays(stepsList.size() - 1 - i);

                Activities existingRecord = activityService.getActivitiesForUserAndDate(user, currentDate);

                if (existingRecord != null) {
                    // If a record for the user and the specified date exists, update it with the new steps.
                    existingRecord.setCalory(Double.valueOf(stepsList.get(i)));
                    activityService.updateActivities(existingRecord);
                    recordedActivities.add(existingRecord); // Add the updated record to the list
                } else {
                    // If no record exists for the specified date, create a new one.
                    Activities newRecord = new Activities();
                    newRecord.setUser(user);
                    newRecord.setCalory(Double.valueOf(stepsList.get(i)));
                    newRecord.setActivityDate(currentDate);
                    // Save the new record as a separate row in the database.
                    activityService.createActivities(newRecord);
                    recordedActivities.add(newRecord); // Add the newly created record to the list
                }
            }
        } else {
            // Handle the case where the user with the provided userId is not found.
            return null; // Placeholder for error handling
        }

        return recordedActivities;
    }


//    @GetMapping("/weekly-steps")
//    public Map<String, Integer> getWeeklySteps(@RequestHeader("Auth") String tokenHeader) {
//        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        // Use the username to fetch the userId from your user service
//        User user = userService.findByUsername(username);
//
//        Map<String, Integer> weeklyStepsMap = new LinkedHashMap<>();
//
//        if (user != null) {
//            LocalDate currentDate = LocalDate.now();
//            DayOfWeek currentDayOfWeek = currentDate.getDayOfWeek();
//
//            // Calculate the start date of the current week (assuming Monday is the first day of the week)
//            LocalDate startOfWeek = currentDate.minusDays(currentDayOfWeek.getValue() - DayOfWeek.MONDAY.getValue());
//
//            for (int i = 0; i < 7; i++) {
//                LocalDate date = startOfWeek.plusDays(i);
//
//                Activities activities = activityService.getActivitiesForUserAndDate(user, date);
//
//                if (activities != null) {
//                    weeklyStepsMap.put(date.getDayOfWeek().toString(), activities.getSteps());
//                } else {
//                    weeklyStepsMap.put(date.getDayOfWeek().toString(), 0);
//                }
//            }
//        }
//
//        return weeklyStepsMap;
//    }
@GetMapping("/weekly-steps")
public ResponseEntity<Map<String, Double>> calculateWaterIntakeForLastWeek(@RequestHeader("Auth") String tokenHeader) {
    String token = tokenHeader.replace("Bearer ", "");
    String username = jwtHelper.getUsernameFromToken(token);
    User user = userService.findByUsername(username);

    Map<String, Double> waterIntakeMap = calculateWaterIntakeForLastWeek(user);
    return new ResponseEntity<>(waterIntakeMap, HttpStatus.OK);
}


    public Map<String, Double> calculateWaterIntakeForLastWeek(User user) {
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


    // to get the steps of the user by fitbit watch
    @GetMapping("/get-steps")
    public Activities getUserSteps(@RequestHeader("Auth") String tokenHeader) {
        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the username to fetch the userId from your user service
        User user = userService.findByUsername(username);

        if (user != null) {
            LocalDate currentDate = LocalDate.now();
            Activities existingRecord = activityService.getActivitiesForUserAndDate(user, currentDate);

            // If no record exists for the current date, return a placeholder or handle it as needed
            return existingRecord; // Return the activities record for the user and current date
        } else {
            // Handle the case where the user with the provided userId is not found.
            return null;
        }
    }


//    @GetMapping("/get-calory")
//    public Activities getUserCalory(@RequestHeader("Auth") String tokenHeader) {
//        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        // Use the username to fetch the userId from your user service
//        User user = userService.findByUsername(username);
//
//        if (user != null) {
//            LocalDate currentDate = LocalDate.now();
//            Activities existingRecord = activityService.getActivitiesForUserAndDate(user, currentDate);
//
//            // If no record exists for the current date, return a placeholder or handle it as needed
//            return existingRecord; // Return the activities record for the user and current date
//        } else {
//            // Handle the case where the user with the provided userId is not found.
//            return null;
//        }
//    }


//    Get steps by date for export

    @GetMapping("/get-steps/Date")
    public ResponseEntity<Map<String, Object>> getUserStepsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestHeader("Auth") String tokenHeader) {
        try {
            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);

            // Use the username to fetch the userId from your user service
            User user = userService.findByUsername(username);

            if (user != null) {
                // Get activities for the specified date and user
                Activities existingRecord = activityService.getActivitiesForUserAndDate(user, date);

                if (existingRecord != null) {
                    // Create a map to hold only steps and activity date
                    Map<String, Object> response = new HashMap<>();

                    // Format the activityDate
                    String formattedActivityDate = existingRecord.getActivityDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));


                    response.put("steps", existingRecord.getSteps());
                    response.put("activityDate", formattedActivityDate);

                    return ResponseEntity.ok(response);
                } else {
                    // Handle the case where no activities are found for the specified date
                    return ResponseEntity.notFound().build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //    Get steps by data for export

//    @GetMapping("/get-steps/custom-range")
//    public ResponseEntity<Map<String, Object>> getUserStepsForCustomRange(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
//            @RequestHeader("Auth") String tokenHeader) {
//        try {
//            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//            String token = tokenHeader.replace("Bearer ", "");
//
//            // Extract the username (email) from the token
//            String username = jwtHelper.getUsernameFromToken(token);
//
//            // Use the username to fetch the userId from your user service
//            User user = userService.findByUsername(username);
//
//            if (user != null) {
//                // Get activities for the custom date range and user
//                List<Activities> activitiesList = activityService.getActivitiesForUserAndCustomRange(user, startDate, endDate);
//
//                // Create a map to hold only steps and formatted activity date
//                Map<String, Object> response = new HashMap<>();
//
//                // Create a list to store activity details for each day
//                List<Map<String, Object>> activitiesForRange = new ArrayList<>();
//
//                // Convert each activity to a map with formatted activityDate and steps
//                for (Activities activity : activitiesList) {
//                    Map<String, Object> activityMap = new HashMap<>();
//
//                    // Format the activityDate
//                    String formattedActivityDate = activity.getActivityDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//
//                    activityMap.put("activityDate", formattedActivityDate);
//                    activityMap.put("steps", activity.getSteps());
//                    activitiesForRange.add(activityMap);
//                }
//
//                // Add the list of activities to the response map
//                response.put("activitiesForRange", activitiesForRange);
//
//                return ResponseEntity.ok(response);
//            } else {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

    @GetMapping("/get-steps/custom-range")
    public ResponseEntity<List<Map<String, Object>>> getUserStepsForCustomRange(
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
                // Get activities for the custom date range and user
                List<Activities> activitiesList = activityService.getActivitiesForUserAndCustomRange(user, startDate, endDate);

                // Create a list to store activity details for each day
                List<Map<String, Object>> activitiesForRange = new ArrayList<>();

                // Convert each activity to a map with formatted activityDate and steps
                for (Activities activity : activitiesList) {
                    Map<String, Object> activityMap = new HashMap<>();

                    // Format the activityDate
                    String formattedActivityDate = activity.getActivityDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    activityMap.put("activityDate", formattedActivityDate);
                    activityMap.put("steps", activity.getSteps());
                    activitiesForRange.add(activityMap);
                }

                return ResponseEntity.ok(activitiesForRange);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

//    @GetMapping("/get-steps/week")
//    public ResponseEntity<Map<String, Object>> getUserStepsForWeek(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
//            @RequestHeader("Auth") String tokenHeader) {
//        try {
//            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//            String token = tokenHeader.replace("Bearer ", "");
//
//            // Extract the username (email) from the token
//            String username = jwtHelper.getUsernameFromToken(token);
//
//            // Use the username to fetch the userId from your user service
//            User user = userService.findByUsername(username);
//
//            if (user != null) {
//                // Calculate the start date of the week preceding the given date
//                LocalDate weekStartDate = date.minusDays(7);
//
//                // Get activities for the week and user
//                List<Activities> activitiesList = activityService.getActivitiesForUserAndWeek(user, weekStartDate, date);
//
//                // Create a map to hold only steps and formatted activity date
//                Map<String, Object> response = new HashMap<>();
//
//                // Create a list to store activity details for each day
//                List<Map<String, Object>> activitiesForWeek = new ArrayList<>();
//
//                // Convert each activity to a map with formatted activityDate and steps
//                for (Activities activity : activitiesList) {
//                    Map<String, Object> activityMap = new HashMap<>();
//
//                    // Format the activityDate
//                    String formattedActivityDate = activity.getActivityDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//
//                    activityMap.put("activityDate", formattedActivityDate);
//                    activityMap.put("steps", activity.getSteps());
//                    activitiesForWeek.add(activityMap);
//                }
//
//                // Add the list of activities to the response map
//                response.put("activitiesForWeek", activitiesForWeek);
//
//                return ResponseEntity.ok(response);
//            } else {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

    //    Get steps by month for export

//    @GetMapping("/get-steps/mon-year")
//    public ResponseEntity<List<Map<String, Object>>> getActivitiesByMonthAndYear(
//            @RequestParam int year,
//            @RequestParam int month,
//            @RequestHeader("Auth") String tokenHeader) {
//        try {
//            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//            String token = tokenHeader.replace("Bearer ", "");
//
//            // Extract the username (email) from the token
//            String username = jwtHelper.getUsernameFromToken(token);
//
//            // Use the username to fetch the userId from your user service
//            User user = userService.findByUsername(username);
//
//            if (user != null) {
//                // Get activities for the specified month and year
//                List<Activities> activitiesList = activityService.getActivitiesByMonthAndYear(user, year, month);
//
//                // Create a list to hold response maps with only activityDate and steps
//                List<Map<String, Object>> responseList = new ArrayList<>();
//
//
//
//                // Convert each activity to a map with activityDate and steps
//                for (Activities activity : activitiesList) {
//                    Map<String, Object> activityMap = new HashMap<>();
//
//                    // Format the activityDate
//                    String formattedActivityDate = activity.getActivityDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//
//
//                    activityMap.put("activityDate", formattedActivityDate);
//                    activityMap.put("steps", activity.getSteps());
//                    responseList.add(activityMap);
//                }
//
//                return ResponseEntity.ok(responseList);
//            } else {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }


// Get steps by year for export

//    @GetMapping("/get-steps/year")
//    public ResponseEntity<List<Map<String, Object>>> getActivitiesByYear(
//            @RequestParam int year,
//            @RequestHeader("Auth") String tokenHeader) {
//        try {
//            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//            String token = tokenHeader.replace("Bearer ", "");
//
//            // Extract the username (email) from the token
//            String username = jwtHelper.getUsernameFromToken(token);
//
//            // Use the username to fetch the userId from your user service
//            User user = userService.findByUsername(username);
//
//            if (user != null) {
//                // Get activities for the specified year
//                List<Activities> activitiesList = activityService.getActivitiesByYear(user, year);
//
//                // Create a list to hold response maps with only activityDate and steps
//                List<Map<String, Object>> responseList = new ArrayList<>();
//
//                // Convert each activity to a map with activityDate and steps
//                for (Activities activity : activitiesList) {
//                    Map<String, Object> activityMap = new HashMap<>();
//
//                    // Format the activityDate
//                    String formattedActivityDate = activity.getActivityDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//
//                    activityMap.put("activityDate", formattedActivityDate);
//                    activityMap.put("steps", activity.getSteps());
//                    responseList.add(activityMap);
//                }
//
//                return ResponseEntity.ok(responseList);
//            } else {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }




    // to get the record by activity of the user
    @PutMapping("/record-by-activity")
    public ResponseEntity<?> recordActivity(@RequestHeader("Auth") String tokenHeader, @RequestBody Activities activity) {
        try {
            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);

            // Use the username to fetch the userId from your user service
            User user = userService.findByUsername(username);

            if (user != null) {
                if (activity.getActivityType() != null) {
                    LocalDate currentDate = LocalDate.now();
                    // Check if an activity of the same type exists for the user and date
                    Activities existingActivity = activityService.findByUserAndActivityTypeAndActivityDate(user, activity.getActivityType(), currentDate);

                    if (existingActivity != null) {
                        // Update the existing activity record for the same date
                        if ("Running".equalsIgnoreCase(activity.getActivityType())) {
                            existingActivity.setRunningDurationInSeconds(existingActivity.getRunningDurationInSeconds() + activity.getRunningDurationInSeconds());
                        } else if ("Jogging".equalsIgnoreCase(activity.getActivityType())) {
                            existingActivity.setJoggingDurationInSeconds(existingActivity.getJoggingDurationInSeconds() + activity.getJoggingDurationInSeconds());
                        }

                        // Save the updated activity record
                        activityService.save(existingActivity);

                        // Convert the existingActivity to a DTO and return it
                        ActivitiesDTO existingActivityDTO = ActivitiesDTO.fromActivities(existingActivity);
                        return ResponseEntity.ok(existingActivityDTO);
                    } else {
                        // Create a new activity record for the current date
                        activity.setUser(user);
                        activity.setActivityDate(currentDate);
                        activityService.save(activity);

                        // Convert the newly created activity to a DTO and return it
                        ActivitiesDTO newActivityDTO = ActivitiesDTO.fromActivities(activity);
                        return ResponseEntity.ok(newActivityDTO);
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid activity type");
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authorized");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    //calculate and get caloriesBurn
    @GetMapping("/calculateCalories-by duration")
    public double calculateCalories(@RequestParam String activityType, @RequestParam String activityDate, @RequestHeader("Auth") String tokenHeader) {
        LocalDate date = LocalDate.parse(activityDate);
        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the username to fetch the userId from your user service
        User user = userService.findByUsername(username);
        if (user != null) {
            return activityService.calculateCaloriesBurnedByDuration(activityType, date, user);
        } else {
            // Handle the case where the user is not found
            return -1.0; // You can use an appropriate error value
        }
    }

    //calculate and get calory burn by steps
    @GetMapping("/calculateCalories-by-steps")
    public List<Double> calculateCalories(@RequestHeader("Auth") String tokenHeader, @RequestParam String date) {

        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the username to fetch the userId from your user service
        User user = userService.findByUsername(username);
        // Parse the date string into a LocalDate, you may need to format the date as required.
        LocalDate parsedDate = LocalDate.parse(date);

        return activityService.calculateCaloriesBurned(user, parsedDate);
    }
}
