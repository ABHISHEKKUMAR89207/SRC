package com.example.jwt.controler.controllerHealth;

import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.TargetAnalysisService;
import com.example.jwt.service.FoodTodayService.IngrdientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/analysis")

public class AnalysisController {

    @Autowired
    private TargetAnalysisService analysisService;
    @Autowired
    private IngrdientService ingrdientService;

    @Autowired
    private JwtHelper jwtHelper;

    @PostMapping("/analyse")
    public List<Map<String,Double>> analysis(@RequestHeader("Auth") String tokenHeader) {

        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

//        User user = userRepository.findByEmail(username)
//                .orElseThrow(() -> new UserNotFoundException("User not found for email: " + username));

        List<Map<String, Double>> map = analysisService.getAnalysis(username);
        System.out.println("Controller " + map);
        return map;
    }




}