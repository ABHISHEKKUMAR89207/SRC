package com.example.jwt.controler;

import com.example.jwt.dtos.NinDataDTO;
import com.example.jwt.dtos.NinDataDTOO;
import com.example.jwt.entities.FoodToday.NinData;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.FoodDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/food")
public class FoodDataController {

    @Autowired
    private FoodDataService foodDataService;
    @Autowired
    private JwtHelper jwtHelper;





//    @GetMapping("/all-row-food")
//    public List<NinData> getAllNinData() {
//        return foodDataService.getAllNinData();
//    }

    @GetMapping("/all-row-food")
    public List<NinDataDTO> getAllNinData() {
        // Fetch data from your data source
        List<NinData> ninDataList = foodDataService.getAllNinData();

        // Map NinData objects to NinDataDTO objects
        List<NinDataDTO> ninDataDTOList = ninDataList.stream()
                .map(this::convertToNinDataDTO)
                .collect(Collectors.toList());

        return ninDataDTOList;
    }

    // Helper method to convert NinData to NinDataDTO
    private NinDataDTO convertToNinDataDTO(NinData ninData) {
        NinDataDTO ninDataDTO = new NinDataDTO();
        ninDataDTO.setId(ninData.getNinDataId());
        ninDataDTO.setName(ninData.getFood());
        ninDataDTO.setFoodCode(ninData.getFoodCode());
        ninDataDTO.setTypesoffood(ninData.getTypesoffood());
        ninDataDTO.setCategory(ninData.getCategory());

        ninDataDTO.setEnergy(ninData.getEnergy());
        ninDataDTO.setProtein(ninData.getProtein());
        ninDataDTO.setTotal_Fat(ninData.getTotal_Fat());
        ninDataDTO.setCalcium(ninData.getCalcium());
        ninDataDTO.setIron(ninData.getIron());
        ninDataDTO.setZinc(ninData.getZinc());
        ninDataDTO.setMagnesium(ninData.getMagnesium());
        ninDataDTO.setThiamine_B1(ninData.getThiamine_B1());
        ninDataDTO.setRiboflavin_B2(ninData.getRiboflavin_B2());
        ninDataDTO.setNiacin_B3(ninData.getNiacin_B3());
        ninDataDTO.setFolates_B9(ninData.getTotalFolates_B9());
        ninDataDTO.setRetinolVit_A(ninData.getRetinolVit_A());
        ninDataDTO.setCarbohydrate(ninData.getCarbohydrate());
        ninDataDTO.setSodium(ninData.getSodium());
        ninDataDTO.setTotal_Dietary_Fibre(ninData.getTotal_Dietary_Fibre());
        ninDataDTO.setVitb6(ninData.getVit_B6());
        ninDataDTO.setVitc(ninData.getVit_C());

        return ninDataDTO;
    }
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
        public ResponseEntity<List<NinDataDTOO>> getTop10ByColumnAndTypesOfFoodnolimits(
                @RequestHeader("Auth") String tokenHeader,
                @RequestParam("column") String column,
                @RequestParam("typesOfFood") String typesOfFood) {
            try {
                // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
                String token = tokenHeader.replace("Bearer ", "");

                // Extract the username (email) from the token using your jwtHelper (replace jwtHelper with your actual class/method)
                String username = jwtHelper.getUsernameFromToken(token);

                // Call your service method to retrieve the top 10 data based on the specified column, typesOfFood, and the authenticated user
                List<NinDataDTOO> top10Data = foodDataService.getTop10ByColumnAndTypesOfFoodNolimit(column, typesOfFood, username);

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



}




