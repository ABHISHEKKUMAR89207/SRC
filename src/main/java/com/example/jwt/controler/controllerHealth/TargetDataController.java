package com.example.jwt.controler.controllerHealth;

import com.example.jwt.entities.TargetData;
import com.example.jwt.entities.User;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.request.TargetDataRequest;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.TargetDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/target")
public class TargetDataController {
    @Autowired
    private TargetDataService targetDataService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtHelper jwtHelper;

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

        return targetDataService.saveOrUpdateTargetData(username,request);
    }


    @GetMapping("/get")
    public TargetData getTargetData() {
        return targetDataService.getTargetData();
    }
}
