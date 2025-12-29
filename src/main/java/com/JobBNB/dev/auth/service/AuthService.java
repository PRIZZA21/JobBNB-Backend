package com.JobBNB.dev.auth.service;

import com.JobBNB.dev.auth.dto.LoginRequest;
import com.JobBNB.dev.auth.dto.RegisterRequest;
import com.JobBNB.dev.auth.dto.AuthResponse;
import com.JobBNB.dev.common.exception.BusinessException;
import com.JobBNB.dev.common.util.JwtUtil;

import lombok.RequiredArgsConstructor;

import java.time.Instant;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.JobBNB.dev.user.model.User;
import com.JobBNB.dev.user.model.Role;
import com.JobBNB.dev.user.repository.RoleRepository;
import com.JobBNB.dev.user.repository.UserRepository;
import com.JobBNB.dev.auth.model.RefreshToken;
import com.JobBNB.dev.auth.repository.RefreshTokenRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtUtil jwtUtil;

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Invalid credentials"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash())) {
            throw new BusinessException("Invalid credentials");
        }

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        AuthResponse response = new AuthResponse();
        response.setAccessToken(
                jwtUtil.generateAccessToken(
                        user.getId(),
                        user.getRole().getName()));
        response.setRefreshToken(refreshToken.getToken());

        return response;
    }

    public AuthResponse register(RegisterRequest request) {

        // Check email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already registered");
        }

        // Get USER role
        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new BusinessException("Role " + request.getRole() + " not found"));

        // Create user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setCreatedAt(Instant.now());

        if (role.getName().equals("USER")) {

                if (request.getResumeUrl() == null) {
                        throw new BusinessException("Resume URL is required for USER");
                }

                user.setLinkedinUrl(request.getLinkedinUrl());
                user.setResumeUrl(request.getResumeUrl());

        } else if (role.getName().equals("EMPLOYER")) {

                if (request.getCompanyUrl() == null) {
                        throw new BusinessException("Company URL is required for EMPLOYER");
                }

                user.setCompanyUrl(request.getCompanyUrl());
        }

        User savedUser = userRepository.save(user);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(savedUser);

        // AUTO LOGIN (JWT generation – temp for now)
        AuthResponse response = new AuthResponse();
        response.setAccessToken(
                jwtUtil.generateAccessToken(
                        savedUser.getId(),
                        savedUser.getRole().getName()));
        response.setRefreshToken(refreshToken.getToken());

        return response;
    }

    public AuthResponse refreshAccessToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new BusinessException("Invalid refresh token"));

        refreshTokenService.verifyExpiration(refreshToken);

        User user = refreshToken.getUser();

        AuthResponse response = new AuthResponse();
        response.setAccessToken(
                jwtUtil.generateAccessToken(
                        user.getId(),
                        user.getRole().getName()));
        response.setRefreshToken(token); // same refresh token

        return response;
    }
}
