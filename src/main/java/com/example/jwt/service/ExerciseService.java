package com.example.jwt.service;

import com.example.jwt.dtos.CalculationResult;
import com.example.jwt.entities.Exercise;
import com.example.jwt.entities.User;
import com.example.jwt.entities.UserProfile;
import com.example.jwt.entities.dashboardEntity.Activities;
import com.example.jwt.exception.ExceedsDurationLimitException;
import com.example.jwt.repository.ExerciseRepository;
import com.example.jwt.repository.UserProfileRepository;
import com.example.jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;


@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final UserProfileRepository userProfileRepository;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository, UserProfileRepository userProfileRepository) {
        this.exerciseRepository = exerciseRepository;
        this.userProfileRepository = userProfileRepository;
    }

//    public Exercise calculateAndSaveExercise(Exercise exercise, User user, double duration) {
//        String activityType = exercise.getActivityType();
//
//        LocalTime startTime = exercise.getStartTime();
//
//
//        LocalTime wakeupTime = user.getUserProfile().getWakeupTime();
//
//        double caloriesBurned = calculateCaloriesBurned(startTime,activityType, duration,user);
//
//        exercise.setCaloriesBurned(caloriesBurned);
//        exercise.setUser(user);
//
//        return exerciseRepository.save(exercise);
//    }

//    public Exercise calculateAndSaveExercise(Exercise exercise, User user, double duration) {
//        String activityType = exercise.getActivityType();
//        LocalTime startTime = exercise.getStartTime();
//        LocalTime wakeupTime = user.getUserProfile().getWakeupTime();
//
//        CalculationResult result = calculateCaloriesBurned(startTime, activityType, duration, user);
//
//        if (result.getErrorMessage() != null) {
//            // Throw the custom exception with the error message
//            throw new ExceedsDurationLimitException(result.getErrorMessage());
//        }
//        double caloriesBurned = result.getCaloriesBurned();
//        exercise.setCaloriesBurned(caloriesBurned);
//        exercise.setUser(user);
//
//        return exerciseRepository.save(exercise);
//    }
public Exercise calculateAndSaveExercise(Exercise exercise, User user, double duration, LocalDate exerciseDate) {
    String activityType = exercise.getActivityType();
    LocalTime startTime = exercise.getStartTime();
    LocalTime wakeupTime = user.getUserProfile().getWakeupTime();

    exercise.setDate(exerciseDate); // Set the exercise date here
    exercise.setUser(user);
    CalculationResult result = calculateCaloriesBurned(startTime, activityType, duration, user, exerciseDate);

    if (result.getErrorMessage() != null) {
        throw new ExceedsDurationLimitException(result.getErrorMessage());
    }
    double caloriesBurned = result.getCaloriesBurned();
    exercise.setCaloriesBurned(caloriesBurned);


    return exerciseRepository.save(exercise);
}

    public List<Exercise> getExerciseForUserAndDate(User user, LocalDate date) {
        return exerciseRepository.findByUserAndDate(user, date);
    }

    public List<Exercise> findByUserAndDateAndActivityType(User user, LocalDate date, String activityType) {
        return exerciseRepository.findByUserAndDateAndActivityType(user, date, activityType);
    }

    public List<Exercise> findByUserAndDate(User user, LocalDate date) {
        return exerciseRepository.findByUserAndDate(user, date);
    }

    public Exercise findById(Long id) {
        Optional<Exercise> exerciseOptional = exerciseRepository.findById(id);
        return exerciseOptional.orElse(null);
    }

    public void deleteExercise(Long id) {
        exerciseRepository.deleteById(id);
    }



//    private double calculateCaloriesBurned(LocalTime startTime, String activityType, double duration, User user) {
//        // Simple assumption: Calories burned per hour for different activities
//        double caloriesPerHour;
//
//        double totalDuration = 0.0;
//        double totalNewDuration = 0.0;
//        double totalHour = 0.0;
//
//
//        // Calculate total duration from exercises within wakeupTime range
//        LocalTime wakeupTime = user.getUserProfile().getWakeupTime();
//        LocalDateTime startDateTime = LocalDate.now().atTime(wakeupTime);
//        System.out.println("Start date ----------" + startDateTime);
//        LocalDateTime endDateTime = startDateTime.minusDays(1);
//        System.out.println("End Date ---------" + endDateTime);
//
//
////        for (Exercise exercise : user.getExercises()) {
////            // Check if exercise date is yesterday or today and if startTime is between wakeupTimes
////            if ((exercise.getDate().isEqual(endDateTime.toLocalDate()) && exercise.getStartTime().isAfter(wakeupTime)) ||
////                    (exercise.getDate().isEqual(startDateTime.toLocalDate()) && exercise.getStartTime().isBefore(wakeupTime))) {
////
////                if (exercise.getCaloriesBurned() != null) {
////                    totalDuration += exercise.getDuration();
////                }
////            }
////        }
//
////        if (startTime.isAfter(wakeupTime)) {
////            System.out.println("duration if condition-------");
////            // Iterate over exercises and calculate the total duration
////            for (Exercise exercise : user.getExercises()) {
////                if ((exercise.getDate().isEqual(endDateTime.toLocalDate()) && exercise.getStartTime().isAfter(wakeupTime)) ||
////                        (exercise.getDate().isEqual(startDateTime.toLocalDate()) && exercise.getStartTime().isBefore(wakeupTime))) {
////
////                    if (exercise.getCaloriesBurned() != null) {
////                        totalDuration += exercise.getDuration();
////                        System.out.println("duration if condition-------duration"+totalDuration);
////                    }
////                }
////            }
//        if (startTime.isAfter(wakeupTime)) {
//            System.out.println("Duration if condition (startTime is after wakeupTime)");
//            // Iterate over exercises and calculate the total duration for today after wakeup time
//            for (Exercise exercise : user.getExercises()) {
//                if (exercise.getDate().isEqual(LocalDate.now()) && exercise.getStartTime().isAfter(wakeupTime)) {
//                    if (exercise.getCaloriesBurned() != null) {
//                        totalDuration += exercise.getDuration();
//                        System.out.println("Duration if condition (current date, after wakeupTime): " + totalDuration);
//                    }
//                }
//            }
//        } else {
//            System.out.println("duration else condition-------");
//            // If startTime is before wakeupTime
//            for (Exercise exercise : user.getExercises()) {
//                if ((exercise.getDate().isEqual(endDateTime.toLocalDate()) && exercise.getStartTime().isAfter(wakeupTime)) ||
//                        (exercise.getDate().isEqual(startDateTime.toLocalDate()) && exercise.getStartTime().isBefore(wakeupTime))) {
//
//                    if (exercise.getCaloriesBurned() != null) {
//                        totalDuration += exercise.getDuration();
//                        System.out.println("duration else condition-------duration" + totalDuration);
//                    }
//                }
//            }
//        }
//
//        totalNewDuration = totalDuration + duration;
//
//        System.out.println("total new duration -------" + totalNewDuration);
//
//        totalHour = totalNewDuration / 60;
//
//        System.out.println("total hour ----------" + totalHour);
//
//        if (totalHour <= 24) {
//            switch (activityType) {
//                case "SLEEPING":
//                    caloriesPerHour = 1;
//                    break;
//
//                case "SITTING ONLY":
//                    caloriesPerHour = 1.2;
//                    break;
//
//                case "WATCHING T.V":
//                    caloriesPerHour = 1.4;
//                    break;
//
//                case "WORKING ON COMPUTERS":
//                    caloriesPerHour = 1.5;
//                    break;
//
//                case "PLAYING GAMES WHILE SITTING (INDOOR GAMES)":
//                    caloriesPerHour = 1.5;
//                    break;
//
//                case "READING PAPER, PUBLIC MAGAZINE, DOING HOMEWORK":
//                    caloriesPerHour = 1.5;
//                    break;
//
//                case "EATING (BREAKFAST/LUNCH/DINNER)":
//                    caloriesPerHour = 1.5;
//                    break;
//
//                case "FRESH UP (BATHING/DRESSING)":
//                    caloriesPerHour = 2.3;
//                    break;
//
//                case "DOING PRAYER/POOJA":
//                    caloriesPerHour = 2;
//                    break;
//
//                case "SOCIALIZATION (TIME PASS WITH FRIENDS/ RELATIVES)":
//                    caloriesPerHour = 1.4;
//                    break;
//
//                case "WALKING/STANDING":
//                    caloriesPerHour = 2;
//                    break;
//
//                case "HOUSEHOLD ACTIVITIES (CLEANING, SWEEPING, WASHING CLOTHES, COOKING)":
//                    caloriesPerHour = 2.5;
//                    break;
//
//                case "GARDENING":
//                    caloriesPerHour = 3.5;
//                    break;
//
//                case "FETCHING WATER":
//                    caloriesPerHour = 4.4;
//                    break;
//
//                case "ANIMAL/CHILD/DEPENDANT CARE":
//                    caloriesPerHour = 2.5;
//                    break;
//
//                case "SHOPPING":
//                    caloriesPerHour = 4;
//                    break;
//
//                case "ARTISAN WORK":
//                    caloriesPerHour = 3.1;
//                    break;
//
//                case "LABOUR WORK":
//                    caloriesPerHour = 5.1;
//                    break;
//
//                case "CULTIVATION":
//                    caloriesPerHour = 5.7;
//                    break;
//
//                case "TRAVELLING":
//                    caloriesPerHour = 2.3;
//                    break;
//
//                case "DRIVING":
//                    caloriesPerHour = 2.3;
//                    break;
//
//                case "CYCLING":
//                    caloriesPerHour = 4.6;
//                    break;
//
//                case "BRISK WALKING":
//                    caloriesPerHour = 3.2;
//                    break;
//
//                case "JOGGING":
//                    caloriesPerHour = 3.8;
//                    break;
//
//                case "ANY EXERCISE":
//                    caloriesPerHour = 3.6;
//                    break;
//
//                case "YOGA":
//                    caloriesPerHour = 2;
//                    break;
//
//                case "SWIMMING":
//                    caloriesPerHour = 9;
//                    break;
//
//                case "PLAYING ANY OUTDOOR GAME":
//                    caloriesPerHour = 6;
//                    break;
//
//                case "LIFTING OF LOADS":
//                    caloriesPerHour = 5.1;
//                    break;
//
//                case "RICKSHAW PULLING":
//                    caloriesPerHour = 5.3;
//                    break;
//
//                case "ANY HEAVY DUTY ACTIVITY":
//                    caloriesPerHour = 7;
//                    break;
//
//                case "MOBILE USAGE":
//                    caloriesPerHour = 1.5;
//                    break;
//
//                default:
//                    caloriesPerHour = 0; // Default value if activity type not recognized
//            }
//
//            // Calculate calories burned based on duration
//            return (user.getUserProfile().getBmr() * caloriesPerHour * (duration / 60)) / 24;
//        } else {
//            return "your 24 hour is complete";
//        }
//    }
public CalculationResult calculateCaloriesBurned(LocalTime startTime, String activityType, double duration, User user, LocalDate exerciseDate) {
    double caloriesPerHour;
    double totalDuration = 0.0;
    double totalNewDuration = 0.0;
    double totalHour = 0.0;
    double durTotalHour = 0.0;

    LocalTime wakeupTime = user.getUserProfile().getWakeupTime();
    System.out.println("Wakeup Time  ---------" + wakeupTime);
    LocalDateTime startDateTime = exerciseDate.atTime(wakeupTime);
//    LocalDateTime startDateTime = LocalDate.now().atTime(wakeupTime);
    System.out.println("Start date ----------" + startDateTime);
//    LocalDateTime endDateTime = startDateTime.minusDays(1);
//    LocalDateTime endDateTime = startDateTime.minusDays(1);
    LocalDateTime endDateTime = startDateTime.plusDays(1).minusMinutes(1);

    System.out.println("End Date ---------" + endDateTime);
    System.out.println("Send StartTime1 ---------" + startTime +" Exercise Date -----"+exerciseDate+" Duration Send -----"+duration);

//    if (startTime.isAfter(wakeupTime)) {
//        System.out.println("duration if condition-------");
//        for (Exercise exercise : user.getExercises()) {
//            if (exercise.getDate().isEqual(LocalDate.now()) && exercise.getStartTime().isAfter(wakeupTime)) {
//                if (exercise.getCaloriesBurned() != null) {
//                    totalDuration += exercise.getDuration();
//                    System.out.println("duration if condition-------duration"+totalDuration);
//                }
//            }
//        }
//    } else {
//        for (Exercise exercise : user.getExercises()) {
//            System.out.println("duration else condition-------");
//            if ((exercise.getDate().isEqual(endDateTime.toLocalDate()) && exercise.getStartTime().isAfter(wakeupTime)) ||
//                    (exercise.getDate().isEqual(startDateTime.toLocalDate()) && exercise.getStartTime().isBefore(wakeupTime))) {
//                if (exercise.getCaloriesBurned() != null) {
//                    totalDuration += exercise.getDuration();
//                    System.out.println("duration else condition-------duration"+totalDuration);
//                }
//            }
//        }
//    }
    if (startTime.isAfter(wakeupTime)) {
        System.out.println("duration if condition-------");
        // Logic for exercises starting after wakeup time on the given exerciseDate
        for (Exercise exercise : user.getExercises()) {
            if (exercise.getDate().isEqual(exerciseDate) && exercise.getStartTime().isAfter(wakeupTime)) {
                if (exercise.getCaloriesBurned() != null) {
                    totalDuration += exercise.getDuration();
                    System.out.println("duration if condition-------duration"+totalDuration);
                }
            }
        }
    } else {
        System.out.println("duration else condition-------");
        // Logic for exercises starting before wakeup time on the given exerciseDate or on the previous day
        for (Exercise exercise : user.getExercises()) {
            if ((exercise.getDate().isEqual(endDateTime.toLocalDate()) && exercise.getStartTime().isAfter(wakeupTime)) ||
                    (exercise.getDate().isEqual(startDateTime.toLocalDate()) && exercise.getStartTime().isBefore(wakeupTime))) {
                if (exercise.getCaloriesBurned() != null) {
                    totalDuration += exercise.getDuration();
                    System.out.println("duration else condition-------duration"+totalDuration);
                }
            }
        }
    }
    // Iterate through user's exercises to calculate totalDuration
//    for (Exercise exercise : user.getExercises()) {
//        LocalDateTime exerciseStartDateTime = LocalDateTime.of(exercise.getDate(), exercise.getStartTime());
//
//        System.out.println("Exercise Start Date Time ----------"+exerciseStartDateTime+" || Duration --------"+exercise.getDuration());
//        // Check if exercise date and time fall within the desired range
//        if (exerciseStartDateTime.isEqual(startDateTime) ||
//                (exerciseStartDateTime.isAfter(startDateTime) && exerciseStartDateTime.isBefore(endDateTime))) {
//
//            if (exercise.getCaloriesBurned() != null) {
//                totalDuration += exercise.getDuration();
//                System.out.println("total Duration ----------- "+totalDuration);
//            }
//        }
//    }
//    // Iterate through user's exercises to calculate total duration
//    for (Exercise exercise : user.getExercises()) {
//        LocalDateTime exerciseStartDateTime = LocalDateTime.of(exercise.getDate(), exercise.getStartTime());
//        System.out.println("Exercise Start Date Time ----------"+exerciseStartDateTime+" || Duration --------"+exercise.getDuration());
//
//        // Check if exercise date and time fall within the desired range
//        if (exerciseStartDateTime.isEqual(startDateTime) ||
//                (exerciseStartDateTime.isAfter(startDateTime) && exerciseStartDateTime.isBefore(endDateTime))) {
//
//            if (exercise.getCaloriesBurned() != null) {
//                totalDuration += exercise.getDuration();
//                                System.out.println("total Duration ----------- "+totalDuration);
//
//            }
//        }
//    }
    // Iterate through user's exercises to calculate totalDuration
    // Iterate through user's exercises to calculate totalDuration
    // Iterate through user's exercises to calculate totalDuration
//    for (Exercise exercise : user.getExercises()) {
//        LocalDateTime exerciseStartDateTime = LocalDateTime.of(exercise.getDate(), exercise.getStartTime());
//        System.out.println("Exercise Start Date Time ----------"+exerciseStartDateTime+" || Duration --------"+exercise.getDuration());
//        // Check if exercise date and time fall within the desired range
//        // Check if exercise date and time fall within the desired range
//        if ((exerciseStartDateTime.isEqual(startDateTime) || exerciseStartDateTime.isAfter(startDateTime)) &&
//                (exerciseStartDateTime.isBefore(endDateTime) || exerciseStartDateTime.isEqual(endDateTime))) {
//
//            if (exercise.getCaloriesBurned() != null) {
//                totalDuration += exercise.getDuration();
//                System.out.println("total Duration ----------- "+totalDuration);
//            }
//        }
//    }


    System.out.println("total Duration ----------- "+totalDuration);

    totalNewDuration = totalDuration + duration;
    System.out.println("total new duration -------" + totalNewDuration);
    totalHour = totalNewDuration / 60;


    durTotalHour = totalDuration/60;
//    String formattedDurTotalHour = String.format("%.2f", durTotalHour); // Format to two decimal places
//    double remainingHours = 24 - durTotalHour;
//    String formattedRemainingHours = String.format("%.2f", remainingHours);
    // Convert totalDuration to HH:MM format
    int hours = (int) totalDuration / 60;
    int minutes = (int) totalDuration % 60;
    String formattedDurTotalHour = String.format("%02d:%02d", hours, minutes);


//    double remainingHours = 24 - durTotalHour;
    double remainingHours = 1440 - totalDuration;
//    String formattedRemainingHours = String.format("%.2f", remainingHours);
//    double remainingHours = 24 - totalHour;
//    hours = (int) remainingHours;
//    minutes = (int) ((remainingHours - hours) * 60);
     hours = (int) remainingHours / 60;
     minutes = (int) remainingHours % 60;
    String formattedRemainingHours = String.format("%02d:%02d", hours, minutes);



    System.out.println("Duration Hour--------------"+durTotalHour);
    System.out.println("total hour ----------" + totalHour);
    if (totalHour <= 24) {
        switch (activityType) {
            case "SLEEPING":
                caloriesPerHour = 1;
                break;
            case "SITTING ONLY":
                caloriesPerHour = 1.2;
                break;
            case "WATCHING T.V":
                caloriesPerHour = 1.4;
                break;
            case "WORKING ON COMPUTERS":
                caloriesPerHour = 1.5;
                break;
            case "PLAYING GAMES WHILE SITTING (INDOOR GAMES)":
                caloriesPerHour = 1.5;
                break;
            case "READING PAPER, PUBLIC MAGAZINE, DOING HOMEWORK":
                caloriesPerHour = 1.5;
                break;
            case "EATING (BREAKFAST/LUNCH/DINNER)":
                caloriesPerHour = 1.5;
                break;
            case "FRESH UP (BATHING/DRESSING)":
                caloriesPerHour = 2.3;
                break;
            case "DOING PRAYER/POOJA":
                caloriesPerHour = 2;
                break;
            case "SOCIALIZATION (TIME PASS WITH FRIENDS/ RELATIVES)":
                caloriesPerHour = 1.4;
                break;
            case "WALKING/STANDING":
                caloriesPerHour = 2;
                break;
            case "HOUSEHOLD ACTIVITIES (CLEANING, SWEEPING, WASHING CLOTHES, COOKING)":
                caloriesPerHour = 2.5;
                break;
            case "GARDENING":
                caloriesPerHour = 3.5;
                break;
            case "FETCHING WATER":
                caloriesPerHour = 4.4;
                break;
            case "ANIMAL/CHILD/DEPENDANT CARE":
                caloriesPerHour = 2.5;
                break;
            case "SHOPPING":
                caloriesPerHour = 4;
                break;
            case "ARTISAN WORK":
                caloriesPerHour = 3.1;
                break;
            case "LABOUR WORK":
                caloriesPerHour = 3.84;
                break;
            case "CULTIVATION":
                caloriesPerHour = 3.93;
                break;
            case "TRAVELLING":
                caloriesPerHour = 2.3;
                break;
            case "DRIVING":
                caloriesPerHour = 2.3;
                break;
            case "CYCLING":
                caloriesPerHour = 4.6;
                break;
            case "BRISK WALKING":
                caloriesPerHour = 3.2;
                break;
            case "JOGGING":
                caloriesPerHour = 3.8;
                break;
            case "ANY EXERCISE":
                caloriesPerHour = 3.6;
                break;
            case "YOGA":
                caloriesPerHour = 2;
                break;
            case "SWIMMING":
                caloriesPerHour = 6;
                break;
            case "PLAYING ANY OUTDOOR GAME":
                caloriesPerHour = 6;
                break;
            case "LIFTING OF LOADS":
                caloriesPerHour = 3.84;
                break;
            case "RICKSHAW PULLING":
                caloriesPerHour = 5.05;
                break;
            case "ANY HEAVY DUTY ACTIVITY":
                caloriesPerHour = 3.8;
                break;
            case "MOBILE USAGE":
                caloriesPerHour = 1.5;
                break;
            default:
                caloriesPerHour = 0;
        }

        double caloriesBurned = (user.getUserProfile().getBmr() * caloriesPerHour * (duration / 60)) / 24;
        return new CalculationResult(caloriesBurned);
    } else {
//        return new CalculationResult("Your "+totalHour+" hours is complete");
        return new CalculationResult(" You have completed " + formattedDurTotalHour + " hours and have only " + formattedRemainingHours + " hours remaining to enter.");
    }
}

//            case "Gardening":
//                caloriesPerHour = 3.5; // Adjust this value based on accurate data
//                break;
//
//            case "Household Activities":
//                caloriesPerHour = 2.5; // Adjust this value based on accurate data
//                break;
//
//            case "Watching TV":
//                caloriesPerHour = 1.4; // Adjust this value based on accurate data
//                break;
//
//            case "PLAYING ANY OUTDOOR GAME":
//                caloriesPerHour = 348; // Adjust this value based on accurate data
//                break;
//
//            case "Table Tennis":
//                caloriesPerHour = 245; // Adjust this value based on accurate data
//                break;
//
//            case "Tennis":
//                caloriesPerHour = 392; // Adjust this value based on accurate data
//                break;
//
//            case "Volley Ball":
//                caloriesPerHour = 180; // Adjust this value based on accurate data
//                break;
//
//            case "Dancing":
//                caloriesPerHour = 372; // Adjust this value based on accurate data
//                break;
//
//            case "Fishing":
//                caloriesPerHour = 222; // Adjust this value based on accurate data
//                break;
//
//            case "Shopping":
//                caloriesPerHour = 204; // Adjust this value based on accurate data
//                break;
//
//            case "Typing":
//                caloriesPerHour = 108; // Adjust this value based on accurate data
//                break;
//
//            case "Sleeping":
//                caloriesPerHour = 1; // Adjust this value based on accurate data
//                break;
//
//            case "Standing":
//                caloriesPerHour = 132; // Adjust this value based on accurate data
//                break;
//
//            case "Sitting":
//                caloriesPerHour = 86; // Adjust this value based on accurate data
//                break;
//
//            case "Cycling":
//                caloriesPerHour = 360; // Adjust this value based on accurate data
//                break;
//
//            case "Walking(4 km per hr)":
//                caloriesPerHour = 160; // Adjust this value based on accurate data
//                break;
//
//            case "Running(12 km per hr)":
//                caloriesPerHour = 750; // Adjust this value based on accurate data
//                break;
//
//            case "Running(10 km per hr)":
//                caloriesPerHour = 655; // Adjust this value based on accurate data
//                break;
//
//            case "Running(8 km per hr)":
//                caloriesPerHour = 522; // Adjust this value based on accurate data
//                break;
//
//            case "Running(6 km per hr)":
//                caloriesPerHour = 353; // Adjust this value based on accurate data
//                break;
//            // Add more cases for other activity types as needed
//
//            default:
//                caloriesPerHour = 0; // Default value if activity type not recognized
//        }
//
//        // Calculate calories burned based on duration
////        return caloriesPerHour * (duration / 60); // Convert duration to hours
//        return (user.getUserProfile().getBmr() * caloriesPerHour * (duration/60))/24;
//    }


    public List<Exercise> getExercisesForUserAndCustomRange(User user, LocalDate startDate, LocalDate endDate) {
        // Implement the logic to fetch exercises for the user and date range from the repository
        return exerciseRepository.findByUserAndDateBetween(user, startDate, endDate);
    }
    // You can add more service methods as needed

    public Exercise findByIdd(Long id) {
        return exerciseRepository.findById(id).orElse(null);
    }

    public Exercise saveExercise(Exercise exercise) {
        return exerciseRepository.save(exercise);
    }

    @Autowired
    private UserRepository userRepository;

//    public double getTotalCaloriesBurned(User user) {
//        UserProfile userProfile = userProfileRepository.findByUserEmail(user.getEmail());
////                .orElseThrow(() -> new RuntimeException("User profile not found"));
//
//        LocalTime wakeupTime = userProfile.getWakeupTime();
//        LocalDate currentDate = LocalDate.now();
//
//        LocalDate startDate = currentDate.minusDays(1);
//        LocalDate endDate = currentDate;
//
//        List<Exercise> exercises = exerciseRepository.findByUserAndDateBetween(user, startDate, endDate);
//
//        return exercises.stream()
//                .filter(exercise -> {
//                    LocalTime exerciseStartTime = exercise.getStartTime().toLocalTime();
//                    LocalTime exerciseEndTime = exercise.getEndTime().toLocalTime();
//                    return (exerciseStartTime.isAfter(wakeupTime) || exerciseStartTime.equals(wakeupTime)) ||
//                            (exerciseEndTime.isBefore(wakeupTime) || exerciseEndTime.equals(wakeupTime));
//                })
//                .mapToDouble(Exercise::getCaloriesBurned)
//                .sum();
//    }

//    public Double calculateCaloriesBurnedFor24HourCycle(Long userId, LocalDate date) {
//        UserProfile userProfile = userProfileRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//
//        LocalTime wakeupTime = userProfile.getWakeupTime();
//        LocalDateTime startDateTime = LocalDateTime.of(date, wakeupTime);
//        LocalDateTime endDateTime = startDateTime.plusHours(24);
//
//        return exerciseRepository.sumCaloriesBurnedWithin24HourCycle(userId, startDateTime, endDateTime);
//    }

//    public Double calculateCaloriesBurnedFor24HourCycle(User user, LocalDate currentDate) {
//        UserProfile userProfile = userProfileRepository.findByUserEmail(user.getEmail());
////                .orElseThrow(() -> new RuntimeException("User not found"));
//
//
//        LocalTime wakeupTime = userProfile.getWakeupTime();
//        LocalDateTime startDate = LocalDateTime.of(currentDate.minusDays(1), wakeupTime); // Start from previous day's wakeup time
//        LocalDateTime endDate = LocalDateTime.of(currentDate, wakeupTime); // End at current day's wakeup time
//
//        return exerciseRepository.sumCaloriesBurnedWithin24HourCycle(user.getUserId(), startDate, endDate);
//    }
//public Double calculateCaloriesBurnedFor24HourCycle(User user, LocalDate currentDate) {
//    UserProfile userProfile = user.getUserProfile();
//
//    LocalTime wakeupTime = userProfile.getWakeupTime();
//    System.out.println("wakeup time --------"+wakeupTime);
//    LocalDateTime startDate = LocalDateTime.of(currentDate.minusDays(1), wakeupTime);
////    LocalDateTime startDate = LocalDateTime.of(currentDate.minusDays(1), wakeupTime);
//
//    System.out.println("start date time ======="+startDate);
////    LocalDateTime endDate = LocalDateTime.of(currentDate, wakeupTime);
//    LocalDateTime endDate = LocalDateTime.of(currentDate, wakeupTime); // Adjust end date to be exclusive
//
//    System.out.println("end date time ======="+endDate);
////    return exerciseRepository.sumCaloriesBurnedWithin24HourCycle(user.getUserId(), startDate.toLocalTime(), endDate.toLocalTime());
//    Double caloriesBurned = exerciseRepository.sumCaloriesBurnedWithin24HourCycle(user.getUserId(), startDate.toLocalTime(), endDate.toLocalTime());
//
//    System.out.println("Calories burned within 24-hour cycle: " + caloriesBurned);
//
//    return caloriesBurned;
//}
public Double calculateCaloriesBurnedFor24HourCycle(User user, LocalDate currentDate) {
    UserProfile userProfile = user.getUserProfile();

    LocalTime wakeupTime = userProfile.getWakeupTime();
    System.out.println("wakeup time --------"+wakeupTime);
    LocalDateTime startDate = LocalDateTime.of(currentDate.minusDays(1), wakeupTime);
    System.out.println("start date time ======="+startDate);
    LocalDateTime endDate = LocalDateTime.of(currentDate, wakeupTime); // Adjust end date to be exclusive
    System.out.println("end date time ======="+endDate);
    // Fetch all exercises within the 24-hour cycle
    List<Exercise> exercises = exerciseRepository.findByUserUserIdAndStartTimeBetween(user.getUserId(), startDate.toLocalTime(), endDate.toLocalTime());
    System.out.println("dsd"+startDate.toLocalTime());
    System.out.println("date between --------------- "+exercises);
    // Calculate total calories burned
    double totalCaloriesBurned = exercises.stream()
            .mapToDouble(Exercise::getCaloriesBurned)
            .sum();

    System.out.println("Calories burned within 24-hour cycle: " + totalCaloriesBurned);

    return totalCaloriesBurned;
}
//    public double getTotalCaloriesBurned(UserProfile userProfile, List<Exercise> exercises) {
//        LocalTime wakeupTime = userProfile.getWakeupTime();
//        LocalDate currentDate = LocalDate.now();
//        LocalDate yesterdayDate = currentDate.minusDays(1);
//        double totalCaloriesBurned = 0;
//
//        // Log values for debugging
//        System.out.println("Wakeup Time: " + wakeupTime);
//        System.out.println("Current Date: " + currentDate);
//        System.out.println("Yesterday Date: " + yesterdayDate);
//
//        for (Exercise exercise : exercises) {
//            System.out.println("Exercise Date: " + exercise.getDate());
//            System.out.println("Exercise Start Time: " + exercise.getStartTime());
//            System.out.println("Exercise Calories Burned: " + exercise.getCaloriesBurned());
//
//
//            if (exercise.getDate().isEqual(currentDate) || exercise.getDate().isEqual(yesterdayDate)) {
//                if (exercise.getStartTime().isBefore(wakeupTime)) {
//                    if (exercise.getCaloriesBurned() != null) {
//                        totalCaloriesBurned += exercise.getCaloriesBurned();
//                        System.out.println("Added Calories: " + exercise.getCaloriesBurned());
//                    }
//                }
//            }
//        }
//
//        System.out.println("Total Calories Burned: " + totalCaloriesBurned);
//        return totalCaloriesBurned;
//    }
//public double getTotalCaloriesBurned(UserProfile userProfile, List<Exercise> exercises) {
//    LocalTime wakeupTime = userProfile.getWakeupTime();
//    LocalDate currentDate = LocalDate.now();
//    LocalDate yesterdayDate = currentDate.minusDays(1);
//    double totalCaloriesBurned = 0;
//
//    // Log values for debugging
//    System.out.println("Wakeup Time: " + wakeupTime);
//    System.out.println("Current Date: " + currentDate);
//    System.out.println("Yesterday Date: " + yesterdayDate);
//
//    for (Exercise exercise : exercises) {
//        System.out.println("Exercise Date: " + exercise.getDate());
//        System.out.println("Exercise Start Time: " + exercise.getStartTime());
//        System.out.println("Exercise Calories Burned: " + exercise.getCaloriesBurned());
//
//        if ((exercise.getDate().isEqual(currentDate) || exercise.getDate().isEqual(yesterdayDate)) &&
//                exercise.getStartTime().isBefore(wakeupTime)) {
//            if (exercise.getCaloriesBurned() != null) {
//                totalCaloriesBurned += exercise.getCaloriesBurned();
//                System.out.println("Added Calories: " + exercise.getCaloriesBurned());
//            }
//        }
//    }
//
//    System.out.println("Total Calories Burned: " + totalCaloriesBurned);
//    return totalCaloriesBurned;
//}
//public double getTotalCaloriesBurned(UserProfile userProfile, List<Exercise> exercises, LocalDate localDate) {
//    LocalTime wakeupTime = userProfile.getWakeupTime();
////    LocalDate currentDate = LocalDate.now();
//    LocalDate currentDate = localDate;
//    LocalDate yesterdayDate = currentDate.minusDays(1);
//    double totalCaloriesBurned = 0;
//
//    // Log values for debugging
//    System.out.println("Wakeup Time: " + wakeupTime);
//    System.out.println("Current Date: " + currentDate);
//    System.out.println("Yesterday Date: " + yesterdayDate);
//
//    for (Exercise exercise : exercises) {
//        System.out.println("Exercise Date: " + exercise.getDate());
//        System.out.println("Exercise Start Time: " + exercise.getStartTime());
//        System.out.println("Exercise Calories Burned: " + exercise.getCaloriesBurned());
//
//        // Check if exercise date is yesterdayDate or currentDate and if startTime is between wakeupTimes
//        if ((exercise.getDate().isEqual(yesterdayDate) && exercise.getStartTime().isAfter(wakeupTime)) ||
//                (exercise.getDate().isEqual(currentDate) && exercise.getStartTime().isBefore(wakeupTime))) {
//
//            if (exercise.getCaloriesBurned() != null) {
//                totalCaloriesBurned += exercise.getCaloriesBurned();
//                System.out.println("Added Calories: " + exercise.getCaloriesBurned());
//            }
//        }
//    }
//
//    System.out.println("Total Calories Burned: " + totalCaloriesBurned);
//    return totalCaloriesBurned;
//}


//    public Map<String, Double> getTotalCaloriesBurnedAndDuration(UserProfile userProfile, List<Exercise> exercises, LocalDate localDate) {
//        LocalTime wakeupTime = userProfile.getWakeupTime();
//        LocalDate currentDate = localDate;
//        LocalDate yesterdayDate = currentDate.minusDays(1);
//        double totalCaloriesBurned = 0;
//        double totalDuration = 0;
//        Map<String, Double> activityTypeCaloriesAndDuration = new HashMap<>();
//
//        // Log values for debugging
//        System.out.println("Wakeup Time: " + wakeupTime);
//        System.out.println("Current Date: " + currentDate);
//        System.out.println("Yesterday Date: " + yesterdayDate);
//
//
//        for (Exercise exercise : exercises) {
//            if ((exercise.getDate().isEqual(yesterdayDate) && exercise.getStartTime().isAfter(wakeupTime)) ||
//                    (exercise.getDate().isEqual(currentDate) && exercise.getStartTime().isBefore(wakeupTime))) {
//
//                if (exercise.getCaloriesBurned() != null) {
//                    totalCaloriesBurned += exercise.getCaloriesBurned();
//                    totalDuration += exercise.getDuration();
//
////                    // Accumulate calories and duration by activity type
//                    String activityType = exercise.getActivityType();
//                    if (activityTypeCaloriesAndDuration.containsKey(activityType)) {
//                        double currentCalories = activityTypeCaloriesAndDuration.get(activityType);
//                        activityTypeCaloriesAndDuration.put(activityType, currentCalories + exercise.getCaloriesBurned());
//                    } else {
//                        activityTypeCaloriesAndDuration.put(activityType, exercise.getCaloriesBurned());
//                    }
//                }
//            }
//        }
//
//        System.out.println("Total Calories Burned: " + totalCaloriesBurned);
//        System.out.println("Total Duration: " + totalDuration);
//
//        // Return map containing activity type, calories burned, and duration
//        return activityTypeCaloriesAndDuration;
//    }


//    public Map<String, Object> getTotalCaloriesBurnedAndDurationRange(UserProfile userProfile, List<Exercise> exercises, LocalDate startDate, LocalDate endDate) {
//        LocalTime wakeupTime = userProfile.getWakeupTime();
//        double totalCaloriesBurned = 0;
//        double totalDuration = 0;
//        Double totalEnergyExpenditure = 0.0;
//        Map<String, Object> result = new HashMap<>();
//
//        // Log values for debugging
//        System.out.println("Wakeup Time: " + wakeupTime);
//        System.out.println("Start Date: " + startDate);
//        System.out.println("End Date: " + endDate);
//
//        for (Exercise exercise : exercises) {
//            System.out.println("Exercise Date: " + exercise.getDate());
//            System.out.println("Exercise Start Time: " + exercise.getStartTime());
//            System.out.println("Exercise Calories Burned: " + exercise.getCaloriesBurned());
//
//            // Check if exercise date is within the date range and if startTime is between wakeupTimes
//            if ((exercise.getDate().isEqual(startDate) || exercise.getDate().isAfter(startDate)) &&
//                    (exercise.getDate().isEqual(endDate) || exercise.getDate().isBefore(endDate.plusDays(1))) &&
//                    (exercise.getStartTime().isAfter(wakeupTime) || exercise.getStartTime().isBefore(wakeupTime))) {
//
//                if (exercise.getCaloriesBurned() != null) {
//                    totalCaloriesBurned += exercise.getCaloriesBurned();
//                    totalDuration += exercise.getDuration();
//                }
//            }
//        }
//
//        // Calculate Total Energy Expenditure
//        double bmr = userProfile.getBmr();
//        totalEnergyExpenditure = (bmr * (24 - totalDuration) / 24) + totalCaloriesBurned;
//
//        // Add total calories burned, total duration, and total energy expenditure to the result map
//        result.put("totalCaloriesBurned", totalCaloriesBurned);
//        result.put("totalDuration", totalDuration);
//        result.put("totalCaloriesExpenditure", totalEnergyExpenditure);
//
//        // Log total values
//        System.out.println("Total Calories Burned: " + totalCaloriesBurned);
//        System.out.println("Total Duration: " + totalDuration);
//        System.out.println("Total Energy Expenditure: " + totalEnergyExpenditure);
//
//        return result;
//    }

//    public Map<String, Object> getTotalCaloriesBurnedAndDuration(UserProfile userProfile, List<Exercise> exercises, LocalDate localDate) {
//    LocalTime wakeupTime = userProfile.getWakeupTime();
//    LocalDate currentDate = localDate;
//    LocalDate yesterdayDate = currentDate.minusDays(1);
//    double totalCaloriesBurned = 0;
//    double totalDuration = 0;
//    Double totalEnergyExpenditure = 0.0;
//    Map<String, Object> result = new HashMap<>();
//
//    // Log values for debugging
//    System.out.println("Wakeup Time: " + wakeupTime);
//    System.out.println("Current Date: " + currentDate);
//    System.out.println("Yesterday Date: " + yesterdayDate);
//
//    for (Exercise exercise : exercises) {
//        System.out.println("Exercise Date: " + exercise.getDate());
//        System.out.println("Exercise Start Time: " + exercise.getStartTime());
//        System.out.println("Exercise Calories Burned: " + exercise.getCaloriesBurned());
//
//        // Check if exercise date is yesterdayDate or currentDate and if startTime is between wakeupTimes
//        if ((exercise.getDate().isEqual(yesterdayDate) && exercise.getStartTime().isAfter(wakeupTime)) ||
//                (exercise.getDate().isEqual(currentDate) && exercise.getStartTime().isBefore(wakeupTime))) {
//
//            if (exercise.getCaloriesBurned() != null) {
//                totalCaloriesBurned += exercise.getCaloriesBurned();
//                totalDuration += exercise.getDuration();
//            }
//        }
//    }
//
//    // Calculate Total Energy Expenditure
//    double bmr = userProfile.getBmr();
//    totalEnergyExpenditure = (bmr * (24 - totalDuration) / 24) + totalCaloriesBurned;
//
////    totalEnergyExpenditure=[userProfile.getBmr()*{24-totalDuration}/24]+totalCaloriesBurned;
//
//
//    // Add total calories burned and total duration to the result map
//    result.put("totalCaloriesBurned", totalCaloriesBurned);
//    result.put("totalDuration", totalDuration);
//    result.put("totalCaloriesExpenditure",totalEnergyExpenditure);
//
//    // Log total values
//    System.out.println("Total Calories Burned: " + totalCaloriesBurned);
//    System.out.println("Total Duration: " + totalDuration);
//    System.out.println("Total Energy Expenditure: " + totalEnergyExpenditure);
//
//    return result;
//}

    public Map<String, Object> getTotalCaloriesBurnedAndDuration(UserProfile userProfile, List<Exercise> exercises, LocalDate localDate) {
        LocalTime wakeupTime = userProfile.getWakeupTime();
        LocalDate currentDate = localDate;
        LocalDate yesterdayDate = currentDate.minusDays(1);
        double totalCaloriesBurned = 0;
        double totalDuration = 0;
        Double totalEnergyExpenditure = 0.0;
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> exerciseDetails = new ArrayList<>();

        // Log values for debugging
        System.out.println("Wakeup Time: " + wakeupTime);
        System.out.println("Current Date: " + currentDate);
        System.out.println("Yesterday Date: " + yesterdayDate);

        for (Exercise exercise : exercises) {
            System.out.println("Exercise Date: " + exercise.getDate());
            System.out.println("Exercise Start Time: " + exercise.getStartTime());
            System.out.println("Exercise Calories Burned: " + exercise.getCaloriesBurned());

            // Check if exercise date is yesterdayDate or currentDate and if startTime is between wakeupTimes
            if ((exercise.getDate().isEqual(yesterdayDate) && exercise.getStartTime().isAfter(wakeupTime)) ||
                    (exercise.getDate().isEqual(currentDate) && exercise.getStartTime().isBefore(wakeupTime))) {

                if (exercise.getCaloriesBurned() != null) {
                    totalCaloriesBurned += exercise.getCaloriesBurned();
                    totalDuration += exercise.getDuration();

                    // Add exercise details to the list
                    Map<String, Object> exerciseData = new HashMap<>();
                    exerciseData.put("activityType", exercise.getActivityType());
                    exerciseData.put("exerciseDuration", exercise.getDuration());
                    exerciseData.put("caloriesBurned", exercise.getCaloriesBurned());
                    exerciseDetails.add(exerciseData);
                }
            }
        }

        // Calculate Total Energy Expenditure
        double bmr = userProfile.getBmr();
        totalEnergyExpenditure = (bmr * (24 - totalDuration/60) / 24) + totalCaloriesBurned;

        // Add total calories burned and total duration to the result map
        result.put("totalCaloriesBurned", totalCaloriesBurned);
        result.put("totalDuration", totalDuration);
        result.put("totalCaloriesExpenditure", totalEnergyExpenditure);
        result.put("exerciseDetails", exerciseDetails);

        // Log total values
        System.out.println("Total Calories Burned: " + totalCaloriesBurned);
        System.out.println("Total Duration: " + totalDuration);
        System.out.println("Total Energy Expenditure: " + totalEnergyExpenditure);

        return result;
    }
//public Map<String, Object> getTotalCaloriesBurnedAndDuration(UserProfile userProfile, List<Exercise> exercises, LocalDate localDate) {
//    LocalTime wakeupTime = userProfile.getWakeupTime();
//    LocalDate currentDate = localDate;
//    LocalDate yesterdayDate = currentDate.minusDays(1);
//    double totalCaloriesBurned = 0;
//    double totalDuration = 0;
//    double totalEnergyExpenditure = 0.0;
//    Map<String, Object> result = new HashMap<>();
//    List<Map<String, Object>> exerciseYesterdayDetails = new ArrayList<>();
//    List<Map<String, Object>> exerciseTodayDetails = new ArrayList<>();
//
//    // Log values for debugging
//    System.out.println("Wakeup Time: " + wakeupTime);
//    System.out.println("Current Date: " + currentDate);
//    System.out.println("Yesterday Date: " + yesterdayDate);
//
//    for (Exercise exercise : exercises) {
//        System.out.println("Exercise Date: " + exercise.getDate());
//        System.out.println("Exercise Start Time: " + exercise.getStartTime());
//        System.out.println("Exercise Calories Burned: " + exercise.getCaloriesBurned());
//
//        if (exercise.getCaloriesBurned() != null) {
//            if (exercise.getDate().isEqual(yesterdayDate) && exercise.getStartTime().isAfter(wakeupTime)) {
//                // Exercise belongs to yesterdayDetails
//                Map<String, Object> exerciseData = new HashMap<>();
//                exerciseData.put("activityType", exercise.getActivityType());
//                exerciseData.put("exerciseDuration", exercise.getDuration());
//                exerciseData.put("caloriesBurned", exercise.getCaloriesBurned());
//                exerciseYesterdayDetails.add(exerciseData);
//
//                totalCaloriesBurned += exercise.getCaloriesBurned();
//                totalDuration += exercise.getDuration();
//            } else if (exercise.getDate().isEqual(currentDate) && exercise.getStartTime().isBefore(wakeupTime)) {
//                // Exercise belongs to yesterdayDetails
//                Map<String, Object> exerciseData = new HashMap<>();
//                exerciseData.put("activityType", exercise.getActivityType());
//                exerciseData.put("exerciseDuration", exercise.getDuration());
//                exerciseData.put("caloriesBurned", exercise.getCaloriesBurned());
//                exerciseYesterdayDetails.add(exerciseData);
//
//                totalCaloriesBurned += exercise.getCaloriesBurned();
//                totalDuration += exercise.getDuration();
//            } else if (exercise.getDate().isEqual(currentDate) && exercise.getStartTime().isAfter(wakeupTime)) {
//                // Exercise belongs to todayDetails
//                Map<String, Object> exerciseData = new HashMap<>();
//                exerciseData.put("activityType", exercise.getActivityType());
//                exerciseData.put("exerciseDuration", exercise.getDuration());
//                exerciseData.put("caloriesBurned", exercise.getCaloriesBurned());
//                exerciseTodayDetails.add(exerciseData);
//
//                totalCaloriesBurned += exercise.getCaloriesBurned();
//                totalDuration += exercise.getDuration();
//            }
//        }
//    }
//
//    // Calculate Total Energy Expenditure
//    double bmr = userProfile.getBmr();
//    totalEnergyExpenditure = (bmr * (24 - totalDuration) / 24) + totalCaloriesBurned;
//
//    // Add total calories burned and total duration to the result map
//    result.put("totalCaloriesBurned", totalCaloriesBurned);
//    result.put("totalDuration", totalDuration);
//    result.put("totalCaloriesExpenditure", totalEnergyExpenditure);
//    result.put("exerciseYesterdayDetails", exerciseYesterdayDetails);
//    result.put("exerciseTodayDetails", exerciseTodayDetails);
//
//    // Log total values
//    System.out.println("Total Calories Burned: " + totalCaloriesBurned);
//    System.out.println("Total Duration: " + totalDuration);
//    System.out.println("Total Energy Expenditure: " + totalEnergyExpenditure);
//
//    return result;
//}





}
