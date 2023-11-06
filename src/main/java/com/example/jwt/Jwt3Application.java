package com.example.jwt;

import com.example.jwt.config.AppConstants;
import com.example.jwt.entities.Role;
import com.example.jwt.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Jwt3Application implements CommandLineRunner {


    @Autowired
    private RoleRepo roleRepo;

    public static void main(String[] args) {
        SpringApplication.run(Jwt3Application.class, args);
    }


    @Override
    public void run(String... args) throws Exception {

//        System.out.println(this.passwordEncoder.encode("abc"));
        try {

            Role role = new Role();
            role.setId(AppConstants.ADMIN_USER);
            role.setName("ROLE_ADMIN");

            Role role1 = new Role();
            role1.setId(AppConstants.NORMAL_USER);
            role1.setName("ROLE_NORMAL");

            //List<Role> roles = List<role>;
            List<Role> roles = new ArrayList<Role>();
            //List<Role> roles = List.of(role,role1);
            roles.add(role);
            roles.add(role1);
            List<Role> result = this.roleRepo.saveAll(roles);

            result.forEach(r -> {
                System.out.println(r.getName());
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
