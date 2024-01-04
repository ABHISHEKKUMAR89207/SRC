package com.example.jwt.oAuth;

import com.example.jwt.entities.User;
import com.example.jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserServicee {
    @Autowired
    private UserRepository userRepository;

//    public void saveUser(UserDetails userDetails) {
//        User user = new User();
//        user.setEmail(userDetails.getEmail);
//        user.setUserName(userDetails.getUsername());
//        // Set other user details
//
//        userRepository.save(user);
//    }

    public void saveUserDetails(UserDTOo userDTO) {
        // Create a User entity and save it to the local database
        User user = new User();
        user.setEmail(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        // ... set other user details

        userRepository.save(user);
    }

}
