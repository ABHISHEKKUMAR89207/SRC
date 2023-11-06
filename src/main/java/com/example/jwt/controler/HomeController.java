package com.example.jwt.controler;

import com.example.jwt.entities.User;
import com.example.jwt.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/home")
@Tag(name = "HomeController", description = "Api for Authentication")
public class HomeController {

        @Autowired
        private UserService userService;

        //http://localhost:8080/home/user
        @GetMapping("/user")
        public List<User> getUser()
        {
            System.out.println("getting user");
            return this.userService.getUser();
        }

        @GetMapping("/current-user")
        public String getLoggedInUser(Principal principal){

            return principal.getName();
        }


}
