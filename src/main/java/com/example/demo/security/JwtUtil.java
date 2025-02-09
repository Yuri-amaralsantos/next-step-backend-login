package com.example.demo.security;

import java.util.Optional;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Secure key

    public String generateToken(String name, String role) {
        return Jwts.builder()
                .setSubject(name)
                .claim("role", role) 
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1-hour expiration
                .signWith(key) // Sign with secure key
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return (String) extractClaims(token).get("role"); // Extract role from token
    }

    public boolean validateToken(String token, String user) {
        return extractUsername(token).equals(user) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public Optional<String> extractTokenFromHeader(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return Optional.of(header.substring(7)); // Remove "Bearer " prefix
        }
        return Optional.empty();
    }
}
