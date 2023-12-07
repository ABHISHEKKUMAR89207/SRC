package com.example.jwt.controler;

import com.example.jwt.entities.AllToggle;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.allToggleService;
import com.example.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification-settings")
public class allToggleController {
    private final allToggleService settingsService;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private allToggleService notificationSettingsService;
    private UserService userService;

    @Autowired
    public allToggleController(allToggleService settingsService) {
        this.settingsService = settingsService;
    }

    //notification get onn/off
    @GetMapping("/get-notify-on-off")
    public ResponseEntity<AllToggle> getCurrentUserNotificationSettings(@RequestHeader("Auth") String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Get the notification settings based on the username
        AllToggle settings = settingsService.getUserNotificationSettings(username);

        if (settings != null) {
            return ResponseEntity.ok(settings);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //notification update onn/off
    @PutMapping("/update-notify-on-off")
    public ResponseEntity<AllToggle> updateUserNotificationSettings(@RequestHeader("Auth") String tokenHeader, @RequestParam boolean notificationOn) {

        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        AllToggle updatedSettings = notificationSettingsService.updateUserNotificationSettings(username, notificationOn);

        if (updatedSettings != null) {
            return ResponseEntity.ok(updatedSettings);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
