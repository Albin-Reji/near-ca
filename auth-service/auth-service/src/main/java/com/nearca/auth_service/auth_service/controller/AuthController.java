package com.nearca.auth_service.auth_service.controller;

import com.nearca.auth_service.auth_service.dto.AuthResponse;
import com.nearca.auth_service.auth_service.dto.RegisterRequest;
import com.nearca.auth_service.auth_service.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    public final AuthService authService;
    public AuthController(AuthService authService){
        this.authService=authService;
    }


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }
}
