package com.JobBNB.dev.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.JobBNB.dev.user.repository.UserRepository;
import com.JobBNB.dev.user.mapper.UserMapper;
import com.JobBNB.dev.user.dto.UserResponse;
import com.JobBNB.dev.user.dto.UpdateProfileRequest;
import com.JobBNB.dev.user.model.User;
import com.JobBNB.dev.common.exception.BusinessException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private Long currentUserId() {
        return (Long) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    // FETCH PROFILE
    public UserResponse getMyProfile() {
        User user = userRepository.findById(currentUserId())
                .orElseThrow(() -> new BusinessException("User not found"));
        return userMapper.toResponse(user);
    }

    // UPDATE PROFILE
    public UserResponse updateProfile(UpdateProfileRequest request) {

        User user = userRepository.findById(currentUserId())
                .orElseThrow(() -> new BusinessException("User not found"));

        String role = user.getRole().getName();

        if (role.equals("USER")) {
            user.setLinkedinUrl(request.getLinkedinUrl());
            user.setResumeUrl(request.getResumeUrl());
        }

        if (role.equals("EMPLOYER")) {
            user.setCompanyUrl(request.getCompanyUrl());
        }

        userRepository.save(user);
        return userMapper.toResponse(user);
    }

    // DELETE ACCOUNT
    public void deleteAccount() {
        userRepository.deleteById(currentUserId());
    }
}

