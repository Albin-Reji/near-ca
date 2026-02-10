package com.nearca.auth_service.auth_service.dto;

import com.nearca.auth_service.auth_service.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    String token;
    String role;
}
