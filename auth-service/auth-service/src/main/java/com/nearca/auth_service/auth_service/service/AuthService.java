package com.nearca.auth_service.auth_service.service;

import com.nearca.auth_service.auth_service.dto.AuthResponse;
import com.nearca.auth_service.auth_service.dto.RegisterRequest;
import com.nearca.auth_service.auth_service.entity.Role;
import com.nearca.auth_service.auth_service.entity.Users;
import com.nearca.auth_service.auth_service.repository.UserRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService=jwtService;
        this.passwordEncoder=passwordEncoder;
    }

    public @Nullable AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered... just login...");
        }
        Users user= builder(request);
        /* Save to DB */
        Users savedUser = userRepository.save(user);
        /* Token Generation */
        String token =jwtService.generateToken(savedUser.getEmail());

        AuthResponse response=new AuthResponse();
        response.setToken(token);
        response.setRole(Role.USER.name());
        return response;

    }
    private  Users builder(RegisterRequest request){
        Users user=new Users();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setMobileNumber(request.getMobileNumber());
        user.setRole(Role.USER);
        user.setActive(true);
        return user;
    }
}
