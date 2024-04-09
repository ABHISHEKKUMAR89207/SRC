package com.example.jwt.service;





import com.example.jwt.repository.UserErrorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserErrorService {

    private final UserErrorRepository userErrorRepository;

    @Autowired
    public UserErrorService(UserErrorRepository userErrorRepository) {
        this.userErrorRepository = userErrorRepository;
    }

    // Add service methods as needed, like save, update, delete, etc.
}
