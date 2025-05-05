package com.vtt.commonfunc;




import com.vtt.entities.User;
import com.vtt.entities.UserDetails;
import com.vtt.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceCom {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    /**
     * Returns a list of UserDetails for a given list of Users.
     *
     * @param users List of User entities
     * @return List of corresponding UserDetails
     */
    public List<UserDetails> getUserDetailsByUsers(List<User> users) {
        return users.stream()
                .map(user -> userDetailsRepository.findByUser(user).orElse(null)) // Use .orElse(null) to handle Optional
                .filter(userDetails -> userDetails != null) // Filter out nulls if needed
                .collect(Collectors.toList());
    }
}
