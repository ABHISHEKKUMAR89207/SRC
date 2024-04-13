package com.example.jwt.controler;

import com.example.jwt.entities.Circumference;
import com.example.jwt.entities.User;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.CircumferenceService;
import com.example.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CircumferenceController {

private final CircumferenceService circumferenceService;

    @Autowired
    public CircumferenceController(CircumferenceService circumferenceService) {
        this.circumferenceService = circumferenceService;
    }

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserService userService;


    @PostMapping("/hip-circumference")
    public ResponseEntity<Map<String, String>> createOrUpdateHipCircumference(
            @RequestHeader("Auth") String tokenHeader,
            @RequestParam String hipCircumference
    ) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            User user = userService.findByUsername(username);

            Circumference createdCircumference = circumferenceService.saveOrUpdateHipCircumference(hipCircumference, user);

            // Construct JSON response
            Map<String, String> response = new HashMap<>();
            response.put("hipCircumference", hipCircumference);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/waist-circumference")
    public ResponseEntity<Map<String, String>> createOrUpdateWaistCircumference(
            @RequestHeader("Auth") String tokenHeader,
            @RequestParam String waistCircumference
    ) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            User user = userService.findByUsername(username);

            Circumference createdCircumference = circumferenceService.saveOrUpdateWaistCircumference(waistCircumference, user);

            // Construct JSON response
            Map<String, String> response = new HashMap<>();
            response.put("waistCircumference", waistCircumference);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//@GetMapping("/hip-circumference/{date}")
//public Map<String, Object> getHipCircumferenceByDate(@RequestHeader("Auth") String tokenHeader, @PathVariable String date) {
//    Map<String, Object> response = new HashMap<>();
//    try {
//        String token = tokenHeader.replace("Bearer ", "");
//        String username = jwtHelper.getUsernameFromToken(token);
//        User user = userService.findByUsername(username);
//
//        LocalDate measurementDate = LocalDate.parse(date); // Parse date from the request
//
//        String hipCircumference = circumferenceService.getHipCircumferenceByDate(measurementDate, user);
//
//        response.put("date", date);
//        response.put("hipCircumference", hipCircumference);
//
//    } catch (Exception e) {
//        e.printStackTrace();
//        response.put("error", "Internal Server Error");
//    }
//    return response;
//    }

    @GetMapping("/latest-hip-circumference")
    public Map<String, Object> getLatestHipCircumference(@RequestHeader("Auth") String tokenHeader) {
        Map<String, Object> response = new HashMap<>();
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            User user = userService.findByUsername(username);

            Map<String, Object> hipCircumferenceDetails = circumferenceService.getLatestHipCircumferenceDetails(user);
            if (hipCircumferenceDetails != null) {
                response.putAll(hipCircumferenceDetails);
            } else {
                response.put("error", "No data available");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "Internal Server Error");
        }
        return response;
    }

//    @GetMapping("/waist-circumference")
//    public Map<String, Object> getWaistCircumferenceByDate(@RequestHeader("Auth") String tokenHeader, @PathVariable String date) {
//        Map<String, Object> response = new HashMap<>();
//        try {
//            String token = tokenHeader.replace("Bearer ", "");
//            String username = jwtHelper.getUsernameFromToken(token);
//            User user = userService.findByUsername(username);
//
//            LocalDate measurementDate = LocalDate.parse(date); // Parse date from the request
//
//            String waistCircumference = circumferenceService.getWaistCircumferenceByDate(measurementDate, user);
//
//            response.put("date", date);
//            response.put("waistCircumference", waistCircumference);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.put("error", "Internal Server Error");
//        }
//        return response;
//    }
//    public Map<String, Object> getLatestWaistCircumference(@RequestHeader("Auth") String tokenHeader) {
//        Map<String, Object> response = new HashMap<>();
//        try {
//            String token = tokenHeader.replace("Bearer ", "");
//            String username = jwtHelper.getUsernameFromToken(token);
//            User user = userService.findByUsername(username);
//
//            String waistCircumference = circumferenceService.getLatestWaistCircumference(user);
//
//            response.put("waistCircumference", waistCircumference);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.put("error", "Internal Server Error");
//        }
//        return response;
//    }
@GetMapping("/latest-waist-circumference")
public Map<String, Object> getLatestWaistCircumference(@RequestHeader("Auth") String tokenHeader) {
        Map<String, Object> response = new HashMap<>();
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            User user = userService.findByUsername(username);

            Map<String, Object> waistCircumferenceDetails = circumferenceService.getLatestWaistCircumferenceDetails(user);
            if (waistCircumferenceDetails != null) {
                response.putAll(waistCircumferenceDetails);
            } else {
                response.put("error", "No data available");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "Internal Server Error");
        }
        return response;
    }

    @GetMapping("/waist-circumference-range")
    public ResponseEntity<List<Map<String, String>>> getWaistCircumferenceByDateRange(
            @RequestHeader("Auth") String tokenHeader,
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            User user = userService.findByUsername(username);

            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            List<Circumference> circumferences = circumferenceService.getWaistCircumferenceByDateRange(start, end, user);

            List<Map<String, String>> response = new ArrayList<>();

            // Iterate through each date in the range
            LocalDate date = start;
            while (!date.isAfter(end)) {
                Map<String, String> entry = new HashMap<>();
                entry.put("date", date.toString());

                // Check if there is a corresponding circumference value for the current date
                boolean found = false;
                for (Circumference c : circumferences) {
                    if (c.getDate().isEqual(date)) {
                        entry.put("waistCircumference", c.getWaistCircumference());
                        found = true;
                        break;
                    }
                }

                // If no value found for the current date, set waistCircumference to zero
                if (!found) {
                    entry.put("waistCircumference", "0");
                }

                response.add(entry);
                date = date.plusDays(1); // Move to the next date
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/hip-circumference-range")
    public ResponseEntity<List<Map<String, String>>> getHipCircumferenceByDateRange(
            @RequestHeader("Auth") String tokenHeader,
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            User user = userService.findByUsername(username);

            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            List<Circumference> circumferences = circumferenceService.getHipCircumferenceByDateRange(start, end, user);

            List<Map<String, String>> response = new ArrayList<>();

            // Iterate through each date in the range
            LocalDate date = start;
            while (!date.isAfter(end)) {
                Map<String, String> entry = new HashMap<>();
                entry.put("date", date.toString());

                // Check if there is a corresponding circumference value for the current date
                boolean found = false;
                for (Circumference c : circumferences) {
                    if (c.getDate().isEqual(date)) {
                        entry.put("hipCircumference", c.getHipCircumference());
                        found = true;
                        break;
                    }
                }

                // If no value found for the current date, set hipCircumference to zero
                if (!found) {
                    entry.put("hipCircumference", "0");
                }

                response.add(entry);
                date = date.plusDays(1); // Move to the next date
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}