package com.nearca.auth_service.auth_service.service;

import com.nearca.auth_service.auth_service.dto.AuthResponse;
import com.nearca.auth_service.auth_service.dto.RegisterRequest;
import com.nearca.auth_service.auth_service.entity.Role;
import com.nearca.auth_service.auth_service.entity.Users;
import com.nearca.auth_service.auth_service.repository.UserRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public UserRepository userRepository;
    public JwtService jwtService;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public @Nullable AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered... just login...");
        }
        Users user = Users.builder().email(request.getEmail()).password(request.getPassword()).fullName(request.getFullName()).mobileNumber(request.getMobileNumber()).role(Role.USER).isActive(true).build();
        /* Save to DB */
        Users savedUser = userRepository.save(user);
        /* Token Generation */
        String token =jwtService.generateToken(savedUser);

        return AuthResponse.builder()
                .token(token)
                .role(savedUser.getRole().name())
                .build();

    }
}
