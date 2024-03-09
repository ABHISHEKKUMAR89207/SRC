package com.example.jwt.entities.myActivity;

import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.Activities;
import com.example.jwt.entities.dashboardEntity.healthTrends.SleepDuration;
import com.example.jwt.entities.Exercise;
import com.example.jwt.repository.ActivityRepository;
import com.example.jwt.repository.ExerciseRepository;
import com.example.jwt.repository.repositoryHealth.SleepDurationRepository;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.ExerciseService;
import com.example.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class HealthDataController {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private SleepDurationRepository sleepDurationRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserService userService;


    @GetMapping("/health-dashboard")
    public ResponseEntity<?> getHealthDashboardDataByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestHeader("Auth") String tokenHeader) {

        // Extract user information from the token
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(username);

        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        // Fetch activities, sleep durations, and exercises for the specified date and user
        List<Activities> activities = activityRepository.findByActivityDateAndUser(date, user);
        List<SleepDuration> sleepDurations = sleepDurationRepository.findByDateOfSleepAndUser(date, user);
        List<Exercise> exercises = exerciseService.findByUserAndDate(user, date);

        // Calculate total exercise duration
        double totalExerciseDuration = exercises.stream().mapToDouble(Exercise::getDuration).sum();

        // Extracting required fields from Activities
        int totalSteps = activities.stream().mapToInt(Activities::getSteps).sum();
        double totalCalories = activities.stream().mapToDouble(Activities::getCalory).sum();

        // Extracting required fields from SleepDuration
        long totalDuration = sleepDurations.stream().mapToLong(SleepDuration::getDuration).sum();
        double totalManualDuration = sleepDurations.stream().mapToDouble(SleepDuration::getManualDuration).sum();

        // Constructing ExerciseData list
        List<HealthDashboardResponse.ExerciseData> exerciseDataList = new ArrayList<>();
        for (Exercise exercise : exercises) {
            exerciseDataList.add(new HealthDashboardResponse.ExerciseData(exercise.getActivityType(), exercise.getDuration(), exercise.getCaloriesBurned()));
        }

        // Construct response object
        HealthDashboardResponse response = new HealthDashboardResponse(date, totalSteps, totalCalories, totalDuration, totalManualDuration, exerciseDataList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/health-dashboard/range")
    public ResponseEntity<List<HealthDashboardResponse>> getHealthDashboardDataByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestHeader("Auth") String tokenHeader) {

        // Extract user information from the token
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(username);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Fetch activities, sleep durations, and exercises for the specified date range and user
        List<LocalDate> dateRange = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());
        List<HealthDashboardResponse> responseList = new ArrayList<>();

        for (LocalDate date : dateRange) {
            List<Activities> activities = activityRepository.findByUserAndActivityDateBetween(user, date, date);
            List<SleepDuration> sleepDurations = sleepDurationRepository.findByUserAndDateOfSleepBetween(user, date, date);
            List<Exercise> exercises = exerciseRepository.findByUserAndDateBetween(user, date, date);

            // Calculate total exercise duration
            double totalExerciseDuration = exercises.stream().mapToDouble(Exercise::getDuration).sum();

            // Extracting required fields from Activities
            int totalSteps = activities.stream().mapToInt(Activities::getSteps).sum();
            double totalCalories = activities.stream().mapToDouble(Activities::getCalory).sum();

            // Extracting required fields from SleepDuration
            long totalDuration = sleepDurations.stream().mapToLong(SleepDuration::getDuration).sum();
            double totalManualDuration = sleepDurations.stream().mapToDouble(SleepDuration::getManualDuration).sum();

            // Constructing ExerciseData list
            List<HealthDashboardResponse.ExerciseData> exerciseDataList = new ArrayList<>();
            for (Exercise exercise : exercises) {
                exerciseDataList.add(new HealthDashboardResponse.ExerciseData(exercise.getActivityType(), exercise.getDuration(), exercise.getCaloriesBurned()));
            }

            // Construct response object
            HealthDashboardResponse response = new HealthDashboardResponse(date, totalSteps, totalCalories, totalDuration, totalManualDuration, exerciseDataList);
            responseList.add(response);
        }

        return new ResponseEntity<>(responseList, HttpStatus.OK);
}

}
