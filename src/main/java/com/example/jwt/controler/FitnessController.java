package com.example.jwt.controler;

import com.example.jwt.entities.UserProfile;
import com.example.jwt.entities.dashboardEntity.FitnessActivity;
import com.example.jwt.service.FitnessServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fitness")
@Tag(name = "FitnessController", description = "Api for Authentication")
public class FitnessController {
    @Autowired
    private FitnessServiceImpl fitnessService;

    // to save the fitness activity of the user
    @PostMapping("/activity")
    public ResponseEntity<FitnessActivity> saveFitnessActivity(@RequestBody FitnessActivity activity) {
        FitnessActivity savedActivity = fitnessService.saveFitnessActivity(activity);
        return ResponseEntity.ok(savedActivity);
    }

    // to get the fitness activities by its type
    @GetMapping("/activity/{type}")
    public ResponseEntity<List<FitnessActivity>> getActivitiesByType(@PathVariable String type) {
        List<FitnessActivity> activities = fitnessService.getActivitiesByType(type);
        return ResponseEntity.ok(activities);
    }

    //Calculate BMI
    @PostMapping("/activity/BMI/{userProfileId}")
    public ResponseEntity<UserProfile> calculateAndSaveBMI(@PathVariable Long userProfileId) {
        UserProfile updatedUserProfile = fitnessService.calculateAndSaveBMI(userProfileId);
        return ResponseEntity.ok(updatedUserProfile);
    }
}
