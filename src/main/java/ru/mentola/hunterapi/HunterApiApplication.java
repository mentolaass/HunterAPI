package ru.mentola.hunterapi;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.mentola.hunterapi.model.user.User;
import ru.mentola.hunterapi.model.user.UserRole;
import ru.mentola.hunterapi.service.InspectionStorageService;
import ru.mentola.hunterapi.service.UserService;

import java.util.List;

@SpringBootApplication
public class HunterApiApplication {
    @Autowired
    private UserService userService;

    @Autowired
    private InspectionStorageService inspectionStorageService;

    @Value("${spring.security.user.password}")
    private String secretToken;

    @PostConstruct
    private void init() {
        if (userService.findByToken(secretToken).isEmpty()) {
            userService.saveUser(User.builder()
                    .token(secretToken)
                    .name("admin")
                    .inspections(List.of())
                    .userRole(UserRole.ADMIN)
                    .createTimestamp(System.currentTimeMillis())
                    .build());
        }
        inspectionStorageService.init();
    }

    public static void main(String[] args) {
        SpringApplication.run(HunterApiApplication.class, args);
    }
}
