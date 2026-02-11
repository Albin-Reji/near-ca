package com.nearca.auth_service.auth_service.dto;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    public String token;
    public String role;


}
