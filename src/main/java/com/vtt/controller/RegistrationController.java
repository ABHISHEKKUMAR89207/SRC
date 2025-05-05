package com.vtt.controller;

import com.vtt.dtos.OtpRequest;
import com.vtt.entities.User;
import com.vtt.event.RegistrationCompleteEvent;
import com.vtt.registration.RegistrationRequest;
import com.vtt.repository.UserRepository;
import com.vtt.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

////package com.vtt.controler;
////
////import com.vtt.entities.User;
////import com.vtt.event.RegistrationCompleteEvent;
////import com.vtt.registration.RegistrationRequest;
////import com.vtt.registration.token.VerificationToken;
////import com.vtt.registration.token.VerificationTokenRepository;
////import com.vtt.service.UserService;
////import io.swagger.v3.oas.annotations.tags.Tag;
////import jakarta.servlet.http.HttpServletRequest;
////import lombok.RequiredArgsConstructor;
////import org.springframework.context.ApplicationEventPublisher;
////import org.springframework.web.bind.annotation.*;
////
////
////@RestController
////@RequiredArgsConstructor
////@RequestMapping("/register")
//////@RequestMapping("/api/v1/auth/")
////@Tag(name = "Registration Controller", description = "This is Registration Controller")
////public class RegistrationController {
////
////    private final UserService userService;
////    private final ApplicationEventPublisher publisher;
////    private final VerificationTokenRepository tokenRepository;
//
//    //for registering the user n application
//    @PostMapping
//    public String registerUser(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest request) {
//        User user = userService.registerUser(registrationRequest);
//        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
//        return "Success!  Please, check your email for to complete your registration";
//    }
//
//    // to verify the e mail
//    @GetMapping("/verifyEmail")
//    public String verifyEmail(@RequestParam("token") String token) {
//        VerificationToken theToken = tokenRepository.findByToken(token);
//        if (theToken.getUser().isEnabled()) {
//            return "This account has already been verified, please, login.";
//        }
//        String verificationResult = userService.validateToken(token);
//        if (verificationResult.equalsIgnoreCase("valid")) {
//            return "Email verified successfully. Now you can login to your account";
//        }
//        return "Invalid verification token";
//    }
//
//    // application uniform resource locator
//    public String applicationUrl(HttpServletRequest request) {
//        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
//    }
//}



@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
@Tag(name = "Registration Controller", description = "This is Registration Controller")
public class RegistrationController {

    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final UserRepository userRepository;

//    @PostMapping
//    public String registerUser(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest request) {
//        User user = userService.registerUser(registrationRequest);
//        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
//        return "Success!  Please, check your Phone for the OTP to complete your registration";
//    }


@PostMapping
public String registerUser(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest request) {
    // Generate email if it's empty

    

    String email = registrationRequest.email();
    if (email == null || email.trim().isEmpty()) {
        email = "vtt_" + System.currentTimeMillis() + "@vtt.com"; // Random email
    }

    // Set default password if empty
    String password = registrationRequest.mobileNo() + "@123";
//    if (password == null || password.trim().isEmpty()) {
//        password = registrationRequest.mobileNo() + "@123"; // Format: phonenumber@123
//    }

    // Create a new RegistrationRequest with the updated email and password
    RegistrationRequest updatedRequest = new RegistrationRequest(
            registrationRequest.userName(),
            registrationRequest.mobileNo(),
            email,
            password,
            registrationRequest.deviceType(),
            registrationRequest.latitude(),
            registrationRequest.longitude(),
            registrationRequest.registrationTermCondition()
    );

    User user = userService.registerUser(updatedRequest);
    publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
    return "Success! Please, check your Phone for the OTP to complete your registration";
}
//    @PostMapping
//    public String registerUser(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest request) {
//        // Generate email if it's empty
//        String email = registrationRequest.email();
//        if (email == null || email.trim().isEmpty()) {
//            email = "vtt_" + System.currentTimeMillis() + "@vtt.com"; // Random email
//        }
//
//        // Create a new RegistrationRequest with the updated email
//        RegistrationRequest updatedRequest = new RegistrationRequest(
//                registrationRequest.userName(),
//                registrationRequest.mobileNo(),
//                email,
//                registrationRequest.password(),
//                registrationRequest.deviceType(),
//                registrationRequest.latitude(),
//                registrationRequest.longitude(),
//                registrationRequest.registrationTermCondition()
//        );
//
//        User user = userService.registerUser(updatedRequest);
//        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
//        return "Success! Please, check your Phone for the OTP to complete your registration";
//    }


//    @PostMapping("/verifyOtp")
//    public String verifyOtp(@RequestBody OtpRequest otpRequest) {
//        User user = userService.findByEmail(otpRequest.getEmail())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        if (user.getOtp().equals(otpRequest.getOtp()) && LocalDateTime.now().isBefore(user.getOtpExpirationTime())) {
//            user.setEmailVerified(true);
//            user.setOtp(null);
//            user.setOtpExpirationTime(null);
//            userRepository.save(user);
//            return "Email verified successfully!";
//        } else {
//            return "Invalid or expired OTP";
//        }
//    }

    @PostMapping("/verifyOtp")
    public String verifyOtp(@RequestBody OtpRequest otpRequest) {
        // Find the user by phone number instead of email
        User user = userRepository.findByMobileNo(otpRequest.getMobileNo())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate the OTP and check if it's expired
        if (user.getOtp().equals(otpRequest.getOtp()) && LocalDateTime.now().isBefore(user.getOtpExpirationTime())) {
            user.setEmailVerified(true); // Or set a phone verification flag if needed
            user.setOtp(null); // Clear the OTP after successful verification
            user.setOtpExpirationTime(null); // Clear the OTP expiration time
            userRepository.save(user); // Save the updated user
            return "OTP verified successfully!";
        } else {
            return "Invalid or expired OTP";
        }
    }

//
//@PostMapping("/verifyOtp")
//public String verifyOtp(@RequestBody OtpRequest otpRequest) {
//    User user = userService.findByEmail(otpRequest.getEmail())
//            .orElseThrow(() -> new RuntimeException("User not found"));
//
//    if (user.getOtp().equals(otpRequest.getOtp()) && LocalDateTime.now().isBefore(user.getOtpExpirationTime())) {
//        user.setEmailVerified(true);
//        user.setOtp(null);
//        user.setOtpExpirationTime(null);
//        userRepository.save(user);
//        return "Email verified successfully!";
//    } else {
//        return "Invalid or expired OTP";
//    }
//}

    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}