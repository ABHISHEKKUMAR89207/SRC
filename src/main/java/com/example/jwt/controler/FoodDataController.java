package com.example.jwt.controler;


import com.example.jwt.dtos.FoodDataDTO;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.FoodDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
public class FoodDataController {
    @Autowired
    private FoodDataService foodDataService;
    @Autowired
    private JwtHelper jwtHelper;

//    @GetMapping("/top10")
//    public List<FoodDataDTO> getTop10ByColumn(@RequestHeader("Auth") String tokenHeader,@RequestParam("column") String column) {
//        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        return foodDataService.getTop10ByColumn(column,username);
//    }
//
//    @GetMapping("/top10")
//    public ResponseEntity<List<FoodDataDTO>> getTop10ByColumn(@RequestHeader("Authorization") String tokenHeader, @RequestParam("column") String column) {
//        try {
//            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//            String token = tokenHeader.replace("Bearer ", "");
//
//            // Extract the username (email) from the token
//            String username = jwtHelper.getUsernameFromToken(token);
//
//            // Check if the user is authenticated (you can add your JWT validation logic here)
//            if (isValidToken(token)) {
//                List<FoodDataDTO> top10Data = foodDataService.getTop10ByColumn(column, username);
//                return ResponseEntity.ok(top10Data);
//            } else {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Unauthorized status
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Internal server error
//        }

        @GetMapping("/top10")
        public ResponseEntity<List<FoodDataDTO>>getTop10ByColumn(@RequestHeader("Auth") String tokenHeader, @RequestParam("column") String column) {
            try {
                // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
                String token = tokenHeader.replace("Bearer ", "");

                // Extract the username (email) from the token
                String username = jwtHelper.getUsernameFromToken(token);

                // Call your service method to retrieve the top 10 data based on the specified column and the authenticated user
                List<FoodDataDTO> top10Data = foodDataService.getTop10ByColumn(column, username);

                return ResponseEntity.ok(top10Data);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Internal server error
            }
        }


    }




