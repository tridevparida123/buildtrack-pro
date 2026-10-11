package com.novaconstruct.service;

import com.novaconstruct.model.RoleChoices;
import com.novaconstruct.model.User;
import com.novaconstruct.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Ensure at least 1 system administrator exists for initial login
        if (userRepository.count() == 0) {
            System.out.println("Creating Primary System Administrator Account...");
            User admin = new User(
                "admin",
                "admin123",
                "admin@buildtrack.com",
                "System",
                "Administrator",
                RoleChoices.SUPER_ADMIN,
                "BuildTrack Enterprise"
            );
            userRepository.save(admin);
            System.out.println("Primary Admin account (username: admin) initialized cleanly.");
        }
    }
}
