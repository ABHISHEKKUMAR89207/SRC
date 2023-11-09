package com.example.jwt.controler;

import com.example.jwt.entities.User;
import com.example.jwt.registration.token.VerificationTokenRepository;
import com.example.jwt.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/register/resendVerificationEmail")
public class ReSendMailVerificationController {



    @Autowired
    private UserService userService;

    @Autowired
    private VerificationTokenRepository tokenRepository;
    @Autowired
    private JavaMailSender mailSender;

//
//    @GetMapping()
//    public String resendVerificationEmail(@RequestParam("email") String email) {
//        // Step 1: Dhundhein agar email se user milta hai
//        Optional<User> userOptional = userService.findByEmail(email);
//
//        if (userOptional.isPresent()) {
//            User theUser = userOptional.get();
//
//            // Check karein ki user ka email pehle se verified hai ya nahi
//            if (theUser.isEmailVerified()) {
//                return "Email has already been verified. Please log in.";
//            }
//
//            // Step 2: Generate a new verification token
//            String newVerificationToken;
//            do {
//                newVerificationToken = generateNewVerificationToken();
//            } while (tokenRepository.findByToken(newVerificationToken) != null);
//
//            // Step 3: Update the user's token in the database
//            userService.saveUserVerificationToken(theUser, newVerificationToken);
//
//            // Step 4: Send the new verification email with the new token using the EmailService
//            sendVerificationEmail(theUser, newVerificationToken);
//
//            return "A new verification email has been sent. Please check your email.";
//        } else {
//            return "User not found. Please check the email address.";
//        }
//    }


    @GetMapping("/resendVerification")
    public String resendVerificationEmail(@RequestParam("email") String email) {
        // Step 1: Search for a user with the provided email
        Optional<User> userOptional = userService.findByEmail(email);

        if (userOptional.isPresent()) {
            User theUser = userOptional.get();

            // Check if the user's email is already verified
            if (theUser.isEmailVerified()) {
                return "Email has already been verified. Please log in.";
            }

            // Step 2: Generate a new verification token
            String newVerificationToken;
            do {
                newVerificationToken = generateNewVerificationToken();
            } while (tokenRepository.findByToken(newVerificationToken) != null);

            // Step 3: Update the user's token in the database
            userService.saveUserVerificationToken(theUser, newVerificationToken);

            // Step 4: Send the new verification email with the new token using your EmailService
            sendVerificationEmail(theUser, newVerificationToken);

            return "A new verification email has been sent. Please check your email.";
        } else {
            return "User not found. Please check the email address.";
        }
    }



//    @GetMapping("/resend/verifyEmail")
//    public String verifyEmail(@RequestParam("token") String verificationToken, @RequestParam(value = "resend", required = false) boolean isResend) {
//        String verificationResult = userService.validateToken(verificationToken);
//
//        if (verificationResult != null && verificationResult.equalsIgnoreCase("valid")) {
//            User user = userService.findByVerificationToken(verificationToken);
//
//            if (user != null) {
//                // Set the user's emailVerified field to true
//                user.setEmailVerified(true);
//
//                // Save the changes to the database
//                userService.saveUser(user);
//
//                if (isResend) {
//                    // For a resend verification, check if the user's email was already verified
//                    if (user.isEmailVerified()) {
//                        return "Email has already been verified. You can log in.";
//                    } else {
//                        return "Resend email verification successful. You can now log in.";
//                    }
//                } else {
//                    // For the initial verification, return the standard success message
//                    return "Email verification successful. You can now log in.";
//                }
//            } else {
//                // Token is valid, but the user is not found, handle this situation as needed
//                return "User not found. Please contact support.";
//            }
//        } else {
//            // Email verification failed, you can redirect to an error page or display an error message
//            return "Email verification failed. Please check your email or request a new verification link.";
//        }
//    }
//
//
//
//


    // Helper method to generate a new verification token
    private String generateNewVerificationToken() {
        return UUID.randomUUID().toString();
    }




    public void sendVerificationEmail(User theUser, String verificationToken) {
        String subject = "Email Verification";
        String senderName = "Nutrify India Now (2.O)";
        String mailContent = "<p> Hi, " + theUser.getEmail() + ", </p>" +
                "<p>Thank you for registering with us. Please follow the link below to complete your registration:</p>" +
                "<a href=\"http://localhost:7073/register/verifyEmail?token=" + verificationToken + "\">Verify your email to activate your account</a>" +
                "<p> Thank you <br> Users Registration Portal Service";


        try {
            MimeMessage message = mailSender.createMimeMessage();
            var messageHelper = new MimeMessageHelper(message);
            messageHelper.setFrom("rajkumariimt2002@gmail.com", senderName);
            messageHelper.setTo(theUser.getEmail());
            messageHelper.setSubject(subject);
            messageHelper.setText(mailContent, true);
            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            // Handle the exceptions here, for example, log the error or throw a custom exception
            throw new RuntimeException("Error sending email", e);
        }
    }



}
