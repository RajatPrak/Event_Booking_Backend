package Koders.com.example.Koders.bootstrap;

import Koders.com.example.Koders.model.Admin;
import Koders.com.example.Koders.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        final String adminEmail = "jaiShreeRam@siya.com";
        if (adminService.findByEmail(adminEmail).isEmpty()) {
            Admin admin = Admin.builder()
                    .name("Ram")
                    .email(adminEmail)
                    .password(passwordEncoder.encode("SiyaKeRam"))
                    .build();
            adminService.save(admin);
            System.out.println("Seeded admin account: " + adminEmail);
        }
    }
}
