package com.example.jwt.AdminDashboard;

import com.example.jwt.booksystem1.books.OrderRepository;
import com.example.jwt.entities.ContactUs;
import com.example.jwt.entities.Feedback;
import com.example.jwt.entities.FoodToday.Dishes;
import com.example.jwt.entities.FoodToday.Ingredients;
import com.example.jwt.entities.FoodToday.NinData;
import com.example.jwt.entities.FoodToday.Recipe.Recipe;
import com.example.jwt.entities.User;
import com.example.jwt.entities.UserProfile;
import com.example.jwt.entities.dashboardEntity.Activities;
import com.example.jwt.entities.dashboardEntity.healthTrends.SleepDuration;
import com.example.jwt.entities.water.WaterEntry;
import com.example.jwt.repository.ContactUsRepository;
import com.example.jwt.repository.FeedbackRepository;
import com.example.jwt.repository.FoodTodayRepository.NinDataRepository;
import com.example.jwt.repository.UserProfileRepository;
import com.example.jwt.repository.UserRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.YearMonth;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private NinDataRepository ninDataRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private FeedbackRepository feedbackRepository; // Assuming you have a repository for Feedback

    @Autowired
    private ContactUsRepository contactUsRepository;

    public List<Feedback> getAllFeedbackData() {
        // Example: Fetch all feedback data from a repository (replace this with your actual logic)
        return feedbackRepository.findAll();
    }

//    public Map<String, Double> calculateAverageWaterIntakeByGenderAndAge() {
//        List<UserProfile> userProfiles = userProfileRepository.findAll();
//        Map<String, Double> averageIntakeByGenderAndAge = new HashMap<>();
//
//        // Group user profiles by gender and age
//        Map<String, List<UserProfile>> profilesByGenderAndAge = userProfiles.stream()
//                .collect(Collectors.groupingBy(profile -> profile.getGender() + "-" + calculateAge(profile.getDateOfBirth())));
//
//        // Calculate average water intake for each group
//        profilesByGenderAndAge.forEach((genderAge, profiles) -> {
//            double totalWaterIntake = profiles.stream()
//                    .mapToDouble(profile -> profile.getWaterEntity().calculateTotalWaterIntake())
//                    .sum();
//            double averageWaterIntake = totalWaterIntake / profiles.size();
//            averageIntakeByGenderAndAge.put(genderAge, averageWaterIntake);
//        });
//
//        return averageIntakeByGenderAndAge;
//    }

    // Calculate age based on date of birth
    private int calculateAge(LocalDate dateOfBirth) {
        LocalDate currentDate = LocalDate.now();
        return Period.between(dateOfBirth, currentDate).getYears();
    }

    public boolean getStatusById(Long contactUsId) {
        Optional<ContactUs> contactUsOptional = contactUsRepository.findById(contactUsId);
        return contactUsOptional.map(ContactUs::isStatus).orElse(false);
    }

    public Map<String, Long> getGenderCounts() {
//        String queryString = "SELECT gender, COUNT(*) FROM UserProfile GROUP BY gender";
        String queryString = "SELECT up.gender, COUNT(*) FROM User u JOIN u.userProfile up GROUP BY up.gender";

        Query query = entityManager.createQuery(queryString);
        List<Object[]> resultList = query.getResultList();

        Map<String, Long> genderCounts = new HashMap<>();
        for (Object[] result : resultList) {
            String gender = (String) result[0];
            Long count = (Long) result[1];
            genderCounts.put(gender, count);
        }

        return genderCounts;
    }


    public List<ContactUs> getAllContactUsData() {
        // Example: Fetch all contactUs data from a repository (replace this with your actual logic)
        return contactUsRepository.findAll();
    }

    public long countRegisteredUsersInMonth(YearMonth yearMonth) {
        LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        List<User> users = userRepository.findByRegistrationTimestampBetween(startOfMonth, endOfMonth);
        return users.size();
    }


//    public Map<String, Integer> calculateAgeCategoriesCount(User user) {
//        List<UserProfile> profiles = userProfileRepository.findAllByUser(user); // Retrieve all user profiles
//        Map<String, Integer> ageCategoryCounts = new HashMap<>();
//
//        // Initialize counters for different age categories
//        int category1Count = 0; // <15 yrs
//        int category2Count = 0; // 15-29 yrs
//        int category3Count = 0; // 30-44 yrs
//        int category4Count = 0; // 45-59 yrs
//        int category5Count = 0; // >60 yrs
//
//        LocalDate currentDate = LocalDate.now();
//
//        // Calculate age and categorize users
//        for (UserProfile profile : profiles) {
//            LocalDate dob = profile.getDateOfBirth();
//            int age = Period.between(dob, currentDate).getYears();
//
//            if (age < 15) {
//                category1Count++;
//            } else if (age >= 15 && age <= 29) {
//                category2Count++;
//            } else if (age >= 30 && age <= 44) {
//                category3Count++;
//            } else if (age >= 45 && age <= 59) {
//                category4Count++;
//            } else {
//                category5Count++;
//            }
//        }
//
//        // Put counts into the map
//        ageCategoryCounts.put("<15 yrs", category1Count);
//        ageCategoryCounts.put("15-29 yrs", category2Count);
//        ageCategoryCounts.put("30-44 yrs", category3Count);
//        ageCategoryCounts.put("45-59 yrs", category4Count);
//        ageCategoryCounts.put(">60 yrs", category5Count);
//
//        return ageCategoryCounts;
//    }

    public Map<String, Integer> calculateAgeCategoriesCount(List<UserProfile> profiles) {
        Map<String, Integer> ageCategoryCounts = new HashMap<>();

        // Initialize counters for different age categories
        int category1Count = 0; // <15 yrs
        int category2Count = 0; // 15-29 yrs
        int category3Count = 0; // 30-44 yrs
        int category4Count = 0; // 45-59 yrs
        int category5Count = 0; // >60 yrs

        LocalDate currentDate = LocalDate.now();

        // Calculate age and categorize users
        for (UserProfile profile : profiles) {
            LocalDate dob = profile.getDateOfBirth();
            int age = Period.between(dob, currentDate).getYears();

            if (age < 15) {
                category1Count++;
            } else if (age >= 15 && age <= 29) {
                category2Count++;
            } else if (age >= 30 && age <= 44) {
                category3Count++;
            } else if (age >= 45 && age <= 59) {
                category4Count++;
            } else {
                category5Count++;
            }
        }

        // Put counts into the map
        ageCategoryCounts.put("<15 yrs", category1Count);
        ageCategoryCounts.put("15-29 yrs", category2Count);
        ageCategoryCounts.put("30-44 yrs", category3Count);
        ageCategoryCounts.put("45-59 yrs", category4Count);
        ageCategoryCounts.put(">60 yrs", category5Count);

        return ageCategoryCounts;
    }


//    public List<Integer> getBMICategoriesByGender(String gender) {
//        List<UserProfile> profiles = userProfileRepository.findByGender(gender);
//        return calculateBMICategoriesCount(profiles);
//    }
//
//    public List<Integer> getMaleBMICategories() {
//        List<UserProfile> maleProfiles = userProfileRepository.findByGender("Male");
//        List<Integer> bmiCategoriesCount = calculateBMICategoriesCount(maleProfiles);
//        return bmiCategoriesCount;
//    }

//    private List<Integer> calculateBMICategoriesCount(List<UserProfile> profiles) {
//        int underweightCount = 0;
//        int normalCount = 0;
//        int overweightCount = 0;
//        int obeseCount = 0;
//
//        for (UserProfile profile : profiles) {
//            double bmi = profile.getBmi();
//            if (bmi < 18.5) {
//                underweightCount++;
//            } else if (bmi >= 18.5 && bmi < 25) {
//                normalCount++;
//            } else if (bmi >= 25 && bmi < 30) {
//                overweightCount++;
//            } else {
//                obeseCount++;
//            }
//        }
//
//        return Arrays.asList(underweightCount, normalCount, overweightCount, obeseCount);
//    }

    public Map<String, Integer> calculateBMICategoriesCount(List<UserProfile> profiles) {
        int underweightCount = 0;
        int normalCount = 0;
        int overweightCount = 0;
        int obeseCount = 0;

        for (UserProfile profile : profiles) {
            double bmi = profile.getBmi();
            if (bmi < 18.5) {
                underweightCount++;
            } else if (bmi >= 18.5 && bmi < 25) {
                normalCount++;
            } else if (bmi >= 25 && bmi < 30) {
                overweightCount++;
            } else {
                obeseCount++;
            }
        }

        Map<String, Integer> bmiCategoriesCount = new HashMap<>();
        bmiCategoriesCount.put("underweightCount", underweightCount);
        bmiCategoriesCount.put("normalCount", normalCount);
        bmiCategoriesCount.put("overweightCount", overweightCount);
        bmiCategoriesCount.put("obeseCount", obeseCount);

        return bmiCategoriesCount;
    }
    public Map<String, Integer> calculateBMICategoriesCount(List<UserProfile> profiles, User currentUser) {
        int underweightCount = 0;
        int normalCount = 0;
        int overweightCount = 0;
        int obeseCount = 0;

        for (UserProfile profile : profiles) {
            double bmi = profile.getBmi();
            if (bmi < 18.5) {
                underweightCount++;
            } else if (bmi >= 18.5 && bmi < 25) {
                normalCount++;
            } else if (bmi >= 25 && bmi < 30) {
                overweightCount++;
            } else {
                obeseCount++;
            }
        }

        Map<String, Integer> bmiCategoriesCount = new HashMap<>();
        bmiCategoriesCount.put("underweightCount", underweightCount);
        bmiCategoriesCount.put("normalCount", normalCount);
        bmiCategoriesCount.put("overweightCount", overweightCount);
        bmiCategoriesCount.put("obeseCount", obeseCount);

        return bmiCategoriesCount;
    }

        public Map<String, Integer> getBMICategoriesByGender(String gender, User currentUser) {
        List<UserProfile> profiles = userProfileRepository.findAllUserByGender(gender);
        return calculateBMICategoriesCount(profiles, currentUser);
    }
//    public Map<String, Integer> getBMICategoriesByGender(String gender) {
//        List<UserProfile> profiles = userProfileRepository.findAllUserByGender(gender);
//        return calculateBMICategoriesCount(profiles);
//    }

        public Map<String, Integer> getMaleBMICategories(User currentUser) {
        List<UserProfile> maleProfiles = userProfileRepository.findAllUserByGender("Male");
        return calculateBMICategoriesCount(maleProfiles, currentUser);
    }
//    public Map<String, Integer> getMaleBMICategories() {
//        List<UserProfile> maleProfiles = userProfileRepository.findAllUserByGender("Male");
//        return calculateBMICategoriesCount(maleProfiles);
//    }


    public Map<String, Integer> getMonthlyUserRegistrations() {
        List<User> allUsers = userRepository.findAll();
        Map<String, Integer> monthlyRegistrations = new HashMap<>();

        for (User user : allUsers) {
            String registrationMonth = user.getLocalDate().getMonth().toString();
            monthlyRegistrations.put(registrationMonth, monthlyRegistrations.getOrDefault(registrationMonth, 0) + 1);
        }
        System.out.println("monthly    "+monthlyRegistrations);
        return monthlyRegistrations;
    }



    private static final String OPENCAGE_API_KEY = "106199112e264ac08fb97c11935a2fc3";

    private static String getAddressComponent(JsonObject result, String componentType) {
        if (result.has("components")) {
            JsonObject components = result.getAsJsonObject("components");

            if (components.has(componentType)) {
                return components.getAsJsonPrimitive(componentType).getAsString();
            }
        }

        return null;
    }


    public static String getStateFromCoordinates(Double latitude, Double longitude) {
        try {
            String apiUrl = "https://api.opencagedata.com/geocode/v1/json"
                    + "?q=" + latitude + "+" + longitude
                    + "&key=" + OPENCAGE_API_KEY;

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();

            // Parse the JSON response to get the state
            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
            JsonArray results = jsonResponse.getAsJsonArray("results");

            if (results.size() > 0) {
                JsonObject firstResult = results.get(0).getAsJsonObject();
                return getAddressComponent(firstResult, "state");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setupModel(Model model) {
        // Retrieve all users from the repository
        List<User> userList = userRepository.findAll();

        // Calculate the total number of users
        int totalUsers = userList.size();

        // Create a map to store users grouped by state
        Map<String, List<User>> usersByState = new HashMap<>();

        // Iterate through the user list and add the address for each user
        for (User user : userList) {
            Double latitude = user.getLatitude();
            Double longitude = user.getLongitude();

            // Skip users with missing latitude or longitude
            if (latitude == null || longitude == null) {
                continue;
            }

            String state = getStateFromCoordinates(latitude, longitude);

            // Add the user to the list corresponding to their state
            usersByState.computeIfAbsent(state, k -> new ArrayList<>()).add(user);

            // Set the address for each user (if needed)
            user.setAddress(state);
        }

        // Add the user list and total users to the model
        model.addAttribute("user", userList);
        model.addAttribute("totalUsers", totalUsers);

        // Add the map of users grouped by state to the model
        model.addAttribute("usersByState", usersByState);
        model.addAttribute("userStatusByState", usersByState);


        // Your additional logic if needed
    }


    private static final Logger logger = LoggerFactory.getLogger(DashbaordController.class);

    public List<Path> getLogFiles(String directoryPath) {
        try {
            // List all files in the directory matching the pattern 'app.log.*' or 'app.log'
            List<Path> logFiles = Files.list(Paths.get(directoryPath))
                    .filter(path -> path.getFileName().toString().matches("app\\.log(\\.\\d{4}-\\d{2}-\\d{2})?"))
                    .sorted(Comparator.comparing(Path::getFileName).reversed()) // Sort in reverse order by file name (including the date suffix)
                    .collect(Collectors.toList());

            return logFiles;
        } catch (IOException e) {
            logger.error("Error listing log files from directory: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private String extractDateFromFilePath(Path filePath) {
        // Extract the date from the file name
        String fileName = filePath.getFileName().toString();
        Matcher matcher = Pattern.compile("\\d{4}-\\d{2}-\\d{2}").matcher(fileName);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }



    private List<String> readAllLogsFromDirectory(String directoryPath) {
        try {
            // List all files in the directory matching the pattern 'app.log.*'
            List<Path> logFiles = Files.list(Paths.get(directoryPath))
                    .filter(path -> path.getFileName().toString().matches("app\\.log\\.\\d{4}-\\d{2}-\\d{2}"))
                    .sorted(Comparator.reverseOrder()) // Sort in descending order (most recent first)
                    .collect(Collectors.toList());

            // Read content from all log files
            return logFiles.stream()
                    .map(this::readLogsFromFile)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("Error reading log files from directory: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
    private List<String> readLogsFromFile(Path logFilePath) {
        try {
            // Read all lines from the log file
            return Files.readAllLines(logFilePath);
        } catch (IOException e) {
            logger.error("Error reading logs from file '{}': {}", logFilePath, e.getMessage());
            return Collections.emptyList();
        }
    }

    public String extractFileNameFromFilePath(String filePath) {
        // Extract the file name from the file path
        return Paths.get(filePath).getFileName().toString();
    }

    public String extractDateFromFilePath(String filePath) {
        // Check if the file name is 'app.log'
        String fileName = Paths.get(filePath).getFileName().toString();
        if ("app.log".equals(fileName)) {
            // Return the current date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(new Date());
        }

        // Assuming the date is in the format 'YYYY-MM-dd'
        String[] parts = filePath.split("\\.");
        for (int i = parts.length - 1; i >= 0; i--) {
            if (parts[i].matches("\\d{4}-\\d{2}-\\d{2}")) {
                return parts[i];
            }
        }
        return "Unknown Date";
    }


//    private String extractDateFromFilePath(Path filePath) {
//        // Extract the date from the file name
//        String fileName = filePath.getFileName().toString();
//        Matcher matcher = Pattern.compile("\\d{4}-\\d{2}-\\d{2}").matcher(fileName);
//        if (matcher.find()) {
//            return matcher.group();
//        }
//        return "";
//    }

//    private List<Path> getLogFiles(String directoryPath) {
//        try {
//            // List all files in the directory matching the pattern 'app.log.*' or 'app.log'
//            List<Path> logFiles = Files.list(Paths.get(directoryPath))
//                    .filter(path -> path.getFileName().toString().matches("app\\.log(\\.\\d{4}-\\d{2}-\\d{2})?"))
//                    .sorted(Comparator.comparing(Path::getFileName).reversed()) // Sort in reverse order by file name (including the date suffix)
//                    .collect(Collectors.toList());
//
//            return logFiles;
//        } catch (IOException e) {
//            logger.error("Error listing log files from directory: {}", e.getMessage());
//            return Collections.emptyList();
//        }
//    }



    private void setupModel1(Model model) {
        // Add data to the model that you want to display on the dashboard.html page
        model.addAttribute("pageTitle", "Dashboard Page");
        model.addAttribute("welcomeMessage", "Welcome to the dashboard!");

        // You can add more attributes as needed for your specific use case
    }




    public List<Activities> getActivitiesForLastWeek(User user) {
        // Calculate the date one week ago from now
        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);

        // Filter activities for the last week
        return user.getActivities().stream()
                .filter(activity -> activity.getActivityDate().isAfter(oneWeekAgo))
                .collect(Collectors.toList());
    }
    public List<SleepDuration> getSleepDurationsForLastWeek(User user) {
        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);

        return user.getSleepDurations().stream()
                .filter(sleep -> sleep.getDateOfSleep().isAfter(oneWeekAgo))
                .collect(Collectors.toList());
    }
//public List<SleepDuration> getSleepDurationsForLastWeek(User user) {
//    LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);
//
//    return user.getSleepDurations().stream()
//            .filter(sleep -> sleep.getDateOfSleep().isAfter(oneWeekAgo))
//            .collect(Collectors.toList());
//}

//    public List<SleepDurationWithAverage> getSleepDurationsWithAverageForLastWeek(User user) {
//        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);
//
//        List<SleepDuration> sleepDurationsForLastWeek = user.getSleepDurations().stream()
//                .filter(sleep -> sleep.getDateOfSleep().isAfter(oneWeekAgo))
//                .collect(Collectors.toList());
//
//        double totalDuration = 0;
//        double totalManualDuration = 0;
//        int numberOfDays = 0;
//
//        for (SleepDuration sleep : sleepDurationsForLastWeek) {
//            totalDuration += sleep.getDuration();
//            totalManualDuration += sleep.getManualDuration();
//            numberOfDays++;
//        }
//
//        double averageDurationPerDay = (totalDuration + totalManualDuration) / numberOfDays;
//
//        // Create SleepDurationWithAverage object
//        SleepDurationWithAverage sleepDurationWithAverage = new SleepDurationWithAverage();
//        sleepDurationWithAverage.setSleepDurations(sleepDurationsForLastWeek);
//        sleepDurationWithAverage.setAverageDurationPerDay(averageDurationPerDay);
//
//        // Return list with only one element as we're combining data
//        return Collections.singletonList(sleepDurationWithAverage);
//    }
    //    public List<WaterEntity> getWaterEntitiesForLastWeek(User user) {
//        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);
//
//        return user.getWaterEntities().stream()
//                .filter(waterEntity -> waterEntity.getLocalDate().isAfter(oneWeekAgo))
//                .collect(Collectors.toList());
//    }
    public List<WaterEntry> getWaterEntriesForLastWeek(User user) {
        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);

        return user.getWaterEntities().stream()
                .flatMap(waterEntity -> waterEntity.getWaterEntries().stream())
                .filter(waterEntry -> waterEntry.getLocalDate().isAfter(oneWeekAgo))
                .collect(Collectors.toList());
    }

    public long getDishCountForDate(User user, LocalDate date) {
        if (user.getDishesList() == null) {
            return 0;
        }

        return user.getDishesList().stream()
                .filter(dish -> dish.getDate().equals(date))
                .count();
    }

    // Update the method signature to accept a list of Dishes
    public String calculateMostFrequentlyConsumedMeal(List<Dishes> meals) {
        // Assuming each Meal has a property 'mealName' indicating the type (breakfast, lunch, dinner, etc.)

        // Count occurrences of each meal type
        Map<String, Long> mealTypeCounts = meals.stream()
                .collect(Collectors.groupingBy(Dishes::getMealName, Collectors.counting()));

        // Find the meal type with the highest count
        Optional<Map.Entry<String, Long>> mostFrequentMealEntry = mealTypeCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        // Get the result
        return mostFrequentMealEntry.map(Map.Entry::getKey).orElse("No data"); // or any default value
    }
    // Add this method to your controller class
    public String calculateMostSkippedMeal(List<Dishes> meals) {
        // Assuming each Meal has a property 'mealName' indicating the type (breakfast, lunch, dinner, etc.)

        // Count occurrences of each meal type
        Map<String, Long> mealTypeCounts = meals.stream()
                .collect(Collectors.groupingBy(Dishes::getMealName, Collectors.counting()));

        // Find the meal type with the lowest count (most skipped)
        Optional<Map.Entry<String, Long>> mostSkippedMealEntry = mealTypeCounts.entrySet().stream()
                .min(Map.Entry.comparingByValue());

        // Get the result
        return mostSkippedMealEntry.map(Map.Entry::getKey).orElse("No data"); // or any default value
    }

    // Add this method to your controller class
    public String calculateMostConsumedDish(List<Dishes> meals) {
        // Assuming each Dishes object has a property 'dishName' indicating the dish name.

        // Count occurrences of each dish
        Map<String, Long> dishCounts = meals.stream()
                .collect(Collectors.groupingBy(Dishes::getDishName, Collectors.counting()));

        // Find the dish with the highest count (most consumed)
        Optional<Map.Entry<String, Long>> mostConsumedDishEntry = dishCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        // Get the result
        return mostConsumedDishEntry.map(Map.Entry::getKey).orElse("No data"); // or any default value
    }






    public String calculateLeastConsumedNutrient(List<Dishes> dishesList) {
        Map<String, Double> totalNutrientIntake = new HashMap<>();

        for (Dishes dish : dishesList) {
            List<Ingredients> ingredients = dish.getIngredientList();

//            for (Ingredients ingredient : ingredients) {
//                NinData ninData = ninDataRepository.findByFoodCode(ingredient.getFoodCode());
//
//                System.out.println("bye  errer nidata v----------------- "+ninData.getEnergy());
//                // Calculate nutrient intake based on ingredient quantity and NinData
//                double nutrientIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getEnergy();
//                totalNutrientIntake.merge("Energy", nutrientIntake, Double::sum);
//
//                nutrientIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getProtein();
//                totalNutrientIntake.merge("Proteins", nutrientIntake, Double::sum);
//
//                nutrientIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getCarbohydrate();
//                totalNutrientIntake.merge("Carbs", nutrientIntake, Double::sum);
//
//                nutrientIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getTotal_Fat();
//                totalNutrientIntake.merge("Fats", nutrientIntake, Double::sum);
//
//                nutrientIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getTotal_Dietary_Fibre();
//                totalNutrientIntake.merge("Fibers", nutrientIntake, Double::sum);
//            }
            for (Ingredients ingredient : ingredients) {
                NinData ninData = ninDataRepository.findByFoodCode(ingredient.getFoodCode());

                // Check if NinData is null
                if (ninData == null) {
                    // Handle the case when NinData is not found for an ingredient
                    continue; // Skip this ingredient and move to the next one
                }

                // Calculate nutrient intake based on ingredient quantity and NinData
                double nutrientIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getEnergy();
                totalNutrientIntake.merge("Energy", nutrientIntake, Double::sum);

                nutrientIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getProtein();
                totalNutrientIntake.merge("Proteins", nutrientIntake, Double::sum);

                nutrientIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getCarbohydrate();
                totalNutrientIntake.merge("Carbs", nutrientIntake, Double::sum);

                nutrientIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getTotal_Fat();
                totalNutrientIntake.merge("Fats", nutrientIntake, Double::sum);

                nutrientIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getTotal_Dietary_Fibre();
                totalNutrientIntake.merge("Fibers", nutrientIntake, Double::sum);
            }
        }

        // Find the least consumed nutrient
        Map.Entry<String, Double> leastConsumedNutrientEntry = totalNutrientIntake.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .orElse(null);

        // Return the result
        return leastConsumedNutrientEntry != null ?
                leastConsumedNutrientEntry.getKey() :
                "No data";
    }


    public String calculateMostConsumedProteinRichDiet(List<Dishes> dishesList) {
        Map<String, Double> proteinIntakePerDiet = new HashMap<>();

        for (Dishes dish : dishesList) {
            List<Ingredients> ingredients = dish.getIngredientList();
            double totalProteinIntake = 0.0;

            // Consider direct ingredients
//            for (Ingredients ingredient : ingredients) {
//                NinData ninData = ninDataRepository.findByFoodCode(ingredient.getFoodCode());
//
//                // Calculate protein intake based on ingredient quantity and NinData
//                double proteinIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getProtein();
//                totalProteinIntake += proteinIntake;
//            }
            for (Ingredients ingredient : ingredients) {
                NinData ninData = ninDataRepository.findByFoodCode(ingredient.getFoodCode());

                // Check if NinData is null
                if (ninData == null) {
                    // Handle the case when NinData is not found for an ingredient
                    continue; // Skip this ingredient and move to the next one
                }

                // Calculate protein intake based on ingredient quantity and NinData
                double proteinIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getProtein();
                totalProteinIntake += proteinIntake;
            }


            // Consider recipe ingredients
            Recipe recipe = dish.getRecipe();
            if (recipe != null) {
                // Add protein from recipe directly
                totalProteinIntake += (recipe.getProtein()/100)*dish.getDishQuantity();
            }

            // Add total protein intake for the dish to the map
            proteinIntakePerDiet.put(dish.getDishName(), totalProteinIntake);
        }

        // Find the dish with the highest total protein intake
        Map.Entry<String, Double> mostProteinRichDietEntry = proteinIntakePerDiet.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        // Return the result
        return mostProteinRichDietEntry != null ?
                mostProteinRichDietEntry.getKey() :
                "No data";
    }
    public String calculateMostConsumedIronRichDiet(List<Dishes> dishesList) {
        Map<String, Double> ironIntakePerDiet = new HashMap<>();

        for (Dishes dish : dishesList) {
            List<Ingredients> ingredients = dish.getIngredientList();
            double totalIronIntake = 0.0;

            // Consider direct ingredients
//            for (Ingredients ingredient : ingredients) {
//                NinData ninData = ninDataRepository.findByFoodCode(ingredient.getFoodCode());
//
//                // Calculate iron intake based on ingredient quantity and NinData
//                double ironIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getIron();
//                totalIronIntake += ironIntake;
//            }

            for (Ingredients ingredient : ingredients) {
                NinData ninData = ninDataRepository.findByFoodCode(ingredient.getFoodCode());

                // Check if NinData is null
                if (ninData == null) {
                    // Handle the case when NinData is not found for an ingredient
                    continue; // Skip this ingredient and move to the next one
                }

                // Calculate iron intake based on ingredient quantity and NinData
                double ironIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getIron();
                totalIronIntake += ironIntake;
            }

            // Consider recipe ingredients
            Recipe recipe = dish.getRecipe();
            if (recipe != null) {
                // Add iron from recipe directly
                totalIronIntake += (recipe.getIron()/100)*dish.getDishQuantity();
            }

            // Add total iron intake for the dish to the map
            ironIntakePerDiet.put(dish.getDishName(), totalIronIntake);
        }

        // Find the dish with the highest total iron intake
        Map.Entry<String, Double> mostIronRichDietEntry = ironIntakePerDiet.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        // Return the result
        return mostIronRichDietEntry != null ?
                mostIronRichDietEntry.getKey() :
                "No data";
    }

    public String calculateMostConsumedCalciumRichDiet(List<Dishes> dishesList) {
        Map<String, Double> calciumIntakePerDiet = new HashMap<>();

        for (Dishes dish : dishesList) {
            List<Ingredients> ingredients = dish.getIngredientList();
            double totalCalciumIntake = 0.0;

            // Consider direct ingredients
//            for (Ingredients ingredient : ingredients) {
//                NinData ninData = ninDataRepository.findByFoodCode(ingredient.getFoodCode());
//
//                // Calculate calcium intake based on ingredient quantity and NinData
//                double calciumIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getCalcium();
//                totalCalciumIntake += calciumIntake;
//            }
            for (Ingredients ingredient : ingredients) {
                NinData ninData = ninDataRepository.findByFoodCode(ingredient.getFoodCode());

                // Check if NinData is null
                if (ninData == null) {
                    // Handle the case when NinData is not found for an ingredient
                    continue; // Skip this ingredient and move to the next one
                }

                // Calculate calcium intake based on ingredient quantity and NinData
                double calciumIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getCalcium();
                totalCalciumIntake += calciumIntake;
            }


            // Consider recipe ingredients
            Recipe recipe = dish.getRecipe();
            if (recipe != null) {
                // Add calcium from recipe directly
                totalCalciumIntake += (recipe.getCalcium() / 100) * dish.getDishQuantity();
            }

            // Add total calcium intake for the dish to the map
            calciumIntakePerDiet.put(dish.getDishName(), totalCalciumIntake);
        }

        // Find the dish with the highest total calcium intake
        Map.Entry<String, Double> mostCalciumRichDietEntry = calciumIntakePerDiet.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        // Return the result
        return mostCalciumRichDietEntry != null ?
                mostCalciumRichDietEntry.getKey() :
                "No data";
    }

    public String calculateMostConsumedCalorieRichDiet(List<Dishes> dishesList) {
        Map<String, Double> calorieIntakePerDiet = new HashMap<>();

        for (Dishes dish : dishesList) {
            List<Ingredients> ingredients = dish.getIngredientList();
            double totalCalorieIntake = 0.0;

            // Consider direct ingredients
//            for (Ingredients ingredient : ingredients) {
//                NinData ninData = ninDataRepository.findByFoodCode(ingredient.getFoodCode());
//
//                // Calculate calorie intake based on ingredient quantity and NinData
//                double calorieIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getEnergy();
//                totalCalorieIntake += calorieIntake;
//            }
            for (Ingredients ingredient : ingredients) {
                NinData ninData = ninDataRepository.findByFoodCode(ingredient.getFoodCode());

                // Check if NinData is null
                if (ninData == null) {
                    // Handle the case when NinData is not found for an ingredient
                    continue; // Skip this ingredient and move to the next one
                }

                // Calculate calorie intake based on ingredient quantity and NinData
                double calorieIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getEnergy();
                totalCalorieIntake += calorieIntake;
            }


            // Consider recipe ingredients
            Recipe recipe = dish.getRecipe();
            if (recipe != null) {
                // Add calorie from recipe directly
                totalCalorieIntake += (recipe.getEnergy_joules() / 100) * dish.getDishQuantity();
            }

            // Add total calorie intake for the dish to the map
            calorieIntakePerDiet.put(dish.getDishName(), totalCalorieIntake);
        }

        // Find the dish with the highest total calorie intake
        Map.Entry<String, Double> mostCalorieRichDietEntry = calorieIntakePerDiet.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        // Return the result
        return mostCalorieRichDietEntry != null ?
                mostCalorieRichDietEntry.getKey() :
                "No data";
    }


    public String calculateMostConsumedCHORichDiet(List<Dishes> dishesList) {
        Map<String, Double> choIntakePerDiet = new HashMap<>();

        for (Dishes dish : dishesList) {
            List<Ingredients> ingredients = dish.getIngredientList();
            double totalCHOIntake = 0.0;

            // Consider direct ingredients
//            for (Ingredients ingredient : ingredients) {
//                NinData ninData = ninDataRepository.findByFoodCode(ingredient.getFoodCode());
//
//                // Calculate CHO intake based on ingredient quantity and NinData
//                double choIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getCarbohydrate();
//                totalCHOIntake += choIntake;
//            }
            for (Ingredients ingredient : ingredients) {
                NinData ninData = ninDataRepository.findByFoodCode(ingredient.getFoodCode());

                // Check if NinData is null
                if (ninData == null) {
                    // Handle the case when NinData is not found for an ingredient
                    continue; // Skip this ingredient and move to the next one
                }

                // Calculate calorie intake based on ingredient quantity and NinData
                double calorieIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getCarbohydrate();
                totalCHOIntake += calorieIntake;
            }


            // Consider recipe ingredients
            Recipe recipe = dish.getRecipe();
            if (recipe != null) {
                // Add CHO from recipe directly
                totalCHOIntake += (recipe.getCarbohydrate() / 100) * dish.getDishQuantity();
            }

            // Add total CHO intake for the dish to the map
            choIntakePerDiet.put(dish.getDishName(), totalCHOIntake);
        }

        // Find the dish with the highest total CHO intake
        Map.Entry<String, Double> mostCHORichDietEntry = choIntakePerDiet.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        // Return the result
        return mostCHORichDietEntry != null ?
                mostCHORichDietEntry.getKey() :
                "No data";
    }


    public String calculateMostConsumedMeal(List<Dishes> meals, String mealType) {
        // Assuming each Dishes object has a property 'mealType' indicating the type (breakfast, lunch, dinner, snacks, etc.)

        // Filter meals for the specified meal type
        List<Dishes> filteredMeals = meals.stream()
                .filter(meal -> mealType.equalsIgnoreCase(meal.getMealName()))
                .collect(Collectors.toList());

        // Count occurrences of each dish for the specified meal type
        Map<String, Long> dishCounts = filteredMeals.stream()
                .collect(Collectors.groupingBy(Dishes::getDishName, Collectors.counting()));

        // Find the dish with the highest count (most consumed)
        Optional<Map.Entry<String, Long>> mostConsumedEntry = dishCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        // Get the result
        return mostConsumedEntry.map(Map.Entry::getKey).orElse("No data"); // or any default value
    }

    //    public String calculateMostConsumedDrink(List<WaterEntity> drinks) {
//        // Count occurrences of each drink
//        Map<String, Double> drinkIntake = drinks.stream()
//                .collect(Collectors.groupingBy(WaterEntity::getDrinkName, Collectors.summingDouble(WaterEntity::getWaterIntake)));
//
//        // Find the drink with the highest total intake (most consumed)
//        Optional<Map.Entry<String, Double>> mostConsumedDrinkEntry = drinkIntake.entrySet().stream()
//                .max(Map.Entry.comparingByValue());
//
//        // Get the result
//        return mostConsumedDrinkEntry.map(entry -> entry.getValue() + " liters)").orElse("No data");
//    }
    public String calculateMostConsumedDrink(List<WaterEntry> drinks) {
        // Count occurrences of each drink
        Map<String, Double> drinkIntake = drinks.stream()
                .collect(Collectors.groupingBy(WaterEntry::getDrinkName, Collectors.summingDouble(WaterEntry::getWaterIntake)));

        // Find the drink with the highest total intake (most consumed)
        Optional<Map.Entry<String, Double>> mostConsumedDrinkEntry = drinkIntake.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        // Get the result
        return mostConsumedDrinkEntry.map(entry -> entry.getKey() + " (" + entry.getValue() + " liters)").orElse("No data");
    }

    public String calculateMostConsumedNutrient(List<Dishes> dishesList) {
        Map<String, Double> totalNutrientIntake = new HashMap<>();

        for (Dishes dish : dishesList) {
            List<Ingredients> ingredients = dish.getIngredientList();

//            for (Ingredients ingredient : ingredients) {
//                NinData ninData = ninDataRepository.findByFood(ingredient.getIngredientName());
//
//                // Calculate nutrient intake based on ingredient quantity and NinData
//                double nutrientIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getEnergy();
//                totalNutrientIntake.merge("Energy", nutrientIntake, Double::sum);
//
//                nutrientIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getProtein();
//                totalNutrientIntake.merge("Proteins", nutrientIntake, Double::sum);
//
//                nutrientIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getCarbohydrate();
//                totalNutrientIntake.merge("Carbs", nutrientIntake, Double::sum);
//
//                nutrientIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getTotal_Fat();
//                totalNutrientIntake.merge("Fats", nutrientIntake, Double::sum);
//
//                nutrientIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getTotal_Dietary_Fibre();
//                totalNutrientIntake.merge("Fibers", nutrientIntake, Double::sum);
//            }
            for (Ingredients ingredient : ingredients) {
                NinData ninData = ninDataRepository.findByFoodCode(ingredient.getFoodCode());
                System.out.println("hsdhs ___________"+ninData);;
                // Check if NinData is null
                if (ninData == null) {
                    // Handle the case when NinData is not found for an ingredient
                    continue; // Skip this ingredient and move to the next one
                }
                System.out.println("hsdhs Energyyyyyyyy___________"+ninData.getEnergy());;
                // Calculate nutrient intake based on ingredient quantity and NinData
                double nutrientIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getEnergy();
                totalNutrientIntake.merge("Energy", nutrientIntake, Double::sum);

                nutrientIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getProtein();
                totalNutrientIntake.merge("Proteins", nutrientIntake, Double::sum);

                nutrientIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getCarbohydrate();
                totalNutrientIntake.merge("Carbs", nutrientIntake, Double::sum);

                nutrientIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getTotal_Fat();
                totalNutrientIntake.merge("Fats", nutrientIntake, Double::sum);

                nutrientIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getTotal_Dietary_Fibre();
                totalNutrientIntake.merge("Fibers", nutrientIntake, Double::sum);
            }

        }

        // Find the most consumed nutrient
        Map.Entry<String, Double> mostConsumedNutrientEntry = totalNutrientIntake.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        // Return the result
        return mostConsumedNutrientEntry != null ?
                mostConsumedNutrientEntry.getKey() :
                "No data";
    }

    public TotalUsersDTO getTotalUsers() {
        TotalUsersDTO totalUsersDTO = new TotalUsersDTO();

        // Fetch total number of users
        long totalUsers = userRepository.count();
        totalUsersDTO.setTotalUsers(totalUsers);

        return totalUsersDTO;
    }

}
