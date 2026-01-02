package com.JobBNB.dev.auth.service;

import com.JobBNB.dev.auth.dto.LoginRequest;
import com.JobBNB.dev.auth.dto.RegisterRequest;
import com.JobBNB.dev.auth.dto.AuthResponse;
import com.JobBNB.dev.auth.dto.ForgotPasswordRequest;
import com.JobBNB.dev.auth.dto.ResetPasswordRequest;
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

        public void forgotPassword(ForgotPasswordRequest request) {
                User user = userRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new BusinessException(
                                                "If an account exists with this email, you will receive a reset link shortly."));

                String token = java.util.UUID.randomUUID().toString();
                user.setPasswordResetToken(token);
                user.setPasswordResetExpiry(Instant.now().plus(java.time.Duration.ofHours(1)));
                userRepository.save(user);

                mailService.sendPasswordResetEmail(user.getEmail(), token);
        }

        public void resetPassword(ResetPasswordRequest request) {
                User user = userRepository.findByPasswordResetToken(request.getToken())
                                .orElseThrow(() -> new BusinessException("Invalid or expired reset token"));

                if (user.getPasswordResetExpiry().isBefore(Instant.now())) {
                        throw new BusinessException("Reset token has expired");
                }

                user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
                user.setIsVerified(true); // If they can reset password via email, they are verified owners
                user.setPasswordResetToken(null);
                user.setPasswordResetExpiry(null);
                userRepository.save(user);
        }

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
                                Role role = roleRepository.findByName(request.getRole())
                                                .orElseThrow(() -> new BusinessException(
                                                                "Role " + request.getRole() + " not found"));

                                User newUser = new User();
                                newUser.setEmail(email);
                                newUser.setName(name);
                                newUser.setRole(role);
                                newUser.setIsVerified(true);
                                newUser.setIsActive(true);
                                newUser.setCreatedAt(Instant.now());
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

                if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
                        throw new BusinessException("Invalid credentials");
                }

                RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

                AuthResponse response = new AuthResponse();
                response.setAccessToken(jwtUtil.generateAccessToken(user.getId(), user.getRole().getName()));
                response.setRefreshToken(refreshToken.getToken());
                return response;
        }

        public AuthResponse register(RegisterRequest request) {
                if (userRepository.existsByEmail(request.getEmail())) {
                        throw new BusinessException("Email already registered");
                }

                Role role = roleRepository.findByName(request.getRole())
                                .orElseThrow(() -> new BusinessException("Role " + request.getRole() + " not found"));

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
                mailService.sendVerificationEmail(savedUser.getEmail(), savedUser.getVerificationToken());

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

                RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
                AuthResponse response = new AuthResponse();
                response.setAccessToken(jwtUtil.generateAccessToken(user.getId(), user.getRole().getName()));
                response.setRefreshToken(refreshToken.getToken());
                response.setMessage("Email verified and logged in successfully!");
                return response;
        }

        public AuthResponse refreshAccessToken(String token) {
                RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                                .orElseThrow(() -> new BusinessException("Invalid refresh token"));

                refreshTokenService.verifyExpiration(refreshToken);
                User user = refreshToken.getUser();

                AuthResponse response = new AuthResponse();
                response.setAccessToken(jwtUtil.generateAccessToken(user.getId(), user.getRole().getName()));
                response.setRefreshToken(token);
                return response;
        }
}
