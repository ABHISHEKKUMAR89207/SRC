package com.example.jwt.controler;

import com.example.jwt.entities.User;
import com.example.jwt.entities.water.WaterEntity;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.UserService;
import com.example.jwt.service.WaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/water")
public class waterController {

    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private WaterService waterService;
    @Autowired
    private UserService userService;


    @Autowired
    public waterController(WaterService waterService) {
        this.waterService = waterService;
    }

    // to get the calculated water intake
//    @GetMapping("/calculate-intake")
//    public ResponseEntity<Double> calculateWaterIntake(@RequestHeader("Auth") String tokenHeader) {
//        String token = tokenHeader.replace("Bearer ", "");
//        String username = jwtHelper.getUsernameFromToken(token);
//        User user = userService.findByUsername(username);
//
//        Double waterIntake = waterService.calculateWaterIntake(user);
//        return new ResponseEntity<>(waterIntake, HttpStatus.OK);
//    }

    // to get the calculated water intake by date
    @GetMapping("/calculate-intake-by-date")
    public ResponseEntity<Double> calculateWaterIntake(@RequestHeader("Auth") String tokenHeader, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(username);

        Double waterIntake = waterService.getWaterIntakeForCurrentDate(user, date);
        return new ResponseEntity<>(waterIntake, HttpStatus.OK);
    }

    // to get the water intake of the last week
    @GetMapping("/calculate-intake-last-week")
    public ResponseEntity<Map<String, Double>> calculateWaterIntakeForLastWeek(@RequestHeader("Auth") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(username);

        Map<String, Double> waterIntakeMap = waterService.calculateWaterIntakeForLastWeek(user);
        return new ResponseEntity<>(waterIntakeMap, HttpStatus.OK);
    }

    // to update the water entity
//    @PostMapping("/update-water-entity")
//    public ResponseEntity<String> updateWaterEntity(@RequestHeader("Auth") String tokenHeader, @RequestBody WaterEntity waterEntity) {
//        try {
//            String token = tokenHeader.replace("Bearer ", "");
//            String username = jwtHelper.getUsernameFromToken(token);
//            waterService.saveOrUpdateWaterEntity(username, waterEntity);
//            return new ResponseEntity<>("Water entity updated successfully", HttpStatus.OK);
//        } catch (UserNotFoundException e) {
//            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
//        }
//    }
    @Autowired
    private UserRepository userRepository;
    @PostMapping("/update-water-entity")
    public ResponseEntity<String> updateWaterEntity(@RequestHeader("Auth") String tokenHeader, @RequestBody WaterEntity newWaterEntity) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            WaterEntity updatedWaterEntity = waterService.saveOrUpdateWaterEntity(username, newWaterEntity);

            return new ResponseEntity<>("Water entity updated successfully. New water intake: " + updatedWaterEntity.getWaterIntake(), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }


}
