package com.novaconstruct.service;

import com.novaconstruct.model.RoleChoices;
import com.novaconstruct.model.User;
import com.novaconstruct.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("Verifying & Initializing System Administrator Account...");

        // 1. Ensure 'admin' user account exists with password 'admin123'
        Optional<User> adminOpt = userRepository.findByUsername("admin");
        User admin = adminOpt.orElseGet(User::new);
        admin.setUsername("admin");
        admin.setPassword("admin123");
        admin.setEmail("admin@buildtrack.com");
        admin.setFirstName("System");
        admin.setLastName("Administrator");
        admin.setPrimaryRole(RoleChoices.SUPER_ADMIN);
        admin.setIsApproved(true);
        admin.setIsActive(true);
        admin.setIsSuperuser(true);
        admin.setOrganization("BuildTrack Enterprise");
        userRepository.save(admin);

        // 2. Ensure 'superadmin' user account exists with password 'admin123'
        Optional<User> superAdminOpt = userRepository.findByUsername("superadmin");
        User superAdmin = superAdminOpt.orElseGet(User::new);
        superAdmin.setUsername("superadmin");
        superAdmin.setPassword("admin123");
        superAdmin.setEmail("superadmin@buildtrack.com");
        superAdmin.setFirstName("Chief");
        superAdmin.setLastName("Administrator");
        superAdmin.setPrimaryRole(RoleChoices.SUPER_ADMIN);
        superAdmin.setIsApproved(true);
        superAdmin.setIsActive(true);
        superAdmin.setIsSuperuser(true);
        superAdmin.setOrganization("BuildTrack Enterprise");
        userRepository.save(superAdmin);

        System.out.println("System Administrator accounts ('admin' and 'superadmin') updated successfully with password 'admin123'.");
    }
}
