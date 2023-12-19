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
    @PostMapping("/record-by-steps")
    public Activities recordStep(@RequestHeader("Auth") String tokenHeader, @RequestParam int steps) {
        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the username to fetch the userId from your user service
        User user = userService.findByUsername(username);

        if (user != null) {
            LocalDate currentDate = LocalDate.now();
            Activities existingRecord = activityService.getActivitiesForUserAndDate(user, currentDate);

            if (existingRecord != null) {
                // If a record for the user and the current date exists, update it with the new steps.
                existingRecord.setSteps(steps);
                activityService.updateActivities(existingRecord);
                return existingRecord; // Return the updated record
            } else {
                // If no record exists for the current date, create a new one.
                Activities newRecord = new Activities();
                newRecord.setUser(user);
                newRecord.setSteps(steps);
                newRecord.setActivityDate(currentDate);
                // Save the new record as a separate row in the database.
                activityService.createActivities(newRecord);
                return newRecord; // Return the newly created record
            }
        } else {
            // Handle the case where the user with the provided userId is not found.
            return null; // Placeholder for error handling
        }
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

    @GetMapping("/get-steps/custom-range")
    public ResponseEntity<Map<String, Object>> getUserStepsForCustomRange(
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

                // Create a map to hold only steps and formatted activity date
                Map<String, Object> response = new HashMap<>();

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

                // Add the list of activities to the response map
                response.put("activitiesForRange", activitiesForRange);

                return ResponseEntity.ok(response);
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
