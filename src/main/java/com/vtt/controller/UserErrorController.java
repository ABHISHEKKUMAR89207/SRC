package com.vtt.controller;




import com.vtt.repository.UserErrorRepository;
import com.vtt.repository.UserRepository;
import com.vtt.security.JwtHelper;
import com.vtt.service.UserErrorService;

import com.vtt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-errors")
public class UserErrorController {

    private final UserErrorService userErrorService;

    @Autowired
    public UserErrorController(UserErrorService userErrorService) {
        this.userErrorService = userErrorService;
    }

@Autowired
private UserRepository userRepository;
    @Autowired
    private UserErrorRepository userErrorRepository;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private UserService userService;

//    @PostMapping("/save")
//    public ResponseEntity<String> saveUserError(@RequestHeader("Auth") String tokenHeader, @RequestBody UserErrorDTO userErrorDTO) {
//        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        // Fetch the user's data from both User and UserProfile entities
//        User user1 = userService.findByUsername(username);
//        // Check if user exists
//        User user = userRepository.findById(user1.getUserId()).orElse(null);
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//        }
//
//        // Use UTC timezone to get current timestamp
//        Instant currentTimestamp = Instant.now();
//
//        // Create UserError instance and save it
//        UserError userError = new UserError();
//        userError.setError(userErrorDTO.getError());
//        userError.setTimestamp(Timestamp.from(currentTimestamp));
//        userError.setUser(user);
//        userErrorRepository.save(userError);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body("User error saved successfully");
//    }



//    @GetMapping("/get-all-error")
//    public List<UserErrorResponseDTO> getAll(@RequestHeader("Auth") String tokenHeader) {
//        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        // Fetch the user's data from both User and UserProfile entities
//        User user1 = userService.findByUsername(username);
//
//        List<UserError> userErrors = userErrorRepository.findAll();
//
//        // Convert UserError objects to UserErrorResponseDTO objects
//        List<UserErrorResponseDTO> responseDTOs = userErrors.stream()
//                .map(userError -> new UserErrorResponseDTO(userError.getError(), userError.getTimestamp()))
//                .collect(Collectors.toList());
//
//        return responseDTOs;
//    }

}
