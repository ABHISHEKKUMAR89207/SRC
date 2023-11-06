package com.example.jwt.event.listener;


import com.example.jwt.entities.User;
import com.example.jwt.event.RegistrationCompleteEvent;
import com.example.jwt.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.context.ApplicationListener;


import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;



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
        //2. Create a verification token for the user
        String verificationToken = UUID.randomUUID().toString();
        //3. Save the verification token for the user
        userService.saveUserVerificationToken(theUser, verificationToken);
        //4 Build the verification url to be sent to the user
        String url = event.getApplicationUrl()+"/register/verifyEmail?token="+verificationToken;
        //5. Send the email.
        try {
            sendVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info("Click the link to verify your registration :  {}", url);
    }
    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String senderName = "Nutrify India Now (2.O)";
        String mailContent = "<p> Hi, "+ theUser.getEmail()+ ", </p>"+
                "<p>Thank you for registering with us,"+"" +
                "Please, follow the link below to complete your registration.</p>"+
                "<a href=\"" +url+ "\">Verify your email to activate your account</a>"+
                "<p> Thank you <br> Users Registration Portal Service";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("rajkumariimt2002@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }


//    @Autowired
//    public RegistrationCompleteEventListener(JavaMailSender mailSender) {
//        this.mailSender = mailSender;
//    }

//    @EventListener
//    public void handleRegistrationCompleteEvent(RegistrationCompleteEvent event) {
//        // Use the mailSender to send emails here
//        // For example, sending a simple email
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(event.getUser().getEmail());
//        message.setSubject("Registration Complete");
//        message.setText("Thank you for registering!");
//
//        mailSender.send(message);
//    }

}
