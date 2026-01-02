package com.JobBNB.dev.auth.service;

import com.JobBNB.dev.auth.dto.LoginRequest;
import com.JobBNB.dev.auth.dto.RegisterRequest;
import com.JobBNB.dev.auth.dto.AuthResponse;
import com.JobBNB.dev.common.exception.BusinessException;
import com.JobBNB.dev.common.util.JwtUtil;

import lombok.RequiredArgsConstructor;

import java.time.Instant;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.JobBNB.dev.user.model.User;
import com.JobBNB.dev.user.model.Role;
import com.JobBNB.dev.user.repository.RoleRepository;
import com.JobBNB.dev.user.repository.UserRepository;
import com.JobBNB.dev.auth.model.RefreshToken;
import com.JobBNB.dev.auth.repository.RefreshTokenRepository;

import com.JobBNB.dev.auth.dto.GoogleLoginRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

        private final UserRepository userRepository;
        private final RoleRepository roleRepository;
        private final PasswordEncoder passwordEncoder;
        private final RefreshTokenService refreshTokenService;
        private final RefreshTokenRepository refreshTokenRepository;

        private final JwtUtil jwtUtil;
        private final com.JobBNB.dev.common.service.MailService mailService;

        @org.springframework.beans.factory.annotation.Value("${app.google.client-id}")
        private String googleClientId;

        public AuthResponse googleLogin(GoogleLoginRequest request) {
                try {
                        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
                                        new GsonFactory())
                                        .setAudience(Collections.singletonList(googleClientId))
                                        .build();

                        GoogleIdToken idToken = verifier.verify(request.getIdToken());
                        if (idToken == null) {
                                throw new BusinessException("Invalid Google token");
                        }

                        GoogleIdToken.Payload payload = idToken.getPayload();
                        String email = payload.getEmail();
                        String name = (String) payload.get("name");

                        User user = userRepository.findByEmail(email).orElseGet(() -> {
                                // Create new user if not exists
                                Role role = roleRepository.findByName(request.getRole())
                                                .orElseThrow(() -> new BusinessException(
                                                                "Role " + request.getRole() + " not found"));

                                User newUser = new User();
                                newUser.setEmail(email);
                                newUser.setName(name);
                                newUser.setRole(role);
                                newUser.setIsVerified(true); // Google emails are already verified
                                newUser.setIsActive(true);
                                newUser.setCreatedAt(Instant.now());
                                // For social login, we can set a random password or leave it null if handled by
                                // security
                                newUser.setPasswordHash(passwordEncoder.encode(java.util.UUID.randomUUID().toString()));
                                return userRepository.save(newUser);
                        });

                        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

                        AuthResponse response = new AuthResponse();
                        response.setAccessToken(jwtUtil.generateAccessToken(user.getId(), user.getRole().getName()));
                        response.setRefreshToken(refreshToken.getToken());
                        response.setMessage("Google login successful");
                        return response;

                } catch (Exception e) {
                        throw new BusinessException("Google authentication failed: " + e.getMessage());
                }
        }

        public AuthResponse login(LoginRequest request) {
                User user = userRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new BusinessException("Invalid credentials"));

                if (!user.getIsVerified()) {
                        throw new BusinessException("Please verify your email before logging in.");
                }

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

                // Get role
                Role role = roleRepository.findByName(request.getRole())
                                .orElseThrow(() -> new BusinessException("Role " + request.getRole() + " not found"));

                // Create user
                User user = new User();
                user.setName(request.getName());
                user.setEmail(request.getEmail());
                user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
                user.setRole(role);
                user.setCreatedAt(Instant.now());
                user.setIsActive(true);
                user.setIsVerified(false);
                user.setVerificationToken(java.util.UUID.randomUUID().toString());

                User savedUser = userRepository.save(user);

                // Send email
                mailService.sendVerificationEmail(savedUser.getEmail(), savedUser.getVerificationToken());

                // We don't return tokens yet because they need to verify first
                AuthResponse response = new AuthResponse();
                response.setMessage("Registration successful! Please check your email to verify your account.");
                return response;
        }

        public AuthResponse verifyEmail(String token) {
                User user = userRepository.findByVerificationToken(token)
                                .orElseThrow(() -> new BusinessException("Invalid or expired verification token"));

                user.setIsVerified(true);
                user.setVerificationToken(null);
                userRepository.save(user);

                // Create tokens for automatic login
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

                AuthResponse response = new AuthResponse();
                response.setAccessToken(
                                jwtUtil.generateAccessToken(
                                                user.getId(),
                                                user.getRole().getName()));
                response.setRefreshToken(refreshToken.getToken());
                response.setMessage("Email verified and logged in successfully!");

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
