package Koders.com.example.Koders.controller;


import Koders.com.example.Koders.config.JwtUtil;
import Koders.com.example.Koders.dto.AuthRequest;
import Koders.com.example.Koders.dto.AuthResponse;
import Koders.com.example.Koders.dto.RegisterUserRequest;
import Koders.com.example.Koders.model.Admin;
import Koders.com.example.Koders.model.User;
import Koders.com.example.Koders.service.AdminService;
import Koders.com.example.Koders.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/user/register")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody RegisterUserRequest req) {
        // ensure email isn't used by either User or Admin
        if (userService.existsByEmail(req.email()) || adminService.existsByEmail(req.email())) {
            throw new ResponseStatusException(CONFLICT, "Email is already in use");
        }

        User user = User.builder()
                .name(req.name())
                .email(req.email())
                .phone(req.phone())
                .password(passwordEncoder.encode(req.password()))
                .build();

        userService.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), "USER", user.getId(), "USER");
        long exp = System.currentTimeMillis() + jwtUtil.getExpirationMs();
        return ResponseEntity.status(CREATED).body(new AuthResponse(token, "USER", user.getEmail(), exp));
    }

    @PostMapping("/user/login")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody AuthRequest req) {
        User user = userService.findByEmail(req.email())
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Invalid credentials"));
        if (!passwordEncoder.matches(req.password(), user.getPassword())) {
            throw new ResponseStatusException(UNAUTHORIZED, "Invalid credentials");
        }
        String token = jwtUtil.generateToken(user.getEmail(), "USER", user.getId(), "USER");
        long exp = System.currentTimeMillis() + jwtUtil.getExpirationMs();
        return ResponseEntity.ok(new AuthResponse(token, "USER", user.getEmail(), exp));
    }

    @PostMapping("/admin/login")
    public ResponseEntity<AuthResponse> loginAdmin(@Valid @RequestBody AuthRequest req) {
        Admin admin = adminService.findByEmail(req.email())
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Invalid credentials"));
        if (!passwordEncoder.matches(req.password(), admin.getPassword())) {
            throw new ResponseStatusException(UNAUTHORIZED, "Invalid credentials");
        }
        String token = jwtUtil.generateToken(admin.getEmail(), "ADMIN", admin.getId(), "ADMIN");
        long exp = System.currentTimeMillis() + jwtUtil.getExpirationMs();
        return ResponseEntity.ok(new AuthResponse(token, "ADMIN", admin.getEmail(), exp));
    }
}
