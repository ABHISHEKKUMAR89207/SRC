package com.example.jwt.controler;

import com.example.jwt.dtos.WaterIntakeResponse;
import com.example.jwt.entities.User;
import com.example.jwt.entities.water.WaterEntity;
import com.example.jwt.entities.water.WaterEntry;
import com.example.jwt.entities.water.WaterEntryRepository;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.WaterEntityRepository;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.UserService;
import com.example.jwt.service.WaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @PostMapping("/update-water-entity1")
    public ResponseEntity<String> updateWaterEntity1(@RequestHeader("Auth") String tokenHeader, @RequestBody WaterEntity newWaterEntity) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            WaterEntity updatedWaterEntity = waterService.saveOrUpdateWaterEntity1(username, newWaterEntity);

            return new ResponseEntity<>("Water entity updated successfully. New water intake: " + updatedWaterEntity.getWaterIntake(), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @Autowired
    private WaterEntityRepository waterEntityRepository;
//    @GetMapping("/daily-intake")
//    public List<WaterIntakeResponse> getDailyWaterIntake() {
//        // Retrieve all water entities for the current date
//        List<WaterEntity> waterEntities = waterEntityRepository.findByLocalDate(LocalDate.now());
//
//        // Process the water entities and create the response
//        List<WaterIntakeResponse> response = processWaterEntities(waterEntities);
//
//        return response;
//    }


    @Autowired
    private WaterEntryRepository waterEntryRepository;
    @GetMapping("/intake")
    public List<WaterIntakeResponse> getWaterIntake() {
        LocalDate currentDate = LocalDate.now();

        List<WaterEntry> waterEntities = waterEntryRepository.findAllByLocalDate(currentDate);

        return waterEntities.stream()
                .map(this::mapToWaterIntakeResponse)
                .collect(Collectors.toList());
    }

    private WaterIntakeResponse mapToWaterIntakeResponse(WaterEntry waterEntity) {
        // Ensure water intake is calculated before mapping
        waterEntity.calculateWaterIntake();

        // Format local time as needed (you can adjust this logic based on your requirements)
        String formattedLocalTime = formatLocalTime(waterEntity.getLocalTime());

        // Format water intake (assuming waterEntity.getWaterIntake() is in milliliters)
        String formattedWaterIntake = formatWaterIntake(waterEntity.getWaterIntake());

        return new WaterIntakeResponse(formattedLocalTime, formattedWaterIntake);
    }

    private String formatLocalTime(String localTime) {
        // Assuming localTime is in the format "5:24 pm"
        LocalTime parsedLocalTime = LocalTime.parse(localTime, DateTimeFormatter.ofPattern("h:mm a"));

        // Format as "h:mm a" (e.g., "5:24 pm")
        return parsedLocalTime.format(DateTimeFormatter.ofPattern("h:mm a"));
    }

    private String formatWaterIntake(Double waterIntake) {
        // Assuming waterIntake is in milliliters
        return String.format("%.0fml", waterIntake);
    }

}
