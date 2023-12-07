package com.example.jwt.service;

import com.example.jwt.entities.User;
import com.example.jwt.repository.ActivityRepository;
import com.example.jwt.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CsvExportService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    @Autowired
    public CsvExportService(ActivityRepository activityRepository, UserRepository userRepository) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
    }

    public List<User> listAll() {

        return userRepository.findAll();
    }
}








