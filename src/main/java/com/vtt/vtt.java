package com.vtt;

import com.vtt.config.AppConstants;
import com.vtt.entities.Role;
import com.vtt.repository.RoleRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableScheduling
@EnableMongoAuditing
public class vtt implements CommandLineRunner {

    @Autowired
    private RoleRepo roleRepo;

    private static final Logger LOG = LoggerFactory.getLogger(vtt.class);

    public static void main(String[] args) {
        SpringApplication.run(vtt.class, args);
    }

    @Override
    public void run(String... args) {

        try {
            Role role = new Role();
            role.setId(AppConstants.ADMIN_USER);
            role.setName("ROLE_ADMIN");

            Role role1 = new Role();
            role1.setId(AppConstants.NORMAL_USER);
            role1.setName("ROLE_NORMAL");

            List<Role> roles = new ArrayList<>();
            roles.add(role);
            roles.add(role1);
            List<Role> result = this.roleRepo.saveAll(roles);

            result.forEach(r -> System.out.println(r.getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        printLog();
    }

    private static void printLog() {
        LOG.debug("Debug Message");
        LOG.warn("Warn Message");
        LOG.error("Error Message");
        LOG.info("Info Message");
        LOG.trace("Trace Message");
    }
}
