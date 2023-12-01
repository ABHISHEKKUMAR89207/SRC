package com.example.jwt.controler;
import com.example.jwt.dtos.WaterGoalDto;
import com.example.jwt.entities.User;
import com.example.jwt.entities.water.WaterEntity;
import com.example.jwt.entities.water.WaterGoal;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.UserService;
import com.example.jwt.service.waterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController@RequestMapping("/api/water")
public class waterController {

    @Autowired
    private  JwtHelper jwtHelper;


    @Autowired
    private  waterService waterService;

    @Autowired
    public waterController(waterService waterService) {
        this.waterService = waterService;
    }



@Autowired
private UserService userService;

//    @GetMapping("/calculate-intake")
//    public ResponseEntity<Double> calculateWaterIntake(
//            @RequestHeader("Auth") String tokenHeader
//
//    ) {
//        String token = tokenHeader.replace("Bearer ", "");
//
//        String username = jwtHelper.getUsernameFromToken(token);
//        User user = userService.findByUsername(username);
//
//
//        Double waterIntake = waterService.calculateWaterIntake(user);
//        return new ResponseEntity<>(waterIntake, HttpStatus.OK);
//    }
@GetMapping("/calculate-intake")
public ResponseEntity<Double> calculateWaterIntake(
        @RequestHeader("Auth") String tokenHeader
) {
    String token = tokenHeader.replace("Bearer ", "");
    String username = jwtHelper.getUsernameFromToken(token);
    User user = userService.findByUsername(username);

    Double waterIntake = waterService.calculateWaterIntake(user);
    return new ResponseEntity<>(waterIntake, HttpStatus.OK);
}

    @GetMapping("/calculate-intake-by-date")
    public ResponseEntity<Double> calculateWaterIntake(
            @RequestHeader("Auth") String tokenHeader,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(username);

        Double waterIntake = waterService.calculateWaterIntake(user, date);
        return new ResponseEntity<>(waterIntake, HttpStatus.OK);
    }


    @GetMapping("/calculate-intake-last-week")
    public ResponseEntity<Map<String, Double>> calculateWaterIntakeForLastWeek(
            @RequestHeader("Auth") String tokenHeader
    ) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(username);

        Map<String, Double> waterIntakeMap = waterService.calculateWaterIntakeForLastWeek(user);
        return new ResponseEntity<>(waterIntakeMap, HttpStatus.OK);
    }


//    @PostMapping("/update-water-entity")
//    public ResponseEntity<String> updateWaterEntity(
//            @RequestHeader("Auth") String tokenHeader,
//            @RequestBody WaterEntity waterEntity
//    ) {
//        try {
//            String token = tokenHeader.replace("Bearer ", "");
//
//            String username = jwtHelper.getUsernameFromToken(token);
//            waterService.saveOrUpdateWaterEntity(username, waterEntity);
//            return new ResponseEntity<>("Water entity updated successfully", HttpStatus.OK);
//        } catch (UserNotFoundException e) {
//            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
//        }
//    }


    @PostMapping("/update-water-entity")
    public ResponseEntity<String> updateWaterEntity(
            @RequestHeader("Auth") String tokenHeader,
            @RequestBody WaterEntity waterEntity
    ) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            waterService.saveOrUpdateWaterEntity(username, waterEntity);
            return new ResponseEntity<>("Water entity updated successfully", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/update-water-goal")
    public WaterGoalDto updateWaterGoal(@RequestHeader("Auth") String tokenHeader, @RequestParam Double newWaterGoal) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        WaterGoal updatedGoal = waterService.saveOrUpdateWaterGoal(username, newWaterGoal);
        return convertToDto(updatedGoal);
    }


    private WaterGoalDto convertToDto(WaterGoal waterGoal) {
        // Implement the conversion from WaterGoal entity to DTO (Data Transfer Object)
        // This is a simple example; you might want to use a library like ModelMapper for this.
        WaterGoalDto waterGoalDto = new WaterGoalDto();
        waterGoalDto.setWaterGoalId(waterGoal.getWaterGoalId());
        waterGoalDto.setWaterGoal(waterGoal.getWaterGoal());
        // Set other properties as needed
        return waterGoalDto;
    }








}
