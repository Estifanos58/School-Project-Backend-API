package com.schoolproject.ChatAPP.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET = System.getenv("JWT_SECRET") != null
            ? System.getenv("JWT_SECRET")
            : "changedefault-secret-key-that-you-must-change"; // Fallback for development

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Base64.getEncoder().encode(SECRET.getBytes()));

    private static final long EXPIRATION_TIME = 86400000; // 24 hours

    /**
     * Generate JWT token with email and userId.
     */
    public String generateToken(String email, String userId) {
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * Extract userId from JWT token.
     */
    public String extractUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("userId", String.class);
    }

    /**
     * Extract JWT from cookies safely.
     */
    public String getJwtFromRequest(HttpServletRequest request) {
        System.out.println("Checking for JWT cookie...");

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                System.out.println("Cookie: " + cookie.getName() + " = " + cookie.getValue());
            }
        } else {
            System.out.println("No cookies found in request.");
        }

        Cookie jwtCookie = WebUtils.getCookie(request, "jwt");
        if (jwtCookie != null) {
            return jwtCookie.getValue();
        }

        throw new RuntimeException("JWT token not found in request");
    }


    /**
     * Validate JWT token.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static SecretKey getSecretKey() {
        return SECRET_KEY;
    }
}
