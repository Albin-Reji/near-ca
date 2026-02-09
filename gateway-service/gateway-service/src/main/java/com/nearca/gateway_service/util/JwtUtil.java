package com.nearca.gateway_service.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class JwtUtil {
    private JwtUtil() {
        /* This utility class should not be instantiated */
    }


    private static final String SECRET =
            "my-secret-key-my-secret-key-my-secret-key"; // ≥ 32 chars

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

