// Log in Controller 

package com.example.jwt.oAuth;

import com.example.jwt.service.UserService;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authh")
public class AuthControllerr {
    @Autowired
    private UserServicee userServicee;

    @Autowired
    private FirebaseApp firebaseApp;

//    @PostMapping("/google-loginn")
//    public ResponseEntity<String> googleLogin(@RequestBody String idToken) {
//        try {
//            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
//
//            // Extract user details from the decoded token
//            String email = decodedToken.getEmail();
//            String name = decodedToken.getName();
//            // ... other details
//
//            // Create a UserDTO object with the extracted user details
//            UserDTOo userDTO = new UserDTOo();
//            userDTO.setEmail(email);
//            userDTO.setUsername(name);
//            // ... set other user details
//
//            // Save user details to the local database
//            userServicee.saveUserDetails(userDTO);
//
//            return ResponseEntity.ok("User details saved successfully");
//        } catch (FirebaseAuthException e) {
//            // Handle authentication failure or token verification error
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
//        }
//    }


    @PostMapping("/google-loginn")
    public ResponseEntity<String> loginUser(@RequestBody String idToken) throws FirebaseAuthException {
//        try {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(firebaseApp); // Get FirebaseAuth instance

            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(idToken);

            // Extract user details from the decoded token
            String email = decodedToken.getEmail();
            String name = decodedToken.getName();
            // ... other details

            // Create a UserDTO object with the extracted user details
            UserDTOo userDTO = new UserDTOo();
            userDTO.setEmail(email);
            userDTO.setUsername(name);
            // ... set other user details

            // Save user details to the local database
            userServicee.saveUserDetails(userDTO);

            return ResponseEntity.ok("User details saved successfully");
//        }
//        catch (FirebaseAuthException e) {
//            // Handle authentication failure or token verification error
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
//        }
    }
}