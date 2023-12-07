package com.example.jwt.service;

import com.example.jwt.entities.Feedback;
import com.example.jwt.entities.User;
import com.example.jwt.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public Feedback saveFeedback(User user, Feedback feedback) {
        feedback.setUser(user);
        feedback.setLocalDateTime(LocalDateTime.now());

        return feedbackRepository.save(feedback);
    }
}