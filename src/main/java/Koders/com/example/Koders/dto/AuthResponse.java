package Koders.com.example.Koders.dto;

public record AuthResponse(
        String token,
        String role,
        String email,
        long expiresAtEpochMs
) {}

