package com.JobBNB.dev.user.controller;

import com.JobBNB.dev.user.service.UserService;
import com.JobBNB.dev.user.dto.UpdateProfileRequest;
import com.JobBNB.dev.user.dto.UserResponse;
import com.JobBNB.dev.common.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    // 1️⃣ Get My Profile
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ApiResponse<UserResponse> getMyProfile() {
        log.info("Get My Profile API called");
        return ApiResponse.success(
                "Profile fetched successfully",
                userService.getMyProfile());
    }

    // 2️⃣ Update Profile
    @PreAuthorize("isAuthenticated()")
    @PutMapping
    public ApiResponse<UserResponse> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request) {

        log.info("Update Profile API called");
        return ApiResponse.success(
                "Profile updated successfully",
                userService.updateProfile(request));
    }

    // 3️⃣ Delete Account
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping
    public ApiResponse<Void> deleteAccount() {
        log.info("Delete Account API called");
        userService.deleteAccount();
        return ApiResponse.success(
                "Account deleted successfully",
                null);
    }
}
