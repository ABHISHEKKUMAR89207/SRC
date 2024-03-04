package com.example.jwt.AdminDashboard;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboardDemo")
@CrossOrigin(origins = "http://localhost:3000")
public class demoController {

    private final demoService demoService;

    public demoController(demoService demoService) {
        this.demoService = demoService;
    }


    @Autowired
    private demoService sleepDurationService;

    @GetMapping("/average")
    public ResponseEntity<List<SleepDurationStatsDTO>> getAverageSleepDurationByAgeAndGender() {
        List<SleepDurationStatsDTO> stats = sleepDurationService.getAverageSleepDurationByAgeAndGender();
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    @GetMapping("/averageCalories")
    public ResponseEntity<List<ActivitiesStatsDTO>> getAverageCaloriesByAgeAndGender() {
        List<ActivitiesStatsDTO> stats = sleepDurationService.getAverageCaloriesByAgeAndGender();
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    @GetMapping("/totalStepCountAndKms")
    public ResponseEntity<StepCountDTO> getTotalStepCountAndKms() {
        StepCountDTO stepCountDTO = demoService.getTotalStepCountAndKms();
        return new ResponseEntity<>(stepCountDTO, HttpStatus.OK);
    }


}
