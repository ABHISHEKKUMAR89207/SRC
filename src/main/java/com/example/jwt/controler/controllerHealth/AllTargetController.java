package com.example.jwt.controler.controllerHealth;//package com.practice.springbootimportcsvfileapp.controller;

import com.example.jwt.dtos.WaterGoalDto;
import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.AllTarget;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.UserService;
import com.example.jwt.service.serviceHealth.AllTargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/all-target")
public class AllTargetController {

    private final UserRepository userRepository;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private AllTargetService allTargetService;
    @Autowired
    private UserService userService;

    public AllTargetController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Set or update user's sleep target
    @PutMapping("/sleepTarget")
    public ResponseEntity<AllTarget> setOrUpdateSleepTarget(@RequestHeader("Auth") String tokenHeader, @RequestBody AllTarget sleepTarget) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);
            // Use the extracted username to associate the heart rate data with the user
            sleepTarget = allTargetService.SleepTarget(sleepTarget, username);
            System.out.println("Username is " + username);
            return new ResponseEntity<>(sleepTarget, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("Username is " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // to get the user's sleep target
    @GetMapping("/get-sleep-target")
    public ResponseEntity<Integer> getUserSleepTarget(@RequestHeader("Auth") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(username);

        if (user != null && user.getAllTarget() != null) {
            return new ResponseEntity<>(user.getAllTarget().getSleepTarget(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // to update the water goal of the user
    @PostMapping("/update-water-goal")
    public WaterGoalDto updateWaterGoal(@RequestHeader("Auth") String tokenHeader, @RequestParam Double newWaterGoal) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        AllTarget updatedGoal = allTargetService.saveOrUpdateWaterGoal(username, newWaterGoal);
        return convertToDto(updatedGoal);
    }

    // it is a DTO class which is used to send the specific amount of data
    private WaterGoalDto convertToDto(AllTarget waterGoal) {
        WaterGoalDto waterGoalDto = new WaterGoalDto();
        waterGoalDto.setWaterGoalId(waterGoal.getTargetId());
        waterGoalDto.setWaterGoal(waterGoal.getWaterGoal());
        return waterGoalDto;
    }

    // to get the water goal of the specicfic user
    @GetMapping("/get-water-goal")
    public ResponseEntity<Double> getUserWaterGoal(@RequestHeader("Auth") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(username);

        if (user != null && user.getAllTarget() != null) {
            return new ResponseEntity<>(user.getAllTarget().getWaterGoal(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}




