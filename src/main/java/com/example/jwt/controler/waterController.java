package com.example.jwt.controler;
import com.example.jwt.entities.waterEntity;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.waterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController@RequestMapping("api/water")
public class waterController {
    @Autowired
    private waterService waterService;
    @Autowired
    private  JwtHelper jwtHelper;

//    @PostMapping("/water")
//    public ResponseEntity<waterEntity> saveWaterActivity(@RequestBody waterEntity ap) {
//        waterEntity savedActivity = waterService.saveWaterActivity(ap);
//        return ResponseEntity.ok(savedActivity);
//    }

    @PostMapping("/water")
    public ResponseEntity<waterEntity>saveWaterActivity(@RequestHeader("Auth") String tokenHeader,
                                                  @RequestBody waterEntity ap) {
        try {
            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);

            // Use the extracted username to associate the heart rate data with the user
            waterEntity waterEntity = waterService.saveWaterActivity(ap, username);

            return new ResponseEntity<>(waterEntity, HttpStatus.CREATED);
        } catch (Exception e) {
            // Handle exceptions, e.g., validation errors or database errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // You can customize the error response as needed
        }
    }



    @PutMapping("/water_update") // Use PUT method
    public ResponseEntity<waterEntity> updateWaterActivity(
            @RequestHeader("Auth") String tokenHeader,
            @RequestBody waterEntity water) {
        try {
            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);

            waterEntity updatedActivity = waterService.updateWaterActivity(water, username);
            return ResponseEntity.ok(updatedActivity);
        }catch (Exception e) {
            // Handle exceptions, e.g., validation errors or database errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // You can customize the error response as needed

        }
    }

    @GetMapping("/user/water-data")
    public ResponseEntity<waterEntity> getWaterData(@RequestHeader("Auth") String tokenHeader) {
        try {
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token
            String username = jwtHelper.getUsernameFromToken(token);

            waterEntity waterData = waterService.getWaterDataForUser(username);

            if (waterData != null) {
                return new ResponseEntity<>(waterData, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null); // Customize the response for not found data
            }
        } catch (Exception e) {
            // Handle exceptions, e.g., database errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Customize the error response as needed
        }
    }
}
