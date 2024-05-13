package com.example.jwt.controler;

import com.example.jwt.entities.FoodToday.ear.Ear;
import com.example.jwt.entities.FoodToday.ear.EarRepository;
import com.example.jwt.entities.FoodToday.ear.EarResponse;
import com.example.jwt.entities.FoodToday.ear.EarService;
import com.example.jwt.entities.User;
import com.example.jwt.entities.UserProfile;

import com.example.jwt.entities.activityType.ActivityType;
import com.example.jwt.entities.activityType.ActivityTypeService;
import com.example.jwt.entities.error.Error;
import com.example.jwt.entities.error.ErrorRepository;
import com.example.jwt.repository.UserProfileRepository;
import com.example.jwt.request.WorkLevelRequest;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.UserProfileService;
import com.example.jwt.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/profile")
@Tag(name = "User Profile Controller", description = "Api for Authentication")
public class UserProfileController {
    private final UserProfileService userProfileService;
    private final JwtHelper jwtHelper;
    private final UserService userService;
    Logger logger = LoggerFactory.getLogger(UserProfileController.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService, UserService userService, JwtHelper jwtHelper) {
        this.userProfileService = userProfileService;
        this.jwtHelper = jwtHelper;
        this.userService = userService;
    }


    @PostMapping("/variable-type")
    public ResponseEntity<UserProfile> saveOrUpdateVariableType(
            @RequestHeader("Auth") String tokenHeader,
            @RequestParam String variableType
    ) {
        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Fetch the user's data from both User and UserProfile entities
        User user = userService.findByUsername(username);

        UserProfile userProfile = userProfileService.saveOrUpdateVariableType(username, variableType);
        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }

    @Autowired
    private ErrorRepository errorRepository;
    // to get the user's
    // profile
//    @GetMapping("/get-userProfile")
////    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public ResponseEntity<Map<String, Object>> getUserProfileByToken(@RequestHeader("Auth") String tokenHeader) {
//        User user = null; // Declare the user variable outside the try block
//
//        try {
//            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//            String token = tokenHeader.replace("Bearer ", "");
//
//            // Extract the username (email) from the token
//            String username = jwtHelper.getUsernameFromToken(token);
//            logger.info("Hello ...");
//
//            // Fetch the user's data from both User and UserProfile entities
//            user = userService.findByUsername(username);
//            UserProfile userProfile = userProfileService.findByUsername(username);
//            String dobString = String.valueOf(userProfile.getDateOfBirth());
//            Integer age = calculatedAge(dobString);
//
//
//            if (user != null && userProfile != null) {
//                Map<String, Object> response = new HashMap<>();
//                response.put("firstName", userProfile.getFirstName());
//                response.put("lastName", userProfile.getLastName());
//                response.put("gender", userProfile.getGender());
//                response.put("email", user.getEmail());
//                response.put("mobile", user.getMobileNo());
////                response.put("height", userProfile.getHeight());
//                response.put("heightFt", userProfile.getHeightFt());
//                response.put("heightIn", userProfile.getHeightIn());
//                response.put("weight", userProfile.getWeight());
//                response.put("bmi", userProfile.getBmi());
//                response.put("googleAccountLink", userProfile.getGoogleAccountLink());
//                response.put("facebookAccountLink", userProfile.getFacebookAccountLink());
//                response.put("twitterAccountLink", userProfile.getTwitterAccountLink());
//                response.put("linkedinAccountLink", userProfile.getLinkedInAccountLink());
//
//                response.put("Android App Link","https://play.google.com/store/apps/details?id=com.icmr.amr_treatment_guidelines&hl=en_IN&gl=US");
//                response.put("IOS App Link","https://play.google.com/store/apps/details?id=com.icmr.amr_treatment_guidelines&hl=en_IN&gl=US");
//                response.put("App message","\uD83C\uDF1F Elevate your health with Nutrify India Now 2.0! \uD83D\uDE80\n" +
//                        "\uD83C\uDF4F Personalized insights, fitness tracking, nutrition guidance, and more! \n" +
//                        "#NIN2Point0 #WellnessRevolution \n" +
//                        "\uD83D\uDD17 Download Now :");
//
//                // Convert dateOfBirth to "yyyy-MM-dd" format
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                String dobString1 = userProfile.getDateOfBirth().format(formatter);
//                response.put("dateOfBirth", dobString1);
//
//                // Calculate the age
//                response.put("age", age);
//
//                return ResponseEntity.ok(response);
//            } else {
//                Error error = new Error();
//                error.setUser(user);
//                error.setExceptionMessage("User Profile Not Created");
//                error.setLocalDateTime(LocalDateTime.now()); // Set current date
//                errorRepository.save(error);
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "User Profile Not Created"));
//            }
//        } catch (Exception e) {
//
//            Error error = new Error();
//            error.setUser(user); // Set the user if available
//            error.setExceptionMessage("Exception occurred: " + e.getMessage());
//            error.setStackTrace(Arrays.toString(e.getStackTrace()));
//            error.setLocalDateTime(LocalDateTime.now()); // Set current date
//            errorRepository.save(error);
//            // Handle any exceptions, e.g., token validation failure
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "User Profile Not Created"));
//        }
//    }

    @GetMapping("/get-userProfile")
    public ResponseEntity<Map<String, Object>> getUserProfileByToken(@RequestHeader("Auth") String tokenHeader) {
        User user = null; // Declare the user variable outside the try block

        try {
            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);
            logger.info("Hello ...");

            // Fetch the user's data from both User and UserProfile entities
            user = userService.findByUsername(username);
            UserProfile userProfile = userProfileService.findByUsername(username);
            String dobString = String.valueOf(userProfile.getDateOfBirth());
            Integer age = calculatedAge(dobString);

            if (user != null && userProfile != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("firstName", userProfile.getFirstName());
                response.put("lastName", userProfile.getLastName());
                response.put("gender", userProfile.getGender());
                response.put("email", user.getEmail());
                response.put("mobile", user.getMobileNo());
                response.put("heightFt", userProfile.getHeightFt());
                response.put("heightIn", userProfile.getHeightIn());
                response.put("weight", userProfile.getWeight());
                response.put("bmi", userProfile.getBmi());
                response.put("bmr", userProfile.getBmr());
                response.put("googleAccountLink", userProfile.getGoogleAccountLink());
                response.put("facebookAccountLink", userProfile.getFacebookAccountLink());
                response.put("twitterAccountLink", userProfile.getTwitterAccountLink());
                response.put("linkedinAccountLink", userProfile.getLinkedInAccountLink());

                // Add null checks for workLevel and occupation
                if (userProfile.getWorkLevel() != null) {
                    response.put("workLevel", userProfile.getWorkLevel());
                }
//                if (userProfile.getOccupation() != null) {
//                    response.put("occupation", userProfile.getOccupation());
//                }

                // Add other fields as needed

                response.put("Android App Link","https://play.google.com/store/apps/details?id=com.icmr.amr_treatment_guidelines&hl=en_IN&gl=US");
                response.put("IOS App Link","https://play.google.com/store/apps/details?id=com.icmr.amr_treatment_guidelines&hl=en_IN&gl=US");
                response.put("App message","\uD83C\uDF1F Elevate your health with Nutrify India Now 2.0! \uD83D\uDE80\n" +
                        "\uD83C\uDF4F Personalized insights, fitness tracking, nutrition guidance, and more! \n" +
                        "#NIN2Point0 #WellnessRevolution \n" +
                        "\uD83D\uDD17 Download Now :");

                // Convert dateOfBirth to "yyyy-MM-dd" format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String dobString1 = userProfile.getDateOfBirth().format(formatter);
                response.put("dateOfBirth", dobString1);

                // Calculate the age
                response.put("age", age);

                return ResponseEntity.ok(response);
            } else {
                Error error = new Error();
                error.setUser(user);
                error.setExceptionMessage("User Profile Not Created");
                error.setLocalDateTime(LocalDateTime.now()); // Set current date
                errorRepository.save(error);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "User Profile Not Created"));
            }
        } catch (Exception e) {
            Error error = new Error();
            error.setUser(user); // Set the user if available
            error.setExceptionMessage("Exception occurred: " + e.getMessage());
            error.setStackTrace(Arrays.toString(e.getStackTrace()));
            error.setLocalDateTime(LocalDateTime.now()); // Set current date
            errorRepository.save(error);
            // Handle any exceptions, e.g., token validation failure
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "User Profile Not Created"));
        }
    }



    // for calculating the age of the user with the given D.O.B
    private Integer calculatedAge(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // Parse the DOB string into a LocalDate object
        LocalDate birthDate = LocalDate.parse(date, formatter);
        // Calculate the period (difference) between the birthDate and the current date
        Period age = Period.between(birthDate, LocalDate.now());
        // Extract the years from the age period
        int years = age.getYears();
        return years;
    }


    @Autowired
    private ActivityTypeService activityTypeService;
    //update profile
    @PutMapping("/update-userProfile")
    public ResponseEntity<?> updateUserProfileByToken(@RequestHeader("Auth") String tokenHeader, @RequestBody Map<String, Object> updateData) {
        try {
            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);

            // Fetch the user's data from both User and UserProfile entities
            User user = userService.findByUsername(username);
            UserProfile userProfile = userProfileService.findByUsername(username);
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            if (user != null && userProfile != null) {
                // Update the profile data based on the input
                if (updateData.containsKey("mobile")) {
                    user.setMobileNo(updateData.get("mobile").toString());
                }

                if (updateData.containsKey("firstName")) {
                    userProfile.setFirstName(updateData.get("firstName").toString());
                }

                if (updateData.containsKey("lastName")) {
                    userProfile.setLastName(updateData.get("lastName").toString());
                }

                if (updateData.containsKey("weight")) {
                    double newWeight = Double.parseDouble(updateData.get("weight").toString());
                    userProfile.setWeight(newWeight);
                }

//                if (updateData.containsKey("height")) {
//                    double newHeight = Double.parseDouble(updateData.get("height").toString());
//                    userProfile.setHeight(newHeight);
//                }
                if (updateData.containsKey("heightFt") && updateData.containsKey("heightIn")) {
                    int newHeightFt = Integer.parseInt(updateData.get("heightFt").toString());
                    int newHeightIn = Integer.parseInt(updateData.get("heightIn").toString());
                    userProfile.setHeightFt(newHeightFt);
                    userProfile.setHeightIn(newHeightIn);
                }



                // Recalculate and update BMI based on the updated weight and height
                if (updateData.containsKey("weight") || updateData.containsKey("height")) {
                    userProfile.setBmi(userProfileService.calculateBMI(userProfile.getGender(), userProfile.getHeightFt(), userProfile.getHeightIn(), userProfile.getWeight()));
                }

                // Recalculate and update BMR based on the updated weight and height
                userProfile.setBmr(userProfileService.calculateBMR(userProfile));

                // Check if email is present in the updateData and it's different from the current email
                if (updateData.containsKey("email")) {
                    String newEmail = updateData.get("email").toString();
                    if (!newEmail.equals(user.getEmail()) && !userService.isEmailInUse(newEmail)) {
                        user.setEmail(newEmail);
                    } else if (newEmail.equals(user.getEmail())) {
                        // Email remains the same, no action needed
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already in use.");
                    }
                }

                if (updateData.containsKey("workLevel")) {
                    String newWorkLevel = updateData.get("workLevel").toString();
                    userProfile.setWorkLevel(newWorkLevel);
                }


//                // Update occupation and work level
//                if (updateData.containsKey("occupation")) {
//                    String newOccupation = updateData.get("occupation").toString();
//                    userProfile.setOccupation(newOccupation);
//
//                    // Fetch the activity type based on the occupation
//                    ActivityType activityType = activityTypeService.findByOccupation(newOccupation);
//
//                    if (activityType != null) {
//                        userProfile.setWorkLevel(activityType.getTypeOfActivity());
//                    } else {
//                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activity type not found for occupation: " + newOccupation);
//                    }
//                }

                if ((updateData.containsKey("googleAccountLink"))) {
                    String newGoogleAccountLink = updateData.get("googleAccountLink").toString();
                    userProfile.setGoogleAccountLink(newGoogleAccountLink);
                }

                if ((updateData.containsKey("facebookAccountLink"))) {
                    String newFacebookAccountLink = updateData.get("facebookAccountLink").toString();
                    userProfile.setFacebookAccountLink(newFacebookAccountLink);
                }

                if ((updateData.containsKey("twitterAccounLinkt"))) {
                    String newTwitterAccountLink = updateData.get("twitterAccountLink").toString();
                    userProfile.setTwitterAccountLink(newTwitterAccountLink);
                }

                if ((updateData.containsKey("linkedinAccountLink"))) {
                    String newLinkedinAccountLink = updateData.get("linkedinAccountLink").toString();
                    userProfile.setLinkedInAccountLink(newLinkedinAccountLink);
                }


                // Save the updated user and userProfile
                userService.updateUser(user);
                userProfileService.saveUserProfile(userProfile);

//                Map<String, Object> response = new HashMap<>();
//                response.put("firstName", userProfile.getFirstName());
//                response.put("lastName", userProfile.getLastName());
//                response.put("email", user.getEmail());
//                response.put("mobile", user.getMobileNo());
//                response.put("height", userProfile.getHeight());
//                response.put("weight", userProfile.getWeight());
//                response.put("bmi", userProfile.getBmi());
//                response.put("googleAccountLink", userProfile.getGoogleAccountLink());
//                response.put("facebookAccountLink", userProfile.getFacebookAccountLink());
//                response.put("twitterAccountLink", userProfile.getTwitterAccountLink());
//                response.put("linkedinAccountLink", userProfile.getLinkedInAccountLink());

//                return ResponseEntity.ok(response);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or profile not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized.");
        }
    }

    //create user profile
    @PostMapping("/createProfile/{userId}")
    public ResponseEntity<UserProfile> createUserProfile(@RequestBody UserProfile userProfile, @PathVariable Long userId) throws ParseException {
        UserProfile userProfile1 = this.userProfileService.createUserProfile(userProfile, userId);
        return new ResponseEntity<UserProfile>(userProfile1, HttpStatus.CREATED);
    }

    @Autowired
    private EarService earService;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @PostMapping("/save-update-work-level")
    public ResponseEntity<UserProfile> saveOrUpdateWorkLevelForUserProfile(
            @RequestHeader("Auth") String tokenHeader,
            @RequestParam String workLevel) {

        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        User user = userService.findByUsername(username);
        UserProfile userProfile = userProfileService.findByUsername(username);

        if (userProfile != null) {
            userProfile.setWorkLevel(workLevel);
            UserProfile updatedUserProfile = userProfileRepository.save(userProfile);

            return ResponseEntity.ok().build(); // Return an empty ResponseEntity with status 200
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/delete-work-level")
    public ResponseEntity<Void> deleteWorkLevelForUserProfile(
            @RequestHeader("Auth") String tokenHeader) {

        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        User user = userService.findByUsername(username);
        UserProfile userProfile = userProfileService.findByUsername(username);

        if (userProfile != null) {
            userProfile.setWorkLevel(null); // Set workLevel to null or handle the deletion logic as needed
            userProfileRepository.save(userProfile);

            return ResponseEntity.ok().build(); // Return an empty ResponseEntity with status 200
        } else {
            return ResponseEntity.notFound().build();
        }
    }




//    @GetMapping("/ear")
//    public ResponseEntity<EarResponse> getEarGroupAndWorkLevel(@RequestHeader("Auth") String tokenHeader) {
//        String token = tokenHeader.replace("Bearer ", "");
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        User user = userService.findByUsername(username);
//
//        if (user != null) {
//            System.out.println("dsdsdfds");
//            // Fetch UserProfile based on the userId (Assuming you have a service for this)
//            Optional<UserProfile> userProfileOptional = userProfileService.getUserProfileById(user.getUserId());
//
//            if (userProfileOptional.isPresent()) {
//                System.out.println("lllllllllll");
//                UserProfile userProfile = userProfileOptional.get();
//                // Use the ProfileService to get Ear group and work level
//                EarResponse earResponse = userProfileService.getEarGroupAndWorkLevel(userProfile);
//
//                if (earResponse != null) {
//                    return new ResponseEntity<>(earResponse, HttpStatus.OK);
//                } else {
//                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//                }
//            } else {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
@GetMapping("/ear")
public ResponseEntity<EarResponse> getEarGroupAndWorkLevel(@RequestHeader("Auth") String tokenHeader) {
    try {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        User user = userService.findByUsername(username);

        if (user != null) {
            UserProfile userProfile = userProfileRepository.findByUserUserId(user.getUserId());

            if (userProfile != null) {
                EarResponse earResponse = userProfileService.getEarGroupAndWorkLevel(userProfile);

                if (earResponse != null) {
                    return new ResponseEntity<>(earResponse, HttpStatus.OK);
                }
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (Exception e) {
        e.printStackTrace();  // Log or print the exception for debugging purposes
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

@Autowired
private EarRepository earRepository;

//    @GetMapping("/get-all-ear")
//    public List<Ear> getAllEars() {
//        return earRepository.findAll();
//    }


    @GetMapping("/get-all-ear")
    public List<EarResponse> getAllEars() {
        List<Ear> ears = earRepository.findAll();
        List<EarResponse> earResponses = ears.stream()
                .map(EarResponse::new)
                .collect(Collectors.toList());
        return earResponses;
    }

//@GetMapping("/ear")
//@ResponseBody
//public ResponseEntity<String> getEarGroupAndWorkLevel(@RequestHeader("Auth") String tokenHeader) {
//    String token = tokenHeader.replace("Bearer ", "");
//    String username = jwtHelper.getUsernameFromToken(token);
//
//    User user = userService.findByUsername(username);
//
//    if (user != null) {
//        // Fetch UserProfile based on the userId (Assuming you have a service for this)
//        Optional<UserProfile> userProfileOptional = userProfileService.getUserProfileById(user.getUserId());
//
//        if (userProfileOptional.isPresent()) {
//            UserProfile userProfile = userProfileOptional.get();
//            // Use the ProfileService to get Ear group and work level
//            String result = userProfileService.getEarGroupAndWorkLevel(userProfile);
//            return ResponseEntity.ok("Result: " + result);
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("UserProfile not found for userId: " + user.getUserId());
//        }
//    } else {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found for username: " + username);
//    }
//}

}
