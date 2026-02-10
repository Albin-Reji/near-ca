package com.nearca.auth_service.auth_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nearca.auth_service.auth_service.dto.AuthResponse;
import com.nearca.auth_service.auth_service.dto.LoginRequest;
import com.nearca.auth_service.auth_service.dto.RegisterRequest;
import com.nearca.auth_service.auth_service.entity.Role;
import com.nearca.auth_service.auth_service.service.AuthService;
import com.nearca.auth_service.auth_service.service.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final JwtService jwtService;
    private  final AuthService authService;
    private  final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(AuthService authService,  JwtService jwtService,  AuthenticationManager authenticationManager){
        this.authService=authService;
        this.jwtService=jwtService;
        this.authenticationManager=authenticationManager;
    }


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }
    @PostMapping("/login")
    public ResponseEntity<?> register(@RequestBody LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        String token= jwtService.generateToken(request.getEmail());
        return ResponseEntity.ok(new AuthResponse(token, Role.USER.name()));
    }
}
