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

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {
        log.info("Login API called for email: {}", request.getEmail());
        return ApiResponse.success(
                "Login successful",
                authService.login(request));
    }

    @PostMapping("/register")
    public ApiResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Register API called for email: {}", request.getEmail());
        return ApiResponse.success(
                "Register successful",
                authService.register(request));
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthResponse> refreshToken(
            @RequestBody RefreshTokenRequest request) {
        log.info("Refresh token API called");
        return ApiResponse.success(
                "Register successful",
                authService.refreshAccessToken(request.getRefreshToken()));
    }
}
