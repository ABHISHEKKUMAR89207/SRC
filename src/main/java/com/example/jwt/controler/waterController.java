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
import java.util.*;
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
//    @GetMapping("/calculate-intake-last-week")
//    public ResponseEntity<Map<String, Double>> calculateWaterIntakeForLastWeek(@RequestHeader("Auth") String tokenHeader) {
//        String token = tokenHeader.replace("Bearer ", "");
//        String username = jwtHelper.getUsernameFromToken(token);
//        User user = userService.findByUsername(username);
//
//        Map<String, Double> waterIntakeMap = waterService.calculateWaterIntakeForLastWeek(user);
//        return new ResponseEntity<>(waterIntakeMap, HttpStatus.OK);
//    }

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
//    @PostMapping("/update-water-entity")
//    public ResponseEntity<String> updateWaterEntity(@RequestHeader("Auth") String tokenHeader, @RequestBody WaterEntity newWaterEntity) {
//        try {
//            String token = tokenHeader.replace("Bearer ", "");
//            String username = jwtHelper.getUsernameFromToken(token);
//            WaterEntity updatedWaterEntity = waterService.saveOrUpdateWaterEntity(username, newWaterEntity);
//
//            return new ResponseEntity<>("Water entity updated successfully. New water intake: " + updatedWaterEntity.getWaterIntake(), HttpStatus.OK);
//        } catch (UserNotFoundException e) {
//            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
//        }
//    }


//    @GetMapping("/get-water-intake/custom-range")
//    public ResponseEntity<Map<LocalDate, Double>> getWaterIntakeForCustomRange(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
//            @RequestHeader("Auth") String tokenHeader) {
//        try {
//            String token = tokenHeader.replace("Bearer ", "");
//            String username = jwtHelper.getUsernameFromToken(token);
//
//            Optional<User> userOptional = userRepository.findByEmail(username);
//
//            if (userOptional.isPresent()) {
//                User user = userOptional.get();
//
//                // Retrieve water entries within the custom date range
//                List<WaterEntity> waterEntries = waterEntityRepository.findByUserAndLocalDateBetween(user, startDate, endDate);
//
//                // Create a map to store water intake for each date
//                Map<LocalDate, Double> waterIntakeMap = new HashMap<>();
//
//                // Populate the water intake map with data from the retrieved entities
//                for (WaterEntity waterEntity : waterEntries) {
//                    waterIntakeMap.put(waterEntity.getLocalDate(), waterEntity.calculateTotalWaterIntake());
//                }
//
//                return ResponseEntity.ok(waterIntakeMap);
//            } else {
//                throw new UserNotFoundException("User not found for username: " + username);
//            }
//        } catch (UserNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }



    @PostMapping("/update-water-entity1")
    public ResponseEntity<String> updateWaterEntity1(@RequestHeader("Auth") String tokenHeader, @RequestBody WaterEntity newWaterEntity) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            WaterEntity updatedWaterEntity = waterService.saveOrUpdateWaterEntity1(username, newWaterEntity);

            return new ResponseEntity<>("Water entity updated successfully. New water intake: ", HttpStatus.OK);
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
//    @GetMapping("/intake")
//    public List<WaterIntakeResponse> getWaterIntake() {
//        LocalDate currentDate = LocalDate.now();
//
//        List<WaterEntry> waterEntities = waterEntryRepository.findAllByLocalDate(currentDate);
//
//        return waterEntities.stream()
//                .map(this::mapToWaterIntakeResponse)
//                .collect(Collectors.toList());
//    }
//@GetMapping("/intake")
//public ResponseEntity<List<WaterIntakeResponse>> getWaterIntake(@RequestHeader("Auth") String tokenHeader) {
//    try {
//        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        // Call a method to verify the user or perform additional authentication logic if needed
//        // Example: userService.verifyUser(username);
//
//        LocalDate currentDate = LocalDate.now();
//
//        List<WaterEntry> waterEntities = waterEntryRepository.findAllByLocalDate(currentDate);
//
//        List<WaterIntakeResponse> waterIntakeResponses = waterEntities.stream()
//                .map(this::mapToWaterIntakeResponse)
//                .collect(Collectors.toList());
//
//        return new ResponseEntity<>(waterIntakeResponses, HttpStatus.OK);
//    } catch (Exception e) {
//        // Handle authentication or other exceptions
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//    }
//}
@GetMapping("/get-water/custom-range")
public ResponseEntity<List<Map<String, Object>>> getUserWaterIntakeForCustomRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestHeader("Auth") String tokenHeader) {
    try {
        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the username to fetch the userId from your user service
        User user = userService.findByUsername(username);

        if (user != null) {
            // Get water entries for the custom date range and user
            List<WaterEntity> waterEntriesList = waterService.getWaterEntriesForUserAndCustomRange(user, startDate, endDate);

            // Create a map to store water entry details for each day
            Map<LocalDate, Double> waterEntriesMap = new HashMap<>();

            // Populate the map with existing water entries
            for (WaterEntity waterEntry : waterEntriesList) {
                waterEntriesMap.put(waterEntry.getLocalDate(), waterEntry.calculateTotalWaterIntake());
            }

            // Create a list to store water entry details for each day
            List<Map<String, Object>> waterEntriesForRange = new ArrayList<>();

            // Iterate over each day in the date range
            for (LocalDate currentDate = startDate; !currentDate.isAfter(endDate); currentDate = currentDate.plusDays(1)) {
                // Create a map with formatted localDate and waterIntake
                Map<String, Object> waterEntryMap = new HashMap<>();
                String formattedLocalDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                waterEntryMap.put("date", formattedLocalDate);
                waterEntryMap.put("value", waterEntriesMap.getOrDefault(currentDate, 0.0));

                waterEntriesForRange.add(waterEntryMap);
            }

            return ResponseEntity.ok(waterEntriesForRange);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}



    @GetMapping("/intake")
    public ResponseEntity<List<WaterIntakeResponse>> getWaterIntake(@RequestHeader("Auth") String tokenHeader) {
        try {
            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);

            // Call a method to verify the user or perform additional authentication logic if needed
            // Example: userService.verifyUser(username);

            // Find the user by username
            User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

            // Get the WaterEntity for the user and current date
            LocalDate currentDate = LocalDate.now();
            WaterEntity waterEntity = waterEntityRepository.findByUserAndLocalDate(user, currentDate);

            // Check if WaterEntity exists for the current date
            if (waterEntity != null) {
                // Fetch WaterEntries for the WaterEntity
                List<WaterEntry> waterEntries = waterEntity.getWaterEntries();

                // Map WaterEntries to WaterIntakeResponse
                List<WaterIntakeResponse> waterIntakeResponses = waterEntries.stream()
                        .map(this::mapToWaterIntakeResponse)
                        .collect(Collectors.toList());

                return new ResponseEntity<>(waterIntakeResponses, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
            }
        } catch (Exception e) {
            // Handle authentication or other exceptions
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }


//    private WaterIntakeResponse mapToWaterIntakeResponse(WaterEntry waterEntry) {
//        // Map WaterEntry to WaterIntakeResponse as needed
//        // Example:
//        // WaterIntakeResponse response = new WaterIntakeResponse();
//        // response.setAmount(waterEntry.getAmount());
//        // response.setTimestamp(waterEntry.getTimestamp());
//        // return response;
//    }


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

//    private String formatWaterIntake(Double waterIntake) {
//        // Assuming waterIntake is in milliliters
//        return String.format("%.0fl", waterIntake);
//    }

//    private String formatWaterIntake(Double waterIntakeInMilliliters) {
//        // Assuming waterIntakeInMilliliters is already in milliliters
//        return String.format("%.1fL", waterIntakeInMilliliters);
//    }

    private String formatWaterIntake(Double waterIntakeInMilliliters) {
        // Assuming waterIntakeInMilliliters is already in milliliters
        return String.valueOf(waterIntakeInMilliliters) + "L";
    }


}
