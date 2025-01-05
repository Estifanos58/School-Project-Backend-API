package com.schoolproject.ChatAPP.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET = System.getenv("JWT_SECRET") != null
            ? System.getenv("JWT_SECRET")
            : "changedefault-secret-key-that-you-must-"; // Fallback for development/testing

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Base64.getEncoder().encode(SECRET.getBytes()));

    private static final long EXPIRATION_TIME = 86400000; // 24 hours

    public String generateToken(String email, String userId) {
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    // Extract userId from token
    public String extractUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("userId", String.class);
    }

    // Get JWT token from request (cookie)
    public String getJwtFromRequest(HttpServletRequest request) {
        String cookie = request.getHeader("Cookie");
        if (cookie != null && cookie.contains("jwt=")) {
            return cookie.split("jwt=")[1].split(";")[0];
        }
        throw new RuntimeException("JWT token not found in request");
    }

    public static SecretKey getSecretKey() {
        return SECRET_KEY;
    }
}

