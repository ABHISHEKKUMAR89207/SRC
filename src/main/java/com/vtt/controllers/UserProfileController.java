package com.vtt.controllers;


import com.vtt.entities.User;
import com.vtt.entities.UserProfile;

import com.vtt.repository.UserProfileRepository;
import com.vtt.security.JwtHelper;
import com.vtt.service.UserProfileService;
import com.vtt.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
                String wakeupTime = userProfile.getWakeupTime() != null ?
                        userProfile.getWakeupTime().plusMinutes(1).format(DateTimeFormatter.ofPattern("HH:mm")) :
                        "00:00";
                response.put("wakeupTime", wakeupTime);
                response.put("googleAccountLink", userProfile.getGoogleAccountLink());
                response.put("facebookAccountLink", userProfile.getFacebookAccountLink());
                response.put("twitterAccountLink", userProfile.getTwitterAccountLink());
                response.put("linkedinAccountLink", userProfile.getLinkedInAccountLink());

                // Add null checks for workLevel and occupation
                if (userProfile.getWorkLevel() != null) {
                    response.put("workLevel", userProfile.getWorkLevel());
                }
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

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "User Profile Not Created"));
            }
        } catch (Exception e) {
            Error error = new Error();

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

                // Update wakeupTime
                if (updateData.containsKey("wakeupTime")) {
                    String wakeupTimeString = updateData.get("wakeupTime").toString();
                    // Assuming wakeupTimeString is in HH:mm format
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    LocalTime wakeupTime = LocalTime.parse(wakeupTimeString, formatter);

                    // Adjust wakeupTime by subtracting 1 minute
                    wakeupTime = wakeupTime.minusMinutes(1);

                    userProfile.setWakeupTime(wakeupTime);
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

                // Update date of birth
                if (updateData.containsKey("dateOfBirth")) {
                    LocalDate newDateOfBirth = LocalDate.parse(updateData.get("dateOfBirth").toString());
                    userProfile.setDateOfBirth(newDateOfBirth);
                }

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

}
