package com.example.jwt.controler;

import com.example.jwt.dtos.NinDataDTO;
import com.example.jwt.dtos.NinDataDTOO;
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

//        @GetMapping("/top10")
//        public ResponseEntity<List<NinDataDTO>>getTop10ByColumn(@RequestHeader("Auth") String tokenHeader, @RequestParam("column") String column) {
//            try {
//                // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//                String token = tokenHeader.replace("Bearer ", "");
//
//                // Extract the username (email) from the token
//                String username = jwtHelper.getUsernameFromToken(token);
//
//                // Call your service method to retrieve the top 10 data based on the specified column and the authenticated user
//                List<NinDataDTO> top10Data = foodDataService.getTop10ByColumn(column, username);
//
//                return ResponseEntity.ok(top10Data);
//            } catch (Exception e) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Internal server error
//            }
//        }



//    @GetMapping("/top10")
//    public ResponseEntity<List<NinDataDTOO>> getTop10ByColumnAndTypesOfFood(
//            @RequestHeader("Auth") String tokenHeader,
//            @RequestParam("column") String column,
//            @RequestParam("typesOfFood") String typesOfFood) {
//        try {
//            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//            String token = tokenHeader.replace("Bearer ", "");
//
//            // Extract the username (email) from the token using your jwtHelper (replace jwtHelper with your actual class/method)
//            String username = jwtHelper.getUsernameFromToken(token);
//
//            // Call your service method to retrieve the top 10 data based on the specified column, typesOfFood, and the authenticated user
//            List<NinDataDTOO> top10Data = foodDataService.getTop10ByColumnAndTypesOfFood(column, typesOfFood, username);
//
//            return ResponseEntity.ok(top10Data);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Internal server error
//        }
//    }
@GetMapping("/top10")
public ResponseEntity<List<NinDataDTOO>> getTop10ByColumnAndTypesOfFood(
        @RequestHeader("Auth") String tokenHeader,
        @RequestParam("column") String column,
        @RequestParam("typesOfFood") String typesOfFood) {
    try {
        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token using your jwtHelper (replace jwtHelper with your actual class/method)
        String username = jwtHelper.getUsernameFromToken(token);

        // Call your service method to retrieve the top 10 data based on the specified column, typesOfFood, and the authenticated user
        List<NinDataDTOO> top10Data = foodDataService.getTop10ByColumnAndTypesOfFood(column, typesOfFood, username);

        return ResponseEntity.ok(top10Data);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Internal server error
    }
}

    @GetMapping("/nolimit")
    public ResponseEntity<List<NinDataDTO>> getTop10ByColumnAndTypesOfFoodnolimits(
            @RequestHeader("Auth") String tokenHeader,
            @RequestParam("column") String column,
            @RequestParam("typesOfFood") String typesOfFood) {
        try {
            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token using your jwtHelper (replace jwtHelper with your actual class/method)
            String username = jwtHelper.getUsernameFromToken(token);

            // Call your service method to retrieve the top 10 data based on the specified column, typesOfFood, and the authenticated user
            List<NinDataDTO> top10Data = foodDataService.getTop10ByColumnAndTypesOfFoodNolimit(column, typesOfFood, username);

            return ResponseEntity.ok(top10Data);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Internal server error
        }
    }


    @GetMapping("/all-food-nolimit")
    public ResponseEntity<List<NinDataDTO>> getTop10ByColumnAndTypesOfFoodremovenutrients(
            @RequestHeader("Auth") String tokenHeader,
//            @RequestParam("column") String column,
            @RequestParam("typesOfFood") String typesOfFood) {
        try {
            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
            String token = tokenHeader.replace("Bearer ", "");

            // Extract the username (email) from the token using your jwtHelper (replace jwtHelper with your actual class/method)
            String username = jwtHelper.getUsernameFromToken(token);

            // Call your service method to retrieve the top 10 data based on the specified column, typesOfFood, and the authenticated user
            List<NinDataDTO> top10Data = foodDataService.getTop10ByColumnAndTypesOfFoodNolimitremovenutrients( typesOfFood, username);

            return ResponseEntity.ok(top10Data);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Internal server error
        }
    }









    @GetMapping("/all-row-food")
    public List<NinDataDTO> getAllNinData() {
        return foodDataService.getAllNinData();
    }
}




