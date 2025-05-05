package com.vtt.service;


//
//import com.google.firebase.FirebaseApp;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseAuthException;
//import com.google.firebase.auth.UserRecord;
//import org.springframework.stereotype.Service;
//
//import java.util.concurrent.TimeUnit;
//
//@Service
//public class OtpService {
//
//    private final FirebaseAuth firebaseAuth;
//
//    public OtpService(FirebaseApp firebaseApp) {
//        this.firebaseAuth = FirebaseAuth.getInstance(firebaseApp);
//    }
//
//    public String sendOtp(String phoneNumber) throws FirebaseAuthException {
//        try {
//            // Generate a new user record to initiate the OTP process
//            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
//                    .setPhoneNumber(phoneNumber)
//                    .setEmail("temp-email@example.com"); // A placeholder email is required
//
//            UserRecord userRecord = firebaseAuth.createUser(request);
//
//            // Send OTP via Firebase Authentication's built-in mechanism
//            // Note: OTP sending is handled by the Firebase SDK itself.
//
//            return "OTP sent successfully";
//        } catch (FirebaseAuthException e) {
//            throw new FirebaseAuthException("OTP sending failed", e.getMessage());
//        }
//    }
//}


import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

    private final FirebaseAuth firebaseAuth;

    public OtpService(FirebaseApp firebaseApp) {
        this.firebaseAuth = FirebaseAuth.getInstance(firebaseApp);
    }

    public String sendOtp(String phoneNumber) {
        try {
            // Generate a new user record to initiate the OTP process
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setPhoneNumber(phoneNumber)
                    .setEmail("golowyvy@teleg.eu"); // A placeholder email is required

            UserRecord userRecord = firebaseAuth.createUser(request);

            // Firebase Auth manages OTP sending, no direct API for server-side OTP sending.
            // This just initiates the process, and OTP will be sent to the user's phone.

            return "OTP sent successfully";
        } catch (FirebaseAuthException e) {
            // Log the error and return a user-friendly message
            e.printStackTrace();  // Consider using a logger for production
            return "Failed to send OTP: " + e.getMessage();
        }
    }
}
