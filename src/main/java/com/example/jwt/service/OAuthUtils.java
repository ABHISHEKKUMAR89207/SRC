package com.example.jwt.service;

import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
@Service
public class OAuthUtils {
    // Generate a cryptographically random code verifier
    public static String generateCodeVerifier() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] codeVerifierBytes = new byte[64];
        secureRandom.nextBytes(codeVerifierBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifierBytes);
    }

    // Generate a SHA-256 hash of the code verifier and encode it as a Base64Url string
    public static String generateCodeChallenge(String codeVerifier) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] codeVerifierBytes = codeVerifier.getBytes();
        byte[] codeChallengeBytes = messageDigest.digest(codeVerifierBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(codeChallengeBytes);
    }

    public static void main(String[] args) {
        try {
            // Step 1: Generate Code Verifier
            String codeVerifier = generateCodeVerifier();

            // Step 2: Generate Code Challenge
            String codeChallenge = generateCodeChallenge(codeVerifier);

            // Print the results
            System.out.println("Code Verifier: " + codeVerifier);
            System.out.println("Code Challenge: " + codeChallenge);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
