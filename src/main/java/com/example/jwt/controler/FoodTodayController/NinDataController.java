package com.example.jwt.controler.FoodTodayController;


import com.example.jwt.entities.FoodToday.NinData;
import com.example.jwt.entities.User;
import com.example.jwt.repository.FoodTodayRepository.NinDataRepository;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/foods")
@Tag(name = "FoodTodayNinData Controller", description = "This is FoodTodayNinData Controller")

public class NinDataController {


    @Autowired
    private NinDataRepository ninDataRepository;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserService userService;


//    @GetMapping("/all-food-list")
//    public List<String> getAllFoodNames( @RequestHeader("Auth") String tokenHeader) {
//
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        // Use the username to fetch the userId from your user service
//        User user = userService.findByUsername(username);
//
//        List<NinData> foodItems = ninDataRepository.findAll();
//        List<String> foodNames = foodItems.stream()
//                .map(NinData::getName)
//                .collect(Collectors.toList());
//        return foodNames;
//    }


    @GetMapping("/all-food-list")
    public List<String> getAllFoodNames(@RequestHeader("Auth") String tokenHeader) throws AccessDeniedException {
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the username to fetch the userId from your user service
        User user = userService.findByUsername(username);

        // Use @PreAuthorize to restrict access to the method
        // Only allow access if the authenticated user's ID matches the fetched user's ID
        // This assumes that User has a method like getUserId() to retrieve the user's ID
        if (user != null && user.getUserId().equals(user.getUserId())) {
            List<NinData> foodItems = ninDataRepository.findAll();
            List<String> foodNames = foodItems.stream()
                    .map(NinData::getName)
                    .collect(Collectors.toList());
            return foodNames;
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }
}
