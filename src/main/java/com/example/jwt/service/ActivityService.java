    package com.example.jwt.service;


    import com.example.jwt.entities.User;
    import com.example.jwt.entities.UserProfile;
    import com.example.jwt.entities.dashboardEntity.Activities;
    import com.example.jwt.repository.ActivityRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.time.LocalDate;
    import java.util.ArrayList;
    import java.util.List;

    @Service
    public class ActivityService {
        @Autowired
        private ActivityRepository activityRepository;

        @Autowired
        private UserService userService;

//        public static final double STEP_CALORIE_BURN_RATE = 0.05; // Example value (calories per step)
        double metValue = 0.0;
        boolean flag = true;
        double strideLength = 0.0;




        private Double calculateCaloriesBurnt(String activityType, int steps, String gender) {

            double metValue = 0.0;
            double stepCalorieBurnRate = 0.035;
            double caloriesBurn = 0.0;
            if (activityType != null) {
                flag = false;
                if ("Male".equalsIgnoreCase(gender)) {
                    if ("Running".equalsIgnoreCase(activityType)) {
                        metValue = 7.0;
                        System.out.println("male by Running");
                    } else if ("Jogging".equalsIgnoreCase(activityType)) {
                        metValue = 6.0;
                        System.out.println("male by Jogging");
                    }
                } else if ("Female".equalsIgnoreCase(gender)) {
                    if ("Running".equalsIgnoreCase(activityType)) {
                        metValue = 7.0;
                        System.out.println("Female by Running");
                    } else if ("Jogging".equalsIgnoreCase(activityType)) {
                        metValue = 5.0;
                        System.out.println("Female by Jogging");
                    }
                }
                caloriesBurn = metValue * (steps / 120.0);

            }
            return caloriesBurn;
        }


        //calculate and get by user
        public double calculateCaloriesBurnedByDuration(String activityType, LocalDate activityDate, User user) {
            Activities userActivity = activityRepository.findActivityByUserAndActivityDateAndActivityType(user, activityDate, activityType);

            if (userActivity != null) {
                double metValue = 0.0; // Set the MET value for each activity type

                if ("Running".equalsIgnoreCase(activityType)) {
                    metValue = user.getUserProfile().getGender().equalsIgnoreCase("Male") ? 9.8 : 8.0; // MET value for running (adjust as needed for male and female)
                } else if ("Jogging".equalsIgnoreCase(activityType)) {
                    metValue = user.getUserProfile().getGender().equalsIgnoreCase("Male") ? 7.0 : 6.0; // MET value for jogging (adjust as needed for male and female)
                }

                // Calculate calories burned using the formula: Calories = MET x Duration (in hours) x Weight (in kg)
                double durationInSeconds = 0.0; // Initialize duration to 0

                if ("Running".equalsIgnoreCase(activityType)) {
                    durationInSeconds = userActivity.getRunningDurationInSeconds();
                } else if ("Jogging".equalsIgnoreCase(activityType)) {
                    durationInSeconds = userActivity.getJoggingDurationInSeconds();
                }

                double durationInHours = durationInSeconds / 3600.0; // Convert seconds to hours

                // Get the user's weight from the User entity
                double weightKg = user.getUserProfile().getWeight();

                return metValue * durationInHours * weightKg;
            }

            return 0.0; // Return 0 if no activity record is found
        }






        //create and update by activity
//        public void recordActivity(User user, Activities activity) {
//
//
//            if (user != null && activity.getActivityType() != null) {
//                LocalDate currentDate = LocalDate.now();
//                // Check if an activity of the same type exists for the user and date
//                Activities existingActivity = activityRepository.findByUserAndActivityTypeAndActivityDate(user, activity.getActivityType(), currentDate);
//
//                if (existingActivity != null) {
//                    // Update the existing activity record for the same date
//                    if ("Running".equalsIgnoreCase(activity.getActivityType())) {
//                        existingActivity.setRunningDurationInSeconds(existingActivity.getRunningDurationInSeconds() + activity.getRunningDurationInSeconds());
//                    } else if ("Jogging".equalsIgnoreCase(activity.getActivityType())) {
//                        existingActivity.setJoggingDurationInSeconds(existingActivity.getJoggingDurationInSeconds() + activity.getJoggingDurationInSeconds());
//                    }
//
//                    // Save the updated activity record
//                    activityRepository.save(existingActivity);
//                } else {
//                    // Create a new activity record for the current date
//                    activity.setUser(user);
//                    activity.setActivityDate(currentDate);
//                    activityRepository.save(activity);
//                }
//            }
//        }

        //create and update by activity
//        public void recordActivity(Long userId, Activities activity) {
//            User user = userService.getUserById(userId);
//
//            if (user != null && activity.getActivityType() != null) {
//                LocalDate currentDate = LocalDate.now();
//                // Check if an activity of the same type exists for the user and date
//                Activities existingActivity = activityRepository.findByUserAndActivityTypeAndActivityDate(user, activity.getActivityType(), currentDate);
//
//                if (existingActivity != null) {
//                    // Update the existing activity record for the same date
//                    if ("Running".equalsIgnoreCase(activity.getActivityType())) {
//                        existingActivity.setRunningDurationInSeconds(existingActivity.getRunningDurationInSeconds() + activity.getRunningDurationInSeconds());
//                    } else if ("Jogging".equalsIgnoreCase(activity.getActivityType())) {
//                        existingActivity.setJoggingDurationInSeconds(existingActivity.getJoggingDurationInSeconds() + activity.getJoggingDurationInSeconds());
//                    }
//
//                    // Save the updated activity record
//                    activityRepository.save(existingActivity);
//                } else {
//                    // Create a new activity record for the current date
//                    activity.setUser(user);
//                    activity.setActivityDate(currentDate);
//                    activityRepository.save(activity);
//                }
//            }
//        }

        public Activities findByUserAndActivityTypeAndActivityDate(User user, String activityType, LocalDate activityDate) {
            // Implement the logic to find the activity based on the provided criteria
            return activityRepository.findByUserAndActivityTypeAndActivityDate(user, activityType, activityDate);
        }

        public Activities save(Activities activity) {
            // Implement the logic to save the activity, typically using a repository
            return activityRepository.save(activity);
        }





////        private double calculateDistanceCovered(String activityType, int steps, String gender) {
////            // Implement your logic to calculate distance covered based on activity type and steps
////            // You may use average stride length for different activities
////
////            double strideLength = 0.0;
////
////            if ("Male".equalsIgnoreCase(gender)) {
////                if ("Walking".equalsIgnoreCase(activityType)) {
////                    strideLength = 0.76; // Example stride length for walking in meters
////                } else if ("Running".equalsIgnoreCase(activityType)) {
////                    strideLength = 1.2; // Example stride length for running in meters
////                } else if ("Jogging".equalsIgnoreCase(activityType)) {
////                    strideLength = 1.35; // Example stride length for jogging in meters
////                }
////            } else if ("Female".equalsIgnoreCase(gender)) {
////                if ("Walking".equalsIgnoreCase(activityType)) {
////                    strideLength = 0.66; //   Example stride length for walking in meters
////                } else if ("Running".equalsIgnoreCase(activityType)) {
////                    strideLength = 0.91; // Example stride length for running in meters
////                } else if ("Jogging".equalsIgnoreCase(activityType)) {
////                    strideLength = 0.76; // Example stride length for jogging in meters
////                }
////            }
//
//
//            // Calculate distance covered
//            double distanceMeters = steps * strideLength;
//            double distanceKilometers = distanceMeters / 1000.0;
//
//            return distanceKilometers;
//        }








        //save steps record
//        public Activities recordStep(Activities activity) {
//            // Check if an activity record for the same user and date already exists
//            Activities existingRecord = activityRepository.findByUserAndActivityDate(activity.getUser(), activity.getActivityDate());
//
//            if (existingRecord != null) {
//                // If a record already exists, update it with the new step count
//                existingRecord.setSteps(existingRecord.getSteps() + activity.getSteps());
//                return activityRepository.save(existingRecord);
//            } else {
//                // If no record exists, create a new one
//                return activityRepository.save(activity);
//            }
//        }

        public Activities getActivitiesForUserAndDate(User user, LocalDate date) {
            return activityRepository.findByUserAndActivityDate(user, date);
        }

        public Activities updateActivities(Activities activities) {
            return activityRepository.save(activities);
        }

        public Activities createActivities(Activities activities) {
            return activityRepository.save(activities);
        }



        //calculate and get steps record
        public List<Double> calculateCaloriesBurned(User user, LocalDate date) {
            double totalCaloriesBurned = 0.0;
            int totalSteps = 0;
            double distanceKilometers = 0.0;
            List<Double> ans = new ArrayList<>();

            // Fetch the Activities records for the specified user and date
            List<Activities> activitiesList = activityRepository.findByActivityDateAndUserUserId(date, user.getUserId());


            for (Activities activities : activitiesList) {
                int steps = activities.getSteps();

                if ("Male".equalsIgnoreCase(user.getUserProfile().getGender())) {
                    metValue = 3.9;
                    strideLength = 0.76;
                    System.out.println("by male");
                } else if ("Female".equalsIgnoreCase(user.getUserProfile().getGender())) {
                    metValue = 2.4;
                    strideLength = 0.66;
                    System.out.println("by female");
                }
                double caloriesBurnedForStep = steps *metValue;

                double distanceMeters = steps * strideLength;
                 distanceKilometers = distanceMeters / 1000.0;

                // Add the calories burned for this step to the total
                totalCaloriesBurned += caloriesBurnedForStep;

                // Add the steps for this activity to the total steps
                totalSteps += steps;
            }

            ans.add((double) totalSteps);
            ans.add(totalCaloriesBurned);
            ans.add(distanceKilometers);

            return ans;
        }


    }
