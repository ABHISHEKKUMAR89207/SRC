package com.example.jwt.registration.token;

import com.example.jwt.entities.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class VerificationToken {
    private static final int TOKEN_LENGTH = 32; // Length of the token in characters
    private static final int EXPIRATION_TIME = 15;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Date expirationTime;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public VerificationToken(String token, User user) {
        super();
        this.token = token;
        this.user = user;
        this.expirationTime = this.getTokenExpirationTime();
    }

    public static String generateNewVerificationToken() {
        // Generate a random part
        SecureRandom random = new SecureRandom();
        byte[] randomBytes = new byte[TOKEN_LENGTH];
        random.nextBytes(randomBytes);
        String randomPart = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

        // Generate a unique timestamp-based part
        long timestamp = System.currentTimeMillis();
        String timestampPart = Long.toString(timestamp);

        // Combine random and timestamp parts
        String combinedToken = randomPart + timestampPart;

        // Add a UUID to ensure uniqueness
        String uniqueToken = combinedToken + "-" + UUID.randomUUID();

        return uniqueToken;
    }

    public Date getTokenExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
        return new Date(calendar.getTime().getTime());
    }
}
