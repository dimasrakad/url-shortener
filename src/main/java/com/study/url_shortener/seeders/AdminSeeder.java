package com.study.url_shortener.seeders;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.study.url_shortener.entities.Role;
import com.study.url_shortener.entities.User;
import com.study.url_shortener.enums.RoleEnum;
import com.study.url_shortener.repositories.RoleRepository;
import com.study.url_shortener.repositories.UserRepository;

@Component
@Order(2)
public class AdminSeeder implements ApplicationRunner {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Role role = roleRepository.findByName(RoleEnum.ADMIN).orElse(null);

        Map<String, String> users = new HashMap<>();

        users.put("admin1", "adminsatu");
        users.put("admin2", "admindua");

        users.forEach((username, password) -> {
            if (userRepository.findByUsername(username).isEmpty()) {
                User user = new User();
                user.setUsername(username);
                user.setPassword(passwordEncoder.encode(password));
                user.setRole(role);
                userRepository.save(user);

                System.out.println("\nUser admin created: " + username + "\n");
            }
        });
    }
    
}
