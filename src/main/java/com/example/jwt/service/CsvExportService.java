package com.example.jwt.service;

import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.Activities;
import com.example.jwt.repository.ActivityRepository;

import com.example.jwt.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

//@Service
//@Transactional
//public class CsvExportService {
//
//    @Autowired
//    public CsvExportService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Autowired
////    private UserRepository userRepository;
//    private final UserRepository userRepository;
//    public List<User> listAll() {
//
//        return userRepository.findAll();
//    }
//}
//


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

//    @Autowired
//    private UserRepository userRepository;
//    private final UserRepository userRepository;

//    public List<Activities> listAll() {
//
//        return activityRepository.findAll();
//    }
public List<User> listAll() {

    return userRepository.findAll();
}
}








