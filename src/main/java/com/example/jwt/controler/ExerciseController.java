package com.example.jwt.controler;

import com.example.jwt.entities.Exercise;
import com.example.jwt.entities.User;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.ExerciseService;
import com.example.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;



@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {
    private final ExerciseService exerciseService;
    private final JwtHelper jwtHelper;
    private final UserService userService;

    @Autowired
    public ExerciseController(
            ExerciseService exerciseService,
            JwtHelper jwtHelper,
            UserService userService
    ) {
        this.exerciseService = exerciseService;
        this.jwtHelper = jwtHelper;
        this.userService = userService;
    }

    @PostMapping("/add")
    public Exercise addExercise(
            @RequestHeader("Auth") String tokenHeader,
            @RequestParam String activityType,
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam double duration
    ) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            User user = userService.findByUsername(username);

            Time startTimestamp = Time.valueOf(startTime.replace("T", " ").replace("Z", ""));
            Time endTimestamp = Time.valueOf(endTime.replace("T", " ").replace("Z", ""));

            Exercise exercise = new Exercise();
            exercise.setActivityType(activityType);
            exercise.setStartTime(startTimestamp);
            exercise.setEndTime(endTimestamp);
            exercise.setDuration(duration);

            System.out.println("Before calculateAndSaveExercise");
            Exercise savedExercise = exerciseService.calculateAndSaveExercise(exercise, user, duration);
            System.out.println("After calculateAndSaveExercise");

            return savedExercise;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/getByDateAndActivityType")
    public List<Exercise> getExercisesByDateAndActivityType(
            @RequestHeader("Auth") String tokenHeader,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam String activityType
    ) {
        try {
            // Extract the username (email) from the token
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            User user = userService.findByUsername(username);

            // Retrieve exercises by date and activityType
            List<Exercise> exercises = exerciseService.findByUserAndDateAndActivityType(user, date, activityType);

            // Exercises retrieved successfully
            return exercises;
        } catch (Exception e) {
             e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/getByDate")
    public List<Exercise> getExercisesByDate(
            @RequestHeader("Auth") String tokenHeader,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            User user = userService.findByUsername(username);

            List<Exercise> exercises = exerciseService.findByUserAndDate(user, date);

            return exercises;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    @PutMapping("/update-exercise/{id}")
    public ResponseEntity<Exercise> updateExercise(
            @PathVariable Long id,
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestHeader("Auth") String tokenHeader
    ) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            User user = userService.findByUsername(username);

            Exercise exercise = exerciseService.findById(id);

            if (exercise == null || !exercise.getUser().equals(user)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            Time startTimestamp = Time.valueOf(startTime.replace("T", " ").replace("Z", ""));
            Time endTimestamp = Time.valueOf(endTime.replace("T", " ").replace("Z", ""));

            exercise.setStartTime(startTimestamp);
            exercise.setEndTime(endTimestamp);
            // Update other exercise fields if needed

            Exercise updatedExercise = exerciseService.saveExercise(exercise);

            return ResponseEntity.ok(updatedExercise);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete/{exerciseId}")
    public ResponseEntity<String> deleteExercise(
            @RequestHeader("Auth") String tokenHeader,
            @PathVariable Long exerciseId
    ) {
        try {
            // Extract the username (email) from the token
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            User user = userService.findByUsername(username);

            // Check if the exercise belongs to the user
            Exercise exercise = exerciseService.findById(exerciseId);
            if (exercise == null || !exercise.getUser().equals(user)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Unauthorized access to exercise with ID: " + exerciseId);
            }

            // Delete the exercise
            exerciseService.deleteExercise(exerciseId);

            return ResponseEntity.ok("Exercise with ID: " + exerciseId + " deleted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }



}