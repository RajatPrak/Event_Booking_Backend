package Koders.com.example.Koders.service;

import Koders.com.example.Koders.model.Admin;
import Koders.com.example.Koders.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    public Optional<Admin> findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return adminRepository.existsByEmail(email);
    }

    public Admin save(Admin admin) {
        return adminRepository.save(admin);
    }
}
