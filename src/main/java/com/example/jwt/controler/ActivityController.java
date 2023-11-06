package com.example.jwt.controler;


import com.example.jwt.dtos.ActivitiesDTO;
import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.Activities;
import com.example.jwt.repository.ActivityRepository;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.ActivityService;
import com.example.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


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
                existingRecord.setSteps(existingRecord.getSteps() + steps);
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




    //update and create by activity
//    @PutMapping("/record-by-activity")
//    public ResponseEntity<String> recordActivity(@RequestHeader("Auth") String tokenHeader, @RequestBody Activities activity) {
//        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        // Use the username to fetch the userId from your user service
//        User user = userService.findByUsername(username);
//        if (activity.getActivityType() != null) {
//            activityService.recordActivity(user, activity); // Pass the userId to your service method
//            return ResponseEntity.ok("Activity recorded successfully");
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid activity type");
//        }
//    }

//    @PutMapping("/record-by-activity")
//    public ResponseEntity<String> recordActivity(@RequestHeader("Auth") String tokenHeader, @RequestBody Activities activity) {
//        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        // Use the username to fetch the userId from your user service
//        User user = userService.findByUsername(username);
//
//        if (activity.getActivityType() != null) {
//            activityService.recordActivity(user, activity); // Pass the userId to your service method
//            return ResponseEntity.ok("Activity recorded successfully");
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid activity type");
//        }
//    }

    //update and create by activity
//    @PutMapping("/record-by-activity")
//    public ResponseEntity<String> recordActivity(@RequestParam Long userId, @RequestBody Activities activity) {
//        if (activity.getActivityType() != null) {
//            activityService.recordActivity(userId, activity); // Pass the userId to your service method
//            return ResponseEntity.ok("Activity recorded successfully");
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid activity type");
//        }
//    }




    @PutMapping("/record-by-activity")
    public ResponseEntity<?> recordActivity(@RequestHeader("Auth") String tokenHeader, @RequestBody Activities activity) {
        try {
            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);

            // Use the username to fetch the userId from your user service
            User user = userService.findByUsername(username);

            // ... (Your existing code to extract the user and validate)

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
    public double calculateCalories(
            @RequestParam String activityType,
            @RequestParam String activityDate,
            @RequestHeader("Auth") String tokenHeader
    ) {
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