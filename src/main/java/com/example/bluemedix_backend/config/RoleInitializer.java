package com.example.bluemedix_backend.config;

// import com.bluemedix.api.model.ERole;
// import com.bluemedix.api.model.Role;
// import com.bluemedix.api.repository.RoleRepository;
import com.example.bluemedix_backend.model.ERole;
import com.example.bluemedix_backend.model.Role;
import com.example.bluemedix_backend.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleInitializer {

    @Bean
    public CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            // Check if roles already exist
            if (roleRepository.count() == 0) {
                // Create roles
                Role userRole = new Role();
                userRole.setName(ERole.ROLE_USER);
                roleRepository.save(userRole);

                Role adminRole = new Role();
                adminRole.setName(ERole.ROLE_ADMIN);
                roleRepository.save(adminRole);

                Role franchiseRole = new Role();
                franchiseRole.setName(ERole.ROLE_FRANCHISE);
                roleRepository.save(franchiseRole);

                Role warehouseRole = new Role();
                warehouseRole.setName(ERole.ROLE_WAREHOUSE);
                roleRepository.save(warehouseRole);

                System.out.println("Roles initialized successfully");
            }
        };
    }
}

