package com.example.jwt.controler;

import com.example.jwt.FoodTodayResponse.mealResponse;
import com.example.jwt.dtos.ExerciseDTO;
import com.example.jwt.entities.Exercise;
import com.example.jwt.entities.User;
import com.example.jwt.entities.UserProfile;
import com.example.jwt.exception.ExceedsDurationLimitException;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.ExerciseService;
import com.example.jwt.service.FoodTodayService.IngrdientService;
import com.example.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


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

//    @PostMapping("/add")
//    public Exercise addExercise(
//            @RequestHeader("Auth") String tokenHeader,
//            @RequestParam String activityType,
//            @RequestParam String startTime,
//            @RequestParam String endTime,
//            @RequestParam double duration
//    ) {
//        try {
//            String token = tokenHeader.replace("Bearer ", "");
//            String username = jwtHelper.getUsernameFromToken(token);
//            User user = userService.findByUsername(username);
//
////            LocalTime startTimestamp = Time.valueOf(startTime.replace("T", " ").replace("Z", ""));
////            LocalTime endTimestamp = Time.valueOf(endTime.replace("T", " ").replace("Z", ""));
//            LocalTime startTimestamp = LocalTime.parse(startTime); // Parse startTime directly to LocalTime
//            LocalTime endTimestamp = LocalTime.parse(endTime);     // Parse endTime directly to LocalTime
//
//            Exercise exercise = new Exercise();
//            exercise.setActivityType(activityType);
//            exercise.setStartTime(startTimestamp);
//            exercise.setEndTime(endTimestamp);
//            exercise.setDuration(duration);
//
//            System.out.println("Before calculateAndSaveExercise");
//            Exercise savedExercise = exerciseService.   calculateAndSaveExercise(exercise, user, duration);
//            System.out.println("After calculateAndSaveExercise");
//
//            return savedExercise;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }
//@PostMapping("/add")
//public Exercise addExercise(
//        @RequestHeader("Auth") String tokenHeader,
//        @RequestParam String activityType,
//        @RequestParam String startTime,
//        @RequestParam String endTime,
//        @RequestParam double duration
//) {
//    try {
//        String token = tokenHeader.replace("Bearer ", "");
//        String username = jwtHelper.getUsernameFromToken(token);
//        User user = userService.findByUsername(username);
//
//        LocalTime startTimestamp = LocalTime.parse(startTime);
//        LocalTime endTimestamp = LocalTime.parse(endTime);
//
//        Exercise exercise = new Exercise();
//        exercise.setActivityType(activityType);
//        exercise.setStartTime(startTimestamp);
//        exercise.setEndTime(endTimestamp);
//        exercise.setDuration(duration);
//
//        Exercise savedExercise = exerciseService.calculateAndSaveExercise(exercise, user, duration);
//        return savedExercise;
//    } catch (Exception e) {
//        e.printStackTrace();
//        throw e;
//    }
//}

    @PostMapping("/add")
    public Exercise addExercise(
            @RequestHeader("Auth") String tokenHeader,
            @RequestParam String activityType,
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam double duration,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate exerciseDate
    ) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            User user = userService.findByUsername(username);

            LocalTime startTimestamp = LocalTime.parse(startTime);
            LocalTime endTimestamp = LocalTime.parse(endTime);

            Exercise exercise = new Exercise();
            exercise.setActivityType(activityType);
            exercise.setStartTime(startTimestamp);
            exercise.setEndTime(endTimestamp);
            exercise.setDuration(duration);

            Exercise savedExercise = exerciseService.calculateAndSaveExercise(exercise, user, duration, exerciseDate);
            return savedExercise;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @ExceptionHandler(ExceedsDurationLimitException.class)
    public ResponseEntity<String> handleExceedsDurationLimitException(ExceedsDurationLimitException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }


//    @GetMapping("/getByDateAndActivityType")
//            public List<Exercise> getExercisesByDateAndActivityType(
//                    @RequestHeader("Auth") String tokenHeader,
//                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
//                    @RequestParam String activityType
//    ) {
//                try {
//                    // Extract the username (email) from the token
//                    String token = tokenHeader.replace("Bearer ", "");
//                    String username = jwtHelper.getUsernameFromToken(token);
//                    User user = userService.findByUsername(username);
//
//                    // Retrieve exercises by date and activityType
//                    List<Exercise> exercises = exerciseService.findByUserAndDateAndActivityType(user, date, activityType);
//
//
//                    // Formatting startTime and endTime to string format "HH:mm:ss" for serialization
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
//                    exercises.forEach(exercise -> {
//                        exercise.setFormattedStartTime(exercise.getFormattedStartTime());
//                        exercise.setFormattedEndTime(exercise.getFormattedEndTime());
//                    });
//
//            // Exercises retrieved successfully
//            return exercises;
//        } catch (Exception e) {
//             e.printStackTrace();
//            throw e;
//        }
//    }
//@GetMapping("/getByDateAndActivityType")
//public List<ExerciseDTO> getExercisesByDateAndActivityType(
//        @RequestHeader("Auth") String tokenHeader,
//        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
//        @RequestParam String activityType
//) {
//    try {
//        // Extract the username (email) from the token
//        String token = tokenHeader.replace("Bearer ", "");
//        String username = jwtHelper.getUsernameFromToken(token);
//        User user = userService.findByUsername(username);
//
//        // Retrieve exercises by date and activityType
//        List<Exercise> exercises = exerciseService.findByUserAndDateAndActivityType(user, date, activityType);
//
//        // Mapping Exercise entities to ExerciseDTOs
//        List<ExerciseDTO> exerciseDTOs = exercises.stream().map(exercise ->
//                new ExerciseDTO(
//                        exercise.getId(),
//                        exercise.getDate().toString(), // Convert LocalDate to String
//                        exercise.getActivityType(),
//                        exercise.getFormattedStartTime(), // Use formatted start time from entity
//                        exercise.getFormattedEndTime(), // Use formatted end time from entity
//                        exercise.getCaloriesBurned()
//                )).collect(Collectors.toList());
//
//        // Exercises retrieved successfully
//        return exerciseDTOs;
//    } catch (Exception e) {
//        e.printStackTrace();
//        throw e;
//    }
//}
@GetMapping("/getByDateAndActivityType")
public List<ExerciseDTO> getExercisesByDateAndActivityType(
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

        // Map Exercise entities to ExerciseDTOs
        List<ExerciseDTO> exerciseDTOs = exercises.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        // Exercises retrieved successfully
        return exerciseDTOs;
    } catch (Exception e) {
        e.printStackTrace();
        throw e;
    }
}

    // Helper method to convert Exercise entity to ExerciseDTO
    private ExerciseDTO convertToDTO(Exercise exercise) {
        ExerciseDTO exerciseDTO = new ExerciseDTO();
        exerciseDTO.setId(exercise.getId());
        exerciseDTO.setDate(exercise.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        exerciseDTO.setActivityType(exercise.getActivityType());
        exerciseDTO.setStartTime(exercise.getFormattedStartTime());
        exerciseDTO.setEndTime(exercise.getFormattedEndTime());
        exerciseDTO.setCaloriesBurned(exercise.getCaloriesBurned());
        exerciseDTO.setDuration(exercise.getDuration());
        return exerciseDTO;
    }


    @GetMapping("/getByDate")
    public List<ExerciseDTO> getExercisesByDate(
            @RequestHeader("Auth") String tokenHeader,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            User user = userService.findByUsername(username);

            List<Exercise> exercises = exerciseService.findByUserAndDate(user, date);

            // Map Exercise entities to ExerciseDTOs
            List<ExerciseDTO> exerciseDTOs = exercises.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return exerciseDTOs;
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

            Exercise exercise = exerciseService.findByIdd(id);

            if (exercise == null || !exercise.getUser().equals(user)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

//            Time startTimestamp = Time.valueOf(startTime.replace("T", " ").replace("Z", ""));
//            Time endTimestamp = Time.valueOf(endTime.replace("T", " ").replace("Z", ""));

            LocalTime startTimestamp = LocalTime.parse(startTime); // Parse startTime directly to LocalTime
            LocalTime endTimestamp = LocalTime.parse(endTime);     // Parse endTime directly to LocalTime
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

//    @GetMapping("/total-calories")
//    public double getTotalCaloriesBurned(@RequestHeader("Auth") String tokenHeader) {
//
//        // Extract the username (email) from the token
//        String token = tokenHeader.replace("Bearer ", "");
//        String username = jwtHelper.getUsernameFromToken(token);
//        User user = userService.findByUsername(username);
//
//        return exerciseService.getTotalCaloriesBurned(user);
//    }
//@GetMapping("/total-calories")
//public double getTotalCaloriesBurned(@RequestHeader("Auth") String tokenHeader) {
//
//    // Extract the username (email) from the token
//    String token = tokenHeader.replace("Bearer ", "");
//    String username = jwtHelper.getUsernameFromToken(token);
//    User user = userService.findByUsername(username);
//
//    return exerciseService.getTotalCaloriesBurned(user);
//}


//    @GetMapping("/calories-burned")
//    public Double getCaloriesBurned(@RequestHeader("Auth") String tokenHeader,
//                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate currentDate) {
//        String token = tokenHeader.replace("Bearer ", "");
//        String username = jwtHelper.getUsernameFromToken(token);
//        User user = userService.findByUsername(username);
//
//        return exerciseService.getTotalCaloriesBurned(user.getUserProfile(), user.getExercises());
//    }
@Autowired
private IngrdientService ingrdientService;
//@GetMapping("/calories-burned")
//public Map<String, Double> getCaloriesBurned(@RequestHeader("Auth") String tokenHeader,
//                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate currentDate
//) {
//    // Extract the username (email) from the token
//    String token = tokenHeader.replace("Bearer ", "");
//    String username = jwtHelper.getUsernameFromToken(token);
//    User user = userService.findByUsername(username);
//
//    double totalCaloriesBurned = exerciseService.getTotalCaloriesBurned(user.getUserProfile(), user.getExercises(),currentDate);
//    // Calculate total energy intake
//    List<mealResponse> totalEnergyIntake = ingrdientService.getTotalEnergyIntakeByDate(user, currentDate);
//
//    // Prepare the response as a map
//    Map<String, Double> response = new HashMap<>();
//    response.put("totalCaloriesBurned", totalCaloriesBurned);
//    response.put("totalEnergyIntake", totalEnergyIntake);
//
//    return response;
//}



//    @GetMapping("/calories-burned-and-energy-intake")
//    public Map<String, Object> getCaloriesBurnedAndEnergyIntake(
//            @RequestHeader("Auth") String tokenHeader,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
//
//        // Extract the username (email) from the token
//        String token = tokenHeader.replace("Bearer ", "");
//        String username = jwtHelper.getUsernameFromToken(token);
//        User user = userService.findByUsername(username);
//
//        // Calculate total calories burned
//        double totalCaloriesBurned = exerciseService.getTotalCaloriesBurnedAndDuration(user.getUserProfile(), user.getExercises(),date);
//
//        LocalDate yesterdayDate = date.minusDays(1);
//        // Calculate total energy intake
//        List<mealResponse> totalEnergyIntake = ingrdientService.getTotalEnergyIntakeByDate(user, yesterdayDate);
//
//        // Calculate rest intake calories
//        double restIntakeCalories = calculateRestIntakeCalories(totalCaloriesBurned, totalEnergyIntake);
//
//
//        // Prepare the response as a map
//        Map<String, Object> response = new HashMap<>();
//        response.put("totalCaloriesBurned", totalCaloriesBurned);
//        response.put("totalEnergyIntake", totalEnergyIntake);
//        response.put("restIntakeCalories", restIntakeCalories);
//
//        return response;
//    }



//    @GetMapping("/calories-burned-and-energy-intake-range")
//    public Map<LocalDate, Map<String, Object>> getCaloriesBurnedAndEnergyIntakeForRange(
//            @RequestHeader("Auth") String tokenHeader,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
//
//        String token = tokenHeader.replace("Bearer ", "");
//        String username = jwtHelper.getUsernameFromToken(token);
//        User user = userService.findByUsername(username);
//
//        Map<LocalDate, Map<String, Object>> responseMap = new TreeMap<>(Comparator.reverseOrder()); // TreeMap to maintain descending order
//        LocalDate currentDate = startDate;
//
//        while (!currentDate.isAfter(endDate)) {
//            // Calculate total calories burned and duration for the current date
//            Map<String, Object> totalCaloriesAndDuration = exerciseService.getTotalCaloriesBurnedAndDuration(user.getUserProfile(), user.getExercises(), currentDate);
//
//            // Calculate total energy intake for the previous date
//            LocalDate yesterdayDate = currentDate.minusDays(1);
//            List<mealResponse> totalEnergyIntake = ingrdientService.getTotalEnergyIntakeByDate(user, yesterdayDate);
//
//            // Calculate rest intake calories
//            double totalCaloriesBurned = (Double) totalCaloriesAndDuration.get("totalCaloriesBurned");
//            double totalCaloriesExpenditure = (Double) totalCaloriesAndDuration.get("totalCaloriesExpenditure");
//            double restIntakeCalories = calculateRestIntakeCalories(totalCaloriesBurned, totalEnergyIntake);
//            List<Map<String, Object>> exerciseDetails = (List<Map<String, Object>>) totalCaloriesAndDuration.get("exerciseDetails");
//            // Calculate energy balance
//            double energyBalance = calculateEnergyBalance(totalEnergyIntake, totalCaloriesExpenditure);
//
//            // Prepare response for the current date
//            Map<String, Object> response = new HashMap<>();
//            response.put("totalCaloriesBurned", totalCaloriesBurned);
//            response.put("totalDuration", (Double) totalCaloriesAndDuration.get("totalDuration"));
//            response.put("totalDuration", (Double) totalCaloriesAndDuration.get("totalDuration")/60); // Include total duration in the response
//            response.put("restDuration", (24-((Double) totalCaloriesAndDuration.get("totalDuration")/60))); // Include total duration in the response
//            response.put("totalEnergyIntake", totalEnergyIntake);
//            response.put("restIntakeCalories", restIntakeCalories);
//            response.put("totalCaloriesExpenditure", totalCaloriesExpenditure);
//            response.put("energyBalance", energyBalance);
//            response.put("exerciseDetails", exerciseDetails); // Include detailed exercise data
//
//            // Add response to the map with current date as key
//            responseMap.put(currentDate, response);
//
//            // Move to the next date
//            currentDate = currentDate.plusDays(1);
//        }
//
//        return responseMap;
//    }

    @GetMapping("/calories-burned-and-energy-intake-range")
    public Map<LocalDate, Map<String, Object>> getCaloriesBurnedAndEnergyIntakeForRange(
            @RequestHeader("Auth") String tokenHeader,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(username);

        Map<LocalDate, Map<String, Object>> responseMap = new TreeMap<>(Comparator.reverseOrder()); // TreeMap to maintain descending order
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            // Calculate total calories burned and duration for the current date
            Map<String, Object> totalCaloriesAndDuration = exerciseService.getTotalCaloriesBurnedAndDuration(user.getUserProfile(), user.getExercises(), currentDate);

            // Calculate total energy intake for the current date, not yesterdayDate
            List<mealResponse> totalEnergyIntake = ingrdientService.getTotalEnergyIntakeByDate(user, currentDate);

            // Calculate rest intake calories
            double totalCaloriesBurned = (Double) totalCaloriesAndDuration.get("totalCaloriesBurned");
            double totalCaloriesExpenditure = (Double) totalCaloriesAndDuration.get("totalCaloriesExpenditure");
            double restIntakeCalories = calculateRestIntakeCalories(totalCaloriesBurned, totalEnergyIntake);
            List<Map<String, Object>> exerciseDetails = (List<Map<String, Object>>) totalCaloriesAndDuration.get("exerciseDetails");
            // Calculate energy balance
            double energyBalance = calculateEnergyBalance(totalEnergyIntake, totalCaloriesExpenditure);

            // Prepare response for the current date
            Map<String, Object> response = new HashMap<>();
            response.put("totalCaloriesBurned", totalCaloriesBurned);
//            response.put("totalDuration", (Double) totalCaloriesAndDuration.get("totalDuration"));

            response.put("totalDuration", (Double) totalCaloriesAndDuration.get("totalDuration")/60); // Include total duration in the response
            response.put("restDuration", (24-((Double) totalCaloriesAndDuration.get("totalDuration")/60))); // Include total duration in the response
            response.put("totalEnergyIntake", totalEnergyIntake);
            response.put("restIntakeCalories", restIntakeCalories);
            response.put("totalCaloriesExpenditure", totalCaloriesExpenditure);
            response.put("energyBalance", energyBalance);
            response.put("exerciseDetails", exerciseDetails); // Include detailed exercise data

            // Add response to the map with current date as key
            responseMap.put(currentDate, response);

            // Move to the next date
            currentDate = currentDate.plusDays(1);
        }

        return responseMap;
    }

    //    @GetMapping("/calories-burned-and-energy-intake")
//    public Map<String, Object> getCaloriesBurnedAndEnergyIntake(
//            @RequestHeader("Auth") String tokenHeader,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
//
//        // Extract the username (email) from the token
//        String token = tokenHeader.replace("Bearer ", "");
//        String username = jwtHelper.getUsernameFromToken(token);
//        User user = userService.findByUsername(username);
//
//        // Calculate total calories burned and duration
//        Map<String, Object> totalCaloriesAndDuration = exerciseService.getTotalCaloriesBurnedAndDuration(user.getUserProfile(), user.getExercises(), date);
//
//        // Extract total calories burned and total duration from the map
//        double totalCaloriesBurned = (Double) totalCaloriesAndDuration.get("totalCaloriesBurned");
//        double totalDuration = (Double) totalCaloriesAndDuration.get("totalDuration");
//        double totalCaloriesExpenditure = (Double) totalCaloriesAndDuration.get("totalCaloriesExpenditure");
//
//        LocalDate yesterdayDate = date.minusDays(1);
//        // Calculate total energy intake
//        List<mealResponse> totalEnergyIntake = ingrdientService.getTotalEnergyIntakeByDate(user, yesterdayDate);
//
//        // Calculate rest intake calories
//        double restIntakeCalories = calculateRestIntakeCalories(totalCaloriesBurned, totalEnergyIntake);
//
//
//        // Calculate energy balance
//        double energyBalance = calculateEnergyBalance(totalEnergyIntake, totalCaloriesExpenditure);
//
//
//
//        // Prepare the response as a map
//        Map<String, Object> response = new HashMap<>();
//        response.put("totalCaloriesBurned", totalCaloriesBurned);
//        response.put("totalDuration", totalDuration); // Include total duration in the response
//        response.put("totalEnergyIntake", totalEnergyIntake);
//        response.put("restIntakeCalories", restIntakeCalories);
//        response.put("totalCaloriesExpenditure", totalCaloriesExpenditure);
//        response.put("energyBalance",energyBalance);
//
//        return response;
//    }
@GetMapping("/calories-burned-and-energy-intake")
public Map<String, Object> getCaloriesBurnedAndEnergyIntake(
        @RequestHeader("Auth") String tokenHeader,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

    // Extract the username (email) from the token
    String token = tokenHeader.replace("Bearer ", "");
    String username = jwtHelper.getUsernameFromToken(token);
    User user = userService.findByUsername(username);

    // Calculate total calories burned and duration
    Map<String, Object> totalCaloriesAndDuration = exerciseService.getTotalCaloriesBurnedAndDuration(user.getUserProfile(), user.getExercises(), date);

    // Extract total calories burned and total duration from the map
    double totalCaloriesBurned = (Double) totalCaloriesAndDuration.get("totalCaloriesBurned");
    double totalDuration = (Double) totalCaloriesAndDuration.get("totalDuration");
    double totalCaloriesExpenditure = (Double) totalCaloriesAndDuration.get("totalCaloriesExpenditure");
    List<Map<String, Object>> exerciseDetails = (List<Map<String, Object>>) totalCaloriesAndDuration.get("exerciseDetails");

    LocalDate yesterdayDate = date.minusDays(1);
    // Calculate total energy intake
    List<mealResponse> totalEnergyIntake = ingrdientService.getTotalEnergyIntakeByDate(user, yesterdayDate);

    // Calculate rest intake calories
    double restIntakeCalories = calculateRestIntakeCalories(totalCaloriesBurned, totalEnergyIntake);

    // Calculate energy balance
    double energyBalance = calculateEnergyBalance(totalEnergyIntake, totalCaloriesExpenditure);

    // Prepare the response as a map
    Map<String, Object> response = new HashMap<>();
    response.put("totalCaloriesBurned", totalCaloriesBurned);
    response.put("totalDuration", totalDuration/60); // Include total duration in the response
    response.put("restDuration", (24-(totalDuration/60))); // Include total duration in the response
    response.put("totalEnergyIntake", totalEnergyIntake);
    response.put("restIntakeCalories", restIntakeCalories);
    response.put("totalCaloriesExpenditure", totalCaloriesExpenditure);
    response.put("energyBalance", energyBalance);
    response.put("exerciseDetails", exerciseDetails); // Include detailed exercise data


    return response;
}
//@GetMapping("/calories-burned-and-energy-intake")
//public Map<String, Object> getCaloriesBurnedAndEnergyIntake(
//        @RequestHeader("Auth") String tokenHeader,
//        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
//
//    // Extract the username (email) from the token
//    String token = tokenHeader.replace("Bearer ", "");
//    String username = jwtHelper.getUsernameFromToken(token);
//    User user = userService.findByUsername(username);
//
//    // Calculate total calories burned and duration
//    Map<String, Object> totalCaloriesAndDuration = exerciseService.getTotalCaloriesBurnedAndDuration(user.getUserProfile(), user.getExercises(), date);
//
//    // Extract total calories burned and total duration from the map
//    double totalCaloriesBurned = (Double) totalCaloriesAndDuration.get("totalCaloriesBurned");
//    double totalDuration = (Double) totalCaloriesAndDuration.get("totalDuration");
//    double totalCaloriesExpenditure = (Double) totalCaloriesAndDuration.get("totalCaloriesExpenditure");
//    List<Map<String, Object>> exerciseYesterdayDetails = (List<Map<String, Object>>) totalCaloriesAndDuration.get("exerciseYesterdayDetails");
//    List<Map<String, Object>> exerciseTodayDetails = (List<Map<String, Object>>) totalCaloriesAndDuration.get("exerciseTodayDetails");
//
//    LocalDate yesterdayDate = date.minusDays(1);
//    // Calculate total energy intake
//    List<mealResponse> totalEnergyIntake = ingrdientService.getTotalEnergyIntakeByDate(user, yesterdayDate);
//
//    // Calculate rest intake calories
//    double restIntakeCalories = calculateRestIntakeCalories(totalCaloriesBurned, totalEnergyIntake);
//
//    // Calculate energy balance
//    double energyBalance = calculateEnergyBalance(totalEnergyIntake, totalCaloriesExpenditure);
//
//    // Prepare the response as a map
//    Map<String, Object> response = new HashMap<>();
//    response.put("totalCaloriesBurned", totalCaloriesBurned);
//    response.put("totalDuration", totalDuration); // Include total duration in the response
//    response.put("totalEnergyIntake", totalEnergyIntake);
//    response.put("restIntakeCalories", restIntakeCalories);
//    response.put("totalCaloriesExpenditure", totalCaloriesExpenditure);
//    response.put("energyBalance", energyBalance);
//    response.put("exerciseYesterdayDetails", exerciseYesterdayDetails); // Include yesterday's exercise details
//    response.put("exerciseTodayDetails", exerciseTodayDetails); // Include today's exercise details
//
//    return response;
//}



    private double calculateEnergyBalance(List<mealResponse> totalEnergyIntake, double totalCaloriesExpenditure) {
        double totalEnergyIntakeSum = 0.0;

        // Sum up total meal energy intake
        for (mealResponse meal : totalEnergyIntake) {
            totalEnergyIntakeSum += meal.getMealEnergy();
        }

        // Calculate energy balance (difference)
        double energyBalance = totalEnergyIntakeSum - totalCaloriesExpenditure;

        return energyBalance;
    }

    private double calculateRestIntakeCalories(double totalCaloriesBurned, List<mealResponse> totalEnergyIntake) {
        double totalMealEnergy = 0.0;

        // Sum up total meal energy intake
        for (mealResponse meal : totalEnergyIntake) {
            totalMealEnergy += meal.getMealEnergy();
        }

        // Calculate rest intake calories (difference)
        double restIntakeCalories = totalMealEnergy - totalCaloriesBurned;

        return restIntakeCalories;
    }

}