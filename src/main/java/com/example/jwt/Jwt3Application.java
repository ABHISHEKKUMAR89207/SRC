package com.example.jwt;

import com.example.jwt.config.AppConstants;
import com.example.jwt.entities.Role;
import com.example.jwt.entities.dashboardEntity.Activities;
import com.example.jwt.repository.RoleRepo;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableScheduling
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




    @Bean
    FirebaseMessaging firebaseMassaging() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource("firebase-service-account.json").getInputStream());
        FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                .setCredentials(googleCredentials).build();
        FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions,"fitnessapp");
        return FirebaseMessaging.getInstance(app);
    }

}
