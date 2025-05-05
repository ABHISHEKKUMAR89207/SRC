package com.vtt.event.listener;

import com.vtt.entities.User;
import com.vtt.event.RegistrationCompleteEvent;
import com.vtt.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;



//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
//    private final UserService userService;
//    private final JavaMailSender mailSender;
//
//    private User theUser;
//
//    @Override
//    public void onApplicationEvent(RegistrationCompleteEvent event) {
//        // 1. Get the newly registered user
//        theUser = event.getUser();
//        //2. Create a verification OTP for the user
//        String otp = generateOtp();
//        //3. Save the verification OTP for the user
//        userService.saveUserVerificationOtp(theUser, otp);
//        //4. Send the email.
//        try {
//            sendVerificationEmail(otp);
//        } catch (MessagingException | UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        }
//        log.info("OTP for verification: {}", otp);
//    }
//
//    private String generateOtp() {
//        // Generate a 6-digit OTP
//        return String.format("%06d", new Random().nextInt(999999));
//    }
//
////    public void sendVerificationEmail(String otp) throws MessagingException, UnsupportedEncodingException {
////        String subject = "Email Verification";
////        String senderName = "Nutrify India Now (2.O)";
////        String mailContent = "<p> Hi, " + theUser.getEmail() + ", </p>" + "<p>Thank you for registering with us," + "" + "Please, use the OTP below to complete your registration.</p>" + "<p>OTP: <strong>" + otp + "</strong></p>" + "<p> Thank you <br> Users Registration Portal Service";
////        MimeMessage message = mailSender.createMimeMessage();
////        var messageHelper = new MimeMessageHelper(message);
////        messageHelper.setFrom("rajkumariimt2002@gmail.com", senderName);
////        messageHelper.setTo(theUser.getEmail());
////        messageHelper.setSubject(subject);
////        messageHelper.setText(mailContent, true);
////        mailSender.send(message);
////    }
//
//    public void sendVerificationEmail(String otp) throws MessagingException, UnsupportedEncodingException {
//        String subject = "Your Vastra Treasure Trove OTP for Sign-In";
//        String senderName = "Vastra Treasure Trove";
//        String mailContent = "<p>Dear Valued Customer,</p>" +
//                "<p>Thank you for choosing <strong>Vastra Treasure Trove!</strong> We’re delighted to have you with us.</p>" +
//                "<p>To proceed with your sign-in, please use the One-Time Password (OTP) below:</p>" +
//                "<p style='font-size: 24px; font-weight: bold; color:#35d12a;'>" + otp + "</p>" +
//                "<p>This OTP is valid for the next <strong>10 minutes</strong>. Please do not share it with anyone for security reasons.</p>" +
//                "<p>If you did not request this OTP, please ignore this email.</p>" +
//                "<p>Best regards,</p>" +
//                "<p><strong>Vastra Treasure Trove Team</strong></p>";
//
//        MimeMessage message = mailSender.createMimeMessage();
//        var messageHelper = new MimeMessageHelper(message);
//        messageHelper.setFrom("abhi@gmail.com", senderName);
//        messageHelper.setTo(theUser.getEmail());
//        messageHelper.setSubject(subject);
//        messageHelper.setText(mailContent, true);
//        mailSender.send(message);
//    }
//
//}




@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    private final UserService userService;
    private final JavaMailSender mailSender;

    private User theUser;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // 1. Get the newly registered user
        theUser = event.getUser();
        // 2. Create a verification OTP for the user
        String otp = generateOtp();
        // 3. Save the verification OTP for the user
        userService.saveUserVerificationOtp(theUser, otp);
        // 4. Send the OTP via SMS
        sendVerificationSms(otp);
        log.info("OTP for verification: {}", otp);
    }

    private String generateOtp() {
        // Generate a 6-digit OTP
        return String.format("%06d", new Random().nextInt(999999));
    }

    private void sendVerificationSms(String otp) {
        String apiKey = "75b451ae-0876-11f0-8b17-0200cd936042"; // Replace with your 2Factor API key


        //real


//        String apiKey = "0ee779f7-0736-11f0-8b17-0200cd936042";
        String phoneNumber = theUser.getMobileNo(); // Assuming the user's phone number is stored in the mobileNo field
        String url = String.format("https://2factor.in/API/V1/%s/SMS/%s/%s", apiKey, phoneNumber, otp);

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                log.info("OTP sent successfully to {}", phoneNumber);
            } else {
                log.error("Failed to send OTP to {}. Response code: {}", phoneNumber, responseCode);
            }
        } catch (Exception e) {
            log.error("Exception occurred while sending OTP: {}", e.getMessage());
        }
    }
}