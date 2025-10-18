package Koders.com.example.Koders.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private final Key key;
    private final long expirationMs;

    public JwtUtil(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-ms:86400000}") long expirationMs) {

        this.expirationMs = expirationMs;

        try {
            // Handle both Base64 and plain text secrets
            byte[] keyBytes;
            if (isBase64(secret)) {
                // If it's Base64 encoded
                keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(secret);
            } else {
                // Plain text secret - encode to bytes
                keyBytes = secret.getBytes(StandardCharsets.UTF_8);
            }

            // Ensure minimum key length (256 bits = 32 bytes)
            if (keyBytes.length < 32) {
                // Pad with zeros if too short
                byte[] padded = new byte[32];
                System.arraycopy(keyBytes, 0, padded, 0, keyBytes.length);
                keyBytes = padded;
            }

            this.key = Keys.hmacShaKeyFor(keyBytes);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create JWT key from secret", e);
        }
    }

    // Helper method to check if string is Base64
    private boolean isBase64(String str) {
        try {
            java.util.Base64.getDecoder().decode(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public String generateToken(String subjectEmail, String role, Long id, String principalType) {
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiry = new Date(now + expirationMs);

        return Jwts.builder()
                .setSubject(subjectEmail)
                .addClaims(Map.of(
                        "role", role,
                        "id", id,
                        "type", principalType
                ))
                .setIssuedAt(issuedAt)
                .setExpiration(expiry)
                .signWith(key)
                .compact();
    }

    public Jws<Claims> parseClaims(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public long getExpirationMs() {
        return expirationMs;
    }
}