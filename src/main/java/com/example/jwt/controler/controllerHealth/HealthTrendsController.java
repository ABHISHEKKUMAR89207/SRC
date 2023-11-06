package com.example.jwt.controler.controllerHealth;



import com.example.jwt.entities.User;
import com.example.jwt.entities.UserProfile;
import com.example.jwt.entities.dashboardEntity.healthTrends.*;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserProfileRepository;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.UserService;
import com.example.jwt.service.serviceHealth.HealthTrendsService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/health-trends")
public class HealthTrendsController {

    private final HealthTrendsService healthTrendsService;
    private final UserRepository userRepository;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserDetailsService userDetailsService;


    @Autowired
    public HealthTrendsController(HealthTrendsService healthTrendsService, UserRepository userRepository) {
        this.healthTrendsService = healthTrendsService;
        this.userRepository = userRepository;
    }

//    @PostMapping("/create")
//    public HealthTrends createHealthTrends(@RequestBody HealthTrends healthTrends) {
//        return healthTrendsService.saveHealthTrends(healthTrends);
//    }


//@PostMapping("/create-healthTrend")
//public ResponseEntity<HealthTrends> createHealthTrends(@RequestBody HealthTrends healthTrends) {
//    HealthTrends savedHealthTrends = healthTrendsService.saveHealthTrends(healthTrends);
//    return ResponseEntity.ok(savedHealthTrends);
//}


//    public HealthTrends saveHealthTrends(HealthTrends healthTrends) {
//        // Fetch the corresponding UserProfile
//        Optional<UserProfile> userProfileOptional = userProfileRepository.findById(healthTrends.getUserProfile().getId());
//
//        if (userProfileOptional.isPresent()) {
//            UserProfile userProfile = userProfileOptional.get();
//            healthTrends.setBmi(userProfile.getBmi());
//            healthTrends.setWeight(userProfile.getWeight());
//        }
//
//        return healthTrendsRepository.save(healthTrends);
//    }
//
//    public List<HealthTrends> getAllHealthTrends() {
//        return healthTrendsRepository.findAll();
//    }

//    @PostMapping("/create")
//    public ResponseEntity<HealthTrends> createHealthTrends(@RequestBody HealthTrends request, @RequestHeader("Auth") String tokenHeader) {
//        try {
//            String token = tokenHeader.replace("Bearer ", "");
//            String username = jwtHelper.getUsernameFromToken(token);
//
//            // Retrieve the user by username
//            User user = userService.findByUsername(username);
//
//            if (user != null) {
//                // Fetch the user's profile
////                UserProfile userProfile = user.getUserProfile();
//                HealthTrends healthTrends = new HealthTrends();
//                Optional<UserProfile> userProfileOptional = userProfileRepository.findById(healthTrends.getUserProfile().getId());
//
//                if (userProfileOptional.isPresent()) {
//                    UserProfile userProfile = userProfileOptional.get();
////                    HealthTrends healthTrends = new HealthTrends();
//
//                    healthTrends.setBmi(userProfile.getBmi());
//                    healthTrends.setWeight(userProfile.getWeight());
//                    healthTrends.setUser(user);
//
//                    // Now, you can save the HealthTrends with the user ID
//                    HealthTrends savedHealthTrends = healthTrendsService.saveHealthTrends(healthTrends);
//
//                    return ResponseEntity.ok(savedHealthTrends);
//                }else {
//                    // Handle the case where the user's profile is not found
//                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//                }
//
//
////                if (userProfile != null) {
////                    HealthTrends healthTrends = new HealthTrends();
////                    healthTrends.setDate(LocalDate.now());
//////                    healthTrends.setWeight(userProfile.getWeight());
//////                    healthTrends.setBmi(userProfile.getBmi());
//////                    healthTrends.setUser(user);
////
////                    // Now, you can save the HealthTrends with the user ID
//////                    HealthTrends savedHealthTrends = healthTrendsService.saveHealthTrends(healthTrends);
//////
//////                    return ResponseEntity.ok(savedHealthTrends);
////                } else {
////                    // Handle the case where the user's profile is not found
////                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
////                }
//            } else {
//                // Handle the case where the user is not found
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//            }
//        } catch (Exception e) {
//            // Handle any exceptions, e.g., token validation failure
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//        }
//    }
//



    @PostMapping("/create-health-trend")
    public ResponseEntity<HealthTrends> createHealthTrends(@RequestBody HealthTrends request, @RequestHeader("Auth") String tokenHeader) {
        try {
            // Extract the token from the header
            String token = tokenHeader.replace("Bearer ", "");

            // Validate the token and get the username
            String username = jwtHelper.getUsernameFromToken(token);

            if (username == null) {
                // Invalid or expired token
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            // Retrieve the user by username
            User user = userService.findByUsername(username);

            if (user != null) {
                // Check if the user has a profile
                UserProfile userProfile = user.getUserProfile();

                if (userProfile != null) {

                    HealthTrends healthTrends = new HealthTrends();

                    healthTrends.setDate(LocalDate.now());
                    healthTrends.setWeight(userProfile.getWeight());
                    healthTrends.setBmi(userProfile.getBmi());

                    healthTrends.setUser(user);
                    healthTrends.setUserProfile(userProfile);

                    // Now, you can save the HealthTrends with the user ID
                    HealthTrends savedHealthTrends = healthTrendsService.saveHealthTrends(healthTrends);

                    return ResponseEntity.ok(savedHealthTrends);
                } else {
                    // Handle the case where the user's profile is not found
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
            } else {
                // Handle the case where the user is not found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            // Handle any exceptions, e.g., token validation failure
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }




    @GetMapping("/get-health-trends")
    public ResponseEntity<String> getAllHealthTrends(@RequestHeader("Auth") String tokenHeader) {
        try {
            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);
            System.out.println("Username from token: " + username);

            // Retrieve the user details from the database
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            System.out.println("UserDetails: " + userDetails);

            if (userDetails != null) {
                // Fetch health trends for the user based on the username
                HealthTrends healthTrends = healthTrendsService.findByUsername(username);
                System.out.println("Health Trends: " + healthTrends);

                if (healthTrends != null) {
                    // You can return healthTrends or perform any other actions here
                    return ResponseEntity.ok("HealthTrends: " + healthTrends);
                } else {
                    System.out.println("HealthTrends not found for username: " + username);
                }
            } else {
                System.out.println("User not found for username: " + username);
                // Handle the case where the user is not found in the database
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // Return the username as a response (this line will not be executed if health trends are found)
            return ResponseEntity.ok("Username: " + username);
        } catch (Exception e) {
            // Handle any exceptions, e.g., token validation failure
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token validation failed");
        }
    }



//    @GetMapping("/healthTrends")
//    public List<HealthTrends> getHealthTrendsForCurrentUser() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName(); // Get the username of the logged-in user
//        return healthTrendsService.getHealthTrendsForLoggedInUser(username);
//    }
//
//    @GetMapping("/healthTrends")
//    public ResponseEntity<List<HealthTrends>> getHealthTrendsForCurrentUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication != null && authentication.getUserName instanceof UserDetails) {
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            String username = userDetails.getUsername();
//
//            try {
//                List<HealthTrends> healthTrends = healthTrendsService.getHealthTrendsForLoggedInUser(username);
//                return ResponseEntity.ok(healthTrends);
//            } catch (UserNotFoundException e) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//            }
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
//        }
//    }

//    @GetMapping("/{username}/healthtrends")
//    public List<HealthTrends> getHealthTrendsForLoggedInUser(@PathVariable String username) {
//        User user = (User) userRepository.findByUserName(username);
//
//        if (user != null) {
//            return user.getHealthTrends();
//        } else {
//            throw new UserNotFoundException(username);
//        }
//    }


//    @GetMapping("/for-user")
//    public HealthTrends getHealthTrendForLoggedInUser() {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        HealthTrends healthTrend = healthTrendsService.getHealthTrendForUser(username);
//        return healthTrend;
//    }
//    @PostMapping("/create-healthTrend")
//    public ResponseEntity<HealthTrendDto> createHealthTrends(@RequestBody HealthTrendDto healthTrendDto) {
//
//        HealthTrendDto savehealthTrends =this.healthTrendsService.saveHealthTrends(healthTrendDto);
////        HealthTrends savedHealthTrends = healthTrendsService.saveHealthTrends(healthTrends);
//        return ResponseEntity.ok(savehealthTrends);
////        return ResponseEntity<HealthTrendDto>(savehealthTrends,HttpStatus.CREATED);
//    }


//    @GetMapping("/all")
//    public List<HealthTrends> getAllHealthTrends() {
//        return healthTrendsService.getAllHealthTrends();
//    }

    @GetMapping("/{id}")
    public Optional<HealthTrends> getHealthTrendsById(@PathVariable Long id) {
        return healthTrendsService.getHealthTrendsById(id);
    }

//    @GetMapping("/{healthTrendId}/heartrates")
//    public List<HeartRate> getHeartRatesByHealthTrend(@PathVariable Long healthTrendId) {
//        return healthTrendsService.getHeartRatesByHealthTrend(healthTrendId);
//    }


    //Heart Rate
    @GetMapping("/{healthTrendId}/heartbeats")
    public ResponseEntity<List<HeartRate>> getHeartRatesByHealthTrend(@PathVariable Long healthTrendId) {
        List<HeartRate> heartRates = healthTrendsService.getHeartRatesByHealthTrend(healthTrendId);

        if (heartRates != null) {
            return ResponseEntity.ok(heartRates);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    //Blood Pressure
    @GetMapping("/{healthTrendId}/bloodPressure")
    public ResponseEntity<List<BloodPressure>> getBloodPressureByHealthTrend(@PathVariable Long healthTrendId) {
        List<BloodPressure> bloodPressure = healthTrendsService.getbloodPressureByHealthTrend(healthTrendId);

        if (bloodPressure != null) {
            return ResponseEntity.ok(bloodPressure);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Blood Glucose
    @GetMapping("/{healthTrendId}/bloodGlucose")
    public ResponseEntity<List<BloodGlucose>> getBloodGlucoseByHealthTrend(@PathVariable Long healthTrendId) {
        List<BloodGlucose> bloodGlucose = healthTrendsService.getbloodGlucoseByHealthTrend(healthTrendId);

        if (bloodGlucose != null) {
            return ResponseEntity.ok(bloodGlucose);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    //Blood OxygenSaturatedLevel
    @GetMapping("/{healthTrendId}/oxygen-saturation-level")
    public ResponseEntity<List<OxygenSaturatedLevel>> getOxygenSaturatedLevelByHealthTrend(@PathVariable Long healthTrendId) {
        List<OxygenSaturatedLevel> oxygenSaturatedLevel = healthTrendsService.getOxygenSaturatedLevelByHealthTrend(healthTrendId);

        if (oxygenSaturatedLevel != null) {
            return ResponseEntity.ok(oxygenSaturatedLevel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
