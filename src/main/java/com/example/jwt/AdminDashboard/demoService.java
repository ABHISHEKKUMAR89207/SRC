package com.example.jwt.AdminDashboard;

import com.example.jwt.entities.User;
import com.example.jwt.entities.UserProfile;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
//        // Calculate average sleep duration and user count for each group
//        groupedUsers.forEach((ageGroup, genderMap) ->
//                genderMap.forEach((gender, userList) -> {
//                    double averageDuration = calculateAverageDuration(userList);
//                    long userCount = userList.size();  // Count of users in the group
//                    SleepDurationStatsDTO statsDTO = new SleepDurationStatsDTO();
//                    statsDTO.setAgeGroup(ageGroup);
//                    statsDTO.setGender(gender);
//                    statsDTO.setAverageDuration(averageDuration);
//                    statsDTO.setUserCount(userCount);
//                    statsList.add(statsDTO);
//                }));
//
//        return statsList;
//    }
//public List<AverageDTOs> getAverageSleepDurationByAgeAndGender() {
//    List<AverageDTOs> statsList = new ArrayList<>();
//
//    // Fetch all users with sleep duration information
//    List<User> users = userRepository.findAll();
//
//    if (users == null) {
//        // Handle the case where users list is null
//        return statsList; // or throw an exception
//    }
//
//    // Group users by age and gender
//    Map<String, Map<String, List<User>>> groupedUsers = users.stream()
//            .filter(user -> user.getUserProfile() != null) // Filter out users with null profiles
//            .collect(Collectors.groupingBy(this::calculateAgeGroup,
//                    Collectors.groupingBy(user -> {
//                        String gender = user.getUserProfile().getGender();
//                        return gender != null ? gender : "Unknown"; // Handle null genders
//                    })));
//
////    // Calculate average sleep duration and user count for each group
////    groupedUsers.forEach((ageGroup, genderMap) ->
////            genderMap.forEach((gender, userList) -> {
////                double averageDuration = calculateAverageDuration(userList);
////                long userCount = userList.size();  // Count of users in the group
////                SleepDurationStatsDTO statsDTO = new SleepDurationStatsDTO();
////                statsDTO.setAgeGroup(ageGroup);
////                statsDTO.setGender(gender);
////                statsDTO.setAverageDuration(averageDuration);
////                statsDTO.setUserCount(userCount);
////                statsList.add(statsDTO);
////            }));
////    groupedUsers.forEach((ageGroup, genderMap) ->
////            genderMap.forEach((gender, userList) -> {
////                double averageDuration = calculateAverageDuration(userList);
////                long userCount = userList.size();  // Count of users in the group
////                String averageDurationWithUnit = String.format("%.2f min", averageDuration); // Format with unit
////                SleepDurationStatsDTO statsDTO = new SleepDurationStatsDTO();
////                statsDTO.setAgeGroup(ageGroup);
////                statsDTO.setGender(gender);
////                statsDTO.setAverageDuration(averageDurationWithUnit); // Set the formatted string with unit
////                statsDTO.setUserCount(userCount);
////                statsList.add(statsDTO);
////            }));
//
//
//    // Calculate average sleep duration and user count for each group
//    groupedUsers.forEach((ageGroup, genderMap) ->
//            genderMap.forEach((gender, userList) -> {
//                double averageDurationMinutes = calculateAverageDuration(userList);
//                String averageDurationStr = convertMinutesToHoursMinutes(averageDurationMinutes); // Convert to hr:min format
//                long userCount = userList.size();  // Count of users in the group
//                AverageDTOs statsDTO = new AverageDTOs();
//                statsDTO.setAgeGroup(ageGroup);
//                statsDTO.setGender(gender);
//                statsDTO.setAverage(averageDurationStr); // Use the converted string
//                statsDTO.setUserCount(userCount);
//                statsList.add(statsDTO);
//            }));
//
//    return statsList;
//}
//public List<AverageDTOs> getAverageSleepDurationByAgeAndGender() {
//    List<AverageDTOs> statsList = new ArrayList<>();
//
//    // Fetch all users with sleep duration information
//    List<User> users = userRepository.findAll();
//    if (users == null) {
//        return statsList; // or throw an exception
//    }
//
//    // Group users by age and gender
//    Map<String, Map<String, List<User>>> groupedUsers = users.stream()
//            .filter(user -> user.getUserProfile() != null) // Filter out users with null profiles
//            .collect(Collectors.groupingBy(this::calculateAgeGroup,
//                    Collectors.groupingBy(user -> {
//                        String gender = user.getUserProfile().getGender();
//                        return gender != null ? gender : "Unknown"; // Handle null genders
//                    })));
//
//    // Calculate average sleep duration, user count, and active users for each group
//    groupedUsers.forEach((ageGroup, genderMap) ->
//            genderMap.forEach((gender, userList) -> {
//                double averageDurationMinutes = calculateAverageDuration(userList);
//                String averageDurationStr = convertMinutesToHoursMinutes(averageDurationMinutes); // Convert to hr:min format
//                long userCount = userList.size();  // Count of users in the group
//                int activeUsers = countActiveUsers(userList);  // Count of active users
//                AverageDTOs statsDTO = new AverageDTOs(averageDurationStr, ageGroup, gender, userCount, activeUsers);
//                statsList.add(statsDTO);
//            }));
//
//    return statsList;
//}
public List<AverageDTOs> getAverageSleepDurationByAgeAndGender() {
    List<AverageDTOs> statsList = new ArrayList<>();

    // Fetch all users with sleep duration information
    List<User> users = userRepository.findAll();
    if (users == null) {
        return statsList; // or throw an exception
    }

    // Group users by age and gender
    Map<String, Map<String, List<User>>> groupedUsers = users.stream()
            .filter(user -> user.getUserProfile() != null) // Filter out users with null profiles
            .collect(Collectors.groupingBy(this::calculateAgeGroup,
                    Collectors.groupingBy(user -> {
                        String gender = user.getUserProfile().getGender();
                        return gender != null ? gender : "Unknown"; // Handle null genders
                    })));

    // Calculate average sleep duration, user count, and active users for each group
    groupedUsers.forEach((ageGroup, genderMap) ->
            genderMap.forEach((gender, userList) -> {
                double averageDurationMinutes = calculateAverageDuration(userList);
                String averageDurationStr = convertMinutesToHoursMinutes(averageDurationMinutes); // Convert to hr:min format
                long userCount = userList.size();  // Count of users in the group
                int activeUsers = countActiveUsers(userList);  // Count of active users
                AverageDTOs statsDTO = new AverageDTOs(averageDurationStr, ageGroup, gender, userCount, activeUsers);
                statsList.add(statsDTO);
            }));

    // Sort statsList by ageGroup using a predefined order
    statsList.sort(Comparator.comparing((AverageDTOs dto) -> ageGroupOrder(dto.getAgeGroup())));

    return statsList;
}

    private int ageGroupOrder(String ageGroup) {
        List<String> order = Arrays.asList("<15yrs", "15-29yrs", "30-44yrs", "45-59yrs", ">60yrs");
        return order.indexOf(ageGroup);
    }

//    private int countActiveUsers(List<User> userList) {
//        return (int) userList.stream()
//                .flatMap(user -> user.getSleepDurations().stream())
//                .filter(sleepDuration -> (sleepDuration.getDuration() + sleepDuration.getManualDuration()) > 0)
//                .count();
//    }
private int countActiveUsers(List<User> userList) {
    // Use a Set to keep track of users who have at least one non-zero sleep duration record
    Set<Long> uniqueActiveUserIds = userList.stream()
            .flatMap(user -> user.getSleepDurations().stream()
                    .filter(sleepDuration -> (sleepDuration.getDuration() + sleepDuration.getManualDuration()) > 0)
                    .map(sleepDuration -> user.getUserId())) // Extract user ID
            .collect(Collectors.toSet()); // Collect into a set to ensure uniqueness

    return uniqueActiveUserIds.size(); // The size of the set is the count of unique active users
}

    public List<AverageDTOs> calculateAverageWaterIntake() {
    // Maps to keep track of total water intake and user counts for each category
    Map<String, Map<String, Double>> totalWaterIntake = new HashMap<>();
    Map<String, Map<String, Integer>> userCounts = new HashMap<>();

    // Initialize these maps for male and female categories
    initializeMaps(totalWaterIntake, userCounts);

    // Fetch all users
    List<User> users = userRepository.findAll();
    if (users == null) {
        return Collections.emptyList();
    }

    // Process each user
    for (User user : users) {
        UserProfile profile = user.getUserProfile();
        if (profile == null || profile.getGender() == null) continue;

        String ageGroup = calculateAgeGroup(user);
        String gender = profile.getGender().toLowerCase();

        Double waterIntake = calculateWaterIntakeForUser(user);
        totalWaterIntake.get(gender).merge(ageGroup, waterIntake, Double::sum);
        userCounts.get(gender).merge(ageGroup, 1, Integer::sum);
    }

    return compileResults(totalWaterIntake, userCounts);
}


    private void initializeMaps(Map<String, Map<String, Double>> intakeMap, Map<String, Map<String, Integer>> countMap) {
        List<String> genders = Arrays.asList("male", "female");
        List<String> ageGroups = Arrays.asList("<15yrs", "15-29yrs", "30-44yrs", "45-59yrs", ">60yrs");

        for (String gender : genders) {
            intakeMap.put(gender, new LinkedHashMap<>());
            countMap.put(gender, new LinkedHashMap<>());
            for (String ageGroup : ageGroups) {
                intakeMap.get(gender).put(ageGroup, 0.0);
                countMap.get(gender).put(ageGroup, 0);
            }
        }
    }

//    private List<AverageDTOs> compileResults(Map<String, Map<String, Double>> totalWaterIntake, Map<String, Map<String, Integer>> userCounts) {
//        List<AverageDTOs> results = new ArrayList<>();
//        totalWaterIntake.forEach((gender, ageMap) -> {
//            ageMap.forEach((ageGroup, total) -> {
//                Integer count = userCounts.get(gender).get(ageGroup);
//                if (count > 0) {
//                    double average = total / count;
//                    results.add(new AverageDTOs(String.format("%.2fml", average), ageGroup, gender, count));
//                }
//            });
//        });
//        return results;
//    }
private List<AverageDTOs> compileResults(Map<String, Map<String, Double>> totalWaterIntake, Map<String, Map<String, Integer>> userCounts) {
    List<AverageDTOs> results = new ArrayList<>();
    totalWaterIntake.forEach((gender, ageMap) -> {
        ageMap.forEach((ageGroup, total) -> {
            Integer count = userCounts.get(gender).get(ageGroup);
            if (count > 0) {
                double average = total / count;
                results.add(new AverageDTOs(String.format("%.2f ml", average), ageGroup, gender, count));
            }
        });
    });

    // Sort results by ageGroup using a predefined order
    results.sort(Comparator.comparing((AverageDTOs dto) -> ageGroupOrder(dto.getAgeGroup())));
    return results;
}

    public String convertMinutesToHoursMinutes(double minutes) {
        int hours = (int) minutes / 60;
        int remainingMinutes = (int) minutes % 60;
        return String.format("%dh:%02dm", hours, remainingMinutes);
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
//    public Map<String, Map<String, Double>> calculateAverageWaterIntake() {
//        // Define average water intake values (in milliliters) for different age groups and genders
//        // These values can be retrieved from a database or configuration file
//        Map<String, Map<String, Double>> averageWaterIntakeResult = new HashMap<>();
//        Map<String, Double> maleHeartMap = new HashMap<>();
//        Map<String, Double> femaleIntakeMap = new HashMap<>();
//
//        // Initialize average water intake values for male and female
//        initializeAverageValueMap(maleHeartMap);
//        initializeAverageValueMap(femaleIntakeMap);
//
//        // Populate the map with gender-specific intake maps
//        averageWaterIntakeResult.put("male", maleHeartMap);
//        averageWaterIntakeResult.put("female", femaleIntakeMap);
//
//        // Iterate through all users to calculate total water intake for each age group and gender
//        List<User> users = userRepository.findAll(); // Assuming you have a repository for User
//        for (User user : users) {
//            String ageGroup = calculateAgeGroup(user);
//            String gender = user.getUserProfile().getGender().toLowerCase();
//            Double waterIntake = calculateWaterIntakeForUser(user);
//
//            // Update the total water intake for the specified age group and gender
//            Map<String, Double> genderMap = averageWaterIntakeResult.get(gender);
//            genderMap.put(ageGroup, genderMap.getOrDefault(ageGroup, 0.0) + waterIntake);
//        }
//
//        return averageWaterIntakeResult;
//    }

//    public Map<String, Map<String, Double>> calculateAverageWaterIntake() {
//        // Define average water intake values (in milliliters) for different age groups and genders
//        // These values can be retrieved from a database or configuration file
//        Map<String, Map<String, Double>> averageWaterIntakeResult = new HashMap<>();
//        Map<String, Double> maleHeartMap = new HashMap<>();
//        Map<String, Double> femaleIntakeMap = new HashMap<>();
//
//        // Initialize average water intake values for male and female
//        initializeAverageValueMap(maleHeartMap);
//        initializeAverageValueMap(femaleIntakeMap);
//
//        // Populate the map with gender-specific intake maps
//        averageWaterIntakeResult.put("male", maleHeartMap);
//        averageWaterIntakeResult.put("female", femaleIntakeMap);
//
//        // Fetch all users
//        List<User> users = userRepository.findAll();
//
//        if (users == null) {
//            // Handle the case where users list is null
//            return averageWaterIntakeResult; // or throw an exception
//        }
//
//        // Iterate through all users to calculate total water intake for each age group and gender
//        for (User user : users) {
//            // Check if user profile is null
//            if (user.getUserProfile() == null) {
//                continue; // Skip this user and continue to the next one
//            }
//
//            String ageGroup = calculateAgeGroup(user);
//            String gender = user.getUserProfile().getGender().toLowerCase();
//            Double waterIntake = calculateWaterIntakeForUser(user);
//
//            // Update the total water intake for the specified age group and gender
//            Map<String, Double> genderMap = averageWaterIntakeResult.get(gender);
//            genderMap.put(ageGroup, genderMap.getOrDefault(ageGroup, 0.0) + waterIntake);
//        }
//
//        return averageWaterIntakeResult;
//    }



//        public Map<String, Map<String, Double>> calculateAverageWaterIntake() {
//            // Define average water intake values (in milliliters) for different age groups and genders
//            // These values can be retrieved from a database or configuration file
//            Map<String, Map<String, Double>> averageWaterIntakeResult = new HashMap<>();
//            Map<String, Double> maleHeartMap = new LinkedHashMap<>(); // Use LinkedHashMap here
//            Map<String, Double> femaleIntakeMap = new LinkedHashMap<>(); // Use LinkedHashMap here
//
//            // Initialize average water intake values for male and female
//            initializeAverageValueMap(maleHeartMap);
//            initializeAverageValueMap(femaleIntakeMap);
//
//            // Populate the map with gender-specific intake maps
//            averageWaterIntakeResult.put("male", maleHeartMap);
//            averageWaterIntakeResult.put("female", femaleIntakeMap);
//
//            // Fetch all users
//            List<User> users = userRepository.findAll();
//
//            if (users == null) {
//                // Handle the case where users list is null
//                return averageWaterIntakeResult; // or throw an exception
//            }
//
//            // Iterate through all users to calculate total water intake for each age group and gender
//            for (User user : users) {
//                // Check if user profile is null
//                if (user.getUserProfile() == null) {
//                    continue; // Skip this user and continue to the next one
//                }
//
//                String ageGroup = calculateAgeGroup(user);
//                String gender = user.getUserProfile().getGender().toLowerCase();
//                Double waterIntake = calculateWaterIntakeForUser(user);
//
//                // Update the total water intake for the specified age group and gender
//                Map<String, Double> genderMap = averageWaterIntakeResult.get(gender);
//                genderMap.put(ageGroup, genderMap.getOrDefault(ageGroup, 0.0) + waterIntake);
//            }
//
//            return averageWaterIntakeResult;
//        }


//    private void initializeAverageValueMap(Map<String, Double> intakeMap) {
//        // Initialize average water intake values for different age groups
//        intakeMap.put("<15", 0.0);
//        intakeMap.put("15-29", 0.0);
//        intakeMap.put("30-44", 0.0);
//        intakeMap.put("45-59", 0.0);
//        intakeMap.put(">60", 0.0);
//    }

    private void initializeAverageValueMap(Map<String, Double> intakeMap) {
        // Initialize average water intake values for different age groups
        intakeMap.put("<15yrs", 0.0);
        intakeMap.put("15-29yrs", 0.0);
        intakeMap.put("30-44yrs", 0.0);
        intakeMap.put("45-59yrs", 0.0);
        intakeMap.put(">60yrs", 0.0);
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

//    public Map<String, Map<String, Double>> calculateAverageHeartRate() {
//        // Define average water intake values (in milliliters) for different age groups and genders
//        // These values can be retrieved from a database or configuration file
//        Map<String, Map<String, Double>> averagezHeartRateResult = new HashMap<>();
//        Map<String, Double> maleIntakeMap = new HashMap<>();
//        Map<String, Double> femaleIntakeMap = new HashMap<>();
//
//        // Initialize average water intake values for male and female
//        initializeAverageValueMap(maleIntakeMap);
//        initializeAverageValueMap(femaleIntakeMap);
//
//        // Populate the map with gender-specific intake maps
//        averagezHeartRateResult.put("male", maleIntakeMap);
//        averagezHeartRateResult.put("female", femaleIntakeMap);
//
//        // Iterate through all users to calculate total water intake for each age group and gender
//        List<User> users = userRepository.findAll(); // Assuming you have a repository for User
//        for (User user : users) {
//            String ageGroup = calculateAgeGroup(user);
//            String gender = user.getUserProfile().getGender().toLowerCase();
//            Double waterIntake = calculateHeartRateForUser(user);
//
//            // Update the total water intake for the specified age group and gender
//            Map<String, Double> genderMap = averagezHeartRateResult.get(gender);
//            genderMap.put(ageGroup, genderMap.getOrDefault(ageGroup, 0.0) + waterIntake);
//        }
//
//        return averagezHeartRateResult;
//    }

//    public Map<String, Map<String, Double>> calculateAverageHeartRate() {
//        // Define average heart rate values for different age groups and genders
//        Map<String, Map<String, Double>> averageHeartRateResult = new HashMap<>();
//        Map<String, Double> maleRateMap = new HashMap<>();
//        Map<String, Double> femaleRateMap = new HashMap<>();
//
//        // Initialize average heart rate values for male and female
//        initializeAverageValueMap(maleRateMap);
//        initializeAverageValueMap(femaleRateMap);
//
//        // Populate the map with gender-specific heart rate maps
//        averageHeartRateResult.put("male", maleRateMap);
//        averageHeartRateResult.put("female", femaleRateMap);
//
//        // Fetch all users
//        List<User> users = userRepository.findAll();
//
//        if (users == null) {
//            // Handle the case where users list is null
//            return averageHeartRateResult; // or throw an exception
//        }
//
//        // Iterate through all users to calculate total heart rate for each age group and gender
//        for (User user : users) {
//            // Check if user profile is null
//            if (user.getUserProfile() == null) {
//                continue; // Skip this user and continue to the next one
//            }
//
//            String ageGroup = calculateAgeGroup(user);
//            String gender = user.getUserProfile().getGender().toLowerCase();
//            Double heartRate = calculateHeartRateForUser(user);
//
//            // Update the total heart rate for the specified age group and gender
//            Map<String, Double> genderMap = averageHeartRateResult.get(gender);
//            genderMap.put(ageGroup, genderMap.getOrDefault(ageGroup, 0.0) + heartRate);
//        }
//
//        return averageHeartRateResult;
//    }
public List<AverageDTOs> calculateAverageHeartRate() {
    Map<String, Map<String, Double>> totalsMap = new HashMap<>();
    Map<String, Map<String, Integer>> countsMap = new HashMap<>();

    List<User> users = userRepository.findAll();
    if (users == null) return Collections.emptyList();

    for (User user : users) {
        if (user.getUserProfile() == null || user.getUserProfile().getGender() == null) continue;

        String ageGroup = calculateAgeGroup(user);
        String gender = user.getUserProfile().getGender().toLowerCase();
        Double heartRate = calculateHeartRateForUser(user);

        totalsMap.computeIfAbsent(gender, k -> new HashMap<>())
                .merge(ageGroup, heartRate, Double::sum);
        countsMap.computeIfAbsent(gender, k -> new HashMap<>())
                .merge(ageGroup, 1, Integer::sum);
    }

    return compileResultss(totalsMap, countsMap);
}
    private List<AverageDTOs> compileResultss(Map<String, Map<String, Double>> totals, Map<String, Map<String, Integer>> counts) {
        List<AverageDTOs> results = new ArrayList<>();
        totals.forEach((gender, ageMap) -> {
            ageMap.forEach((ageGroup, total) -> {
                Integer count = counts.get(gender).getOrDefault(ageGroup, 0);
                if (count > 0) {
                    double average = total / count;
                    String formattedAverage = String.format("%.2fbpm", average);
                    results.add(new AverageDTOs(formattedAverage, ageGroup, gender, count));
                } else {
                    results.add(new AverageDTOs("0.00bpm", ageGroup, gender, 0));
                }
            });
        });
        return results;
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







//    private String calculateAgeGroup(User user) {
//        LocalDate currentDate = LocalDate.now();
//        LocalDate birthDate = user.getUserProfile().getDateOfBirth();
//        int age = Period.between(birthDate, currentDate).getYears();
//
//        // Implement logic to categorize age into groups
//        if (age < 15) {
//            return "<15";
//        } else if (age >= 15 && age <= 29) {
//            return "15-29";
//        } else if (age >= 30 && age <= 44) {
//            return "30-44";
//        } else if (age >= 45 && age <= 59) {
//            return "45-59";
//        } else {
//            return ">60";
//        }
//    }

    private String calculateAgeGroup(User user) {
        LocalDate currentDate = LocalDate.now();
        LocalDate birthDate = user.getUserProfile() != null ? user.getUserProfile().getDateOfBirth() : null;

        if (birthDate == null) {
            // Handle the case where birthDate is null
            return "Unknown"; // or throw an exception
        }

        int age = Period.between(birthDate, currentDate).getYears();

        // Implement logic to categorize age into groups
        if (age < 15) {
            return "<15yrs";
        } else if (age >= 15 && age <= 29) {
            return "15-29yrs";
        } else if (age >= 30 && age <= 44) {
            return "30-44yrs";
        } else if (age >= 45 && age <= 59) {
            return "45-59yrs";
        } else {
            return ">60yrs";
        }
    }
    private String calculateAgeGroupp(User user) {
        LocalDate currentDate = LocalDate.now();
        LocalDate birthDate = user.getUserProfile() != null ? user.getUserProfile().getDateOfBirth() : null;

        if (birthDate == null) {
            // Handle the case where birthDate is null
            return "Unknown"; // or throw an exception
        }

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

//    private double calculateAverageDuration(List<User> userList) {
//        // Assuming sleep duration is stored in the SleepDuration entity
//        List<Double> sleepDurations = userList.stream()
//                .flatMap(user -> user.getSleepDurations().stream()
//                        .map(sleepDuration -> Double.valueOf(sleepDuration.getDuration())))
//                .collect(Collectors.toList());
//
//        // Calculate average sleep duration
//        double averageDuration = sleepDurations.stream()
//                .mapToDouble(Double::doubleValue)
//                .average()
//                .orElse(0.0);
//
//        return averageDuration;
//    }

    private double calculateAverageDuration(List<User> userList) {
        // Assuming sleep duration is stored in the SleepDuration entity
        List<Double> sleepDurations = userList.stream()
                .flatMap(user -> user.getSleepDurations().stream()
                        .map(sleepDuration -> (double) (sleepDuration.getDuration() + sleepDuration.getManualDuration())))
                .collect(Collectors.toList());

        // Calculate average sleep duration
        double averageDuration = sleepDurations.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        return averageDuration;
    }


//    by calories
//public List<ActivitiesStatsDTO> getAverageCaloriesByAgeAndGender() {
//    List<ActivitiesStatsDTO> statsList = new ArrayList<>();
//
//    // Fetch all users with activity information
//    List<User> users = userRepository.findAll();
//
//    // Group users by age and gender
//    Map<String, Map<String, List<User>>> groupedUsers = users.stream()
//            .collect(Collectors.groupingBy(this::calculateAgeGroup,
//                    Collectors.groupingBy(user -> user.getUserProfile().getGender())));
//
//    // Calculate average calories and activity count for each group
//    groupedUsers.forEach((ageGroup, genderMap) ->
//            genderMap.forEach((gender, userList) -> {
//                double averageCalories = calculateAverageCalories(userList);
//                long userCount = userList.size();
//                long activityCount = calculateActivityCount(userList);
//                ActivitiesStatsDTO statsDTO = new ActivitiesStatsDTO();
//                statsDTO.setAgeGroup(ageGroup);
//                statsDTO.setGender(gender);
//                statsDTO.setAverageCalories(averageCalories);
//                statsDTO.setUserCount(userCount);
//                statsDTO.setActivityCount(activityCount);
//                statsList.add(statsDTO);
//            }));
//
//    return statsList;
//}
//public List<ActivitiesStatsDTO> getAverageCaloriesByAgeAndGender() {
//    List<ActivitiesStatsDTO> statsList = new ArrayList<>();
//
//    // Fetch all users with activity information
//    List<User> users = userRepository.findAll();
//
//    if (users == null) {
//        // Handle the case where users list is null
//        return statsList; // or throw an exception
//    }
//
//    // Group users by age and gender
//    Map<String, Map<String, List<User>>> groupedUsers = users.stream()
//            .filter(user -> user.getUserProfile() != null) // Filter out users with null profiles
//            .collect(Collectors.groupingBy(this::calculateAgeGroup,
//                    Collectors.groupingBy(user -> user.getUserProfile().getGender())));
//
//    // Calculate average calories and activity count for each group
//    groupedUsers.forEach((ageGroup, genderMap) ->
//            genderMap.forEach((gender, userList) -> {
//                double averageCalories = calculateAverageCalories(userList);
//                long userCount = userList.size();
//                long activityCount = calculateActivityCount(userList);
//                ActivitiesStatsDTO statsDTO = new ActivitiesStatsDTO();
//                statsDTO.setAgeGroup(ageGroup);
//                statsDTO.setGender(gender);
//                statsDTO.setAverageCalories(averageCalories);
//                statsDTO.setUserCount(userCount);
//                statsDTO.setActivityCount(activityCount);
//                statsList.add(statsDTO);
//            }));
//
//    return statsList;
//}

//    public List<ActivitiesStatsDTO> getAverageCaloriesByAgeAndGender() {
//        List<ActivitiesStatsDTO> statsList = new ArrayList<>();
//        List<User> users = userRepository.findAll();
//
//        if (users == null) {
//            return statsList; // Optionally, throw an exception if needed
//        }
//
//        Map<String, Map<String, List<User>>> groupedUsers = users.stream()
//                .filter(user -> user.getUserProfile() != null)
//                .collect(Collectors.groupingBy(this::calculateAgeGroup,
//                        Collectors.groupingBy(user -> user.getUserProfile().getGender())));
//
//        groupedUsers.forEach((ageGroup, genderMap) -> genderMap.forEach((gender, userList) -> {
//            String averageCalories = calculateAverageCalories(userList);
//            long userCount = userList.size();
//            long activityCount = calculateActivityCount(userList);
//            ActivitiesStatsDTO statsDTO = new ActivitiesStatsDTO();
//            statsDTO.setAgeGroup(ageGroup);
//            statsDTO.setGender(gender);
//            statsDTO.setAverageCalories(averageCalories);
//            statsDTO.setUserCount(userCount);
//            statsDTO.setActivityCount(activityCount);
//            statsList.add(statsDTO);
//        }));
//
//        // Sorting the list based on age groups
//        Map<String, Integer> ageGroupOrder = new HashMap<>();
//        ageGroupOrder.put("<15yrs", 1);
//        ageGroupOrder.put("15-29yrs", 2);
//        ageGroupOrder.put("30-44yrs", 3);
//        ageGroupOrder.put("45-59yrs", 4);
//        ageGroupOrder.put(">60yrs", 5);
//
//        Collections.sort(statsList, Comparator.comparingInt(
//                (ActivitiesStatsDTO stats) -> ageGroupOrder.getOrDefault(stats.getAgeGroup(), Integer.MAX_VALUE)
//        ));
//
//        return statsList;
//    }
public List<ActivitiesStatsDTO> getAverageCaloriesByAgeAndGender() {
    List<ActivitiesStatsDTO> statsList = new ArrayList<>();
    List<User> users = userRepository.findAll();

    if (users == null) {
        return statsList; // Optionally, throw an exception if needed
    }

    Map<String, Map<String, List<User>>> groupedUsers = users.stream()
            .filter(user -> user.getUserProfile() != null)
            .collect(Collectors.groupingBy(this::calculateAgeGroup,
                    Collectors.groupingBy(user -> user.getUserProfile().getGender())));

    groupedUsers.forEach((ageGroup, genderMap) -> genderMap.forEach((gender, userList) -> {
        String averageCalories = calculateAverageCalories(userList);
        long userCount = userList.size();
        long activityCount = calculateActivityCount(userList);
        long activeUserCount = calculateActiveUserCount(userList);
        ActivitiesStatsDTO statsDTO = new ActivitiesStatsDTO();
        statsDTO.setAgeGroup(ageGroup);
        statsDTO.setGender(gender);
        statsDTO.setAverageCalories(averageCalories);
        statsDTO.setUserCount(userCount);
        statsDTO.setActivityCount(activityCount);
        statsDTO.setActiveUsers(activeUserCount);
        statsList.add(statsDTO);
    }));

    // Sorting the list based on age groups
    Map<String, Integer> ageGroupOrder = new HashMap<>();
    ageGroupOrder.put("<15yrs", 1);
    ageGroupOrder.put("15-29yrs", 2);
    ageGroupOrder.put("30-44yrs", 3);
    ageGroupOrder.put("45-59yrs", 4);
    ageGroupOrder.put(">60yrs", 5);

    Collections.sort(statsList, Comparator.comparingInt(
            (ActivitiesStatsDTO stats) -> ageGroupOrder.getOrDefault(stats.getAgeGroup(), Integer.MAX_VALUE)
    ));

    return statsList;
}
//    private long calculateActivityCount(List<User> userList) {
//        // Assuming activities are stored in the Activities entity
//        return userList.stream()
//                .flatMap(user -> user.getActivities().stream())
//                .count();
//    }
private long calculateActivityCount(List<User> userList) {
    // Assuming activities are stored in the Activities entity
    return userList.stream()
            .filter(user -> user.getActivities() != null)
            .flatMap(user -> user.getActivities().stream())
            .count();


}
private long calculateActiveUserCount(List<User> userList) {
    return userList.stream()
            .filter(user -> user.getActivities() != null && user.getActivities().stream()
                    .anyMatch(activity -> activity.getCalory() != null && activity.getCalory() > 0))
            .count();
}
//    private double calculateAverageCalories(List<User> userList) {
//        // Assuming calories are stored in the Activities entity
//        List<Double> caloriesList = userList.stream()
//                .flatMap(user -> user.getActivities().stream().map(Activities::getCalory))
//                .collect(Collectors.toList());
//
//        // Calculate average calories
//        double averageCalories = caloriesList.stream()
//                .mapToDouble(Double::doubleValue)
//                .average()
//                .orElse(0.0);
//
//        return averageCalories;
//    }

//    private double calculateAverageCalories(List<User> userList) {
//        // Assuming calories are stored in the Activities entity
//        List<Double> caloriesList = userList.stream()
//                .flatMap(user -> {
//                    List<Activities> activities = user.getActivities();
//                    if (activities != null) {
//                        return activities.stream().map(Activities::getCalory);
//                    } else {
//                        return Stream.empty(); // Return an empty stream if activities is null
//                    }
//                })
//                .filter(Objects::nonNull) // Filter out null values
//                .collect(Collectors.toList());
//
//        // Calculate average calories
//        double averageCalories = caloriesList.stream()
//                .mapToDouble(Double::doubleValue)
//                .average()
//                .orElse(0.0);
//
//        return averageCalories;
//    }
private String calculateAverageCalories(List<User> userList) {
    List<Double> caloriesList = userList.stream()
            .flatMap(user -> {
                List<Activities> activities = user.getActivities();
                if (activities != null) {
                    return activities.stream().map(Activities::getCalory);
                } else {
                    return Stream.empty();
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

    double average = caloriesList.stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);

    return String.format("%.1f kcal", average); // Return formatted string with "kcal"
}

//    public List<AverageStepsByAgeDTO> getAverageStepsByAge() {
//        List<AverageStepsByAgeDTO> statsList = new ArrayList<>();
//
//        // Fetch all users with activity information
//        List<User> users = userRepository.findAll();
//
//        // Group users by age
//        Map<String, List<User>> groupedUsers = users.stream()
//                .collect(Collectors.groupingBy(this::calculateAgeGroup));
//
//        // Calculate average steps for each age group
//        groupedUsers.forEach((ageGroup, userList) -> {
//            double averageSteps = calculateAverageSteps(userList);
//            AverageStepsByAgeDTO statsDTO = new AverageStepsByAgeDTO();
//            statsDTO.setAgeGroup(ageGroup);
//            statsDTO.setAverageSteps(averageSteps);
//            statsList.add(statsDTO);
//        });
//
//        return statsList;
//    }

//    private double calculateAverageSteps(List<User> userList) {
//        // Assuming steps are stored in the Activities entity
//        List<Integer> stepsList = userList.stream()
//                .flatMap(user -> user.getActivities().stream().map(Activities::getSteps))
//                .collect(Collectors.toList());
//
//        // Calculate average steps
//        double averageSteps = stepsList.stream()
//                .mapToInt(Integer::intValue)
//                .average()
//                .orElse(0.0);
//
//        return averageSteps;
//    }

//    public List<AverageStepsByAgeDTO> getAverageStepsByAge() {
//        List<AverageStepsByAgeDTO> statsList = new ArrayList<>();
//
//        // Fetch all users with activity information
//        List<User> users = userRepository.findAll();
//
//        // Group users by age
//        Map<String, List<User>> groupedUsers = users.stream()
//                .filter(user -> !calculateAgeGroup(user).equals("Unknown")) // Filter out users with unknown age
//                .collect(Collectors.groupingBy(this::calculateAgeGroup));
//
//        // Add ">60" age group if not present
//        if (!groupedUsers.containsKey(">60")) {
//            groupedUsers.put(">60", new ArrayList<>());
//        }
//
//        // Calculate average steps for each age group
//        groupedUsers.forEach((ageGroup, userList) -> {
//            double averageSteps = calculateAverageSteps(userList);
//            AverageStepsByAgeDTO statsDTO = new AverageStepsByAgeDTO();
//            statsDTO.setAgeGroup(ageGroup);
//            statsDTO.setAverageSteps(averageSteps);
//            statsList.add(statsDTO);
//        });
//
//        // Sort the statsList based on age groups
//        Collections.sort(statsList, new AgeGroupComparator());
//
//        return statsList;
//    }

//    public List<AverageDTOs> getAverageStepsByAge() {
//        List<AverageStepsByAgeDTO> statsList = new ArrayList<>();
//
//        List<User> users = userRepository.findAll();
//        // Group by age and then by gender
//        Map<String, Map<String, List<User>>> groupedUsers = users.stream()
//                .filter(user -> user.getUserProfile() != null && user.getUserProfile().getGender() != null)
//                .collect(Collectors.groupingBy(this::calculateAgeGroup,
//                        Collectors.groupingBy(user -> user.getUserProfile().getGender())));
//
//        groupedUsers.forEach((ageGroup, genderMap) -> {
//            genderMap.forEach((gender, userList) -> {
//                double averageSteps = calculateAverageSteps(userList);
//                long userCount = userList.size();
//                long activeUsers = userList.stream().filter(u -> u.getSteps() > 0).count();
//
//                AverageStepsByAgeDTO statsDTO = new AverageStepsByAgeDTO();
//                statsDTO.setAgeGroup(ageGroup);
//                statsDTO.setGender(gender);
//                statsDTO.setAverageSteps(averageSteps);
//                statsDTO.setUserCount(userCount);
//                statsDTO.setActiveUsers(activeUsers);
//
//                statsList.add(statsDTO);
//            });
//        });
//
//        // Optionally sort the list by age group and gender for structured output
//        statsList.sort(Comparator.comparing(AverageStepsByAgeDTO::getAgeGroup)
//                .thenComparing(AverageStepsByAgeDTO::getGender));
//
//        return statsList;
//    }
//
//    private double calculateAverageSteps(List<User> userList) {
//        return userList.stream()
//                .mapToDouble(user -> user.getSteps()) // Assuming `getSteps()` gets the number of steps
//                .average()
//                .orElse(0.0);
//    }


//    public List<AverageDTOs> getAverageStepsByAge() {
//        List<User> users = userRepository.findAll();
//
//        // Group users by age and gender
//        Map<String, Map<String, List<User>>> groupedUsers = users.stream()
//                .filter(user -> user.getUserProfile() != null && user.getUserProfile().getDateOfBirth() != null)
//                .collect(Collectors.groupingBy(this::calculateAgeGroup,
//                        Collectors.groupingBy(user -> {
//                            String gender = user.getUserProfile().getGender();
//                            return gender != null ? gender : "Unknown";
//                        })));
//
//        List<AverageDTOs> statsList = new ArrayList<>();
//
//        // Calculate average steps, user count, and active users for each age group and gender
//        groupedUsers.forEach((ageGroup, genderMap) ->
//                genderMap.forEach((gender, userList) -> {
//                    double averageSteps = calculateAverageSteps(userList);
//                    long userCount = userList.size();
//                    long activeUsers = countActiveUsers(userList);  // Implement this method based on criteria for active
//                    String averageStepsFormatted = String.format("%.2f", averageSteps);  // Format steps if necessary
//
//                    AverageDTOs statsDTO = new AverageDTOs(averageStepsFormatted, ageGroup, gender, userCount, activeUsers);
//                    statsList.add(statsDTO);
//                }));
//
//        // Sort the list by age group and possibly gender if required
//        statsList.sort(Comparator.comparing(AverageDTOs::getAgeGroup).thenComparing(AverageDTOs::getGender));
//
//        return statsList;
//    }

//    public Map<String, List<AverageDTOs>> getAverageStepsByAge() {
//        List<User> users = userRepository.findAll();
//
//        // Group users by age and gender
//        Map<String, Map<String, List<User>>> groupedUsers = users.stream()
//                .filter(user -> user.getUserProfile() != null && user.getUserProfile().getDateOfBirth() != null)
//                .collect(Collectors.groupingBy(this::calculateAgeGroup,
//                        Collectors.groupingBy(user -> {
//                            String gender = user.getUserProfile().getGender();
//                            return gender != null ? gender : "Unknown";
//                        })));
//
//        Map<String, List<AverageDTOs>> groupedStats = new HashMap<>();
//
//        // Calculate average steps, user count, and active users for each age group and gender
//        groupedUsers.forEach((ageGroup, genderMap) -> {
//            List<AverageDTOs> statsByAge = new ArrayList<>();
//            genderMap.forEach((gender, userList) -> {
//                double averageSteps = calculateAverageSteps(userList);
//                long userCount = userList.size();
//                long activeUsers = countActiveUsers(userList);
//                String averageStepsFormatted = String.format("%.2f", averageSteps);
//
//                AverageDTOs statsDTO = new AverageDTOs(averageStepsFormatted, ageGroup, gender, userCount, activeUsers);
//                statsByAge.add(statsDTO);
//            });
//            groupedStats.put(ageGroup, statsByAge);
//        });
//
//        return groupedStats;
//    }

    public List<AverageDTOs> getAverageStepsByAge() {
        List<User> users = userRepository.findAll();

        // Group users by age and gender
        Map<String, Map<String, List<User>>> groupedUsers = users.stream()
                .filter(user -> user.getUserProfile() != null && user.getUserProfile().getDateOfBirth() != null)
                .collect(Collectors.groupingBy(this::calculateAgeGroup,
                        Collectors.groupingBy(user -> {
                            String gender = user.getUserProfile().getGender();
                            return gender != null ? gender : "Unknown";
                        })));

        List<AverageDTOs> statsList = new ArrayList<>();

        // Calculate average steps, user count, and active users for each age group and gender
        groupedUsers.forEach((ageGroup, genderMap) ->
                genderMap.forEach((gender, userList) -> {
                    double averageSteps = calculateAverageSteps(userList);
                    long userCount = userList.size();
                    long activeUsers = countActiveUsers(userList);
                    String averageStepsFormatted = String.format("%.2f", averageSteps);

                    AverageDTOs statsDTO = new AverageDTOs(averageStepsFormatted, ageGroup, gender, userCount, activeUsers);
                    statsList.add(statsDTO);
                }));

        // Sort the list by age group and possibly gender if required
        statsList.sort(Comparator.comparing(AverageDTOs::getAgeGroup).thenComparing(AverageDTOs::getGender));

        return statsList;
    }


//public List<AverageStepsByAgeDTO> getAverageStepsByAge() {
//    List<AverageStepsByAgeDTO> statsList = new ArrayList<>();
//
//    // Fetch all users with activity information
//    List<User> users = userRepository.findAll();
//
//    // Group users by age
//    Map<String, List<User>> groupedUsers = users.stream()
//            .filter(user -> !calculateAgeGroup(user).equals("Unknown")) // Filter out users with unknown age
//            .collect(Collectors.groupingBy(this::calculateAgeGroup));
//
//    // Calculate average steps for each age group
//    groupedUsers.forEach((ageGroup, userList) -> {
//        double averageSteps = calculateAverageSteps(userList);
//        AverageStepsByAgeDTO statsDTO = new AverageStepsByAgeDTO();
//        statsDTO.setAgeGroup(ageGroup);
//        statsDTO.setAverageSteps(averageSteps);
//        statsList.add(statsDTO);
//    });
//
//    // Sort the statsList based on age groups
//    Collections.sort(statsList, new AgeGroupComparator());
//
//    return statsList;
//}

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

    // Custom comparator to sort age groups
//    private class AgeGroupComparator implements Comparator<AverageStepsByAgeDTO> {
//        private List<String> ageGroupsOrder = Arrays.asList("<15", "15-29", "30-44", "45-59",">60");
//
//        @Override
//        public int compare(AverageStepsByAgeDTO dto1, AverageStepsByAgeDTO dto2) {
//            int index1 = ageGroupsOrder.indexOf(dto1.getAgeGroup());
//            int index2 = ageGroupsOrder.indexOf(dto2.getAgeGroup());
//            return Integer.compare(index1, index2);
//        }
//    }
//    private class AgeGroupComparator implements Comparator<AverageStepsByAgeDTO> {
//        private List<String> ageGroupsOrder = Arrays.asList("<15", "15-29", "30-44", "45-59", ">60");
//
//        @Override
//        public int compare(AverageStepsByAgeDTO dto1, AverageStepsByAgeDTO dto2) {
//            String ageGroup1 = dto1.getAgeGroup();
//            String ageGroup2 = dto2.getAgeGroup();
//
//            // Check if age group is ">60", if yes, return 1 to push it to the end
//            if (ageGroup1.equals(">60")) {
//                return 1;
//            } else if (ageGroup2.equals(">60")) {
//                return -1;
//            }
//
//            // If age groups are not ">60", compare based on their index in the ageGroupsOrder list
//            int index1 = ageGroupsOrder.indexOf(ageGroup1);
//            int index2 = ageGroupsOrder.indexOf(ageGroup2);
//
//            // Compare based on the index in ageGroupsOrder list
//            return Integer.compare(index1, index2);
//        }
//    }


//public List<AverageStepsByAgeDTO> getAverageStepsByAge() {
//    List<AverageStepsByAgeDTO> statsList = new ArrayList<>();
//
//    // Fetch all users with activity information
//    List<User> users = userRepository.findAll();
//
//    // Group users by age, filtering out unknown age group
//    Map<String, List<User>> groupedUsers = users.stream()
//            .filter(user -> !calculateAgeGroup(user).equals("Unknown"))
//            .collect(Collectors.groupingBy(this::calculateAgeGroup));
//
//    // Calculate average steps for each age group
//    groupedUsers.forEach((ageGroup, userList) -> {
//        double averageSteps = calculateAverageSteps(userList);
//        AverageStepsByAgeDTO statsDTO = new AverageStepsByAgeDTO();
//        statsDTO.setAgeGroup(ageGroup);
//        statsDTO.setAverageSteps(averageSteps);
//        statsList.add(statsDTO);
//    });
//
//    // Define the custom comparator for age groups
//    Comparator<String> ageGroupComparator = (ag1, ag2) -> {
//        if (ag1.equals("<15")) return -1;
//        if (ag2.equals("<15")) return 1;
//        if (ag1.equals(">60")) return 1;
//        if (ag2.equals(">60")) return -1;
//        return ag1.compareTo(ag2);
//    };
//
//    // Sort the statsList based on age groups using the custom comparator
//    statsList.sort(Comparator.comparing(AverageStepsByAgeDTO::getAgeGroup, ageGroupComparator));
//
//    return statsList;
//}
//
//    private double calculateAverageSteps(List<User> userList) {
//        // Assuming steps are stored in the Activities entity
//        List<Integer> stepsList = userList.stream()
//                .flatMap(user -> user.getActivities().stream().map(Activities::getSteps))
//                .collect(Collectors.toList());
//
//        // Calculate average steps
//        double averageSteps = stepsList.stream()
//                .mapToInt(Integer::intValue)
//                .average()
//                .orElse(0.0);
//
//        return averageSteps;
//    }

    public TotalUsersDTO getTotalUsers() {
        TotalUsersDTO totalUsersDTO = new TotalUsersDTO();

        // Fetch total number of users
        long totalUsers = userRepository.count();
        totalUsersDTO.setTotalUsers(totalUsers);

        return totalUsersDTO;
    }


//    public UsersByWorkLevelDTO getUsersByWorkLevel() {
//        UsersByWorkLevelDTO usersByWorkLevelDTO = new UsersByWorkLevelDTO();
//
//        // Fetch all users
//        List<User> users = userRepository.findAll();
//
//        // Group users by work level
//        Map<String, Long> usersByWorkLevel = users.stream()
//                .collect(Collectors.groupingBy(user -> user.getUserProfile().getWorkLevel(), Collectors.counting()));
//
//        usersByWorkLevelDTO.setUsersByWorkLevel(usersByWorkLevel);
//
//        return usersByWorkLevelDTO;
//    }
public UsersByWorkLevelDTO getUsersByWorkLevel() {
    UsersByWorkLevelDTO usersByWorkLevelDTO = new UsersByWorkLevelDTO();

    // Fetch all users
    List<User> users = userRepository.findAll();

    if (users == null) {
        // Handle the case where users list is null
        return usersByWorkLevelDTO; // or throw an exception
    }

    // Group users by work level
    Map<String, Long> usersByWorkLevel = users.stream()
            .filter(user -> user.getUserProfile() != null) // Filter out users with null profiles
            .collect(Collectors.groupingBy(user -> {
                String workLevel = user.getUserProfile().getWorkLevel();
                return workLevel != null ? workLevel : "Unknown"; // Handle null work levels
            }, Collectors.counting()));

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
