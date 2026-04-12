package com.Qr.Qr.config; // Adjust package name if needed

import com.Qr.Qr.model.User;
import com.Qr.Qr.model.enums.Role;
import com.Qr.Qr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if the Super Admin already exists
        if (userRepository.findByEmail("super@admin.com").isEmpty()) {
            
            User superAdmin = new User();
            superAdmin.setEmail("super@admin.com");
            superAdmin.setPassword(passwordEncoder.encode("password123")); // Hashes perfectly
            superAdmin.setFullName("System Super Admin");
            superAdmin.setRole(Role.SUPER_ADMIN); 
            superAdmin.setIsActive(true);

            userRepository.save(superAdmin);
            
            // Print a massive success message in your terminal
            System.out.println("\n================================================");
            System.out.println("✅ SUPER ADMIN CREATED SECURELY!");
            System.out.println("Email: super@admin.com");
            System.out.println("Password: password123");
            System.out.println("================================================\n");
            
        } else {
            System.out.println("\n✅ SUPER ADMIN ALREADY EXISTS IN DATABASE.\n");
        }
    }
}
