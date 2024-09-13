//package com.example.jwt.service;
//
//
//
//import com.google.firebase.FirebaseException;
//import com.google.firebase.auth.FirebaseAuth;
//
//import org.springframework.stereotype.Service;
//
//@Service
//public class FirebaseAuthService {
//
//    private final FirebaseAuth firebaseAuth;
//
//    public FirebaseAuthService(FirebaseAuth firebaseAuth) {
//        this.firebaseAuth = firebaseAuth;
//    }
//
//    public void sendOtp(String phoneNumber) throws Exception {
//        // Generate a verification ID
//        PhoneAuthProvider.ProviderVerificationToken token = PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                phoneNumber,
//                60L, // Timeout duration
//                java.util.concurrent.TimeUnit.SECONDS,
//                // If you want to handle this in a callback
//                /*executor=*/ null,
//                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                    @Override
//                    public void onVerificationCompleted(PhoneAuthCredential credential) {
//                        // Auto-retrieval or instant verification has succeeded
//                    }
//
//                    @Override
//                    public void onVerificationFailed(FirebaseException e) {
//                        // Verification failed
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
//                        // The OTP is sent. You should save the verificationId and token for later use
//                    }
//                });
//    }
//}
