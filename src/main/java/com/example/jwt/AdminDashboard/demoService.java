package com.example.jwt.AdminDashboard;

import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.Activities;
import com.example.jwt.entities.dashboardEntity.healthTrends.HeartRate;
import com.example.jwt.entities.water.WaterEntity;
import com.example.jwt.entities.water.WaterEntry;
import com.example.jwt.entities.water.WaterEntryRepository;
import com.example.jwt.repository.ActivityRepository;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.WaterEntityRepository;
import com.example.jwt.repository.repositoryHealth.HeartRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
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
//    public Double calculateAverageWaterIntake(Long userId) {
//        User user = userRepository.findById(userId).orElse(null);
//        if (user == null) {
//            throw new IllegalArgumentException("User not found with ID: " + userId);
//        }
//
//        // Get user's age group
//        String ageGroup = calculateAgeGroup(user);
//
//        // Get user's gender from profile
//        String gender = user.getUserProfile().getGender();
//
//        // Calculate average water intake based on age group and gender
//        Double averageWaterIntake = calculateAverageWaterIntakeByAgeAndGender(ageGroup, gender);
//        return averageWaterIntake;
//    }
//

//    private Double calculateAverageWaterIntakeByAgeAndGender(String ageGroup, String gender) {
//        // Define average water intake values (in milliliters) for different age groups and genders
//        // You can adjust these values based on your requirements
//        // For simplicity, I'm providing example values here
//        Map<String, Map<String, Double>> averageWaterIntakeMap = new HashMap<>();
//        Map<String, Double> maleIntakeMap = new HashMap<>();
//        Map<String, Double> femaleIntakeMap = new HashMap<>();
//
//        // Average water intake values for male (in milliliters) by age group
//        maleIntakeMap.put("<15", 1500.0);
//        maleIntakeMap.put("15-29", 2000.0);
//        maleIntakeMap.put("30-44", 1800.0);
//        maleIntakeMap.put("45-59", 1700.0);
//        maleIntakeMap.put(">60", 1600.0);
//
//        // Average water intake values for female (in milliliters) by age group
//        femaleIntakeMap.put("<15", 1500.0);
//        femaleIntakeMap.put("15-29", 1900.0);
//        femaleIntakeMap.put("30-44", 1700.0);
//        femaleIntakeMap.put("45-59", 1600.0);
//        femaleIntakeMap.put(">60", 1500.0);
//
//        // Populate the map with gender-specific intake maps
//        averageWaterIntakeMap.put("Male", maleIntakeMap);
//        averageWaterIntakeMap.put("Female", femaleIntakeMap);
//
//        // Get the average water intake based on age group and gender
//        return averageWaterIntakeMap.get(gender).getOrDefault(ageGroup, 0.0);
//    }
@Autowired
private WaterEntityRepository waterEntityRepository;

    @Autowired
    private WaterEntryRepository waterEntryRepository;
//    private Double calculateAverageWaterIntakeByAgeAndGender(String ageGroup, String gender) {
//        Double totalWaterIntake = 0.0;
//        int userCount = 0;
//
//        // Iterate through all water entries to calculate total water intake for the specified age group and gender
//        List<WaterEntry> waterEntries = waterEntryRepository.findAll(); // Assuming you have a repository for WaterEntry
//        for (WaterEntry entry : waterEntries) {
//            WaterEntity waterEntity = entry.getWaterEntity();
//            User user = waterEntity.getUser();
//
//            // Calculate age group for the user
//            String userAgeGroup = calculateAgeGroup(user);
//
//            // Check if the user's age group and gender match the specified age group and gender
//            if (userAgeGroup.equals(ageGroup) && user.getUserProfile().getGender().equals(gender)) {
//                totalWaterIntake += entry.getWaterIntake();
//                userCount++;
//            }
//        }
//
//        // Calculate average water intake
//        if (userCount > 0) {
//            return totalWaterIntake / userCount;
//        } else {
//            return 0.0; // Handle the case when no users match the specified age group and gender
//        }
//    }
public Map<String, Map<String, Double>> calculateAverageWaterIntake() {
    // Define average water intake values (in milliliters) for different age groups and genders
    // These values can be retrieved from a database or configuration file
    Map<String, Map<String, Double>> averageWaterIntakeResult = new HashMap<>();
    Map<String, Double> maleHeartMap = new HashMap<>();
    Map<String, Double> femaleIntakeMap = new HashMap<>();

    // Initialize average water intake values for male and female
    initializeAverageValueMap(maleHeartMap);
    initializeAverageValueMap(femaleIntakeMap);

    // Populate the map with gender-specific intake maps
    averageWaterIntakeResult.put("male", maleHeartMap);
    averageWaterIntakeResult.put("female", femaleIntakeMap);

    // Iterate through all users to calculate total water intake for each age group and gender
    List<User> users = userRepository.findAll(); // Assuming you have a repository for User
    for (User user : users) {
        String ageGroup = calculateAgeGroup(user);
        String gender = user.getUserProfile().getGender().toLowerCase();
        Double waterIntake = calculateWaterIntakeForUser(user);

        // Update the total water intake for the specified age group and gender
        Map<String, Double> genderMap = averageWaterIntakeResult.get(gender);
        genderMap.put(ageGroup, genderMap.getOrDefault(ageGroup, 0.0) + waterIntake);
    }

    return averageWaterIntakeResult;
}

    private void initializeAverageValueMap(Map<String, Double> intakeMap) {
        // Initialize average water intake values for different age groups
        intakeMap.put("<15", 0.0);
        intakeMap.put("15-29", 0.0);
        intakeMap.put("30-44", 0.0);
        intakeMap.put("45-59", 0.0);
        intakeMap.put(">60", 0.0);
    }


    private Double calculateWaterIntakeForUser(User user) {
        List<WaterEntity> waterEntities = user.getWaterEntities();
        Double totalWaterIntake = 0.0;

        // Iterate through all water entities for the user
        for (WaterEntity waterEntity : waterEntities) {
            List<WaterEntry> waterEntries = waterEntity.getWaterEntries();

            // Iterate through all water entries for the current water entity
            for (WaterEntry waterEntry : waterEntries) {
                totalWaterIntake += waterEntry.getWaterIntake();
            }
        }

        return totalWaterIntake;
    }



@Autowired
private HeartRateRepository heartRateRepository;

    public Map<String, Map<String, Double>> calculateAverageHeartRate() {
        // Define average water intake values (in milliliters) for different age groups and genders
        // These values can be retrieved from a database or configuration file
        Map<String, Map<String, Double>> averagezHeartRateResult = new HashMap<>();
        Map<String, Double> maleIntakeMap = new HashMap<>();
        Map<String, Double> femaleIntakeMap = new HashMap<>();

        // Initialize average water intake values for male and female
        initializeAverageValueMap(maleIntakeMap);
        initializeAverageValueMap(femaleIntakeMap);

        // Populate the map with gender-specific intake maps
        averagezHeartRateResult.put("male", maleIntakeMap);
        averagezHeartRateResult.put("female", femaleIntakeMap);

        // Iterate through all users to calculate total water intake for each age group and gender
        List<User> users = userRepository.findAll(); // Assuming you have a repository for User
        for (User user : users) {
            String ageGroup = calculateAgeGroup(user);
            String gender = user.getUserProfile().getGender().toLowerCase();
            Double waterIntake = calculateHeartRateForUser(user);

            // Update the total water intake for the specified age group and gender
            Map<String, Double> genderMap = averagezHeartRateResult.get(gender);
            genderMap.put(ageGroup, genderMap.getOrDefault(ageGroup, 0.0) + waterIntake);
        }

        return averagezHeartRateResult;
    }

    //    private Double calculateHeartRateForUser(User user) {
//        List<com.example.jwt.entities.dashboardEntity.healthTrends.HeartRate> waterEntities = user.getHeartRates();
//        Double totalWaterIntake = 0.0;
//
//        // Iterate through all water entities for the user
//        for (com.example.jwt.entities.dashboardEntity.healthTrends.HeartRate heartRate : waterEntities) {
//            List<HeartRate> values = heartRate.getValue();
//
//            // Iterate through all water entries for the current water entity
//            for (HeartRate heartRate1 : values) {
//                totalWaterIntake += heartRate1.getWaterIntake();
//            }
//        }
//
//        return totalWaterIntake;
//    }
    private Double calculateHeartRateForUser(User user) {
        List<HeartRate> heartRates = user.getHeartRates();
        Double totalHeartRate = 0.0;

        // Iterate through all heart rate entries for the user
        for (com.example.jwt.entities.dashboardEntity.healthTrends.HeartRate heartRate : heartRates) {
            totalHeartRate += heartRate.getValue();
        }

        return totalHeartRate;
    }

    private void initializeAverageHeartRateMap(Map<String, Double> heartRateMap) {
        // Initialize average heart rate values for different age groups
        heartRateMap.put("<15", 0.0);
        heartRateMap.put("15-29", 0.0);
        heartRateMap.put("30-44", 0.0);
        heartRateMap.put("45-59", 0.0);
        heartRateMap.put(">60", 0.0);
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
