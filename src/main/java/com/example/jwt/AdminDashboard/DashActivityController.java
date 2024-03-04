package com.example.jwt.AdminDashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashActivityController {

    @Autowired
    private demoService demoService;

    @GetMapping("/average-steps-by-age")
    public ResponseEntity<List<AverageStepsByAgeDTO>> getAverageStepsByAge() {
        List<AverageStepsByAgeDTO> statsList = demoService.getAverageStepsByAge();
        return new ResponseEntity<>(statsList, HttpStatus.OK);
    }
}
