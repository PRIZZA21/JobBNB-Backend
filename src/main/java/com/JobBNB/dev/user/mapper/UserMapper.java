package com.JobBNB.dev.user.mapper;

import com.JobBNB.dev.user.dto.UserResponse;
import com.JobBNB.dev.user.model.User;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {

        UserResponse res = new UserResponse();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setRole(user.getRole().getName());

        if (user.getRole().getName().equals("USER")) {
            res.setLinkedinUrl(user.getLinkedinUrl());
            res.setResumeUrl(user.getResumeUrl());
        }

        if (user.getRole().getName().equals("EMPLOYER")) {
            res.setCompanyUrl(user.getCompanyUrl());
        }

        return res;
    }
}

