package com.example.jwt.controler;

import com.example.jwt.entities.User;
import com.example.jwt.entities.UserProfile;

import com.example.jwt.entities.dashboardEntity.healthTrends.HealthTrends;
import com.example.jwt.exception.ResourceNotFoundException;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.UserProfileService;
import com.example.jwt.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/profile")
@Tag(name = "User Profile Controller", description = "Api for Authentication")
public class UserProfileController {
    private final UserProfileService userProfileService;
    private final JwtHelper jwtHelper;
    private final UserService userService;


    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService, UserService userService,
                                 JwtHelper jwtHelper) {
        this.userProfileService = userProfileService;
        this.jwtHelper = jwtHelper;
        this.userService = userService;

    }




//    @GetMapping("/another-testing")
//    public ResponseEntity<String> getProfileByUserEmail(@RequestHeader("Auth") String tokenHeader) {
//        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//        System.out.println("Username from token: " + username);
//
////         Retrieve the user details from the database
//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//        System.out.println("UserDetails: " + userDetails);
//        return ResponseEntity.ok("Username: " + username);
//    }


//    @GetMapping("/user-profile")
//    public ResponseEntity<UserProfile> getUserProfileByToken(@RequestHeader("Auth") String tokenHeader) {
//        try {
//            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//            String token = tokenHeader.replace("Bearer ", "");
//
//            // Extract the username (email) from the token
//            String username = jwtHelper.getUsernameFromToken(token);
//            System.out.println("Username from token: " + username);
//
////            Retrieve the user details from the database
//            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//            System.out.println("UserDetails: " + userDetails);
////        return ResponseEntity.ok("Username: " + username);
//
//            if (userDetails != null) {
//                // Fetch health trends for the user based on the username
//                UserProfile userProfile = userProfileService.findByUsername(username);
//                System.out.println("Health Trends: " + userProfile);
//            } else {
//                return ResponseEntity.notFound().build(); // UserProfile not found
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Token validation failed
//        }
//    }

//    @GetMapping("/get-userProfile")
//    public ResponseEntity<UserProfile> getUserProfileByToken(@RequestHeader("Auth") String tokenHeader) {
//        try {
//            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//            String token = tokenHeader.replace("Bearer ", "");
//
//            // Extract the username (email) from the token
//            String username = jwtHelper.getUsernameFromToken(token);
//
//            // Retrieve the user's profile based on the username
//            UserProfile userProfile = userProfileService.findByUsername(username);
//
//            if (userProfile != null) {
//                return ResponseEntity.ok(userProfile);
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//            }
//        } catch (Exception e) {
//            // Handle any exceptions, e.g., token validation failure
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//        }
//    }
//





    @GetMapping("/get-userProfile")
    public ResponseEntity<Map<String, Object>> getUserProfileByToken(@RequestHeader("Auth") String tokenHeader) {
        try {

            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);

            // Fetch the user's data from both User and UserProfile entities
            User user = userService.findByUsername(username);
            UserProfile userProfile = userProfileService.findByUsername(username);
            String dobString = String.valueOf(userProfile.getDateOfBirth());
            Integer age = calculatedAge(dobString);

            if (user != null && userProfile != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("firstName", userProfile.getFirstName());
                response.put("lastName", userProfile.getLastName());
                response.put("gender",userProfile.getGender());
                response.put("email", user.getEmail());
                response.put("mobile", user.getMobileNo());
                response.put("height", userProfile.getHeight());
                response.put("weight", userProfile.getWeight());
                response.put("bmi", userProfile.getBmi());
                response.put("googleAccountLink", userProfile.getGoogleAccountLink());
                response.put("facebookAccountLink", userProfile.getFacebookAccountLink());
                response.put("twitterAccountLink", userProfile.getTwitterAccountLink());
                response.put("linkedinAccountLink", userProfile.getLinkedInAccountLink());

//                response.put("dateOfBirth", userProfile.getDateOfBirth());
//                Date date = userProfile.getDateOfBirth();
//                calculatedAge(String.valueOf(date));


                // Convert dateOfBirth to "yyyy-MM-dd" format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String dobString1 = userProfile.getDateOfBirth().format(formatter);
                response.put("dateOfBirth", dobString1);

                // Calculate the age
//                String dobString = String.valueOf(userProfile.getDateOfBirth());
//                Integer age = calculatedAge(dobString);
                response.put("age", age);

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            // Handle any exceptions, e.g., token validation failure
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

     private Integer calculatedAge(String date){
             // Define the date format for the input DOB
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

                if (updateData.containsKey("weight")) {
                    double newWeight = Double.parseDouble(updateData.get("weight").toString());
                    userProfile.setWeight(newWeight);
                    // Calculate and update BMI
                    String str = String.valueOf(userProfile.getHeight());
                    String[] parts = str.split("\\.");
                    double befDecimal = Double.parseDouble(parts[0]);
                    double aftDecimal =Double.parseDouble(parts[1]);

                    double heightInInches = (befDecimal*12) + aftDecimal;
                    double heightInMeters = heightInInches * 0.0254; // Convert height to meters
                    double bmi = newWeight / (heightInMeters * heightInMeters);
                    String formatedBmi = decimalFormat.format(bmi);
                    userProfile.setBmi(Double.parseDouble(formatedBmi));
                }

                if (updateData.containsKey("height")) {
                    double newHeight = Double.parseDouble(updateData.get("height").toString());
                    userProfile.setHeight(newHeight);
                    // Calculate and update BMI
                    String str = String.valueOf(newHeight);
                    String[] parts = str.split("\\.");
                    double befDecimal = Double.parseDouble(parts[0]);
                    double aftDecimal =Double.parseDouble(parts[1]);

                    double heightInInches = (befDecimal*12) + aftDecimal;
                    double heightInMeters = heightInInches * 0.0254; // Convert height to meters
                    double bmi = userProfile.getWeight() / ((heightInMeters / 100.0) * (heightInMeters / 100.0));
                    String formatedBmi = decimalFormat.format(bmi);
                    userProfile.setBmi(Double.parseDouble(formatedBmi));
                }


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

                if ((updateData.containsKey("googleAccountLink"))){
                    String newGoogleAccountLink = updateData.get("googleAccountLink").toString();
                    userProfile.setGoogleAccountLink(newGoogleAccountLink);
                }

                if ((updateData.containsKey("facebookAccountLink"))){
                    String newFacebookAccountLink = updateData.get("facebookAccountLink").toString();
                    userProfile.setFacebookAccountLink(newFacebookAccountLink);
                }

                if ((updateData.containsKey("twitterAccounLinkt"))){
                    String newTwitterAccountLink = updateData.get("twitterAccountLink").toString();
                    userProfile.setTwitterAccountLink(newTwitterAccountLink);
                }

                if ((updateData.containsKey("linkedinAccountLink"))){
                    String newLinkedinAccountLink = updateData.get("linkedinAccountLink").toString();
                    userProfile.setLinkedInAccountLink(newLinkedinAccountLink);
                }


                // Save the updated user and userProfile
                userService.updateUser(user);
                userProfileService.saveUserProfile(userProfile);

                Map<String, Object> response = new HashMap<>();
                response.put("firstName", userProfile.getFirstName());
                response.put("lastName", userProfile.getLastName());
                response.put("email", user.getEmail());
                response.put("mobile", user.getMobileNo());
                response.put("height", userProfile.getHeight());
                response.put("weight", userProfile.getWeight());
                response.put("bmi", userProfile.getBmi());
                response.put("googleAccountLink", userProfile.getGoogleAccountLink());
                response.put("facebookAccountLink", userProfile.getFacebookAccountLink());
                response.put("twitterAccountLink", userProfile.getTwitterAccountLink());
                response.put("linkedinAccountLink", userProfile.getLinkedInAccountLink());

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or profile not found.");
            }
        } catch (Exception e) {
            // Handle any exceptions, e.g., token validation failure
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized.");
        }
    }







//    @GetMapping("/testing")
//    public ResponseEntity<String> getProfile(@RequestHeader("Auth") String tokenHeader) {
//        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//        System.out.println("Username from token: " + username);
//
//        // Retrieve the user details from the database
////        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
////        System.out.println("UserDetails: " + userDetails);
//        return ResponseEntity.ok("Username: " + username);
//    }



    //create user profile
    @PostMapping("/createProfile/{userId}")
    public ResponseEntity<UserProfile> createUserProfile(
            @RequestBody UserProfile userProfile,
            @PathVariable Long userId
    ) throws ParseException {
//        return userProfileService.createUserProfile(userProfile);
        UserProfile userProfile1 = this.userProfileService.createUserProfile(userProfile, userId);
        return new ResponseEntity<UserProfile>(userProfile1, HttpStatus.CREATED);
    }



//
//    // create user profile
//    @PostMapping("/createProfile/{userId}")
//    public ResponseEntity<UserProfile> createUserProfile(
//            @RequestBody UserProfile userProfile,
//            @PathVariable Long userId
//    ) {
//        User user = this.userRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("User Profile", "UserProfile id", userId));
//
//        // Set the user reference in the UserProfile
//        userProfile.setUser(user);
//
//        // Calculate and set the BMI
//        double bmi = calculateBMI(userProfile);
//        userProfile.setBmi(bmi);
//
//        UserProfile newProfile = this.userProfileRepository.save(userProfile);
//
//        return new ResponseEntity<>(newProfile, HttpStatus.CREATED);
//    }
//
//
//    private double calculateBMI(double heightInCentimeters, double weight) {
//        double heightInMeters = heightInCentimeters / 100; // Convert height to meters
//        return weight / (heightInMeters * heightInMeters);
//    }

////update profile
//    @PutMapping("/updateProfile/{userId}")
//    public ResponseEntity<UserProfile> updateUserProfile(@PathVariable Long userId, @RequestBody UserProfile updatedProfile) {
//        userProfileService.updateUserProfile(userId, updatedProfile);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }


}
