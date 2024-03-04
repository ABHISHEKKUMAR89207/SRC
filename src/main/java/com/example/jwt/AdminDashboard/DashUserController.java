package com.example.jwt.AdminDashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashUserController {
    @Autowired
    private demoService demoService;

    @GetMapping("/total-users")
    public ResponseEntity<TotalUsersDTO> getTotalUsers() {
        TotalUsersDTO totalUsersDTO = demoService.getTotalUsers();
        return new ResponseEntity<>(totalUsersDTO, HttpStatus.OK);
    }

    @GetMapping("/byWorkLevel")
    public ResponseEntity<UsersByWorkLevelDTO> getUsersByWorkLevel() {
        UsersByWorkLevelDTO usersByWorkLevelDTO = demoService.getUsersByWorkLevel();
        return new ResponseEntity<>(usersByWorkLevelDTO, HttpStatus.OK);
    }

//    @GetMapping("/totalStepCountAndKms")
//    public ResponseEntity<StepCountDTO> getTotalStepCountAndKms() {
//        StepCountDTO stepCountDTO = demoService.getTotalStepCountAndKms();
//        return new ResponseEntity<>(stepCountDTO, HttpStatus.OK);
//    }
}
