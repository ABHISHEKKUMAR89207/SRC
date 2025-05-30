package com.vtt.controller;

import com.vtt.commonfunc.TokenUtils;
import com.vtt.dtoforSrc.AdminRegistrationRequest;
import com.vtt.dtoforSrc.AdminUserEditRequest;
import com.vtt.entities.User;

import com.vtt.event.RegistrationCompleteEvent;
import com.vtt.exception.UserNotFoundException;
import com.vtt.model.JwtResponse;
import com.vtt.model.OtpRequest;
import com.vtt.model.OtpVerificationRequest;
import com.vtt.otherclass.MainRole;
import com.vtt.registration.RegistrationRequest;
import com.vtt.repository.UserRepository;
import com.vtt.request.ChangePasswordRequest;
import com.vtt.request.RefreshTokenRequest;
import com.vtt.security.JwtHelper;
import com.vtt.security.Refresh.RefreshToken;
import com.vtt.security.Refresh.RefreshTokenService;
import com.vtt.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
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
    private RefreshTokenService refreshTokenService;

    @Autowired
    private TokenUtils tokenUtils;
    private Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final ApplicationEventPublisher publisher;


    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        try {
            // Input validation
            String refreshToken = request.get("refreshToken");
            if (refreshToken == null || refreshToken.isEmpty()) {
                return ResponseEntity.badRequest().body("Refresh token is required");
            }

            // Verify the refresh token
            RefreshToken verifiedRefreshToken = refreshTokenService.verifyRefreshToken(refreshToken);
            User user = verifiedRefreshToken.getUser();

            // Generate new JWT token
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
            String newJwtToken = helper.generateToken(userDetails);

            // Create new refresh token (optional - you can keep the same one if you want)
            RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getEmail());

            // Build response
            JwtResponse response = JwtResponse.builder()
                    .jwtToken(newJwtToken)
                    .refreshToken(newRefreshToken.getRefreshToken())
                    .userId(user.getUserId().toString())
                    .username(user.getName())
                    .mainRole(user.getMainRole()!=null?user.getMainRole().toString():"CLIENT")
                    .activeStatus(user.isActiveStatus())
                    .BankDetailsStatus(user.isBankDetailsStatus())
                    .mobileNumber(user.getMobileNo())
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid refresh token: " + e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> verifyOtpAndLogin(@RequestBody OtpVerificationRequest verificationRequest) {
        // Input validation
        String mobileNo = verificationRequest.getMobileNo();
        String otp = verificationRequest.getOtp();


        if (mobileNo == null || mobileNo.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Mobile number is required");
        }
        if (otp == null || otp.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("OTP is required");
        }

        // Find user by mobile number
        User user = userService.findUserByMobileNo(mobileNo);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        if (user.isEmailVerified() == false) {
            user.setEmailVerified(true);
            userDao.save(user);
        }


        // Verify OTP
        if (!otp.equals(user.getOtp())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");
        }

        // Clear OTP after successful verification
        user.setOtp(null);
        userDao.save(user);

        try {
            // Authenticate user
            this.doAuthenticate(user.getEmail(), user.getPasswordTemp());

            // Load user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

            // Generate JWT token
            String token = this.helper.generateToken(userDetails);

            // Generate refresh token
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

            // Get user details
            Optional<User> userOptional = userDao.findByEmail(user.getEmail());
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User data not found");
            }

            User usr = userOptional.get();

            // Build response
            JwtResponse response = JwtResponse.builder()
                    .jwtToken(token)
                    .refreshToken(refreshToken.getRefreshToken())
                    .userId(usr.getUserId().toString())
                    .username(usr.getName())
//                    .mainRole(usr.getMainRole().toString())
                    .mainRole(user.getMainRole()!=null?user.getMainRole().toString():"CLIENT")
                    .activeStatus(usr.isActiveStatus())
                    .BankDetailsStatus(usr.isBankDetailsStatus())
                    .mobileNumber(usr.getMobileNo().toString())
                    .subRole(usr.getSubRole() != null ? usr.getSubRole().toString() : "")
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during authentication: " + e.getMessage());
        }
    }



    @PostMapping("/otpforlogin")
    public ResponseEntity<String> sendOtpForLogin(@RequestBody OtpRequest otpRequest, HttpServletRequest request) {
        String mobileNo = otpRequest.getMobileNo();

        // Find user by mobile number
        User user = userService.findUserByMobileNo(mobileNo);

        if (user == null) {
            // Auto-register new user if not found
            try {
                String email = "vtt_" + System.currentTimeMillis() + "@vtt.com";
                String password = email + "@123";

                RegistrationRequest registrationRequest = new RegistrationRequest(
                        "",               // firstName
                        mobileNo,         // mobileNo
                        email,            // email
                        password,         // password
                        "",               // deviceType
                       0.0,               // notificationToken
                    0.0,               // address
                        true              // emailVerified
                );

                user = userService.registerUser(registrationRequest);
                publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
                return new ResponseEntity<>("OTP sent successfully", HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("Failed to register new user: " + e.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        try {
            // Call 2Factor.in API to generate OTP
            String apiUrl = "https://2factor.in/API/V1/75b451ae-0876-11f0-8b17-0200cd936042/SMS/" +
                    mobileNo + "/AUTOGEN2/";


            //real

//            String apiUrl = "https://2factor.in/API/V1/0ee779f7-0736-11f0-8b17-0200cd936042/SMS/" +
//                    mobileNo + "/AUTOGEN2/";

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.getForEntity(apiUrl, Map.class);

            if (response.getStatusCode() == HttpStatus.OK &&
                    response.getBody() != null &&
                    "Success".equalsIgnoreCase((String) response.getBody().get("Status"))) {

                // Store OTP in user record
                String otp = (String) response.getBody().get("OTP");
                user.setOtp(otp);
                userDao.save(user);

                return new ResponseEntity<>("OTP sent successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Failed to generate OTP", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error while processing OTP request: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // do authentication of the user
    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
                manager.authenticate(authentication);
        }catch (BadCredentialsException e){
                throw new BadCredentialsException("Invalid Username or Password !!");
        }
    }


// Exception handler for BadCredentialsException
@ExceptionHandler(BadCredentialsException.class)
public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException e) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credentials Invalid !!");
}


    @PostMapping("/admin/register")
    public ResponseEntity<String> registerAdmin(@RequestBody AdminRegistrationRequest adminRegistrationRequest,
                                                HttpServletRequest request) {
        try {
//            // Verify the requesting user is ADMIN
//            String tokenHeader = request.getHeader("Authorization");
//            if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is required");
//            }
//
//            String token = tokenHeader.replace("Bearer ", "");
//            String username = jwtHelper.getUsernameFromToken(token);
//
//            User requestingUser = userRepository.findByEmail(username)
//                    .orElseThrow(() -> new UserNotFoundException("User not found"));
//
//            if (requestingUser.getMainRole() != MainRole.ADMIN) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only ADMIN can access this endpoint");
//            }
            // 1. Authorization check - verify requesting user is ADMIN
            String tokenHeader = request.getHeader("Authorization");
            if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Authorization token is required");
            }

            User requestingUser;
            try {
                requestingUser = tokenUtils.getUserFromToken(tokenHeader);
                if (requestingUser.getMainRole() != MainRole.ADMIN) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body("Only ADMIN can access this endpoint");
                }
            } catch (UserNotFoundException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid authorization token");
            }

            // Validate the request
            if (adminRegistrationRequest.getEmail() == null || adminRegistrationRequest.getEmail().isEmpty()) {
                return ResponseEntity.badRequest().body("Email is required");
            }
            if (adminRegistrationRequest.getPassword() == null || adminRegistrationRequest.getPassword().isEmpty()) {
                return ResponseEntity.badRequest().body("Password is required");
            }

            // Check if user already exists
            if (userRepository.findByMobileNo(adminRegistrationRequest.getMobileNo()).isPresent()) {
                return ResponseEntity.badRequest().body("User with this Mobile Number already exists");
            }

            String email = "vtt_" + System.currentTimeMillis() + "@vtt.com";
            String password = email + "@123";



            RegistrationRequest registrationRequest = new RegistrationRequest(
                    adminRegistrationRequest.getUserName(),               // firstName
                    adminRegistrationRequest.getMobileNo(),         // mobileNo
                    email,            // email
                    password,         // password
                    adminRegistrationRequest.getDeviceType(),               // deviceType
                    0.0,               // notificationToken
                    0.0,               // address
                    true              // emailVerified
            );
            User user = new User();
            user = userService.registerUser(registrationRequest);
            publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
            // Create new user with ADMIN role

            System.out.println("gdsfgdfhgfdhdh+"+adminRegistrationRequest.getSubRole());
            System.out.println("gdsfgdfhgfdhdh+"+adminRegistrationRequest.getAddress());
            user.setDeviceType(adminRegistrationRequest.getDeviceType());
            user.setLatitude(adminRegistrationRequest.getLatitude());
            user.setLongitude(adminRegistrationRequest.getLongitude());
            user.setAddress(adminRegistrationRequest.getAddress());
            user.setMainRole(adminRegistrationRequest.getMainRole());
            user.setSubRole(adminRegistrationRequest.getSubRole());
            user.setEmailVerified(true); // Bypass email verification
            user.setRegistrationTimestamp(LocalDateTime.now());

            // Save the user
            userRepository.save(user);

            return ResponseEntity.ok("Admin user registered successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error registering admin user: " + e.getMessage());
        }
    }
    // to change the password of the specific user
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest, HttpServletRequest request) {
        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
        String tokenHeader = request.getHeader("Authorization");
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
//    @PostMapping("/refresh-token")
//    public ResponseEntity<JwtResponse> refreshJwtToken(@RequestBody RefreshTokenRequest request){
//
//        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(request.getRefreshToken());
//
//        User user = refreshToken.getUser();
//
//        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail()); // Assuming email is the username
//
//        String token = this.helper.generateToken(userDetails);
//
//        JwtResponse response = JwtResponse.builder()
//                .jwtToken(token)
//                .refreshToken(refreshToken.getRefreshToken())
//                .userId(user.getUserId().toString())
//                .username(userDetails.getUsername())
//                .build();
//
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }


    @GetMapping("/users/by-role")
    public ResponseEntity<?> getUsersByRole(@RequestParam MainRole role, HttpServletRequest request) {
        try {
            // Verify the requesting user is ADMIN
            String tokenHeader = request.getHeader("Authorization");
            if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Authorization token is required");
            }

            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN can access this endpoint");
            }

            // Get all users with the specified role
            List<User> users = userRepository.findByMainRole(role);

            // You might want to create a DTO to return only necessary user information
            List<Map<String, Object>> response = users.stream()
                    .map(user -> {
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("userName", user.getUserName());
                        userMap.put("userId", user.getUserId());
                        userMap.put("email", user.getEmail());
                        userMap.put("mobileNo", user.getMobileNo());
                        userMap.put("mainRole", user.getMainRole());
                        userMap.put("subRole", user.getSubRole());
                        userMap.put("address", user.getAddress());
                        // Add other fields as needed
                        return userMap;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(response);

        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid authorization token");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching users: " + e.getMessage());
        }
    }



    @PutMapping("/admin/edit-user/{userId}")
    public ResponseEntity<?> editUserByAdmin(
            @PathVariable String userId,
            @RequestBody AdminUserEditRequest editRequest,
            HttpServletRequest request) {

        try {
            // 1. Authorization check - verify requesting user is ADMIN
            String tokenHeader = request.getHeader("Authorization");
            if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Authorization token is required");
            }

            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN can access this endpoint");
            }

            // 2. Find the user to edit
            User userOptional = userRepository.findByUserId(userId);
            if (userOptional==null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found with ID: " + userId);
            }

            User userToEdit = userOptional;

            // 3. Update fields if they are provided in the request
            if (editRequest.getMobileNo() != null && !editRequest.getMobileNo().isEmpty()) {
                // Check if mobile number is already taken by another user
                Optional<User> existingUserWithMobile = userRepository.findByMobileNo(editRequest.getMobileNo());
                if (existingUserWithMobile.isPresent() &&
                        !existingUserWithMobile.get().getUserId().equals(userId)) {
                    return ResponseEntity.badRequest()
                            .body("Mobile number already in use by another user");
                }
                userToEdit.setMobileNo(editRequest.getMobileNo());
            }

            if (editRequest.getMainRole() != null) {
                userToEdit.setMainRole(editRequest.getMainRole());
            }

            if (editRequest.getSubRole() != null) {
                userToEdit.setSubRole(editRequest.getSubRole());
            }

            if (editRequest.getAddress() != null) {
                userToEdit.setAddress(editRequest.getAddress());
            }

            if (editRequest.getUserName() != null && !editRequest.getUserName().isEmpty()) {
                userToEdit.setUserName(editRequest.getUserName());
            }

            if (editRequest.getDeviceType() != null && !editRequest.getDeviceType().isEmpty()) {
                userToEdit.setDeviceType(editRequest.getDeviceType());
            }

            if (editRequest.getLatitude() != null) {
                userToEdit.setLatitude(editRequest.getLatitude());
            }

            if (editRequest.getLongitude() != null) {
                userToEdit.setLongitude(editRequest.getLongitude());
            }

            // 4. Save the updated user
            userRepository.save(userToEdit);

            // 5. Return success response
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User updated successfully");
            response.put("userId", userToEdit.getUserId());
            response.put("updatedFields", editRequest);

            return ResponseEntity.ok(response);

        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid authorization token");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating user: " + e.getMessage());
        }
    }
}
