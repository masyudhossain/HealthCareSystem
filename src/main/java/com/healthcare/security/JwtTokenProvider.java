package com.healthcare.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final Key jwtSecret = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private final long jwtExpirationMs = 3600_000; // 1 hour

    public String generateToken(String userId, String role, List<String> permissions) {
        return Jwts.builder()
                .setSubject(userId)
                .claim("role", role)
                .claim("permissions", permissions)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(jwtSecret)
                .compact();
    }

    public Jws<Claims> validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token);
    }

    public String getUserIdFromToken(String token) {
        return validateToken(token).getBody().getSubject();
    }

    public String getRoleFromToken(String token) {
        return validateToken(token).getBody().get("role", String.class);
    }

    public List<String> getPermissionsFromToken(String token) {
        return (List<String>) validateToken(token).getBody().get("permissions", List.class);
    }
}
