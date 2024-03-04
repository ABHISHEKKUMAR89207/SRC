package com.example.jwt.AdminDashboard;

import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.Activities;
import com.example.jwt.repository.ActivityRepository;
import com.example.jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class demoService {

    @Autowired
    private UserRepository userRepository;

    private final ActivityRepository activityRepository;

    public demoService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }


//    public List<SleepDurationStatsDTO> getAverageSleepDurationByAgeAndGender() {
//        List<SleepDurationStatsDTO> statsList = new ArrayList<>();
//
//        // Fetch all users with sleep duration information
//        List<User> users = userRepository.findAll();
//
//        // Group users by age and gender
//        Map<String, Map<String, List<User>>> groupedUsers = users.stream()
//                .collect(Collectors.groupingBy(this::calculateAgeGroup,
//                        Collectors.groupingBy(user -> user.getUserProfile().getGender())));
//
//        // Calculate average sleep duration for each group
//        groupedUsers.forEach((ageGroup, genderMap) ->
//                genderMap.forEach((gender, userList) -> {
//                    double averageDuration = calculateAverageDuration(userList);
//                    SleepDurationStatsDTO statsDTO = new SleepDurationStatsDTO();
//                    statsDTO.setAgeGroup(ageGroup);
//                    statsDTO.setGender(gender);
//                    statsDTO.setAverageDuration(averageDuration);
//                    statsList.add(statsDTO);
//                }));
//
//        return statsList;
//    }

    public List<SleepDurationStatsDTO> getAverageSleepDurationByAgeAndGender() {
        List<SleepDurationStatsDTO> statsList = new ArrayList<>();

        // Fetch all users with sleep duration information
        List<User> users = userRepository.findAll();

        // Group users by age and gender
        Map<String, Map<String, List<User>>> groupedUsers = users.stream()
                .collect(Collectors.groupingBy(this::calculateAgeGroup,
                        Collectors.groupingBy(user -> user.getUserProfile().getGender())));

        // Calculate average sleep duration and user count for each group
        groupedUsers.forEach((ageGroup, genderMap) ->
                genderMap.forEach((gender, userList) -> {
                    double averageDuration = calculateAverageDuration(userList);
                    long userCount = userList.size();  // Count of users in the group
                    SleepDurationStatsDTO statsDTO = new SleepDurationStatsDTO();
                    statsDTO.setAgeGroup(ageGroup);
                    statsDTO.setGender(gender);
                    statsDTO.setAverageDuration(averageDuration);
                    statsDTO.setUserCount(userCount);
                    statsList.add(statsDTO);
                }));

        return statsList;
    }

    private String calculateAgeGroup(User user) {
        LocalDate currentDate = LocalDate.now();
        LocalDate birthDate = user.getUserProfile().getDateOfBirth();
        int age = Period.between(birthDate, currentDate).getYears();

        // Implement logic to categorize age into groups
        if (age < 15) {
            return "<15";
        } else if (age >= 15 && age <= 29) {
            return "15-29";
        } else if (age >= 30 && age <= 44) {
            return "30-44";
        } else if (age >= 45 && age <= 59) {
            return "45-59";
        } else {
            return ">60";
        }
    }


    private double calculateAverageDuration(List<User> userList) {
        // Assuming sleep duration is stored in the SleepDuration entity
        List<Double> sleepDurations = userList.stream()
                .flatMap(user -> user.getSleepDurations().stream()
                        .map(sleepDuration -> Double.valueOf(sleepDuration.getDuration())))
                .collect(Collectors.toList());

        // Calculate average sleep duration
        double averageDuration = sleepDurations.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        return averageDuration;
    }



//    by calories
public List<ActivitiesStatsDTO> getAverageCaloriesByAgeAndGender() {
    List<ActivitiesStatsDTO> statsList = new ArrayList<>();

    // Fetch all users with activity information
    List<User> users = userRepository.findAll();

    // Group users by age and gender
    Map<String, Map<String, List<User>>> groupedUsers = users.stream()
            .collect(Collectors.groupingBy(this::calculateAgeGroup,
                    Collectors.groupingBy(user -> user.getUserProfile().getGender())));

    // Calculate average calories and activity count for each group
    groupedUsers.forEach((ageGroup, genderMap) ->
            genderMap.forEach((gender, userList) -> {
                double averageCalories = calculateAverageCalories(userList);
                long userCount = userList.size();
                long activityCount = calculateActivityCount(userList);
                ActivitiesStatsDTO statsDTO = new ActivitiesStatsDTO();
                statsDTO.setAgeGroup(ageGroup);
                statsDTO.setGender(gender);
                statsDTO.setAverageCalories(averageCalories);
                statsDTO.setUserCount(userCount);
                statsDTO.setActivityCount(activityCount);
                statsList.add(statsDTO);
            }));

    return statsList;
}

    private long calculateActivityCount(List<User> userList) {
        // Assuming activities are stored in the Activities entity
        return userList.stream()
                .flatMap(user -> user.getActivities().stream())
                .count();
    }
    private double calculateAverageCalories(List<User> userList) {
        // Assuming calories are stored in the Activities entity
        List<Double> caloriesList = userList.stream()
                .flatMap(user -> user.getActivities().stream().map(Activities::getCalory))
                .collect(Collectors.toList());

        // Calculate average calories
        double averageCalories = caloriesList.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        return averageCalories;
    }

    public List<AverageStepsByAgeDTO> getAverageStepsByAge() {
        List<AverageStepsByAgeDTO> statsList = new ArrayList<>();

        // Fetch all users with activity information
        List<User> users = userRepository.findAll();

        // Group users by age
        Map<String, List<User>> groupedUsers = users.stream()
                .collect(Collectors.groupingBy(this::calculateAgeGroup));

        // Calculate average steps for each age group
        groupedUsers.forEach((ageGroup, userList) -> {
            double averageSteps = calculateAverageSteps(userList);
            AverageStepsByAgeDTO statsDTO = new AverageStepsByAgeDTO();
            statsDTO.setAgeGroup(ageGroup);
            statsDTO.setAverageSteps(averageSteps);
            statsList.add(statsDTO);
        });

        return statsList;
    }

    private double calculateAverageSteps(List<User> userList) {
        // Assuming steps are stored in the Activities entity
        List<Integer> stepsList = userList.stream()
                .flatMap(user -> user.getActivities().stream().map(Activities::getSteps))
                .collect(Collectors.toList());

        // Calculate average steps
        double averageSteps = stepsList.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        return averageSteps;
    }

    public TotalUsersDTO getTotalUsers() {
        TotalUsersDTO totalUsersDTO = new TotalUsersDTO();

        // Fetch total number of users
        long totalUsers = userRepository.count();
        totalUsersDTO.setTotalUsers(totalUsers);

        return totalUsersDTO;
    }


    public UsersByWorkLevelDTO getUsersByWorkLevel() {
        UsersByWorkLevelDTO usersByWorkLevelDTO = new UsersByWorkLevelDTO();

        // Fetch all users
        List<User> users = userRepository.findAll();

        // Group users by work level
        Map<String, Long> usersByWorkLevel = users.stream()
                .collect(Collectors.groupingBy(user -> user.getUserProfile().getWorkLevel(), Collectors.counting()));

        usersByWorkLevelDTO.setUsersByWorkLevel(usersByWorkLevel);

        return usersByWorkLevelDTO;
    }


    public StepCountDTO getTotalStepCountAndKms() {
        StepCountDTO stepCountDTO = new StepCountDTO();

        // Fetch all activities
        List<Activities> activities = activityRepository.findAll();

        // Calculate total steps and convert to kilometers
        int totalSteps = activities.stream().mapToInt(Activities::getSteps).sum();
        double totalKms = calculateKilometersFromSteps(totalSteps);

        stepCountDTO.setTotalSteps(totalSteps);
        stepCountDTO.setTotalKms(totalKms);

        return stepCountDTO;
    }

    private double calculateKilometersFromSteps(int steps) {
        // Assuming 1 step is equal to 0.0008 kilometers
        double conversionFactor = 0.0008;
        return steps * conversionFactor;
    }

}
