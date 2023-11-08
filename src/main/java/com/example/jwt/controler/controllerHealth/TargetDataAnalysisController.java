package com.example.jwt.controler.controllerHealth;

import com.example.jwt.entities.TargetData;
import com.example.jwt.request.TargetDataRequest;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.TargetAnalysisService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/target")
public class TargetDataAnalysisController {


    @Autowired
    private TargetAnalysisService targetAnalysisService;

    @Autowired
    private JwtHelper jwtHelper;


    //target set and update
    @PostMapping("/update")
    public TargetData updateTargetData(
            @RequestHeader("Auth") String tokenHeader,
            @RequestBody TargetDataRequest request
    ) {
        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

//        User user = userRepository.findByEmail(username)
//                .orElseThrow(() -> new UserNotFoundException("User not found for email: " + username));

        return targetAnalysisService.saveOrUpdateTargetData(username,request);
    }


    // target get
    @GetMapping("/get")
    public TargetData getTargetData(@RequestHeader("Auth") String tokenHeader) {

        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        return targetAnalysisService.getTargetData(username);
    }




    // target based analysis and get
    @GetMapping("/analyse")
    public List<Map<String,Double>> analysis(@RequestHeader("Auth") String tokenHeader) {

        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

//        User user = userRepository.findByEmail(username)
//                .orElseThrow(() -> new UserNotFoundException("User not found for email: " + username));

        List<Map<String, Double>> map = targetAnalysisService.getAnalysis(username);
        System.out.println("Controller " + map);
        return map;
    }


}
