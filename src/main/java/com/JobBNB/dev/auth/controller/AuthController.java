package com.JobBNB.dev.auth.controller;

import com.JobBNB.dev.auth.dto.ForgotPasswordRequest;
import com.JobBNB.dev.auth.dto.GoogleLoginRequest;
import com.JobBNB.dev.auth.dto.LoginRequest;
import com.JobBNB.dev.auth.dto.RefreshTokenRequest;
import com.JobBNB.dev.auth.dto.RegisterRequest;
import com.JobBNB.dev.auth.dto.ResetPasswordRequest;
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

    @PostMapping("/forgot-password")
    public ApiResponse<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        log.info("Forgot password request for email: {}", request.getEmail());
        authService.forgotPassword(request);
        return ApiResponse.success("If an account exists, a reset link has been sent to your email.");
    }

    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        log.info("Password reset request received");
        authService.resetPassword(request);
        return ApiResponse.success("Password has been reset successfully. You can now login with your new password.");
    }

    @GetMapping("/verify")
    public ApiResponse<AuthResponse> verifyEmail(@RequestParam String token) {
        log.info("Email verification called with token");
        return ApiResponse.success("Email verified successfully! You are now logged in.",
                authService.verifyEmail(token));
    }
}
