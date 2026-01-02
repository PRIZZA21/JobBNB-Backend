package com.JobBNB.dev.auth.controller;

import com.JobBNB.dev.auth.dto.GoogleLoginRequest;
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

    @PostMapping("/google")
    public ApiResponse<AuthResponse> googleLogin(
            @Valid @RequestBody GoogleLoginRequest request) {
        log.info("Google Login API called");
        return ApiResponse.success(
                "Google login successful",
                authService.googleLogin(request));
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
                "Token refreshed successfully",
                authService.refreshAccessToken(request.getRefreshToken()));
    }

    @GetMapping("/verify")
    public ApiResponse<AuthResponse> verifyEmail(@RequestParam String token) {
        log.info("Email verification called with token");
        return ApiResponse.success("Email verified successfully! You are now logged in.",
                authService.verifyEmail(token));
    }
}
