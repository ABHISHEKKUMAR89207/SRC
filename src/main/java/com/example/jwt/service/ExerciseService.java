package com.example.jwt.service;

import com.example.jwt.entities.Exercise;
import com.example.jwt.entities.User;
import com.example.jwt.repository.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public Exercise calculateAndSaveExercise(Exercise exercise, User user, double duration) {
        String activityType = exercise.getActivityType();
        double caloriesBurned = calculateCaloriesBurned(activityType, duration);

        exercise.setCaloriesBurned(caloriesBurned);
        exercise.setUser(user);

        return exerciseRepository.save(exercise);
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



    private double calculateCaloriesBurned(String activityType, double duration) {
        // Simple assumption: Calories burned per hour for different activities
        double caloriesPerHour;

        switch (activityType) {
            case "Gardening":
                caloriesPerHour = 300; // Adjust this value based on accurate data
                break;

            case "Cleaning or Mopping":
                caloriesPerHour = 210; // Adjust this value based on accurate data
                break;

            case "Watching TV":
                caloriesPerHour = 86; // Adjust this value based on accurate data
                break;

            case "Shuttle":
                caloriesPerHour = 348; // Adjust this value based on accurate data
                break;

            case "Table Tennis":
                caloriesPerHour = 245; // Adjust this value based on accurate data
                break;

            case "Tennis":
                caloriesPerHour = 392; // Adjust this value based on accurate data
                break;

            case "Volley Ball":
                caloriesPerHour = 180; // Adjust this value based on accurate data
                break;

            case "Dancing":
                caloriesPerHour = 372; // Adjust this value based on accurate data
                break;

            case "Fishing":
                caloriesPerHour = 222; // Adjust this value based on accurate data
                break;

            case "Shopping":
                caloriesPerHour = 204; // Adjust this value based on accurate data
                break;

            case "Typing":
                caloriesPerHour = 108; // Adjust this value based on accurate data
                break;

            case "Sleeping":
                caloriesPerHour = 57; // Adjust this value based on accurate data
                break;

            case "Standing":
                caloriesPerHour = 132; // Adjust this value based on accurate data
                break;

            case "Sitting":
                caloriesPerHour = 86; // Adjust this value based on accurate data
                break;
            // Add more cases for other activity types as needed

            default:
                caloriesPerHour = 0; // Default value if activity type not recognized
        }

        // Calculate calories burned based on duration
        return caloriesPerHour * (duration / 60); // Convert duration to hours
    }


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
