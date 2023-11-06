package com.example.jwt.controler;


import com.example.jwt.entities.UserProfile;
import com.example.jwt.entities.dashboardEntity.FitnessActivity;
import com.example.jwt.service.FitnessServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fitness")
@Tag(name = "FitnessController", description = "Api for Authentication")
public class FitnessController {
    @Autowired
    private FitnessServiceImpl fitnessService;

    @PostMapping("/activity")
    public ResponseEntity<FitnessActivity> saveFitnessActivity(@RequestBody FitnessActivity activity) {
        FitnessActivity savedActivity = fitnessService.saveFitnessActivity(activity);
        return ResponseEntity.ok(savedActivity);
//        return ResponseEntity.created(null).body(savedActivity);
    }

    @GetMapping("/activity/{type}")
    public ResponseEntity<List<FitnessActivity>> getActivitiesByType(@PathVariable String type) {
        List<FitnessActivity> activities = fitnessService.getActivitiesByType(type);
        return ResponseEntity.ok(activities);
    }

//    @GetMapping("/activity/BMI")
//    public ResponseEntity<List<UserProfile>> getActivitiesBy(@PathVariable UserProfile userProfile) {
//        List<UserProfile> profileBMI = (List<UserProfile>) fitnessService.saveBMI(userProfile);
//        return ResponseEntity.ok(profileBMI);
//    }


    //Calculate BMI
    @PostMapping("/activity/BMI/{userProfileId}")
    public ResponseEntity<UserProfile> calculateAndSaveBMI(@PathVariable Long userProfileId) {
        UserProfile updatedUserProfile = fitnessService.calculateAndSaveBMI(userProfileId);
        return ResponseEntity.ok(updatedUserProfile);
    }


//    @GetMapping("/activity/{profileId}/BMI")
//    public ResponseEntity<UserProfile> getActivitiesByBmi(
//            @RequestBody UserProfile userProfile,
////            @PathVariable UserProfile userProfile
//            @PathVariable Long profileId
//    ) {
////        List<UserProfile> profileBMI = (List<UserProfile>) fitnessService.saveBMI(userProfile, profileId);
//
//        UserProfile userProfile1 = this.fitnessService.saveBMI(userProfile,profileId);
//
//        return new ResponseEntity<UserProfile>(userProfile1, HttpStatus.CREATED);
//    }

}
