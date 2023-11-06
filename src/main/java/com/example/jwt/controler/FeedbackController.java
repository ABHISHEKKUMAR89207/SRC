package com.example.jwt.controler;

import com.example.jwt.entities.Feedback;
import com.example.jwt.entities.User;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;
    private final JwtHelper jwtHelper;
    private final UserRepository userRepository;

    @Autowired
    public FeedbackController(FeedbackService feedbackService, JwtHelper jwtHelper, UserRepository userRepository) {
        this.feedbackService = feedbackService;
        this.jwtHelper = jwtHelper;
        this.userRepository = userRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<Feedback> addFeedback(@RequestHeader("Auth") String tokenHeader,  @RequestBody Feedback feedback) {

        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found for email: " + username));

        Feedback savedFeedback = feedbackService.saveFeedback(user,feedback);
        return new ResponseEntity<>(savedFeedback, HttpStatus.CREATED);
    }
}