package com.example.jwt.controler;


import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.HealthTrends;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.model.JwtRequest;
import com.example.jwt.model.JwtResponse;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.request.ChangePasswordRequest;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.UserService;
import com.example.jwt.service.serviceHealth.HealthTrendsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
//@RequestMapping("/api/v2/auth/")
@Tag(name = "AuthController", description = "Api for Authentication")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtHelper helper;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRepository userDao;
    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private HealthTrendsService healthTrendsService;


    private Logger logger = LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request)
    {
        this.doAuthenticate(request.getEmail(), request.getPassword());

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.helper.generateToken(userDetails);

      Optional<User> user = userDao.findByEmail(request.getEmail());

      User usr = user.get();

        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .userId(usr.getUserId().toString())
                .username(userDetails.getUsername()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
                manager.authenticate(authentication);
        }catch (BadCredentialsException e){
                throw new BadCredentialsException("Invalid Username or Password !!");
        }


    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler(){

        return "Credentials Invalid !!";
    }

//    @PostMapping("/create-user")
//    public User createUser(@RequestBody User user){
//        return userService.createUser(user);
//    }


//    @GetMapping("/profile")
//    public ResponseEntity<String> getProfile(@RequestHeader("Authorization") String tokenHeader) {
//        // Extract the username from the JWT token
//        String token = tokenHeader.replace("Bearer ", "");
//        String username = jwtTokenUtil.extractUsername(token);
//        return ResponseEntity.ok("Username: " + username);
//    }





//    @GetMapping("/health-trends")
//    public List<HealthTrends> getHealthTrendsForLoggedInUser(@RequestHeader("Auth") String tokenHeader) {
////        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        // Extract the username from the JWT token
//        String token = tokenHeader.replace("Bearer ", "");
//        String username = jwtHelper.getUsernameFromToken(token);
//        System.out.println("UserName mila token se......."+username);
//
//        User user = userRepository.findByUserNameIgnoreCase(username);
//        if (user != null) {
//            return user.getHealthTrends();
//        } else {
//            throw new UserNotFoundException(username);
//        }
//    }

//    @GetMapping("/get-username")
//    public ResponseEntity<String> getUsernameFromToken(@RequestHeader("Auth") String tokenHeader) {
//        try {
//
//            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//            String token = tokenHeader.replace("Bearer ", "");
//
//            // Extract the username from the token
//            String username = jwtHelper.getUsernameFromToken(token);
//
////            User user = userRepository.findByEmail(username);
////            System.out.println("user.........."+us);
//////            if (user != null) {
//////                return user.getHealthTrends();
//////            } else {
//////                throw new UserNotFoundException(username);
//////            }
//
//            // Return the username as a response
//            return ResponseEntity.ok("Username: " + username);
//        } catch (Exception e) {
//            // Handle any exceptions, e.g., token validation failure
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token validation failed");
//        }
//    }










    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest, HttpServletRequest request) {
        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
        String tokenHeader = request.getHeader("Auth");
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);

            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UserNotFoundException("User not found for email: " + username));

            // Check if the old password matches the user's current password
            if (passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
                // Old password matches, proceed with the password change.
                user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
                userRepository.save(user);
                return new ResponseEntity<>("Password changed successfully.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Old password is incorrect.", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Unauthorized. Please provide a valid JWT token.", HttpStatus.UNAUTHORIZED);
        }
    }
























    //
//@GetMapping("/health-trends")
//public List<HealthTrends> getHealthTrendsForLoggedInUser() {
//    String username = SecurityContextHolder.getContext().getAuthentication().getName();
//
//    User user = userRepository.findByUserNameIgnoreCase(username);
//
//    if (user != null) {
//        return user.getHealthTrends();
//    } else {
//        throw new UserNotFoundException(username);
//    }
//}

}
