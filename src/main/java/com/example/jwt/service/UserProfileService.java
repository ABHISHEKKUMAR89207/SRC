package com.example.jwt.service;

import com.example.jwt.entities.FoodToday.ear.Ear;
import com.example.jwt.entities.FoodToday.ear.EarRepository;
import com.example.jwt.entities.FoodToday.ear.EarResponse;
import com.example.jwt.entities.User;
import com.example.jwt.entities.UserProfile;
import com.example.jwt.entities.UserProfileResponse;
import com.example.jwt.entities.activityType.ActivityType;
import com.example.jwt.entities.activityType.ActivityTypeRepository;
import com.example.jwt.entities.activityType.ActivityTypeService;
import com.example.jwt.entities.dashboardEntity.healthTrends.AllTarget;
import com.example.jwt.exception.ResourceNotFoundException;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.ActivityRepository;
import com.example.jwt.repository.UserProfileRepository;
import com.example.jwt.repository.UserRepository;

import com.example.jwt.repository.repositoryHealth.AllTargetRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public Iterable<UserProfile> getAllUserProfiles() {
        return userProfileRepository.findAll();
    }

    public Optional<UserProfile> getUserProfileById(Long id) {
        return userProfileRepository.findById(id);
    }

    public Optional<UserProfile> findById(Long id) {
        return userProfileRepository.findById(id);
    }

    public UserProfile saveOrUpdateVariableType(String username, String variableType) {
        // Fetch the UserProfile based on the userId
        Optional<UserProfile> userProfileOptional = Optional.ofNullable(userProfileRepository.findByUserEmail(username));

        UserProfile userProfile = userProfileOptional.orElseThrow(() ->
                new UserNotFoundException("User with email " + username + " not found"));

        // Update the variableType
        userProfile.setVariableType(variableType);

        // Save and return the updated UserProfile
        return userProfileRepository.save(userProfile);
    }

    @Autowired
    private ActivityTypeService activityTypeService;
    @Autowired
    private ActivityTypeRepository activityTypeRepository;
    // to create the user profile
//    public UserProfile createUserProfile(UserProfile userProfile, Long userId) throws ParseException {
//        User user = this.userRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("User Profile"));
//
//
//        // Set the user reference in the UserProfile
//        userProfile.setUser(user);
//
//        // Calculate and set the BMI
//        double heightInInches = userProfile.getHeightFt() * 12 + userProfile.getHeightIn();
//        double heightInDecimal = heightInInches / 12.0; // Convert total inches to feet in decimal format
//         System.out.println("height in decimal .........."+heightInDecimal);
//        double bmi = calculateBMI(userProfile.getGender(),userProfile.getHeightFt(),userProfile.getHeightIn(), userProfile.getWeight());
//        userProfile.setBmi(bmi);
//
////        userProfile = activityTypeService.updateActivityType(user.getUserId(), userProfile.getOccupation());
//
////        UserProfile userProfile = activityTypeService.updateActivityType(user.getUserId(), updateActivityTypeDTO.getOccupation());
//
//        UserProfile newProfile = this.userProfileRepository.save(userProfile);
//        return newProfile;

    @Autowired
    private AllTargetRepository allTargetRepository;
//    }
    public UserProfile createUserProfile(UserProfile userProfile, Long userId) throws ParseException {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Profile"));

        // Set the user reference in the UserProfile
        userProfile.setUser(user);

        // Calculate and set the BMI
        double heightInInches = userProfile.getHeightFt() * 12 + userProfile.getHeightIn();
        double heightInDecimal = heightInInches / 12.0; // Convert total inches to feet in decimal format
        System.out.println("height in decimal .........."+heightInDecimal);
        double bmi = calculateBMI(userProfile.getGender(), userProfile.getHeightFt(), userProfile.getHeightIn(), userProfile.getWeight());
        userProfile.setBmi(bmi);


        // Calculate and set the BMR
        double bmr = calculateBMR(userProfile);
        userProfile.setBmr(bmr);


//        // Find the ActivityType based on the occupation
//        ActivityType activityType = activityTypeRepository.findByOccupation(userProfile.getOccupation());
//
//        if (activityType == null) {
//            // Handle error: ActivityType not found for the given occupation
//            throw new RuntimeException("ActivityType not found for occupation: " + userProfile.getOccupation());
//        }
//
        // Set the work level based on the ActivityType
//        userProfile.setWorkLevel(activityType.getTypeOfActivity());

        // Calculate water goal and update AllTarget entity
        double waterGoal = ((userProfile.getWeight() * 30)/1000); // Calculate water goal based on weight in litter

        AllTarget existingGoal = allTargetRepository.findByUser(user);
        if (existingGoal == null) {
            // If no goal exists, create a new one
            AllTarget newWaterGoalEntity = new AllTarget();
            newWaterGoalEntity.setWaterGoal(waterGoal);
            newWaterGoalEntity.setUser(user);
            allTargetRepository.save(newWaterGoalEntity);
        } else {
            // If a goal already exists, update the existing one
            existingGoal.setWaterGoal(waterGoal);
            allTargetRepository.save(existingGoal);
        }

        // Save the userProfile
        UserProfile newProfile = this.userProfileRepository.save(userProfile);
        return newProfile;
    }
//    public UserProfile createUserProfile(UserProfile userProfile, Long userId, String occupation) throws ParseException {
//        User user = this.userRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("User Profile"));
//
//        // Set the user reference in the UserProfile
//        userProfile.setUser(user);
//
//        // Calculate and set the BMI
//        double heightInInches = userProfile.getHeightFt() * 12 + userProfile.getHeightIn();
//        double heightInDecimal = heightInInches / 12.0; // Convert total inches to feet in decimal format
//        double bmi = calculateBMI(userProfile.getGender(),userProfile.getHeightFt(),userProfile.getHeightIn(), userProfile.getWeight());
//        userProfile.setBmi(bmi);
//
//        // Set the occupation
//        userProfile = activityTypeService.updateActivityType(user.getUserId(), occupation);
//        userProfile.setOccupation(occupation);
//
//        UserProfile newProfile = this.userProfileRepository.save(userProfile);
//        return newProfile;
//    }


//    public UserProfile updateActivityType(Long userId, String occupation) {
//        UserProfile userProfile = userProfileRepository.findByUserUserId(userId);
//        ActivityType activityType = activityTypeRepository.findByOccupation(occupation);
//
//        if (activityType == null) {
//            // Handle error: ActivityType not found for the given occupation
//            throw new RuntimeException("ActivityType not found for occupation: " + occupation);
//        }
//
//        userProfile.setWorkLevel(activityType.getTypeOfActivity());
//        userProfile.setOccupation(occupation);
//        return userProfileRepository.save(userProfile);
//    }

    // to calculate the BMI of the user
//    private double calculateBMI(double heightInFeet, double weight) {
//        DecimalFormat decimalFormat = new DecimalFormat("#.##");
//        String str = String.valueOf(heightInFeet);
//        String[] parts = str.split("\\.");
//        double befDecimal = Double.parseDouble(parts[0]);
//        double aftDecimal =Double.parseDouble(parts[1]);
//
//        double heightInInches = (befDecimal*12) + aftDecimal;
//        double heightInMeters = heightInInches * 0.0254; // Convert height to meters
//        double bmis = weight / (heightInMeters * heightInMeters);
//        String formatedBmi = decimalFormat.format(bmis);
//        return Double.parseDouble(formatedBmi);
//    }

//    private double calculateBMI(String gender, double heightInFeet, double weight) {
//        // Convert height from feet to meters
//        double heightInMeters = heightInFeet * 0.3048; // 1 foot = 0.3048 meters
//
//        // Calculate BMI based on gender
//        double bmi;
//        if (gender.equalsIgnoreCase("male")) {
//            bmi = weight / (heightInMeters * heightInMeters);
//        } else if (gender.equalsIgnoreCase("female")) {
//            // Adjusted calculation for females
//            bmi = (weight / (heightInMeters * heightInMeters)) * 1.07 - (148 * (weight / heightInMeters)) + 4.5;
//        } else {
//            // Default to a generic BMI calculation if gender is not specified or recognized
//            bmi = weight / (heightInMeters * heightInMeters);
//        }
//        return bmi;
//    }

//    public double calculateBMI(String gender, double heightInFeet, double weight) {
//        // Convert height from feet to centimeters
//        double heightInCM = heightInFeet * 30.48; // 1 foot = 30.48 centimeters
//
//        // Convert height from centimeters to meters for BMI calculation
//        double heightInMeters = heightInCM / 100; // Convert centimeters to meters
//
//        // Calculate BMI based on gender
//        double bmi;
//        if (gender.equalsIgnoreCase("male")) {
//            bmi = weight / (heightInMeters * heightInMeters);
//        } else if (gender.equalsIgnoreCase("female")) {
//            // Adjusted calculation for females
////            bmi = (weight / (heightInMeters * heightInMeters)) * 1.07 - (148 * (weight / heightInMeters)) + 4.5;
//            bmi = weight / (heightInMeters * heightInMeters);
//        } else {
//            // Default to a generic BMI calculation if gender is not specified or recognized
//            bmi = weight / (heightInMeters * heightInMeters);
//        }
//        return bmi;
//    }
//

    public double calculateBMI(String gender, int heightFt, int heightIn, double weight) {
        // Convert height to centimeters
        double heightInCM = (heightFt * 12 + heightIn) * 2.54; // 1 inch = 2.54 centimeters

        // Convert height from centimeters to meters for BMI calculation
        double heightInMeters = heightInCM / 100; // Convert centimeters to meters

        // Calculate BMI based on gender
        double bmi;
        if (gender.equalsIgnoreCase("male")) {
            bmi = weight / (heightInMeters * heightInMeters);
        } else if (gender.equalsIgnoreCase("female")) {
            // Adjusted calculation for females
            // bmi = (weight / (heightInMeters * heightInMeters)) * 1.07 - (148 * (weight / heightInMeters)) + 4.5;
            bmi = weight / (heightInMeters * heightInMeters);
        } else {
            // Default to a generic BMI calculation if gender is not specified or recognized
            bmi = weight / (heightInMeters * heightInMeters);
        }
        return bmi;
    }

    // Method to calculate BMR
    public double calculateBMR(UserProfile userProfile) {
        String gender = userProfile.getGender();
        int age = calculateAgee(userProfile.getDateOfBirth());
        double weight = userProfile.getWeight();
//        double heightInCM = convertToCentimeters(userProfile.getHeightFt(), userProfile.getHeightIn());

        double bmr;

        if (gender.equalsIgnoreCase("male")) {
            if (age >= 18 && age <= 30) {
                bmr = (15.1 * weight) + 692.2;
            } else if (age > 30 && age <= 60) {
                bmr = (11.5 * weight) + 873;
            } else {
                bmr = (11.7 * weight) + 587.7;
            }
            bmr *= 0.9; // Apply activity factor for males
        } else if (gender.equalsIgnoreCase("female")) {
            if (age >= 18 && age <= 30) {
                bmr = (14.8 * weight) + 486.6;
            } else if (age > 30 && age <= 60) {
                bmr = (8.1 * weight) + 845.6;
            } else {
                bmr = (9.1 * weight) + 658.5;
            }
            bmr *= 0.91; // Apply activity factor for females
        } else {
            throw new IllegalArgumentException("Invalid gender specified");
        }
        System.out.println("output of bmr" + bmr);

        return bmr;
    }



    //update user profile
    public UserProfile saveUserProfile(UserProfile userProfile) {
        // This method should save or update the user's profile data in the database
        return userProfileRepository.save(userProfile);
    }
    public UserProfile findByUsername(String username) {
        // Implement the logic to fetch health trends by the user's username
        return userProfileRepository.findByUserEmail(username);
    }


// to update the user profile
    public void updateUserProfile(Long id, UserProfile updatedProfile) {
        Optional<UserProfile> existingProfile = userProfileRepository.findById(id);
        if (existingProfile.isPresent()) {
            UserProfile userProfile = existingProfile.get();
            User user = userProfile.getUser();
            if (user != null) {
                user.setEmail(updatedProfile.getUser().getEmail());
                user.setMobileNo(updatedProfile.getUser().getMobileNo());
                // You can update other fields as well
                userProfileRepository.save(userProfile);
            } else {
                // Handle the case where the user is not associated with the UserProfile
                // Return an error response or take appropriate action
            }
        }
    }



//    public EarResponse getEarGroupAndWorkLevel(UserProfile userProfile) {
//        // Extract gender from profile
//        String gender = userProfile.getGender();
//        System.out.println("User Profile Gender: " + gender);
//
//        // Calculate age from dateOfBirth
//        LocalDate dateOfBirth = userProfile.getDateOfBirth();
//
//
//        String ageString = calculateAge(dateOfBirth);
//
//       String HGroup = earRepository.findHgroupByAge(ageString);
//        System.out.println("H Group ..."+HGroup);
////        String ageString = age + " years onwards";
//        System.out.println("User Profile Age: " + ageString);
//
//        // Find Ear corresponding to age and gender
//        Ear ear = findEars(ageString, gender);
//
//        // Generate response
//        if (ear != null) {
//            System.out.println("Match found in Ear table:");
//            System.out.println("Ear Age from Ear table: " + ear.getAge() + " years onwards");
//            System.out.println("Ear Gender from Ear table: " + ear.getGender());
//            System.out.println("Ear Group from Ear table: " + ear.getHgroup());
//            System.out.println("Ear Work Level from Ear table: " + ear.getWorkLevel());
//
//            // Create an instance of EarResponse and return it
////            return new EarResponse(gender, ageString, ear.getHgroup(), ear.getWorkLevel());
//            return new EarResponse(gender, ageString, ear.getHgroup(), ear.getWorkLevel(),
//                    ear.getEnergy(), ear.getProtein(), ear.isFatsOilsVisible(),
//                    ear.getFiber(), ear.getCalcium(), ear.getMagnesium(),
//                    ear.getIron(), ear.getZinc(), ear.getThiamine(),
//                    ear.getRiboflavin(), ear.getNiacin());
//        } else {
//            // Return null or an appropriate default EarResponse instance
//            return null;
//        }
//    }

//    public Ear findEars(String age, String gender, String HGroup) {
////    List<Ear> ears = earRepository.findEarsByAgeAndGender(age, String.valueOf(Integer.MAX_VALUE), gender);
//        List<Ear> ears = earRepository.findEarsByGender(gender);
//
//        System.out.println("List of Ears ...." + ears);
//
//        for (Ear ear : ears) {
//            String earAge = ear.getAge();
//            System.out.println("ear Age....." + earAge);
//            System.out.println("Comparing with Ear table - Age: " + earAge + " Gender: " + ear.getGender());
//
//            // Assuming Hgroup and workLevel are properties in Ear class
////            String hgroup = ear.getHgroup();
//            String workLevel = ear.getWorkLevel();
//
////            System.out.println("Hgroup: " + hgroup);
//            System.out.println("Work Level: " + workLevel);
//
//            // Check age and gender conditions
////            if (isAgeInRange(earAge, "18", String.valueOf(Integer.MAX_VALUE)) &&
////                    ear.getGender().equalsIgnoreCase(gender)) {
////                // Return the first matching Ear
////                return ear;
////            }
//            if ("6 months".equals(age) && "Children".equals(HGroup))
//            {
//                return ear;
//            }
////            else if (isAgeInRange(earAge, "18", String.valueOf(Integer.MAX_VALUE)) &&
////                    ear.getGender().equalsIgnoreCase(gender)) {
////                // Return the first matching Ear
////                return ear;
////            }
//        }
//
//        // If no matching Ear is found, return null
//        return null;
//    }
public Ear findEars(String age, String gender, String HGroup) {
    List<Ear> ears = earRepository.findEarsByHgroup(HGroup); // Assuming you have a method to find by HGroup

    System.out.println("List of Ears ...." + ears);

    for (Ear ear : ears) {
        String earAge = ear.getAge();
        System.out.println("ear Age....." + earAge);
        System.out.println("Comparing with Ear table - Age: " + earAge + " Gender: " + ear.getGender());

        String workLevel = ear.getWorkLevel();
        System.out.println("Work Level: " + workLevel);

        // Check age and gender conditions
        if (isAgeInRange(earAge, "18 years onwards", "18 years onwards") &&
                ear.getGender().equalsIgnoreCase(gender)) {
            // Print matching details
            System.out.println("Match found in Ear table:");
            System.out.println("Ear Age from Ear table: " + earAge + " years onwards");
            System.out.println("Ear Gender from Ear table: " + ear.getGender());
            System.out.println("Ear Group from Ear table: " + ear.getHgroup());
            System.out.println("Ear Work Level from Ear table: " + workLevel);

            // Return the first matching Ear
            return ear;
        }
    }

    // If no matching Ear is found, return null
    return null;
}

//    public EarResponse getEarGroupAndWorkLevel(UserProfile userProfile) {
//        // Extract gender from profile
//        String gender = userProfile.getGender();
//        System.out.println("User Profile Gender: " + gender);
//
//        // Calculate age from dateOfBirth
//        LocalDate dateOfBirth = userProfile.getDateOfBirth();
//        String ageString = calculateAge(dateOfBirth);
//
//        String workLevel = userProfile.getWorkLevel();
//
//
//        // Find HGroup based on ageString
//        String HGroup = earRepository.findHgroupByGender(gender);
//        System.out.println("H Group: " + HGroup);
//        System.out.println("User Profile Age: " + ageString);
////        Ear ear = findEars(ageString, gender, HGroup);
//
//        List<Ear> allByAgeAndHgroupAndWorkLevel = earRepository.findAllByAgeAndHgroupAndWorkLevel(ageString, HGroup, workLevel);
//
////        // Generate response based on HGroup and ageString
////        if (ear != null) {
////            System.out.println("Match found in Ear table:");
////            System.out.println("Ear Age from Ear table: " + ear.getAge() + " years onwards");
////            System.out.println("Ear Gender from Ear table: " + ear.getGender());
////            System.out.println("Ear Group from Ear table: " + HGroup);
////            System.out.println("Ear Work Level from Ear table: " + ear.getWorkLevel());
////
////            // Check specifically for age "6 months" and HGroup "Children"
////            if ("6 months".equals(ageString) && "Children".equals(HGroup)) {
////                // Return specific nutrient values directly from Ear table
////                return new EarResponse(gender, ageString, HGroup, ear.getWorkLevel(),
////                        ear.getEnergy(), ear.getProtein(), ear.isFatsOilsVisible(),
////                        ear.getFiber(), ear.getCalcium(), ear.getMagnesium(),
////                        ear.getIron(), ear.getZinc(), ear.getThiamine(),
////                        ear.getRiboflavin(), ear.getNiacin());
////            } else {
////                // Handle other cases if needed
////
////                // You might want to add more conditions here for other ageString and HGroup values
////            }
////        }
//
//        // Return null or an appropriate default EarResponse instance
//        return null;
//    }

    public EarResponse getEarGroupAndWorkLevel(UserProfile userProfile) {
        // Extract gender from profile
        String gender = userProfile.getGender();
        System.out.println("User Profile Gender: " + gender);

        // Calculate age from dateOfBirth
        LocalDate dateOfBirth = userProfile.getDateOfBirth();
        int age = calculateAgee(dateOfBirth);
        System.out.println("Age calculated from user profile: " + age);

        // String workLevel = userProfile.getWorkLevel();
//        String workLevel = "NoLeavel";
//        System.out.println("Work level from user profile: " + workLevel);


        // Calculate age from dateOfBirth
        LocalDate dateOfBirth1 = userProfile.getDateOfBirth();
        String ageString = calculateAge(dateOfBirth1);
        System.out.println("Age String calculated from user profile ......"+ageString);

        List<Ear> matchingEars = new ArrayList<>();

        if (age < 18) {
            String workLevel1 = "NoLeavel";
            matchingEars = earRepository.findAllByAgeAndGenderAndWorkLevel(ageString, gender, workLevel1);
        } else if(age>60){
            String workLevel2 = "NoLeavel";
            matchingEars = earRepository.findAllByGenderAndWorkLevelAndAge(gender,workLevel2,ageString);

        }
        else {
//            String workLevel = "Sedentary";
            // Find HGroup based on age
            String workLevel = userProfile.getWorkLevel();
            System.out.println("Get workLevel from User Profile... "+workLevel);
            String hGroup = earRepository.findHgroupByGenderAndWorkLevel(gender, workLevel);
            System.out.println("Fetching H Group based on user profile gender and work level: " + hGroup);

            // Retrieve ears based on age, HGroup, and workLevel
            matchingEars = earRepository.findAllByHgroupAndWorkLevel(hGroup, workLevel);
        }

        // Generate response based on matching ears
        if (!matchingEars.isEmpty()) {
            System.out.println("Match found in Ear table:");

            // Assuming you want to consider the first matching Ear
            Ear matchingEar = matchingEars.get(0);

            // Print matching details
            System.out.println("Ear Age from Ear table: " + matchingEar.getAge() + " years onwards");
            System.out.println("Ear Gender from Ear table: " + matchingEar.getGender());
            System.out.println("Ear Work Level from Ear table: " + matchingEar.getWorkLevel());

            // Return specific nutrient values directly from Ear table
            return new EarResponse(matchingEar.getGender(), matchingEar.getAge(), matchingEar.getHgroup(), matchingEar.getWorkLevel(),
                    matchingEar.getEnergy(), matchingEar.getProtein(), matchingEar.getFats(),
                    matchingEar.getFiber(), matchingEar.getCalcium(), matchingEar.getMagnesium(),
                    matchingEar.getIron(), matchingEar.getZinc(), matchingEar.getThiamine(),
                    matchingEar.getRiboflavin(), matchingEar.getNiacin(),matchingEar.getFolate(),matchingEar.getVitA(), matchingEar.getCarbohyderate());
        }

        // Return null or an appropriate default EarResponse instance
        return null;
    }

//    public EarResponse getEarGroupAndWorkLevel(UserProfile userProfile) {
//        // Extract gender from profile
//        String gender = userProfile.getGender();
//        System.out.println("User Profile Gender: " + gender);
//
//        LocalDate dateOfBirth1 = userProfile.getDateOfBirth();
//        String ageString1 = calculateAge(dateOfBirth1);
//
//
//        // Calculate age from dateOfBirth
//        LocalDate dateOfBirth = userProfile.getDateOfBirth();
//        String ageString = calculateAge(dateOfBirth);
//        System.out.println("Age String calculated from user profile ......"+ageString);
//
////        String workLevel = userProfile.getWorkLevel();
//        String workLevel = "NoLeavel";
//        System.out.println("work level from user profile.... "+workLevel);
//
//
//
//
//        // Find HGroup based on ageString
//// Find HGroup based on ageString
////        String hGroup = earRepository.findHgroupByGenderAndWorkLevel(gender, workLevel);
////        System.out.println("Fetching H Group based on user profile gender and work level: " + hGroup);
//
//// Retrieve ears based on age, HGroup, and workLevel
//        List<Ear> matchingEars = earRepository.findAllByAgeAndGenderAndWorkLevel(ageString, gender, workLevel);
////        List<Ear> matchingEars = earRepository.findAllByHgroupAndWorkLevel( hGroup, workLevel);
//
//// Generate response based on matching ears
//        if (!matchingEars.isEmpty()) {
//            System.out.println("Match found in Ear table:");
//
//            // Assuming you want to consider the first matching Ear
//            Ear matchingEar = matchingEars.get(0);
//
//            // Print matching details
//            System.out.println("Ear Age from Ear table: " + matchingEar.getAge() + " years onwards");
//            System.out.println("Ear Gender from Ear table: " + matchingEar.getGender());
////            System.out.println("Ear Group from Ear table: " + hGroup);
//            System.out.println("Ear Work Level from Ear table: " + matchingEar.getWorkLevel());
//
//            // Return specific nutrient values directly from Ear table
//            return new EarResponse(matchingEar.getGender(), matchingEar.getAge(), matchingEar.getHgroup(), matchingEar.getWorkLevel(),
//                    matchingEar.getEnergy(), matchingEar.getProtein(), matchingEar.isFatsOilsVisible(),
//                    matchingEar.getFiber(), matchingEar.getCalcium(), matchingEar.getMagnesium(),
//                    matchingEar.getIron(), matchingEar.getZinc(), matchingEar.getThiamine(),
//                    matchingEar.getRiboflavin(), matchingEar.getNiacin());
//        }
//
//        // Return null or an appropriate default EarResponse instance
//        return null;
//    }


    private int calculateAgee(LocalDate dateOfBirth) {
        LocalDate currentDate = LocalDate.now();
        return Period.between(dateOfBirth, currentDate).getYears();
    }
//private String calculateAge(LocalDate dateOfBirth) {
//    LocalDate currentDate = LocalDate.now();
//    Period period = Period.between(dateOfBirth, currentDate);
//
//    int years = period.getYears();
//    int months = period.getMonths();
//
//    if (years == 0) {
//        if (months == 0) {
//            return "0 years"; // If both years and months are zero
//        } else {
//            return months + " months"; // If years is zero but there are some months
//        }
//    } else {
//        return years + " years onwards"; // If there are some years
//    }
//}

    private String calculateAge(LocalDate dateOfBirth) {
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(dateOfBirth, currentDate);

        int years = period.getYears();
        int months = period.getMonths();

        if (years == 0) {
            if (months == 0) {
                return "0 years"; // If both years and months are zero
            } else {
                return months + " months"; // If years is zero but there are some months
            }
        } else if (years < 18) {
            return years + " years"; // If there are some years, but less than 18
        } else if (years >= 60) {
            return "above " + 60 + " years"; // If the age is 60 years or more
        } else {
            return years + " years onwards"; // If there are some years and at least 18 but less than 60
        }
    }



    @Autowired
    private EarRepository earRepository;



    private boolean isAgeInRange(String age, String minAge, String maxAge) {
        // Extract numeric age from the string
        int ageInt;
        try {
            ageInt = Integer.parseInt(age.split(" ")[0]);
        } catch (NumberFormatException e) {
            // Handle the case where the age cannot be parsed
            return false;
        }

        int minAgeInt = Integer.parseInt(minAge);
        int maxAgeInt = Integer.parseInt(maxAge);

        return ageInt >= minAgeInt && ageInt <= maxAgeInt;
    }






//    public UserProfileResponse getUserProfileDetails(Long userId) {
//        UserProfile userProfile = userProfileRepository.findByUserUserId(userId);
//
//        if (userProfile == null) {
//            // Handle error: User not found
//            throw new RuntimeException("User not found for userId: " + userId);
//        }
//
//        // Check for null values and handle it appropriately
//        if (userProfile.getWorkLevel() == null || userProfile.getOccupation() == null) {
//            // You can throw an exception or return a default response
//            throw new RuntimeException("WorkLevel or Occupation is null for userId: " + userId);
//        }
//
//        return new UserProfileResponse(userProfile.getWorkLevel(), userProfile.getOccupation());
//    }


}

