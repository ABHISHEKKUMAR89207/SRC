package com.example.jwt.AdminDashboard;


import com.example.jwt.entities.User;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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

//    @GetMapping("/average")
//    public ResponseEntity<List<SleepDurationStatsDTO>> getAverageSleepDurationByAgeAndGender() {
//        List<SleepDurationStatsDTO> stats = sleepDurationService.getAverageSleepDurationByAgeAndGender();
//        return new ResponseEntity<>(stats, HttpStatus.OK);
//    }

    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private UserService userService;

//    @RequestHeader("Auth") String tokenHeader
//@GetMapping("/average-sleep")
//public ResponseEntity<List<SleepDurationStatsDTO>> getAverageSleepDurationByAgeAndGender( @RequestHeader("Auth") String tokenHeader) {
//    try {
//        String token = tokenHeader.replace("Bearer ", "");
//        String username = jwtHelper.getUsernameFromToken(token);
//        User user1 = userService.findByUsername(username);
//
//        // Check if the user is authenticated
////        if (user1 == null) {
////            // User is not authenticated, return an appropriate response
////            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
////        }
//        List<SleepDurationStatsDTO> stats = sleepDurationService.getAverageSleepDurationByAgeAndGender();
//        return new ResponseEntity<>(stats, HttpStatus.OK);
//    } catch (Exception e) {
//        // Handle exception
//        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//}

//    @GetMapping("/averageCalories")
//    public ResponseEntity<List<ActivitiesStatsDTO>> getAverageCaloriesByAgeAndGender() {
//        List<ActivitiesStatsDTO> stats = sleepDurationService.getAverageCaloriesByAgeAndGender();
//        return new ResponseEntity<>(stats, HttpStatus.OK);
//    }
//@GetMapping("/averageCalories")
//public ResponseEntity<List<ActivitiesStatsDTO>> getAverageCaloriesByAgeAndGender( @RequestHeader("Auth") String tokenHeader) {
//    String token = tokenHeader.replace("Bearer ", "");
//    String username = jwtHelper.getUsernameFromToken(token);
//    User user1 = userService.findByUsername(username);
//
////    // Check if the user is authenticated
////    if (user1 == null) {
////        // User is not authenticated, return an appropriate response
////        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
////    }
//    List<ActivitiesStatsDTO> stats = sleepDurationService.getAverageCaloriesByAgeAndGender();
//    if (stats == null) {
//        // Handle the case where stats is null, perhaps by returning an error response
//        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//    return new ResponseEntity<>(stats, HttpStatus.OK);
//}

    @GetMapping("/totalStepCountAndKms")
    public ResponseEntity<StepCountDTO> getTotalStepCountAndKms() {
        StepCountDTO stepCountDTO = demoService.getTotalStepCountAndKms();
        return new ResponseEntity<>(stepCountDTO, HttpStatus.OK);
    }


}
