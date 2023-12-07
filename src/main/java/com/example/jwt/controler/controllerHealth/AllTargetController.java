package com.example.jwt.controler.controllerHealth;//package com.practice.springbootimportcsvfileapp.controller;

import com.example.jwt.dtos.WaterGoalAndIntakeResponse;
import com.example.jwt.dtos.WaterGoalDto;
import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.AllTarget;
import com.example.jwt.entities.water.WaterEntity;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.UserService;
import com.example.jwt.service.WaterService;
import com.example.jwt.service.serviceHealth.AllTargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

<<<<<<< HEAD
=======
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


>>>>>>> 495700b4804df131a48b75088fdae4d03dbf4e57
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

<<<<<<< HEAD
    // to get the water goal of the specicfic user
    @GetMapping("/get-water-goal")
    public ResponseEntity<Double> getUserWaterGoal(@RequestHeader("Auth") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(username);
=======


//    @GetMapping("/get-water-goal")
//    public ResponseEntity<Double> getUserWaterGoal(
//            @RequestHeader("Auth") String tokenHeader
//    ) {
//        String token = tokenHeader.replace("Bearer ", "");
//        String username = jwtHelper.getUsernameFromToken(token);
//        User user = userService.findByUsername(username);
//        LocalDate currentDate = LocalDate.now();
//
//        // Define the date format
//        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//        // Format the current date
//        String formattedDate = currentDate.format(dateFormat);
//        user.getWaterEntities().getWaterIntake();
//        if (user != null && user.getAllTarget() != null) {
//            return new ResponseEntity<>(user.getAllTarget().getWaterGoal(), HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
>>>>>>> 495700b4804df131a48b75088fdae4d03dbf4e57

    @Autowired
    private WaterService waterService;
@GetMapping("/get-water-goal")
public ResponseEntity<WaterGoalAndIntakeResponse> getUserWaterGoal(@RequestHeader("Auth") String tokenHeader) {
    String token = tokenHeader.replace("Bearer ", "");
    String username = jwtHelper.getUsernameFromToken(token);
    User user = userService.findByUsername(username);

    if (user != null && user.getAllTarget() != null && user.getWaterEntities() != null) {
        // Get water goal from AllTarget
        Double waterGoal = user.getAllTarget().getWaterGoal();

        LocalDate currentDate = LocalDate.now();

        // Calculate total water intake for the current date
        Double waterIntakeForCurrentDate = waterService.calculateWaterIntake(user, currentDate);

        if (waterGoal != null) {
            WaterGoalAndIntakeResponse response = new WaterGoalAndIntakeResponse();
            response.setWaterGoal(waterGoal);
            response.setWaterIntakeForCurrentDate(waterIntakeForCurrentDate);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
<<<<<<< HEAD
=======
}








>>>>>>> 495700b4804df131a48b75088fdae4d03dbf4e57
}




