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

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Invalid credentials"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash())) {
            throw new BusinessException("Invalid credentials");
        }

        AuthResponse response = new AuthResponse();
        response.setAccessToken(
                jwtUtil.generateAccessToken(
                        user.getId(),
                        user.getRole().getName()));
        response.setRefreshToken("TEMP_REFRESH");

        return response;
    }

    public AuthResponse register(RegisterRequest request) {

        // Check email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already registered");
        }

        // Get USER role
        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new BusinessException("Role USER not found"));

        // Create user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setCreatedAt(Instant.now());

        User savedUser = userRepository.save(user);

        // AUTO LOGIN (JWT generation – temp for now)
        AuthResponse response = new AuthResponse();
        response.setAccessToken(
                jwtUtil.generateAccessToken(
                        savedUser.getId(),
                        savedUser.getRole().getName()));
        response.setRefreshToken("TEMP_REFRESH_" + savedUser.getId());

        return response;
    }

}
