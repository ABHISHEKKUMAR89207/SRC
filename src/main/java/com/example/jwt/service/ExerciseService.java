package com.example.jwt.service;

import com.example.jwt.entities.Exercise;
import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.Activities;
import com.example.jwt.repository.ExerciseRepository;
import com.example.jwt.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final UserProfileRepository userProfileRepository;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository, UserProfileRepository userProfileRepository) {
        this.exerciseRepository = exerciseRepository;
        this.userProfileRepository = userProfileRepository;
    }

    public Exercise calculateAndSaveExercise(Exercise exercise, User user, double duration) {
        String activityType = exercise.getActivityType();
        double caloriesBurned = calculateCaloriesBurned(activityType, duration,user);

        exercise.setCaloriesBurned(caloriesBurned);
        exercise.setUser(user);

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



    private double calculateCaloriesBurned(String activityType, double duration, User user) {
        // Simple assumption: Calories burned per hour for different activities
        double caloriesPerHour;

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
                caloriesPerHour = 5.1;
                break;

            case "CULTIVATION":
                caloriesPerHour = 5.7;
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
                caloriesPerHour = 9;
                break;

            case "PLAYING ANY OUTDOOR GAME":
                caloriesPerHour = 6;
                break;

            case "LIFTING OF LOADS":
                caloriesPerHour = 5.1;
                break;

            case "RICKSHAW PULLING":
                caloriesPerHour = 5.3;
                break;

            case "ANY HEAVY DUTY ACTIVITY":
                caloriesPerHour = 7;
                break;

            case "MOBILE USAGE":
                caloriesPerHour = 1.5;
                break;

            default:
                caloriesPerHour = 0; // Default value if activity type not recognized
        }

        // Calculate calories burned based on duration
        return (user.getUserProfile().getBmr() * caloriesPerHour * (duration / 60)) / 24;
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
}
