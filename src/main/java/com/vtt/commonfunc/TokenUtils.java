package com.vtt.commonfunc;


import com.vtt.entities.User;
import com.vtt.exception.UserNotFoundException;
import com.vtt.otherclass.MainRole;
import com.vtt.repository.UserRepository;
import com.vtt.security.JwtHelper;
import org.springframework.stereotype.Component;

@Component
public class TokenUtils {

    private final JwtHelper jwtHelper;
    private final UserRepository userRepository;

    public TokenUtils(JwtHelper jwtHelper, UserRepository userRepository) {
        this.jwtHelper = jwtHelper;
        this.userRepository = userRepository;
    }

    /**
     * Gets the User object from the JWT token
     * @param token The JWT token (with or without "Bearer " prefix)
     * @return The User object
     * @throws UserNotFoundException if user not found
     */
    public User getUserFromToken(String token) throws UserNotFoundException {
        // Remove "Bearer " prefix if present
        String cleanedToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        String username = jwtHelper.getUsernameFromToken(cleanedToken);
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found for token"));
    }

    /**
     * Gets the username (email) from the JWT token
     * @param token The JWT token (with or without "Bearer " prefix)
     * @return The username (email)
     */
    public String getUsernameFromToken(String token) {
        String cleanedToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        return jwtHelper.getUsernameFromToken(cleanedToken);
    }

    /**
     * Validates if the token belongs to an ADMIN user
     * @param token The JWT token
     * @return true if user is ADMIN, false otherwise
     */
    public boolean isAdminUser(String token) {
        try {
            User user = getUserFromToken(token);
            return user.getMainRole() == MainRole.ADMIN;
        } catch (Exception e) {
            return false;
        }
    }
}