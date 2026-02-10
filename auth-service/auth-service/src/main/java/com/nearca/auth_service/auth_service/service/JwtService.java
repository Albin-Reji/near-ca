package com.nearca.auth_service.auth_service.service;

import com.nearca.auth_service.auth_service.entity.Users;
import com.nearca.auth_service.auth_service.repository.UserRepository;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
public class JwtService {


    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
    private final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing() // This prevents the crash you're seeing
            .load();

    // These will now pull from Docker environment variables if .env is missing
    private final String jwtSecret = dotenv.get("JWT_SECRET");
    private final String jwtExpirationStr = dotenv.get("JWT_EXPIRATION");
    private final Long jwtExpiration = (jwtExpirationStr != null) ? Long.valueOf(jwtExpirationStr) : 3600000L;
    private final UserRepository userRepository;

    @Autowired
    public JwtService( UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public String generateToken(String email) {
        logger.info("generate token method initiated");
        System.err.println(jwtSecret + " " + jwtExpiration);

        Map<String, Object> claims = new HashMap<>();

        Users users=userRepository.findByEmail(email);
        /* can call repo and add all other claims*/
        claims.put("uuid", users.getUserId());
        claims.put("fullName", users.getFullName());
        claims.put("role", users.getRole().name());


        return Jwts.builder()
                .setClaims(claims)
                .setSubject(users.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public UUID extractUUId(String token){
        return extractAllClaims(token).get("uuid", UUID.class);
    }
    public String extractFullName(String token){
        return  extractAllClaims(token).get("fullName", String.class);
    }
    public String extractRole(String token){
        return extractAllClaims(token).get("role", String.class);
    }
    public boolean isTokenValid(String token, String email) {
        return email.equals(extractUsername(token)) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
}
