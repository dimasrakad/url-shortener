package com.study.url_shortener.seeders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.study.url_shortener.entities.Role;
import com.study.url_shortener.enums.RoleEnum;
import com.study.url_shortener.repositories.RoleRepository;

@Component
@Order(1)
public class RoleSeeder implements ApplicationRunner {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        RoleEnum[] roles = RoleEnum.values();

        for (RoleEnum role : roles) {
            if (!roleRepository.existsByName(role)) {
                Role newRole = new Role();
                newRole.setName(role);
                roleRepository.save(newRole);

                System.out.println("\nSeeder: role " + role + " created\n");
            }
        }
    }
}