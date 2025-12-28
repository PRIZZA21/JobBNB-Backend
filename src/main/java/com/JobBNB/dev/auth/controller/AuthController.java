package com.JobBNB.dev.auth.controller;

import com.JobBNB.dev.auth.dto.LoginRequest;
import com.JobBNB.dev.auth.dto.RefreshTokenRequest;
import com.JobBNB.dev.auth.dto.RegisterRequest;
import com.JobBNB.dev.auth.dto.AuthResponse;
import com.JobBNB.dev.auth.service.AuthService;
import com.JobBNB.dev.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(
                "Login successful",
                authService.login(request));
    }

    @PostMapping("/register")
    public ApiResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.success(
                "Register successful",
                authService.register(request));
    }

    @PostMapping("/refresh")
    public AuthResponse refreshToken(
            @RequestBody RefreshTokenRequest request) {
        return authService.refreshAccessToken(request.getRefreshToken());
    }
}
