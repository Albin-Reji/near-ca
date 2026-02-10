package com.nearca.gateway_service.util;


import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import java.security.Key;

public class JwtUtil {
    private JwtUtil() {
        /* This utility class should not be instantiated */
    }

    private static final Dotenv dotenv=Dotenv.load();

    private static final String SECRET =dotenv.get("JWT_SECRET");


    private static final Key KEY =
            Keys.hmacShaKeyFor(SECRET.getBytes());

    public static Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static boolean isValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getUserId(String token) {
        return extractAllClaims(token).getSubject();
    }

    public static String getRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }
}

