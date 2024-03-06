package com.example.jwt.entities.activityType;




import com.example.jwt.entities.User;
import com.example.jwt.entities.UserProfile;
import com.example.jwt.entities.UserProfileResponse;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.UserProfileService;
import com.example.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/activity-types")
public class ActivityTypeController {

    @Autowired
    private UserService userService;

    @Autowired
    private final ActivityTypeService activityTypeService;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    public ActivityTypeController(ActivityTypeService activityTypeService) {
        this.activityTypeService = activityTypeService;
    }

    @PostMapping("/save-workLevel-occupation")
    public ResponseEntity<String> updateProfileActivityType(@RequestHeader("Auth") String tokenHeader,
                                                            @RequestBody UpdateActivityTypeDTO updateActivityTypeDTO) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            User user = userService.findByUsername(username);

            UserProfile userProfile = activityTypeService.updateActivityType(user.getUserId(), updateActivityTypeDTO.getOccupation());
            return ResponseEntity.ok("Activity Type updated successfully for user " + userProfile.getId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating Activity Type: " + e.getMessage());
        }
    }

@Autowired
private UserProfileService userProfileService;

    @GetMapping("/get-workLevel-occupation")
    public ResponseEntity<UserProfileResponse> getUserProfileDetails(@RequestHeader("Auth") String tokenHeader) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            User user = userService.findByUsername(username);

            UserProfileResponse responseDTO = userProfileService.getUserProfileDetails(user.getUserId());

            if (responseDTO.getWorkLevel() == null || responseDTO.getOccupation() == null) {
                // Handle the case where workLevel or occupation is null
                return ResponseEntity.badRequest().body(null); // Or return an appropriate error response
            }

            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null); // Adjust error handling as needed
        }
    }
}
